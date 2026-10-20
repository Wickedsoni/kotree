package dev.kotree.site.tree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.tree.camera.Camera
import dev.kotree.site.tree.camera.FlyTarget
import dev.kotree.site.tree.camera.Fly
import dev.kotree.site.tree.camera.InputCallbacks
import dev.kotree.site.tree.camera.ViewState
import dev.kotree.site.tree.camera.ViewTracker
import dev.kotree.site.tree.camera.ZoomLevel
import dev.kotree.site.tree.camera.attachCameraInput
import dev.kotree.site.tree.gen.generate
import dev.kotree.site.tree.gen.geometryHash
import dev.kotree.site.tree.gen.layoutReport
import dev.kotree.site.tree.hit.Kind
import dev.kotree.site.tree.hit.Node
import dev.kotree.site.tree.hit.TreeNodes
import dev.kotree.site.tree.model.TreeData
import dev.kotree.site.tree.overlay.A11yNodes
import dev.kotree.site.tree.overlay.NODE_ATTRIBUTE
import dev.kotree.site.tree.render.FrameLoop
import dev.kotree.site.tree.render.LabelFonts
import dev.kotree.site.tree.render.VectorRenderer
import dev.kotree.site.ui.layout.BranchNodeCard
import dev.kotree.site.ui.layout.LeafNodeCard
import dev.kotree.site.ui.layout.NavItem
import dev.kotree.site.ui.layout.SiteHeader
import dev.kotree.site.ui.layout.StemNodeCard
import dev.kotree.site.ui.layout.ZoomControls
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import kotlin.math.roundToInt

private const val REPORT_WIDTH_PX = 1280
private const val REPORT_HEIGHT_PX = 800
private const val VIEW_LABEL = "Kotree. Drag to pan, scroll or plus and minus to zoom, Home to fit the tree, Tab to move between nodes."
private const val ROOTS = "roots"
/** Escape without a card flies below the enter threshold of the current level, by this factor (below every leave threshold). */
private const val ZOOM_OUT_FACTOR = 0.85
/** Node card width in CSS px (6 x space.16). */
private const val CARD_WIDTH_PX = Tokens.Spacing.s16 * 6

/** A node card to show and where its node is on screen (CSS px) when it opened. */
private class CardRequest(val node: Node, val anchorX: Double, val anchorY: Double)

private class ElementHolder {
    var element: HTMLElement? = null
    var listSeen = false
}

/**
 * The whole tree, full size of its parent (ADR 006): a smooth vector canvas drawn by one requestAnimationFrame loop, a
 * visually hidden button list for the keyboard, one node card for every click, the floating header and the zoom controls.
 * Compose state is written only when the view (level, bough, branch, stem) or the open card changes, never per camera
 * change. State is read only in child composables, never in this scope: reading it here would recompose the container and
 * re-apply its attributes, which drops keyboard focus.
 */
