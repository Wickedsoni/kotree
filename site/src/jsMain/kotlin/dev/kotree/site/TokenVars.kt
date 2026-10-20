package dev.kotree.site

import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.setVariable
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgb

/*
 * Design tokens as CSS custom properties (docs/02 section 3.1, docs/13 section 2).
 * No literal lives here: every value is read from Tokens.kt. Names follow the docs/02 section 3.1 rule, with one
 * observed exception (docs/reviews/P1-probes.md, P0-5): Kobweb does not put a dash before a digit, so
 * space2Var is `--space2`, not `--space-2`. The token name stays the contract; read variables with `.value()`
 * or build the name as "--" + xVar.name, never by typing the string.
 */

val colorSurfaceVar by StyleVariable<CSSColorValue>()
val colorOnSurfaceVar by StyleVariable<CSSColorValue>()
val colorOnSurfaceMutedVar by StyleVariable<CSSColorValue>()
val colorOutlineVar by StyleVariable<CSSColorValue>()
val colorFocusRingVar by StyleVariable<CSSColorValue>()
val colorOnBranchVar by StyleVariable<CSSColorValue>()
val colorBranchFoundationVar by StyleVariable<CSSColorValue>()
val colorBranchFoundationTintVar by StyleVariable<CSSColorValue>()
val colorBranchKotlinVar by StyleVariable<CSSColorValue>()
val colorBranchKotlinTintVar by StyleVariable<CSSColorValue>()
val colorBranchAndroidCoreVar by StyleVariable<CSSColorValue>()
val colorBranchAndroidCoreTintVar by StyleVariable<CSSColorValue>()
val colorBranchUiVar by StyleVariable<CSSColorValue>()
val colorBranchUiTintVar by StyleVariable<CSSColorValue>()
val colorBranchConcurrencyVar by StyleVariable<CSSColorValue>()
val colorBranchConcurrencyTintVar by StyleVariable<CSSColorValue>()
val colorBranchArchitectureDataVar by StyleVariable<CSSColorValue>()
val colorBranchArchitectureDataTintVar by StyleVariable<CSSColorValue>()
val colorBranchAppNetworkingVar by StyleVariable<CSSColorValue>()
val colorBranchAppNetworkingTintVar by StyleVariable<CSSColorValue>()
val colorBranchKmpVar by StyleVariable<CSSColorValue>()
val colorBranchKmpTintVar by StyleVariable<CSSColorValue>()
val colorBranchDsaVar by StyleVariable<CSSColorValue>()
val colorBranchDsaTintVar by StyleVariable<CSSColorValue>()
val colorBranchDaaVar by StyleVariable<CSSColorValue>()
val colorBranchDaaTintVar by StyleVariable<CSSColorValue>()
val colorBranchOsVar by StyleVariable<CSSColorValue>()
val colorBranchOsTintVar by StyleVariable<CSSColorValue>()
val colorBranchNetworkingVar by StyleVariable<CSSColorValue>()
val colorBranchNetworkingTintVar by StyleVariable<CSSColorValue>()
val colorBranchTestingVar by StyleVariable<CSSColorValue>()
val colorBranchTestingTintVar by StyleVariable<CSSColorValue>()
val colorBranchProfilingVar by StyleVariable<CSSColorValue>()
val colorBranchProfilingTintVar by StyleVariable<CSSColorValue>()
val colorBranchNativeNdkVar by StyleVariable<CSSColorValue>()
val colorBranchNativeNdkTintVar by StyleVariable<CSSColorValue>()
val colorBranchSecurityVar by StyleVariable<CSSColorValue>()
val colorBranchSecurityTintVar by StyleVariable<CSSColorValue>()
val colorBranchToolsVar by StyleVariable<CSSColorValue>()
val colorBranchToolsTintVar by StyleVariable<CSSColorValue>()
val colorBoughRootsVar by StyleVariable<CSSColorValue>()
val colorBoughKotlinAndroidVar by StyleVariable<CSSColorValue>()
val colorBoughArchitectureKmpVar by StyleVariable<CSSColorValue>()
val colorBoughCsTheoryVar by StyleVariable<CSSColorValue>()
val colorBoughCraftVar by StyleVariable<CSSColorValue>()
val colorFeedbackCorrectVar by StyleVariable<CSSColorValue>()
val colorFeedbackCorrectTintVar by StyleVariable<CSSColorValue>()
val colorFeedbackWrongVar by StyleVariable<CSSColorValue>()
val colorFeedbackWrongTintVar by StyleVariable<CSSColorValue>()
val colorFeedbackHintVar by StyleVariable<CSSColorValue>()
val colorFeedbackHintTintVar by StyleVariable<CSSColorValue>()
val fontUiVar by StyleVariable<String>()
val fontCodeVar by StyleVariable<String>()
val typeDisplaySizeVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeDisplayLineVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeDisplayWeightVar by StyleVariable<Number>()
val typeHeadlineSizeVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeHeadlineLineVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeHeadlineWeightVar by StyleVariable<Number>()
val typeTitleSizeVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeTitleLineVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeTitleWeightVar by StyleVariable<Number>()
val typeBodySizeVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeBodyLineVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeBodyWeightVar by StyleVariable<Number>()
val typeLabelSizeVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeLabelLineVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeLabelWeightVar by StyleVariable<Number>()
val typeCodeSizeVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeCodeLineVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val typeCodeWeightVar by StyleVariable<Number>()
val shapeXsVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val shapeMdVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val shapeXlVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val shapeFullVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val shapeBlobVar by StyleVariable<String>()
val space1Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val space2Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val space3Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val space4Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val space6Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val space8Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val space12Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val space16Var by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val elevation0Var by StyleVariable<String>()
val elevation1Var by StyleVariable<String>()
val elevation2Var by StyleVariable<String>()
val layoutViewportMinVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val layoutLessonTextVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val layoutLessonWideVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()
val layoutPageGutterVar by StyleVariable<CSSSizeValue<CSSUnit.px>>()

