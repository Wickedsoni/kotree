package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.layoutMeasureShortVar
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.space1Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.spaceStackSectionVar
import dev.kotree.site.tree.model.Branch
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.tree.model.treeData
import dev.kotree.site.typeBodyLineVar
import dev.kotree.site.typeBodySizeVar
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.ui.core.Meter
import dev.kotree.site.ui.core.SectionHeader
import dev.kotree.site.ui.core.StatusIcon
import dev.kotree.site.ui.layout.NavItem
import dev.kotree.site.ui.shell.PageShell
import dev.kotree.site.ui.topics.BoughFilter
import dev.kotree.site.ui.topics.BranchCard
import dev.kotree.site.ui.topics.BranchGrid
import dev.kotree.site.ui.topics.FoundationHero
import dev.kotree.site.ui.topics.INTRO_CLASS
import dev.kotree.site.ui.topics.STEMS_COLLAPSED
import dev.kotree.site.ui.topics.StemSheet
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event
import dev.kotree.site.ui.core.LeafStatus as ChipStatus

private const val PAGE_TITLE = "Topics - Kotree"
private const val TREE_HREF = "/"
private const val FOUNDATION = "foundation"
private const val INTRO_TEXT =
    "Every branch of the tree, topic by topic. Open a stem to see its topics and where each one stands."

private val boughs = treeData.boughs.sortedBy { it.order }
private val allBranches: List<Branch> = boughs.flatMap { b -> b.branches.sortedBy { it.order } }
private val foundation: Branch? = allBranches.firstOrNull { it.id == FOUNDATION }

/** Boughs that hold at least one grid branch (Foundation has its own banner). */
private val gridBoughs = boughs.filter { bough -> bough.branches.any { it.id != FOUNDATION } }
private val stemById: Map<String, Stem> = allBranches.flatMap { it.stems }.associateBy { it.id }
private val branchById: Map<String, Branch> = allBranches.associateBy { it.id }
private val leafTitles: Map<String, String> =
    allBranches.flatMap { it.stems }.flatMap { it.leaves }.associate { it.id to it.title }

private fun currentHash(): String = window.location.hash.removePrefix("#")

/** The stem a hash points at: `<branch>/<stem>` or a leaf id `<branch>/<stem>/<leaf>`. */
private fun stemOfHash(hash: String): Stem? {
    val parts = hash.split("/")
    return if (parts.size >= 2) stemById[parts[0] + "/" + parts[1]] else null
}

/** True when the stem is one of the cards' hidden rows, so the card must open its disclosure. */
private fun isCollapsedStem(stem: Stem): Boolean {
    val branch = branchById[stem.id.substringBefore("/")] ?: return false
    return branch.stems.sortedBy { it.order }.indexOfFirst { it.id == stem.id } >= STEMS_COLLAPSED
}

/**
 * All topics (ADR 007): intro with the overall meter and the status legend, the Foundation banner, a bough filter, and
 * one grid of branch cards (1 / 2 / 4 columns, every row full). A stem opens a side sheet with its leaves. Anchors:
 * branch card `id="<branch>"`, stem row `id="<branch>/<stem-slug>"`. `/topics#<branch>/<stem>` opens that sheet; opening
 * or closing a sheet rewrites the hash with `history.replaceState`, so there is no scroll jump and no extra history entry.
 */