@Composable
fun TreeView(data: TreeData) {
    val geometry = remember(data) {
        val t0 = window.performance.now()
        generate(data, data.seed).also {
            console.log("generate + treeData init ms", (window.performance.now() - t0).roundToInt(), "(plan <= 50)")
            console.log("geometry hash", geometryHash(it))
            // Check at the zoom each branch label really flies to (docs/14 §5), on a 1280 x 800 viewport.
            val probe = Camera(it.bounds).also { c -> c.setViewport(Viewport(REPORT_WIDTH_PX, REPORT_HEIGHT_PX)) }
            layoutReport(it, data, Viewport(REPORT_WIDTH_PX, REPORT_HEIGHT_PX)) { b -> probe.branchTarget(b, upNextIds(data)[b.id]).z }
                .forEach { line -> console.log(line) }
        }
    }
    val nodes = remember(geometry) { TreeNodes(geometry, data) }
    val camera = remember(geometry) { Camera(geometry.bounds) }
    val fly = remember(camera) { Fly(camera) }
    val tracker = remember(geometry) { ViewTracker(geometry) }
    val canvas = remember(geometry) {
        val c = document.createElement("canvas") as HTMLCanvasElement
        c.setAttribute("aria-hidden", "true")
        c.style.setProperty("position", "absolute")
        c.style.setProperty("inset", "0")
        c.style.setProperty("width", "100%")
        c.style.setProperty("height", "100%")
        c.style.setProperty("display", "block")
        c
    }
    val renderer = remember(canvas) { VectorRenderer(canvas, geometry, nodes) }
    val viewState = remember(geometry) { mutableStateOf(ViewState(ZoomLevel.Z0, null, null, null)) }
    val loop = remember(renderer) { FrameLoop(camera, fly, tracker, renderer) { viewState.value = it } }
    val card = remember(data) { mutableStateOf<CardRequest?>(null) }
    val upNext = remember(data) { upNextIds(data) }
    val holder = remember { ElementHolder() }

    fun flyOrSet(t: FlyTarget, instant: Boolean) {
        if (instant) camera.set(t.z, t.centre.x, t.centre.y) else fly.to(t)
    }

    /** Opens the right response for [node]: fly (stems and branches always, leaves only when [goto]) and show its card. */
    fun activate(node: Node, goto: Boolean, instant: Boolean) {
        val vw = camera.viewport.widthPx.toDouble()
        val vh = camera.viewport.heightPx.toDouble()
        when (node.kind) {
            Kind.BOUGH -> {
                tracker.unpin()
                node.boughGeo?.let { flyOrSet(camera.boughTarget(it), instant) }
                card.value = null
            }
            Kind.BRANCH -> {
                tracker.pin(node.id)
                node.branchGeo?.let { flyOrSet(camera.branchTarget(it, upNext[node.id]), instant) }
                card.value = CardRequest(node, vw / 2.0, vh / 2.0)
            }
            Kind.STEM -> {
                tracker.pin(node.branchId)
                node.stemGeo?.let { flyOrSet(camera.stemTarget(it), instant) }
                card.value = CardRequest(node, vw / 2.0, vh / 2.0)
            }
            else -> {
                if (goto) {
                    tracker.pin(node.branchId)
                    node.leafGeo?.let { flyOrSet(camera.leafTarget(it), instant) }
                    card.value = CardRequest(node, vw / 2.0, vh / 2.0)
                } else {
                    val s = camera.scale
                    card.value = CardRequest(node, (node.ax - camera.cx) * s + vw / 2.0, (node.ay - camera.cy) * s + vh / 2.0)
                }
            }
        }
        loop.requestFrame()
    }

    /** From the Bough level the roots label and limb act as the Foundation branch; at the Canopy they fly to the roots. */
    fun resolve(clicked: Node): Node {
        val foundation = nodes.foundation
        return if (clicked.kind == Kind.BOUGH && clicked.id == ROOTS && viewState.value.level >= ZoomLevel.Z1 && foundation != null) foundation else clicked
    }

    fun goHome() {
        tracker.unpin()
        card.value = null
        fly.to(camera.homeTarget())
    }

    fun closeCard() {
        val node = card.value?.node
        card.value = null
        val button = if (node == null) null else document.querySelector("[$NODE_ATTRIBUTE=\"${node.id}\"]") as? HTMLElement
        (button ?: holder.element)?.focus()
    }

    fun zoomOutOneLevel() {
        val enter = when (viewState.value.level) {
            ZoomLevel.Z4 -> Tokens.Zoom.leafLeave
            ZoomLevel.Z3 -> Tokens.Zoom.stemLeave
            ZoomLevel.Z2 -> Tokens.Zoom.branchEnter
            ZoomLevel.Z1 -> Tokens.Zoom.boughLeave
            ZoomLevel.Z0 -> return
        }
        fly.to(FlyTarget(enter * ZOOM_OUT_FACTOR, Vec(camera.cx, camera.cy)))
    }

    fun onFocusNode(node: Node?) {
        // The ring is for keyboard focus only, not for a button focused after a mouse click.
        val visible = node != null && (document.activeElement as? Element)?.matches(":focus-visible") == true
        renderer.focus = if (visible) node else null
        if (visible && node != null) {
            val s = camera.scale
            val vw = camera.viewport.widthPx.toDouble()
            val vh = camera.viewport.heightPx.toDouble()
            val x = (node.ax - camera.cx) * s + vw / 2.0
            val y = (node.ay - camera.cy) * s + vh / 2.0
            val margin = Tokens.Space.hitAreaPx * 2.0
            if (x < margin || y < margin || x > vw - margin || y > vh - margin) fly.to(FlyTarget(camera.z, Vec(node.ax, node.ay)))
        }
        loop.requestFrame()
    }

    Div(
        Modifier.styleModifier {
            property("position", "relative")
            property("width", "100%")
            property("height", "${Tokens.Layout.fullViewportHeightVh}vh")
            property("overflow", "hidden")
            property("touch-action", "none")
            property("user-select", "none")
            property("background-color", colorSurfaceVar.value())
            property("outline", "none")
        }.toAttrs {
            attr("tabindex", "-1")
            attr("role", "group")
            attr("aria-label", VIEW_LABEL)
            ref { el ->
                holder.element = el
                el.insertBefore(canvas, el.firstChild)

                val measure: () -> Unit = {
                    val r = el.getBoundingClientRect()
                    camera.setViewport(Viewport(r.width.toInt(), r.height.toInt()))
                }
                measure()
                val observer: dynamic = js("Reflect").construct(js("ResizeObserver"), arrayOf<dynamic>(measure))
                observer.observe(el)

                // Start on the whole tree, or on the node named by the URL hash (unknown hash: the whole tree).
                val first = camera.homeTarget()
                camera.set(first.z, first.centre.x, first.centre.y)
                val target = nodeFromHash(nodes)
                if (target != null) activate(target, goto = true, instant = true)

                val removeInput = attachCameraInput(
                    el, canvas, camera, fly, loop,
                    InputCallbacks(
                        onClick = { x, y ->
                            val hit = renderer.hitNode(x, y)
                            if (hit != null) {
                                activate(resolve(hit), goto = false, instant = false)
                            } else {
                                card.value = null
                            }
                        },
                        onHome = { goHome() },
                        onEscape = { if (card.value != null) closeCard() else zoomOutOneLevel() },
                    ),
                )
                val removeHash = attachDeepLink(nodes) { node ->
                    if (node == null) goHome() else activate(node, goto = true, instant = false)
                }

                // Canvas text is measured with the web font: lay the labels out again once it has loaded.
                val fonts: dynamic = document.asDynamic().fonts
                if (fonts != null) {
                    val refresh: (dynamic) -> Unit = { _ -> renderer.fontsChanged(); loop.requestFrame() }
                    fonts.load(LabelFonts.label).then(refresh, refresh)
                    fonts.load(LabelFonts.title).then(refresh, refresh)
                }
                PerfProbe.install { renderer.labelRects() }
                loop.requestFrame()

                onDispose {
                    removeInput()
                    removeHash()
                    observer.disconnect()
                    loop.dispose()
                    canvas.remove()
                    holder.element = null
                }
            }
        }
    ) {
        // Visually hidden page heading (screen readers and document outline).
        H1(Modifier.styleModifier {
            property("position", "absolute")
            property("width", "${Tokens.Space.hairlinePx}px")
            property("height", "${Tokens.Space.hairlinePx}px")
            property("margin", "-${Tokens.Space.hairlinePx}px")
            property("padding", "0")
            property("overflow", "hidden")
            property("clip", "rect(0, 0, 0, 0)")
            property("white-space", "nowrap")
            property("border", "0")
        }.toAttrs()) { Text("Kotree") }
        SiteHeader(
            NavItem.Tree,
            floating = true,
            modifier = Modifier.styleModifier {
                property("position", "absolute")
                property("left", space4Var.value())
                property("top", space4Var.value())
            },
        )
        A11yNodes(
            nodes = nodes,
            view = viewState,
            onActivate = { activate(resolve(it), goto = false, instant = false) },
            onFocusNode = { onFocusNode(it) },
            onListChanged = {
                // The focused button was removed on a level switch: keep focus inside the view.
                val active = document.activeElement
                if (!holder.listSeen) {
                    holder.listSeen = true
                } else if (active == null || active == document.body) {
                    holder.element?.focus()
                }
            },
        )
        CardHost(
            card = card,
            camera = camera,
            onClose = { closeCard() },
            onStem = { id -> nodes.byId[id]?.let { activate(it, goto = true, instant = false) } },
        )
        ZoomControls(
            onZoomIn = { fly.cancel(); camera.zoomAtCentre(Tokens.Camera.keyZoomFactor) },
            onZoomOut = { fly.cancel(); camera.zoomAtCentre(1.0 / Tokens.Camera.keyZoomFactor) },
            onReset = { goHome() },
            onStartHere = { nodes.byId[data.startHere]?.let { activate(it, goto = true, instant = false) } },
            modifier = Modifier.styleModifier {
                property("position", "absolute")
                property("right", space4Var.value())
                property("bottom", space4Var.value())
            },
        )
    }
}

