package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorFeedbackCorrectTintVar
import dev.kotree.site.colorFeedbackCorrectVar
import dev.kotree.site.colorFeedbackHintTintVar
import dev.kotree.site.colorFeedbackHintVar
import dev.kotree.site.colorFeedbackWrongTintVar
import dev.kotree.site.colorFeedbackWrongVar
import dev.kotree.site.colorOnBranchVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation0Var
import dev.kotree.site.elevation1Var
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontCodeVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.layoutLessonTextVar
import dev.kotree.site.layoutLessonWideVar
import dev.kotree.site.layoutPageGutterVar
import dev.kotree.site.layoutViewportMinVar
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeBlobVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.shapeMdVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.shapeXsVar
import dev.kotree.site.space12Var
import dev.kotree.site.space16Var
import dev.kotree.site.space1Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeBodyWeightVar
import dev.kotree.site.typeCodeLineVar
import dev.kotree.site.typeCodeSizeVar
import dev.kotree.site.typeCodeWeightVar
import dev.kotree.site.typeDisplayLineVar
import dev.kotree.site.typeDisplaySizeVar
import dev.kotree.site.typeDisplayWeightVar
import dev.kotree.site.typeHeadlineLineVar
import dev.kotree.site.typeHeadlineSizeVar
import dev.kotree.site.typeHeadlineWeightVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.core.Chip
import dev.kotree.site.ui.core.Difficulty
import dev.kotree.site.ui.core.DifficultyChip
import dev.kotree.site.ui.core.LeafStatus
import dev.kotree.site.ui.core.StatusChip
import dev.kotree.site.ui.layout.BranchPanel
import dev.kotree.site.ui.layout.Breadcrumb
import dev.kotree.site.ui.layout.ComingSoonBadge
import dev.kotree.site.ui.layout.Crumb
import dev.kotree.site.ui.layout.LeafPopup
import dev.kotree.site.ui.layout.ProgressCounter
import dev.kotree.site.ui.layout.StemSummary
import dev.kotree.site.ui.layout.TopBar
import dev.kotree.site.ui.layout.ZoomControls
import dev.kotree.site.ui.lesson.CodeBlock
import dev.kotree.site.ui.lesson.EditorShell
import dev.kotree.site.ui.lesson.FeedbackBox
import dev.kotree.site.ui.lesson.FeedbackKind
import dev.kotree.site.ui.lesson.FixBug
import dev.kotree.site.ui.lesson.LessonBlock
import dev.kotree.site.ui.lesson.LessonHeader
import dev.kotree.site.ui.lesson.PredictOption
import dev.kotree.site.ui.lesson.PredictOutput
import dev.kotree.site.ui.lesson.SimFrame
import kotlinx.browser.document
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Section
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.round

/*
 * /kitchen-sink: every token and every component in each state (docs/13 section 3). Unlisted, not secret: no link from
 * any page, noindex through a runtime meta tag, not in a sitemap. Copy is "Sample text" and names no real leaf.
 * Hover, focus and pressed live inside the components and cannot be forced from here; they are exercised with real
 * input in the browser (docs/reviews/P1-acceptance.md).
 */

private const val SAMPLE_CODE = "fun main() {\n    val sample: String? = null\n    println(sample?.length)\n}"
private const val SAMPLE_BUG = "fun main() {\n    val sample: String? = null\n    println(sample.length)\n}"
private const val ROBOTS_NOINDEX = "noindex"

