package dev.kotree.site.ui.topics

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.tree.model.Stem
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.ui.core.ListRow
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

/**
 * One stem of a branch card (ADR 007): a 44 px row with the title (ellipsis, full text in `title`) and "n/m" at the
 * end. It opens the stem sheet through [onOpen]. The `<li>` id is the stem id, the anchor `/topics#<branch>/<stem-slug>`.
 */
@Composable
fun StemRow(branch: String, stem: Stem, onOpen: (Stem) -> Unit) {
    Li(Modifier.attrsModifier { attr("id", stem.id) }.toAttrs()) {
        ListRow(
            title = stem.title,
            branch = branch,
            onClick = { onOpen(stem) },
            trailing = { StemCount(stem.counts.live, stem.counts.total) },
        )
    }
}

/** "n/m" in tabular figures. The visible text is short; the label says it in words. */
@Composable
fun StemCount(live: Int, total: Int) {
    Span(
        Modifier
            .attrsModifier {
                attr("role", "img")
                attr("aria-label", "$live of $total live")
            }
            .styleModifier {
                property("color", colorOnSurfaceMutedVar.value())
                property("font-size", typeLabelSizeVar.value())
                property("line-height", typeLabelLineVar.value())
                property("font-weight", typeLabelWeightVar.value())
                property("font-variant-numeric", "tabular-nums")
            }
            .toAttrs()
    ) { Text("$live/$total") }
}