private fun rgbOf(rgb: Int): CSSColorValue = rgb((rgb shr 16) and 0xFF, (rgb shr 8) and 0xFF, rgb and 0xFF)
private fun rgbText(rgb: Int): String = "rgb(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF})"
private fun rgbaText(rgb: Int, alphaPercent: Int): String =
    "rgba(${(rgb shr 16) and 0xFF}, ${(rgb shr 8) and 0xFF}, ${rgb and 0xFF}, ${alphaPercent / 100.0})"
private fun quoted(family: String): String = if (family.contains(' ')) "\"$family\"" else family

/** Sets every token variable. Apply once, on the root element (AppEntry). */
fun Modifier.tokenVariables(): Modifier = this
        .setVariable(colorSurfaceVar, rgbOf(Tokens.Color.surface))
        .setVariable(colorOnSurfaceVar, rgbOf(Tokens.Color.onSurface))
        .setVariable(colorOnSurfaceMutedVar, rgbOf(Tokens.Color.onSurfaceMuted))
        .setVariable(colorOutlineVar, rgbOf(Tokens.Color.outline))
        .setVariable(colorFocusRingVar, rgbOf(Tokens.Chrome.focusRingDefault))
        .setVariable(colorOnBranchVar, rgbOf(Tokens.Chrome.onBranch))
        .setVariable(colorBranchFoundationVar, rgbOf(Tokens.Branch.foundation))
        .setVariable(colorBranchFoundationTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.foundation)))
        .setVariable(colorBranchKotlinVar, rgbOf(Tokens.Branch.kotlin))
        .setVariable(colorBranchKotlinTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.kotlin)))
        .setVariable(colorBranchAndroidCoreVar, rgbOf(Tokens.Branch.androidCore))
        .setVariable(colorBranchAndroidCoreTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.androidCore)))
        .setVariable(colorBranchUiVar, rgbOf(Tokens.Branch.ui))
        .setVariable(colorBranchUiTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.ui)))
        .setVariable(colorBranchConcurrencyVar, rgbOf(Tokens.Branch.concurrency))
        .setVariable(colorBranchConcurrencyTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.concurrency)))
        .setVariable(colorBranchArchitectureDataVar, rgbOf(Tokens.Branch.architectureData))
        .setVariable(colorBranchArchitectureDataTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.architectureData)))
        .setVariable(colorBranchAppNetworkingVar, rgbOf(Tokens.Branch.appNetworking))
        .setVariable(colorBranchAppNetworkingTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.appNetworking)))
        .setVariable(colorBranchKmpVar, rgbOf(Tokens.Branch.kmp))
        .setVariable(colorBranchKmpTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.kmp)))
        .setVariable(colorBranchDsaVar, rgbOf(Tokens.Branch.dsa))
        .setVariable(colorBranchDsaTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.dsa)))
        .setVariable(colorBranchDaaVar, rgbOf(Tokens.Branch.daa))
        .setVariable(colorBranchDaaTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.daa)))
        .setVariable(colorBranchOsVar, rgbOf(Tokens.Branch.os))
        .setVariable(colorBranchOsTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.os)))
        .setVariable(colorBranchNetworkingVar, rgbOf(Tokens.Branch.networking))
        .setVariable(colorBranchNetworkingTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.networking)))
        .setVariable(colorBranchTestingVar, rgbOf(Tokens.Branch.testing))
        .setVariable(colorBranchTestingTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.testing)))
        .setVariable(colorBranchProfilingVar, rgbOf(Tokens.Branch.profiling))
        .setVariable(colorBranchProfilingTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.profiling)))
        .setVariable(colorBranchNativeNdkVar, rgbOf(Tokens.Branch.nativeNdk))
        .setVariable(colorBranchNativeNdkTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.nativeNdk)))
        .setVariable(colorBranchSecurityVar, rgbOf(Tokens.Branch.security))
        .setVariable(colorBranchSecurityTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.security)))
        .setVariable(colorBranchToolsVar, rgbOf(Tokens.Branch.tools))
        .setVariable(colorBranchToolsTintVar, rgbOf(Tokens.Branch.tint(Tokens.Branch.tools)))
        .setVariable(colorBoughRootsVar, rgbOf(Tokens.Bough.roots))
        .setVariable(colorBoughKotlinAndroidVar, rgbOf(Tokens.Bough.kotlinAndroid))
        .setVariable(colorBoughArchitectureKmpVar, rgbOf(Tokens.Bough.architectureKmp))
        .setVariable(colorBoughCsTheoryVar, rgbOf(Tokens.Bough.csTheory))
        .setVariable(colorBoughCraftVar, rgbOf(Tokens.Bough.craft))
        .setVariable(colorFeedbackCorrectVar, rgbOf(Tokens.Chrome.feedbackCorrect))
        .setVariable(colorFeedbackCorrectTintVar, rgbOf(Tokens.Branch.tint(Tokens.Chrome.feedbackCorrect)))
        .setVariable(colorFeedbackWrongVar, rgbOf(Tokens.Chrome.feedbackWrong))
        .setVariable(colorFeedbackWrongTintVar, rgbOf(Tokens.Branch.tint(Tokens.Chrome.feedbackWrong)))
        .setVariable(colorFeedbackHintVar, rgbOf(Tokens.Chrome.feedbackHint))
        .setVariable(colorFeedbackHintTintVar, rgbOf(Tokens.Branch.tint(Tokens.Chrome.feedbackHint)))
        .setVariable(fontUiVar, Tokens.Type.fontUi.joinToString(", ") { quoted(it) })
        .setVariable(fontCodeVar, Tokens.Type.fontCode.joinToString(", ") { quoted(it) })
        .setVariable(typeDisplaySizeVar, Tokens.TypeScale.display.sizePx.px)
        .setVariable(typeDisplayLineVar, Tokens.TypeScale.display.lineHeightPx.px)
        .setVariable(typeDisplayWeightVar, Tokens.TypeScale.display.weight)
        .setVariable(typeHeadlineSizeVar, Tokens.TypeScale.headline.sizePx.px)
        .setVariable(typeHeadlineLineVar, Tokens.TypeScale.headline.lineHeightPx.px)
        .setVariable(typeHeadlineWeightVar, Tokens.TypeScale.headline.weight)
        .setVariable(typeTitleSizeVar, Tokens.TypeScale.title.sizePx.px)
        .setVariable(typeTitleLineVar, Tokens.TypeScale.title.lineHeightPx.px)
        .setVariable(typeTitleWeightVar, Tokens.TypeScale.title.weight)
        .setVariable(typeBodySizeVar, Tokens.TypeScale.body.sizePx.px)
        .setVariable(typeBodyLineVar, Tokens.TypeScale.body.lineHeightPx.px)
        .setVariable(typeBodyWeightVar, Tokens.TypeScale.body.weight)
        .setVariable(typeLabelSizeVar, Tokens.TypeScale.label.sizePx.px)
        .setVariable(typeLabelLineVar, Tokens.TypeScale.label.lineHeightPx.px)
        .setVariable(typeLabelWeightVar, Tokens.TypeScale.label.weight)
        .setVariable(typeCodeSizeVar, Tokens.TypeScale.code.sizePx.px)
        .setVariable(typeCodeLineVar, Tokens.TypeScale.code.lineHeightPx.px)
        .setVariable(typeCodeWeightVar, Tokens.TypeScale.code.weight)
        .setVariable(shapeXsVar, Tokens.Shape.xs.px)
        .setVariable(shapeMdVar, Tokens.Shape.md.px)
        .setVariable(shapeXlVar, Tokens.Shape.xl.px)
        .setVariable(shapeFullVar, Tokens.Shape.full.px)
        .setVariable(shapeBlobVar, "${Tokens.Shape.blobLarge}px ${Tokens.Shape.blobSmall}px ${Tokens.Shape.blobLarge}px ${Tokens.Shape.blobSmall}px")
        .setVariable(space1Var, Tokens.Spacing.s1.px)
        .setVariable(space2Var, Tokens.Spacing.s2.px)
        .setVariable(space3Var, Tokens.Spacing.s3.px)
        .setVariable(space4Var, Tokens.Spacing.s4.px)
        .setVariable(space6Var, Tokens.Spacing.s6.px)
        .setVariable(space8Var, Tokens.Spacing.s8.px)
        .setVariable(space12Var, Tokens.Spacing.s12.px)
        .setVariable(space16Var, Tokens.Spacing.s16.px)
        .setVariable(elevation0Var, "none")
        .setVariable(elevation1Var, "${Tokens.Elevation.borderPx}px solid ${rgbText(Tokens.Color.outline)}")
        .setVariable(elevation2Var, "0 ${Tokens.Elevation.shadowOffsetYPx}px ${Tokens.Elevation.shadowBlurPx}px ${rgbaText(Tokens.Color.bark, Tokens.Elevation.shadowAlphaPercent)}")
        .setVariable(layoutViewportMinVar, Tokens.LayoutW.viewportMin.px)
        .setVariable(layoutLessonTextVar, Tokens.LayoutW.lessonText.px)
        .setVariable(layoutLessonWideVar, Tokens.LayoutW.lessonWide.px)
        .setVariable(layoutPageGutterVar, Tokens.LayoutW.gutter.px)

