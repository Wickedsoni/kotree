package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.layoutPageGutterVar
import dev.kotree.site.layoutPageMaxVar
import dev.kotree.site.space2Var
import dev.kotree.site.space4Var
import dev.kotree.site.space8Var
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.treeData
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeHeadlineLineVar
import dev.kotree.site.typeHeadlineSizeVar
import dev.kotree.site.typeHeadlineWeightVar
import dev.kotree.site.ui.core.StatusChip
import dev.kotree.site.ui.layout.NavItem
import dev.kotree.site.ui.layout.SiteHeader
import dev.kotree.site.ui.topics.BentoGrid
import dev.kotree.site.ui.topics.BoughHeader
import dev.kotree.site.ui.topics.BranchCard
import dev.kotree.site.ui.topics.CardKind
import dev.kotree.site.ui.topics.WIDE_STEM_COUNT
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event
import dev.kotree.site.ui.core.LeafStatus as ChipStatus

private const val PAGE_TITLE = "Topics - Kotree"
private const val TREE_HREF = "/"
private const val FOUNDATION = "foundation"

private fun currentHash(): String = window.location.hash.removePrefix("#")

/**
 * All topics as a bento grid of branch cards (ADR 006). Data is the compiled `treeData`. Anchors:
 * branch card `id="<branch>"`, stem row `id="<branch>/<stem-slug>"`; `/topics#<branch>/<stem>` opens that stem.
 */
@Page("/topics")
@Composable
fun TopicsPage() {
    document.title = PAGE_TITLE
    var hash by remember { mutableStateOf(currentHash()) }
    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = { hash = currentHash() }
        window.addEventListener("hashchange", listener)
        onDispose { window.removeEventListener("hashchange", listener) }
    }
    // The browser scrolls to the fragment before the cards exist; scroll again once they are drawn.
    LaunchedEffect(hash) {
        if (hash.isNotEmpty()) {
            window.requestAnimationFrame {
                document.getElementById(hash)?.asDynamic()?.scrollIntoView()
            }
        }
    }
    val leaves = treeData.boughs.flatMap { it.branches }.flatMap { it.stems }.flatMap { it.leaves }
    val titles = leaves.associate { it.id to it.title }
    val startLeaf = leaves.firstOrNull { it.id == treeData.startHere }
    val startHref = startLeaf?.takeIf { it.status == LeafStatus.LIVE }?.path ?: "$TREE_HREF#$FOUNDATION"
    // A leaf-id hash (branch/stem/leaf) still opens its stem.
    val hashStem = hash.split("/").take(2).joinToString("/")
    SiteHeader(NavItem.Topics)
    Main(
        Modifier.styleModifier {
            property("box-sizing", "border-box")
            property("max-width", layoutPageMaxVar.value())
            property("margin", "0 auto")
            property("padding", "${space8Var.value()} ${layoutPageGutterVar.value()} 0")
            property("font-family", fontUiVar.value())
            property("font-size", typeBodySizeVar.value())
            property("line-height", typeBodyLineVar.value())
            property("color", colorOnSurfaceVar.value())
        }.toAttrs()
    ) {
        H1(
            Modifier.styleModifier {
                property("margin", "0 0 ${space2Var.value()}")
                property("font-size", typeHeadlineSizeVar.value())
                property("line-height", typeHeadlineLineVar.value())
                property("font-weight", typeHeadlineWeightVar.value())
            }.toAttrs()
        ) { Text("Topics") }
        P(Modifier.styleModifier {
            property("margin", "0 0 ${space4Var.value()}")
            property("color", colorOnSurfaceMutedVar.value())
        }.toAttrs()) {
            Text("Every branch of the tree, topic by topic. Open a stem to see its topics and where each one stands.")
        }
        Div(
            Modifier.styleModifier {
                property("display", "flex")
                property("flex-wrap", "wrap")
                property("gap", space4Var.value())
            }.toAttrs()
        ) {
            StatusChip(ChipStatus.Live)
            StatusChip(ChipStatus.UpNext)
            StatusChip(ChipStatus.Shadow)
        }
    }
    BentoGrid {
        treeData.boughs.sortedBy { it.order }.forEach { bough ->
            val branches = bough.branches.sortedBy { it.order }
            BoughHeader(bough.title, branches.first().id)
            branches.forEach { branch ->
                val kind = when {
                    branch.id == FOUNDATION -> CardKind.Hero
                    branch.stems.size >= WIDE_STEM_COUNT -> CardKind.Wide
                    else -> CardKind.Regular
                }
                val openStemId = hashStem.takeIf { it.startsWith(branch.id + "/") }
                BranchCard(branch, kind, titles, openStemId, startHref)
            }
        }
    }
}
