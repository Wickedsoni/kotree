package dev.kotree.site.ui.topics

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.dom.Div

/**
 * The branch grid (ADR 007; the file keeps its ADR 006 name). One grid for all branch cards in tree order, with fixed
 * 1 / 2 / 4 columns from the stylesheet rule of [GRID_CLASS], so every row is full and cards in a row have equal height.
 */
@Composable
fun BranchGrid(content: @Composable () -> Unit) {
    Div(Modifier.toAttrs { classes(GRID_CLASS) }) { content() }
}
