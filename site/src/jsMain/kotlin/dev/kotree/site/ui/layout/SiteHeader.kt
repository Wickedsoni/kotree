package dev.kotree.site.ui.layout

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.elevation2Var
import dev.kotree.site.fontUiVar
import dev.kotree.site.shapeFullVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.tree.model.treeData
import dev.kotree.site.typeLabelLineVar
import dev.kotree.site.typeLabelSizeVar
import dev.kotree.site.typeLabelWeightVar
import dev.kotree.site.typeTitleLineVar
import dev.kotree.site.typeTitleSizeVar
import dev.kotree.site.typeTitleWeightVar
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Nav
import org.jetbrains.compose.web.dom.Text

/** Which nav pill is current (ADR 006). [Lesson] marks no pill: a lesson is not a destination of the nav. */
enum class NavItem { Tree, Topics, Lesson }

private const val BRANCH_DEFAULT = "kotlin"
private const val HOME_HREF = "/"
private const val TOPICS_HREF = "/topics"

/**
 * Site header (ADR 006): wordmark linking home, nav pills (Home, Topics) in one flex row with a gap, and the
 * progress counter. [floating] = true sits over the tree canvas (surface fill, elevation.2, shape.full);
 * false is in page flow (bottom border). The current pill carries `aria-current="page"` and a tonal fill.
 */
@Composable
fun SiteHeader(current: NavItem, floating: Boolean = false, modifier: Modifier = Modifier) {
    Header(
        modifier
            .styleModifier {
                property("display", "flex")
                property("align-items", "center")
                property("gap", space4Var.value())
                property("box-sizing", "border-box")
                property("padding", "${space2Var.value()} ${space6Var.value()}")
                property("background-color", colorSurfaceVar.value())
                property("font-family", fontUiVar.value())
                if (floating) {
                    property("box-shadow", elevation2Var.value())
                    property("border-radius", shapeFullVar.value())
                } else {
                    property("border-bottom", elevation1Var.value())
                }
            }
            .toAttrs()
    ) {
        A(
            HOME_HREF,
            Modifier.styleModifier {
                property("color", branchInk("foundation").value().toString())
                property("text-decoration", "none")
                property("font-size", typeTitleSizeVar.value())
                property("line-height", typeTitleLineVar.value())
                property("font-weight", typeTitleWeightVar.value())
            }.toAttrs()
        ) { Text("Kotree") }
        Nav(
            Modifier.styleModifier {
                property("display", "flex")
                property("align-items", "center")
                property("gap", space2Var.value())
            }.toAttrs { attr("aria-label", "Main") }
        ) {
            NavPill("Home", HOME_HREF, current == NavItem.Tree)
            NavPill("Topics", TOPICS_HREF, current == NavItem.Topics)
        }
        ProgressCounter(treeData.counts.live, treeData.counts.total)
    }
}

/** One nav pill: a link with a 44 px target. Current = tonal fill and ink text; the focus ring comes from Base.kt. */
@Composable
private fun NavPill(text: String, href: String, isCurrent: Boolean) {
    A(
        href,
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("justify-content", "center")
            property("box-sizing", "border-box")
            property("min-height", Tokens.Spacing.minTargetPx.px)
            property("min-width", Tokens.Spacing.minTargetPx.px)
            property("padding", "0 ${space3Var.value()}")
            property("border-radius", shapeFullVar.value())
            property("background-color", if (isCurrent) branchTint(BRANCH_DEFAULT).value().toString() else "transparent")
            property("color", if (isCurrent) branchInk(BRANCH_DEFAULT).value().toString() else colorOnSurfaceVar.value().toString())
            property("text-decoration", "none")
            property("font-family", fontUiVar.value())
            property("font-size", typeLabelSizeVar.value())
            property("line-height", typeLabelLineVar.value())
            property("font-weight", typeLabelWeightVar.value())
            property("white-space", "nowrap")
        }.toAttrs {
            if (isCurrent) attr("aria-current", "page")
        }
    ) { Text(text) }
}