@Page("/topics")
@Composable
fun TopicsPage() {
    document.title = PAGE_TITLE
    var hash by remember { mutableStateOf(currentHash()) }
    var openStemId by remember { mutableStateOf(stemOfHash(currentHash())?.id) }
    var selectedBough by remember { mutableStateOf(0) }
    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = {
            hash = currentHash()
            openStemId = stemOfHash(hash)?.id
        }
        window.addEventListener("hashchange", listener)
        onDispose { window.removeEventListener("hashchange", listener) }
    }
    // The browser scrolls to the fragment before the cards exist; scroll again once they are drawn.
    LaunchedEffect(hash) {
        if (hash.isNotEmpty()) {
            window.requestAnimationFrame {
                val target = document.getElementById(hash) ?: stemOfHash(hash)?.let { document.getElementById(it.id) }
                target?.asDynamic()?.scrollIntoView()
            }
        }
    }
    val openStem = { stem: Stem ->
        openStemId = stem.id
        window.history.replaceState(null, "", "#" + stem.id)
    }
    val closeSheet = {
        val id = openStemId
        openStemId = null
        if (id != null) window.history.replaceState(null, "", "#" + id.substringBefore("/"))
    }
    val startLeaf = treeData.startHere.let { id -> allBranches.flatMap { it.stems }.flatMap { it.leaves }.firstOrNull { it.id == id } }
    val startHref = startLeaf?.takeIf { it.status == LeafStatus.LIVE }?.path ?: "$TREE_HREF#$FOUNDATION"
    val sheetStem = openStemId?.let { stemById[it] }
    val sheetBranch = sheetStem?.let { branchById[it.id.substringBefore("/")] }

    PageShell(NavItem.Topics) {
        Div(
            Modifier.styleModifier {
                property("display", "grid")
                property("gap", spaceStackSectionVar.value())
            }.toAttrs()
        ) {
            Intro()
            if (foundation != null) FoundationHero(foundation, startHref, openStem)
            Div(Modifier.styleModifier { property("display", "grid"); property("gap", space6Var.value()) }.toAttrs()) {
                BoughFilter(gridBoughs.map { it.title }, selectedBough) { selectedBough = it }
                BranchGrid {
                    gridBoughs.forEach { bough ->
                        bough.branches.sortedBy { it.order }.filter { it.id != FOUNDATION }.forEach { branch ->
                            val inFilter = selectedBough == 0 || gridBoughs[selectedBough - 1].id == bough.id
                            val forceAll = sheetStem != null && sheetStem.id.startsWith(branch.id + "/") && isCollapsedStem(sheetStem)
                            key(branch.id) {
                                BranchCard(branch, bough.title, inFilter, forceAll, openStem)
                            }
                        }
                    }
                }
            }
        }
        if (sheetStem != null && sheetBranch != null) {
            key(sheetStem.id) { StemSheet(sheetBranch, sheetStem, leafTitles, closeSheet) }
        }
    }
}

/** Title, one sentence, the overall meter and the legend. Two columns from layout.bp.large (stylesheet rule), stacked below. */
@Composable
private fun Intro() {
    Div(Modifier.toAttrs { classes(INTRO_CLASS) }) {
        Div(Modifier.styleModifier { property("display", "grid"); property("gap", space3Var.value()) }.toAttrs()) {
            SectionHeader("Topics", level = 1)
            P(
                Modifier.styleModifier {
                    property("margin", "0")
                    property("max-width", layoutMeasureShortVar.value())
                    property("color", colorOnSurfaceMutedVar.value())
                    property("font-size", typeBodySizeVar.value())
                    property("line-height", typeBodyLineVar.value())
                }.toAttrs()
            ) { Text(INTRO_TEXT) }
        }
        Div(Modifier.styleModifier { property("display", "grid"); property("gap", space4Var.value()) }.toAttrs()) {
            Meter(treeData.counts.live, treeData.counts.total, "Live topics in the whole tree", FOUNDATION)
            Legend()
        }
    }
}

/** The three statuses with their icons, written out once for the whole page. */
@Composable
private fun Legend() {
    Div(
        Modifier.styleModifier {
            property("display", "flex")
            property("flex-wrap", "wrap")
            property("gap", space4Var.value())
        }.toAttrs()
    ) {
        listOf(ChipStatus.Live, ChipStatus.UpNext, ChipStatus.Shadow).forEach { status ->
            Span(
                Modifier.styleModifier {
                    property("display", "inline-flex")
                    property("align-items", "center")
                    property("gap", space1Var.value())
                    property("color", colorOnSurfaceVar.value())
                    property("font-size", typeLabelSizeVar.value())
                    property("line-height", typeLabelLineVar.value())
                    property("font-weight", typeLabelWeightVar.value())
                }.toAttrs()
            ) {
                StatusIcon(status)
                Text(status.label)
            }
        }
    }
}