private fun css(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"

private fun hex(rgb: Int): String = "#" + rgb.toString(16).padStart(6, '0').uppercase()

private fun luminance(rgb: Int): Double {
    fun channel(shift: Int): Double {
        val c = ((rgb shr shift) and 0xFF) / 255.0
        return if (c <= 0.03928) c / 12.92 else ((c + 0.055) / 1.055).pow(2.4)
    }
    return 0.2126 * channel(16) + 0.7152 * channel(8) + 0.0722 * channel(0)
}

/** WCAG contrast ratio of two sRGB colours, two decimals. */
private fun ratio(a: Int, b: Int): String {
    val la = luminance(a)
    val lb = luminance(b)
    val value = (max(la, lb) + 0.05) / (min(la, lb) + 0.05)
    return (round(value * 100.0) / 100.0).toString()
}

private fun StyleScope.fontRow(
    size: Any,
    line: Any,
    weight: Any,
    code: Boolean = false,
) {
    property("font-family", if (code) fontCodeVar.value() else fontUiVar.value())
    property("font-size", size.toString())
    property("line-height", line.toString())
    property("font-weight", weight.toString())
}

private fun StyleScope.label() {
    property("font-family", fontUiVar.value())
    property("font-size", typeLabelSizeVar.value())
    property("line-height", typeLabelLineVar.value())
    property("font-weight", typeLabelWeightVar.value())
}

private fun StyleScope.wrapRow() {
    property("display", "flex")
    property("flex-wrap", "wrap")
    property("align-items", "center")
    property("gap", space4Var.value())
}

@Composable
private fun Section(id: String, title: String, content: @Composable () -> Unit) {
    Section(
        Modifier.attrsModifier { id(id) }.styleModifier {
            property("display", "grid")
            property("gap", space4Var.value())
            property("padding-top", space8Var.value())
            property("border-top", elevation1Var.value())
            // Sections stay out from under the anchor when scrolled to.
            property("scroll-margin-top", space8Var.value())
        }.toAttrs()
    ) {
        H2(
            Modifier.styleModifier {
                property("margin", "0")
                fontRow(typeHeadlineSizeVar.value(), typeHeadlineLineVar.value(), Tokens.TypeScale.headline.weight)
            }.toAttrs()
        ) { Text(title) }
        content()
    }
}

@Composable
private fun Note(text: String) {
    P(
        Modifier.styleModifier {
            property("margin", "0")
            label()
            property("color", colorOnSurfaceMutedVar.value())
        }.toAttrs()
    ) { Text(text) }
}

@Composable
private fun Swatch(fill: String, name: String, detail: String, textColor: String = colorOnSurfaceVar.value().toString()) {
    Div(
        Modifier.styleModifier {
            property("display", "grid")
            property("gap", space1Var.value())
            property("width", layoutLessonTextVar.value().toString().let { "calc($it / 3)" })
        }.toAttrs()
    ) {
        Div(
            Modifier.styleModifier {
                property("height", space16Var.value())
                property("background-color", fill)
                property("border", elevation1Var.value())
                property("border-radius", shapeMdVar.value())
            }.toAttrs()
        )
        Span(Modifier.styleModifier { label(); property("color", textColor) }.toAttrs()) { Text(name) }
        Span(Modifier.styleModifier { label(); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) { Text(detail) }
    }
}

@Composable
private fun SpringDemo(name: String, params: Tokens.Spring.Params) {
    var moved by remember { mutableStateOf(false) }
    val (seconds, easing) = remember { Spring(params.dampingRatio, params.stiffness).cssLinear() }
    Div(Modifier.styleModifier { property("display", "grid"); property("gap", space2Var.value()) }.toAttrs()) {
        Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
            Button("Move: $name", { moved = !moved }, kind = ButtonKind.Tonal)
            Span(Modifier.styleModifier { label(); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
                Text("damping ${params.dampingRatio}, stiffness ${params.stiffness}, css easing ${round(seconds * 1000.0).toInt()} ms")
            }
        }
        Div(
            Modifier.styleModifier {
                property("width", layoutLessonTextVar.value())
                property("max-width", "100%")
                property("border", elevation1Var.value())
                property("border-radius", shapeFullVar.value())
                property("padding", space1Var.value())
                property("box-sizing", "border-box")
            }.toAttrs()
        ) {
            Div(
                Modifier.styleModifier {
                    property("width", space8Var.value())
                    property("height", space8Var.value())
                    property("border-radius", shapeFullVar.value())
                    property("background-color", branchInk("kotlin").value().toString())
                    property("transition", "transform ${seconds}s $easing")
                    property("transform", if (moved) "translateX(calc(${layoutLessonTextVar.value()} - ${space8Var.value()} - ${space4Var.value()}))" else "none")
                }.toAttrs()
            )
        }
    }
}

@Composable
fun KitchenSink() {
    // noindex per page: a meta tag set while this page is mounted. See docs/reviews/P1-acceptance.md for what the export shows.
    DisposableEffect(Unit) {
        val meta = document.createElement("meta")
        meta.setAttribute("name", "robots")
        meta.setAttribute("content", ROBOTS_NOINDEX)
        document.head?.appendChild(meta)
        onDispose { document.head?.removeChild(meta) }
    }
    var zoom by remember { mutableStateOf(0) }
    var popup by remember { mutableStateOf("") }
    Div(
        Modifier.styleModifier {
            property("box-sizing", "border-box")
            property("min-width", layoutViewportMinVar.value())
            property("padding", "${space12Var.value()} ${layoutPageGutterVar.value()}")
            property("display", "grid")
            property("gap", space12Var.value())
            property("background-color", colorSurfaceVar.value())
            property("color", colorOnSurfaceVar.value())
        }.toAttrs()
    ) {
        Div(Modifier.styleModifier { property("display", "grid"); property("gap", space2Var.value()) }.toAttrs()) {
            H1(
                Modifier.styleModifier {
                    property("margin", "0")
                    fontRow(typeDisplaySizeVar.value(), typeDisplayLineVar.value(), Tokens.TypeScale.display.weight)
                }.toAttrs()
            ) { Text("Kitchen sink") }
            Note("Unlisted and noindex. Every token and component in each state. Sample text only; no real leaf is named.")
        }

        // ---- Tokens ----
        Section("colour-neutrals", "Colour: neutrals and focus") {
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                val neutrals = listOf(
                    "color.surface" to Tokens.Color.surface,
                    "color.bark" to Tokens.Color.bark,
                    "color.on-surface" to Tokens.Color.onSurface,
                    "color.on-surface-muted" to Tokens.Color.onSurfaceMuted,
                    "color.outline" to Tokens.Color.outline,
                    "color.leaf.shadow.label" to Tokens.Color.leafShadowLabel,
                    "color.focus-ring (default)" to Tokens.Chrome.focusRingDefault,
                    "color.on-branch" to Tokens.Chrome.onBranch,
                )
                neutrals.forEach { (name, rgb) -> Swatch(css(rgb), name, "${hex(rgb)}, ${ratio(rgb, Tokens.Color.surface)} on white") }
            }
        }
        Section("colour-branches", "Colour: 17 branch inks and tints") {
            Note("Ink on white, ink on its own tint (${Tokens.Branch.tintPercent} percent ink in white), on-branch text on the ink. Every ratio must be 4.5 or more.")
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                Tokens.Branch.inks.forEach { (slug, ink) ->
                    val tint = Tokens.Branch.tint(ink)
                    Div(
                        Modifier.styleModifier {
                            property("display", "grid")
                            property("gap", space1Var.value())
                            property("width", "calc(${layoutLessonTextVar.value()} / 3)")
                        }.toAttrs()
                    ) {
                        Div(Modifier.styleModifier { property("display", "flex"); property("height", space16Var.value()) }.toAttrs()) {
                            Div(
                                Modifier.styleModifier {
                                    property("flex", "1")
                                    property("background-color", branchInk(slug).value().toString())
                                    property("color", colorOnBranchVar.value())
                                    property("border-radius", "${shapeMdVar.value()} 0 0 ${shapeMdVar.value()}")
                                    property("padding", space2Var.value())
                                    label()
                                }.toAttrs()
                            ) { Text("Aa ink") }
                            Div(
                                Modifier.styleModifier {
                                    property("flex", "1")
                                    property("background-color", branchTint(slug).value().toString())
                                    property("color", branchInk(slug).value().toString())
                                    property("border-top-right-radius", shapeMdVar.value().toString())
                                    property("border-bottom-right-radius", shapeMdVar.value().toString())
                                    property("padding", space2Var.value())
                                    label()
                                }.toAttrs()
                            ) { Text("Aa tint") }
                        }
                        Span(Modifier.styleModifier { label() }.toAttrs()) { Text(slug) }
                        Span(Modifier.styleModifier { label(); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
                            Text("${hex(ink)} / ${hex(tint)}")
                        }
                        Span(Modifier.styleModifier { label(); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
                            Text("white ${ratio(ink, Tokens.Color.surface)}, tint ${ratio(ink, tint)}, on-ink ${ratio(Tokens.Chrome.onBranch, ink)}")
                        }
                    }
                }
            }
        }
        Section("colour-bough-leaf", "Colour: boughs, leaf states, fruit, feedback") {
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                listOf(
                    "bough.roots" to Tokens.Bough.roots,
                    "bough.kotlin-android" to Tokens.Bough.kotlinAndroid,
                    "bough.architecture-kmp" to Tokens.Bough.architectureKmp,
                    "bough.cs-theory" to Tokens.Bough.csTheory,
                    "bough.craft" to Tokens.Bough.craft,
                    "leaf.shadow" to Tokens.Leaf.shadow,
                    "leaf.upnext" to Tokens.Leaf.upnext,
                    "leaf.upnext.glow" to Tokens.Leaf.upnextGlow,
                    "leaf.easy" to Tokens.Leaf.easy,
                    "leaf.medium" to Tokens.Leaf.medium,
                    "leaf.hard" to Tokens.Leaf.hard,
                    "leaf.done-mark" to Tokens.Leaf.doneMark,
                    "fruit" to Tokens.Leaf.fruit,
                ).forEach { (name, rgb) -> Swatch(css(rgb), name, hex(rgb)) }
            }
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                listOf(
                    Triple("feedback.correct", Tokens.Chrome.feedbackCorrect, colorFeedbackCorrectTintVar to colorFeedbackCorrectVar),
                    Triple("feedback.wrong", Tokens.Chrome.feedbackWrong, colorFeedbackWrongTintVar to colorFeedbackWrongVar),
                    Triple("feedback.hint", Tokens.Chrome.feedbackHint, colorFeedbackHintTintVar to colorFeedbackHintVar),
                ).forEach { (name, rgb, vars) ->
                    val tint = Tokens.Branch.tint(rgb)
                    Div(
                        Modifier.styleModifier {
                            property("background-color", vars.first.value())
                            property("color", vars.second.value())
                            property("border", "${Tokens.Elevation.borderPx}px solid ${vars.second.value()}")
                            property("border-radius", shapeMdVar.value())
                            property("padding", space4Var.value())
                            property("width", "calc(${layoutLessonTextVar.value()} / 2)")
                            label()
                        }.toAttrs()
                    ) { Text("$name ${hex(rgb)} on ${hex(tint)}: ${ratio(rgb, tint)} on tint, ${ratio(rgb, Tokens.Color.surface)} on white") }
                }
            }
        }
        Section("type", "Type scale") {
            val rows = listOf(
                Triple("display", Tokens.TypeScale.display, Triple(typeDisplaySizeVar.value(), typeDisplayLineVar.value(), typeDisplayWeightVar.value())),
                Triple("headline", Tokens.TypeScale.headline, Triple(typeHeadlineSizeVar.value(), typeHeadlineLineVar.value(), typeHeadlineWeightVar.value())),
                Triple("title", Tokens.TypeScale.title, Triple(typeTitleSizeVar.value(), typeTitleLineVar.value(), typeTitleWeightVar.value())),
                Triple("body", Tokens.TypeScale.body, Triple(typeBodySizeVar.value(), typeBodyLineVar.value(), typeBodyWeightVar.value())),
                Triple("label", Tokens.TypeScale.label, Triple(typeLabelSizeVar.value(), typeLabelLineVar.value(), typeLabelWeightVar.value())),
                Triple("code", Tokens.TypeScale.code, Triple(typeCodeSizeVar.value(), typeCodeLineVar.value(), typeCodeWeightVar.value())),
            )
            rows.forEach { (name, row, vars) ->
                val isCode = name == "code"
                Div(Modifier.styleModifier { property("display", "grid"); property("gap", space1Var.value()) }.toAttrs()) {
                    Span(Modifier.styleModifier { label(); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) {
                        Text("type.$name: ${row.sizePx} / ${row.lineHeightPx}, weight ${row.weight}; emphasized ${row.weight + Tokens.TypeScale.emphasisWeightStep}")
                    }
                    Span(Modifier.styleModifier { fontRow(vars.first, vars.second, vars.third, isCode) }.toAttrs()) {
                        Text("Sample text for $name, regular")
                    }
                    Span(Modifier.styleModifier { fontRow(vars.first, vars.second, row.weight + Tokens.TypeScale.emphasisWeightStep, isCode) }.toAttrs()) {
                        Text("Sample text for $name, emphasized")
                    }
                }
            }
        }
        Section("shape-space-elevation", "Shape, spacing, elevation") {
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                listOf(
                    "shape.xs" to shapeXsVar.value().toString(),
                    "shape.md" to shapeMdVar.value().toString(),
                    "shape.xl" to shapeXlVar.value().toString(),
                    "shape.full" to shapeFullVar.value().toString(),
                    "shape.blob" to shapeBlobVar.value(),
                ).forEach { (name, radius) ->
                    Div(Modifier.styleModifier { property("display", "grid"); property("gap", space1Var.value()); label() }.toAttrs()) {
                        Div(
                            Modifier.styleModifier {
                                property("width", space16Var.value())
                                property("height", space16Var.value())
                                property("border", elevation1Var.value())
                                property("background-color", branchTint("kotlin").value().toString())
                                property("border-radius", radius)
                            }.toAttrs()
                        )
                        Text(name)
                    }
                }
            }
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                listOf(
                    "space.1" to space1Var.value().toString(), "space.2" to space2Var.value().toString(), "space.3" to space3Var.value().toString(),
                    "space.4" to space4Var.value().toString(), "space.6" to space6Var.value().toString(), "space.8" to space8Var.value().toString(),
                    "space.12" to space12Var.value().toString(), "space.16" to space16Var.value().toString(),
                ).forEach { (name, width) ->
                    Div(Modifier.styleModifier { property("display", "grid"); property("gap", space1Var.value()); label() }.toAttrs()) {
                        Div(
                            Modifier.styleModifier {
                                property("width", width)
                                property("height", space4Var.value())
                                property("background-color", branchInk("kotlin").value().toString())
                            }.toAttrs()
                        )
                        Text(name)
                    }
                }
            }
            Div(Modifier.styleModifier { wrapRow(); property("gap", space8Var.value()) }.toAttrs()) {
                listOf("elevation.0" to elevation0Var.value(), "elevation.1" to elevation1Var.value(), "elevation.2" to elevation2Var.value())
                    .forEach { (name, shadow) ->
                        Div(
                            Modifier.styleModifier {
                                property("width", "calc(${space16Var.value()} * 2)")
                                property("height", space16Var.value())
                                property("border-radius", shapeMdVar.value())
                                property("padding", space2Var.value())
                                property("box-sizing", "border-box")
                                label()
                                // elevation.1 is a border, 0 is none, 2 is a shadow: pick the property by value.
                                if (name == "elevation.2") property("box-shadow", shadow) else property("border", shadow)
                            }.toAttrs()
                        ) { Text(name) }
                    }
            }
        }
        Section("motion", "Motion") {
            Note("Springs from Compose Material 3 as CSS linear() easings. Under prefers-reduced-motion every transition is instant.")
            SpringDemo("spatial", Tokens.Spring.spatial)
            SpringDemo("spatial fast", Tokens.Spring.spatialFast)
            SpringDemo("spatial slow", Tokens.Spring.spatialSlow)
        }
        Section("layout-widths", "Layout widths") {
            listOf(
                "layout.viewport.min" to layoutViewportMinVar.value().toString(),
                "layout.lesson.wide" to layoutLessonWideVar.value().toString(),
                "layout.lesson.text" to layoutLessonTextVar.value().toString(),
            ).forEach { (name, width) ->
                Div(Modifier.styleModifier { property("display", "grid"); property("gap", space1Var.value()) }.toAttrs()) {
                    Span(Modifier.styleModifier { label() }.toAttrs()) { Text(name) }
                    Div(
                        Modifier.styleModifier {
                            property("width", width)
                            property("max-width", "100%")
                            property("height", space4Var.value())
                            property("background-color", branchTint("kotlin").value().toString())
                            property("border", elevation1Var.value())
                        }.toAttrs()
                    )
                }
            }
        }

        // ---- Core components ----
        Section("buttons", "Button") {
            Note("Default and disabled shown here. Hover, focus and pressed come from the component and are exercised with real input.")
            listOf(ButtonKind.Filled, ButtonKind.Tonal, ButtonKind.Text).forEach { kind ->
                Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                    Span(Modifier.styleModifier { label(); property("width", space16Var.value()) }.toAttrs()) { Text(kind.name) }
                    Button("Sample action", {}, kind = kind)
                    Button("Sample disabled", {}, kind = kind, enabled = false)
                    Button("Another branch", {}, kind = kind, branch = "os")
                    Button("Third branch", {}, kind = kind, branch = "dsa")
                }
            }
        }
        Section("chips", "Chip") {
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                Difficulty.entries.forEach { DifficultyChip(it) }
                LeafStatus.entries.forEach { StatusChip(it) }
                Chip("Sample chip", Tokens.Leaf.fruit)
            }
        }

        // ---- Layout components ----
        Section("layout-components", "Layout components") {
            TopBar(grown = 3, total = 12)
            Breadcrumb(
                listOf(Crumb("Sample tree", "#"), Crumb("Sample bough", "#"), Crumb("Sample branch", "#")),
                onSelect = { zoom = it },
                branch = "kotlin",
            )
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                ZoomControls({ zoom++ }, { zoom-- }, { zoom = 0 }, { zoom = 0 })
                Span(Modifier.styleModifier { label(); property("color", colorOnSurfaceMutedVar.value()) }.toAttrs()) { Text("Sample zoom step: $zoom") }
            }
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                ProgressCounter(grown = 3, total = 12)
                ComingSoonBadge()
            }
            BranchPanel(
                slug = "kotlin",
                title = "Sample branch",
                summary = "Sample text for a branch summary.",
                stems = listOf(StemSummary("Sample stem", 1, 4), StemSummary("Another sample stem", 0, 3)),
                onClose = {},
            )
            Div(Modifier.styleModifier { wrapRow() }.toAttrs()) {
                Button("Open popup with video", { popup = "video" }, kind = ButtonKind.Tonal)
                Button("Open popup without video", { popup = "plain" }, kind = ButtonKind.Tonal)
            }
            if (popup.isNotEmpty()) {
                LeafPopup(
                    branch = "kotlin",
                    title = "Sample leaf",
                    summary = "Sample text for a leaf summary.",
                    difficulty = Difficulty.Easy,
                    onOpenLesson = {},
                    onClose = { popup = "" },
                    videoUrl = if (popup == "video") "https://example.com" else "",
                )
            }
        }

        // ---- Lesson components ----
        Section("lesson-components", "Lesson components") {
            LessonHeader("Sample title", Difficulty.Medium, "kotlin", progressLine = "Sample progress line", videoUrl = "https://example.com")
            LessonHeader("Sample title without video", Difficulty.Hard, "os")
            LessonBlock("Sample section", id = "sample-section") {
                P { Text("Sample text for a lesson block.") }
                CodeBlock(SAMPLE_CODE)
            }
            EditorShell("Sample editor") { CodeBlock(SAMPLE_CODE) }
            EditorShell("Sample editor offline", online = false) { CodeBlock(SAMPLE_CODE) }
            SimFrame("Sample simulation")
        }
        Section("challenges", "Challenge cards") {
            Note("Answers and explanations are absent from the page until you act.")
            PredictOutput(
                id = "kitchen-predict",
                code = SAMPLE_CODE,
                options = listOf(
                    PredictOption("a", "alpha-answer", "Reason-alpha-text."),
                    PredictOption("b", "bravo-answer", "Reason-bravo-text.", correct = true),
                    PredictOption("c", "charlie-answer", "Reason-charlie-text."),
                ),
                output = "output-marker-text",
            )
            FixBug(
                code = SAMPLE_BUG,
                expected = "expected-marker-text",
                hints = listOf("Hint-one-text.", "Hint-two-text."),
                fix = "fun fixed() {\n    println(\"fix-marker-text\")\n}",
                explanation = "Explanation-marker-text.",
            )
            Div(Modifier.styleModifier { property("display", "grid"); property("gap", space3Var.value()) }.toAttrs()) {
                FeedbackBox(FeedbackKind.Correct, "Feedback: correct") { P { Text("Sample text.") } }
                FeedbackBox(FeedbackKind.Neutral, "Feedback: neutral") { P { Text("Sample text.") } }
                FeedbackBox(FeedbackKind.Hint, "Feedback: hint") { P { Text("Sample text.") } }
            }
        }
    }
}

@Page("/kitchen-sink")
@Composable
fun KitchenSinkPage() {
    KitchenSink()
}
