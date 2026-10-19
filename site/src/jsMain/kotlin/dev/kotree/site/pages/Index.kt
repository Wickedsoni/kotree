package dev.kotree.site.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.core.Page
import dev.kotree.site.tree.TreeView
import dev.kotree.site.tree.model.sampleTree

/** Home: the whole tree on a white page (docs/12 §1). */
@Page
@Composable
fun HomePage() {
    TreeView(sampleTree)
}