/** Ink and tint variables of a branch slug (docs/00 section 5), for components that take a slug. */
fun branchInk(slug: String): StyleVariable<CSSColorValue, *> = branchVars.getValue(slug).first

fun branchTint(slug: String): StyleVariable<CSSColorValue, *> = branchVars.getValue(slug).second

/** Scope a subtree to one branch: the focus ring takes the branch ink (docs/02 section 2.1). */
fun Modifier.inBranch(slug: String): Modifier = setVariable(colorFocusRingVar, rgbOf(Tokens.Branch.inks.first { it.first == slug }.second))

private val branchVars: Map<String, Pair<StyleVariable<CSSColorValue, *>, StyleVariable<CSSColorValue, *>>> = mapOf(
    "foundation" to (colorBranchFoundationVar to colorBranchFoundationTintVar),
    "kotlin" to (colorBranchKotlinVar to colorBranchKotlinTintVar),
    "android-core" to (colorBranchAndroidCoreVar to colorBranchAndroidCoreTintVar),
    "ui" to (colorBranchUiVar to colorBranchUiTintVar),
    "concurrency" to (colorBranchConcurrencyVar to colorBranchConcurrencyTintVar),
    "architecture-data" to (colorBranchArchitectureDataVar to colorBranchArchitectureDataTintVar),
    "app-networking" to (colorBranchAppNetworkingVar to colorBranchAppNetworkingTintVar),
    "kmp" to (colorBranchKmpVar to colorBranchKmpTintVar),
    "dsa" to (colorBranchDsaVar to colorBranchDsaTintVar),
    "daa" to (colorBranchDaaVar to colorBranchDaaTintVar),
    "os" to (colorBranchOsVar to colorBranchOsTintVar),
    "networking" to (colorBranchNetworkingVar to colorBranchNetworkingTintVar),
    "testing" to (colorBranchTestingVar to colorBranchTestingTintVar),
    "profiling" to (colorBranchProfilingVar to colorBranchProfilingTintVar),
    "native-ndk" to (colorBranchNativeNdkVar to colorBranchNativeNdkTintVar),
    "security" to (colorBranchSecurityVar to colorBranchSecurityTintVar),
    "tools" to (colorBranchToolsVar to colorBranchToolsTintVar),
)
