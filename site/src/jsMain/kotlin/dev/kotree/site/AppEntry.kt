package dev.kotree.site

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import dev.kotree.site.ui.baseTypography
import org.jetbrains.compose.web.css.vh

/** White only, no dark mode (L9): the colour mode is fixed to LIGHT and never read from the system. */
@InitSilk
fun initColorMode(ctx: InitSilkContext) {
    ctx.config.initialColorMode = ColorMode.LIGHT
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    SilkApp {
        Box(
            Modifier
                .fillMaxSize()
                .minHeight(Tokens.Layout.fullViewportHeightVh.vh)
                .backgroundColor(Color.rgb(Tokens.Color.surface))
                .tokenVariables()
                .baseTypography()
        ) {
            content()
        }
    }
}
