package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorOnSurfaceMutedVar
import dev.kotree.site.space4Var
import dev.kotree.site.typeBodySmallLineVar
import dev.kotree.site.typeBodySmallSizeVar
import dev.kotree.site.typeBodySmallWeightVar
import dev.kotree.site.typeTitleSmallLineVar
import dev.kotree.site.typeTitleSmallSizeVar
import dev.kotree.site.typeTitleSmallWeightVar
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

/** Empty state (ADR 007): muted, centred in its container, `space.4` between the parts. */
@Composable
fun EmptyState(
    title: String,
    body: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    Div(
        Modifier.styleModifier {
            property("display", "grid")
            property("justify-items", "center")
            property("text-align", "center")
            property("gap", space4Var.value())
            property("color", colorOnSurfaceMutedVar.value())
        }.toAttrs()
    ) {
        P(
            Modifier.styleModifier {
                property("margin", "0")
                property("font-size", typeTitleSmallSizeVar.value())
                property("line-height", typeTitleSmallLineVar.value())
                property("font-weight", typeTitleSmallWeightVar.value())
            }.toAttrs()
        ) { Text(title) }
        if (body != null) {
            P(
                Modifier.styleModifier {
                    property("margin", "0")
                    property("font-size", typeBodySmallSizeVar.value())
                    property("line-height", typeBodySmallLineVar.value())
                    property("font-weight", typeBodySmallWeightVar.value())
                }.toAttrs()
            ) { Text(body) }
        }
        if (action != null) action()
    }
}
