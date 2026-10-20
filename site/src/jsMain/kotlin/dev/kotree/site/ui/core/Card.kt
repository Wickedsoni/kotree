package dev.kotree.site.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.branchTint
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorSurfaceVar
import dev.kotree.site.elevation1Var
import dev.kotree.site.inBranch
import dev.kotree.site.motion.Spring
import dev.kotree.site.shapeLgIncreasedVar
import dev.kotree.site.shapeXlIncreasedVar
import dev.kotree.site.shapeXlVar
import dev.kotree.site.shapeXxlVar
import dev.kotree.site.spaceInsetCardVar
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div

/** Card looks (ADR 007): [Outlined] = surface with a hairline, [Tonal] = branch tint, [Hero] = tint with the largest radius. */
enum class CardVariant { Outlined, Tonal, Hero }

private val morphSpring: Pair<Double, String> by lazy {
    Spring(Tokens.Spring.spatial.dampingRatio, Tokens.Spring.spatial.stiffness).cssLinear()
}

/**
 * Surface card (ADR 007). Padding is space.inset.card. [inkBand] adds a branch-ink band on the top edge. With [href] the
 * whole card is one `<a>` that shows the hover state layer and morphs to shape.xl.increased; without it the card has
 * no hover effect, so it does not look clickable. [branch] is a branch slug: it supplies the tint and scopes the focus ring.
 */
@Composable
fun SurfaceCard(
    variant: CardVariant,
    branch: String,
    modifier: Modifier = Modifier,
    inkBand: Boolean = false,
    href: String? = null,
    content: @Composable () -> Unit,
) {
    var hovered by remember { mutableStateOf(false) }
    val linked = href != null
    val (seconds, easing) = morphSpring
    val layer = "color-mix(in srgb, ${colorOnSurfaceVar.value()} ${Tokens.StateLayer.hoverPercent}%, transparent)"
    val rest = when (variant) {
        CardVariant.Outlined -> shapeXlVar.value().toString()
        CardVariant.Tonal -> shapeLgIncreasedVar.value().toString()
        CardVariant.Hero -> shapeXxlVar.value().toString()
    }
    val radius = if (linked && hovered) shapeXlIncreasedVar.value().toString() else rest
    val style: StyleScope.() -> Unit = {
        property("display", "block")
        property("box-sizing", "border-box")
        property("padding", spaceInsetCardVar.value())
        property("color", colorOnSurfaceVar.value())
        property("text-decoration", "none")
        property("border-radius", radius)
        when (variant) {
            CardVariant.Outlined -> {
                property("background-color", colorSurfaceVar.value())
                property("border", elevation1Var.value())
            }
            CardVariant.Tonal, CardVariant.Hero -> property("background-color", branchTint(branch).value().toString())
        }
        if (inkBand) property("border-top", "${Tokens.Spacing.s1}px solid ${branchInk(branch).value()}")
        if (linked) {
            if (hovered) property("background-image", "linear-gradient($layer, $layer)")
            property("transition", "border-radius ${seconds}s $easing")
        }
    }
    val scoped = modifier.inBranch(branch).styleModifier(style)
    if (href != null) {
        A(
            href,
            scoped.toAttrs {
                onMouseEnter { hovered = true }
                onMouseLeave { hovered = false }
                onBlur { hovered = false }
            }
        ) { content() }
    } else {
        Div(scoped.toAttrs()) { content() }
    }
}