/**
 * The open node card, placed beside the node it came from: to the right of the anchor, to the left when there is no room
 * (centred on narrow screens), always vertically centred and never off screen. Reads [card], so only this composable
 * recomposes when a card opens or closes.
 */
@Composable
private fun CardHost(card: State<CardRequest?>, camera: Camera, onClose: () -> Unit, onStem: (String) -> Unit) {
    val request = card.value ?: return
    val node = request.node
    key(node.id) {
        val vw = camera.viewport.widthPx.toDouble()
        val gap = Tokens.Spacing.s6.toDouble()
        val fitsRight = request.anchorX + gap + CARD_WIDTH_PX <= vw - gap
        val fitsLeft = request.anchorX - gap - CARD_WIDTH_PX >= gap
        Div(
            Modifier.styleModifier {
                property("position", "absolute")
                property("inset", "0")
                property("display", "flex")
                property("align-items", "center")
                property("box-sizing", "border-box")
                property("padding", space6Var.value())
                property("pointer-events", "none")
                if (fitsRight) {
                    property("padding-left", "${request.anchorX + gap}px")
                } else if (fitsLeft) {
                    property("justify-content", "flex-end")
                    property("padding-right", "${vw - request.anchorX + gap}px")
                } else {
                    property("justify-content", "center")
                }
            }.toAttrs()
        ) {
            val cardStyle = Modifier.styleModifier {
                property("width", "${CARD_WIDTH_PX}px")
                property("max-width", "100%")
                property("pointer-events", "auto")
            }
            val leaf = node.leaf
            val stem = node.stem
            val branch = node.branch
            when {
                node.kind == Kind.LEAF && leaf != null && stem != null && branch != null -> LeafNodeCard(leaf, branch, stem, onClose, cardStyle)
                node.kind == Kind.STEM && stem != null && branch != null -> StemNodeCard(stem, branch, onClose, cardStyle)
                node.kind == Kind.BRANCH && branch != null -> BranchNodeCard(branch, { onStem(it.id) }, onClose, cardStyle)
            }
        }
    }
}

/** Branch id to the full id of its up-next leaf (absent when every leaf is live). */
private fun upNextIds(data: TreeData): Map<String, String> =
    data.boughs.flatMap { it.branches }.mapNotNull { b -> b.upNext?.let { b.id to it } }.toMap()
