package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.Tokens
import dev.kotree.site.branchInk
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.fontUiVar
import dev.kotree.site.inBranch
import dev.kotree.site.layoutLessonTextVar
import dev.kotree.site.layoutLessonWideVar
import dev.kotree.site.layoutPageGutterVar
import dev.kotree.site.layoutRailWidthVar
import dev.kotree.site.lessons.BlockData
import dev.kotree.site.lessons.LeafInfo
import dev.kotree.site.lessons.LessonData
import dev.kotree.site.lessons.LessonState
import dev.kotree.site.lessons.findLeaf
import dev.kotree.site.lessons.isExporting
import dev.kotree.site.lessons.rememberLesson
import dev.kotree.site.space12Var
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.space6Var
import dev.kotree.site.space8Var
import dev.kotree.site.tree.model.LeafStatus
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import dev.kotree.site.ui.layout.Breadcrumb
import dev.kotree.site.ui.layout.NavItem
import dev.kotree.site.ui.layout.SiteHeader
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
import org.jetbrains.compose.web.dom.Main
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.Ul
import dev.kotree.site.tree.model.Difficulty as TreeDifficulty
import dev.kotree.site.ui.core.Difficulty as ChipDifficulty

/** The id of the element that holds the lesson body. The export bakes the static copy of the prose into it. */
const val LESSON_BODY_ID = "kt-lesson-body"

/** Shown where the Playground will run. Phase 6 replaces it (ADR 005 addendum, gap 2). */
private const val RUN_NOT_WIRED = "Running code is not switched on yet."

private const val RETRY_FAILED = "This lesson could not load."

/**
 * The lesson page for one leaf (ADR 005 section 2). [preview] reads `/lessons-preview/<id>.json`, accepts a draft,
 * shows a banner and marks the page noindex; it is never linked from anywhere.
 */
@Composable
fun LessonPage(id: String, preview: Boolean) {
    val info = findLeaf(id)
    if (info == null || (!preview && info.leaf.status != LeafStatus.LIVE)) {
        NotFoundContent(window.location.pathname)
        return
    }
    val leaf = info.leaf
    var retries by remember(id) { mutableStateOf(0) }
    val state = rememberLesson(
        id = id,
        url = if (preview) "/lessons-preview/$id.json" else "/lessons/$id.json",
        useInline = !preview,
        retries = retries,
    )
    DisposableEffect(id, preview) {
        document.title = (if (preview) "Preview: " else "") + leaf.title + " · Kotree"
        val meta = if (preview) document.createElement("meta").also {
            it.setAttribute("name", "robots"); it.setAttribute("content", "noindex"); document.head?.appendChild(it)
        } else null
        onDispose { meta?.let { document.head?.removeChild(it) } }
    }
    val wide = rememberWideLayout()
    val ready = state is LessonState.Ready && !isExporting()
    SiteHeader(NavItem.Lesson)
    Main(
        Modifier.inBranch(info.branch.id).styleModifier {
            property("box-sizing", "border-box")
            if (wide) {
                // text column + gap + rail, plus the gutter on both sides
                property(
                    "max-width",
                    "calc(${layoutLessonWideVar.value()} + ${space12Var.value()} + ${layoutRailWidthVar.value()} + " +
                        "${layoutPageGutterVar.value()} + ${layoutPageGutterVar.value()})"
                )
            } else {
                property("max-width", layoutLessonWideVar.value())
            }
            property("margin", "0 auto")
            property("padding", "${space8Var.value()} ${layoutPageGutterVar.value()} ${space12Var.value()}")
            property("font-family", fontUiVar.value())
            property("color", colorOnSurfaceVar.value())
        }.toAttrs()
    ) {
        Breadcrumb(crumbs = info.crumbs, branch = info.branch.id)
        if (preview) PreviewBanner(leaf.status)
        Div(
            Modifier.styleModifier {
                if (wide) {
                    property("display", "grid")
                    property("grid-template-columns", "minmax(0, ${layoutLessonWideVar.value()}) ${layoutRailWidthVar.value()}")
                    property("column-gap", space12Var.value())
                }
            }.toAttrs()
        ) {
            Div(Modifier.styleModifier { property("min-width", "0") }.toAttrs()) {
                Div(Modifier.styleModifier { property("margin", "${space4Var.value()} 0 ${space6Var.value()}") }.toAttrs()) {
                    LessonHeader(
                        title = leaf.title,
                        difficulty = leaf.difficulty.chip(),
                        branch = info.branch.id,
                        progressLine = info.progressLine,
                        videoUrl = leaf.video.orEmpty(),
                    )
                }
                Div(
                    Modifier.styleModifier {
                        property("box-sizing", "border-box")
                        property("max-width", layoutLessonTextVar.value())
                    }.toAttrs { id(LESSON_BODY_ID) }
                ) {
                    when {
                        isExporting() -> Unit // the export bakes the static prose here
                        state is LessonState.Ready -> LessonBody(state.data, info, preview)
                        state is LessonState.Failed -> LoadFailed { retries += 1 }
                        else -> Div(Modifier.styleModifier { property("min-height", "calc(${space12Var.value()} * 8)") }.toAttrs()) {}
                    }
                }
                if (state is LessonState.Ready && !isExporting()) {
                    if (preview && !state.data.notes.isNullOrEmpty()) ReviewerNotes(state.data.notes!!.toList())
                    if (wide) LessonLinks(info, cards = true)
                }
            }
            LessonRail(info, wide, ready)
        }
        // Narrow: previous / next already sit in the rail under the body, so only the way back is left here.
        if (ready && !wide) LessonLinks(info, cards = false)
    }
}

private fun TreeDifficulty.chip(): ChipDifficulty = when (this) {
    TreeDifficulty.EASY -> ChipDifficulty.Easy
    TreeDifficulty.MEDIUM -> ChipDifficulty.Medium
    TreeDifficulty.HARD -> ChipDifficulty.Hard
}

@Composable
private fun PreviewBanner(status: LeafStatus) {
    Div(
        Modifier.attrsModifier { attr("role", "note") }.styleModifier {
            property("margin-top", space4Var.value())
            property("padding", space3Var.value())
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", dev.kotree.site.shapeMdVar.value())
            typo(Typo.Label)
        }.toAttrs()
    ) {
        Text("Preview only. This page is not published, not linked and not indexed. Status: ${status.name.lowercase()}.")
    }
}

@Composable
private fun ReviewerNotes(notes: List<String>) {
    Div(
        Modifier.styleModifier {
            property("margin-top", space8Var.value())
            property("padding", space4Var.value())
            property("border", "${Tokens.Elevation.borderPx}px solid ${colorOutlineVar.value()}")
            property("border-radius", dev.kotree.site.shapeMdVar.value())
            typo(Typo.Body)
        }.toAttrs()
    ) {
        P(Modifier.styleModifier { property("margin", "0 0 ${space2Var.value()}"); typo(Typo.Title) }.toAttrs()) {
            Text("Author notes in the file (preview only)")
        }
        Ul { notes.forEach { Li { Text(it) } } }
    }
}

@Composable
private fun LoadFailed(onRetry: () -> Unit) {
    Div(Modifier.attrsModifier { attr("role", "alert") }.styleModifier { typo(Typo.Body) }.toAttrs()) {
        P { Text(RETRY_FAILED) }
        Button(text = "Retry", onClick = onRetry, kind = ButtonKind.Tonal)
    }
}

/** The blocks of one lesson, in page order, in one composition (ADR 005 section 2). */
@Composable
private fun LessonBody(data: LessonData, info: LeafInfo, preview: Boolean) {
    val branch = info.branch.id
    data.blocks.forEachIndexed { index, block ->
        key(data.id, index) {
            Div(Modifier.styleModifier { property("margin-bottom", space6Var.value()) }.toAttrs()) {
                Block(block, data, branch, preview)
            }
        }
    }
}

@Composable
private fun Block(block: BlockData, data: LessonData, branch: String, preview: Boolean) {
    when (block.t) {
        "html" -> Prose(block.html.orEmpty())
        "sim" -> SimHost(block.id.orEmpty(), branch, preview)
        "code" -> {
            val code = block.code.orEmpty()
            if (block.runnable == true && data.playground) {
                EditorShell(title = "Try it", branch = branch, runEnabled = false, runHint = RUN_NOT_WIRED) {
                    CodeBlock(code, branch = branch, label = "Kotlin code you can edit")
                }
            } else {
                CodeBlock(code, branch = branch, label = "Kotlin example")
            }
        }
        "predict" -> PredictOutput(
            id = block.id.orEmpty(),
            code = block.code.orEmpty(),
            options = block.options.orEmpty().map { PredictOption(it.id, it.text, it.why, it.correct) },
            output = block.output.orEmpty(),
            branch = branch,
            checkLabel = if (block.run == false) "Show the answer" else "Run it to check",
        )
        "fix" -> FixBug(
            code = block.code.orEmpty(),
            expected = block.expected.orEmpty(),
            hints = block.hints.orEmpty().toList(),
            fix = block.fix.orEmpty(),
            explanation = block.explanation.orEmpty(),
            branch = branch,
            runEnabled = false,
            runHint = RUN_NOT_WIRED,
        )
        else -> Unit
    }
}

/**
 * Sims that exist, by id. Empty until Phase 5 (docs/05). A sim that is not registered draws nothing on a live page, so
 * the "Try this" text beside it still teaches (docs/04 section 4.3), and a labelled frame in a preview.
 */
private val simRegistry: Map<String, @Composable () -> Unit> = emptyMap()

@Composable
private fun SimHost(id: String, branch: String, preview: Boolean) {
    val sim = simRegistry[id]
    when {
        sim != null -> SimFrame(title = "Simulation: $id", branch = branch) { sim() }
        preview -> SimFrame(title = "Simulation: $id", branch = branch) { Text("Simulation \"$id\" is not built yet. Preview only.") }
    }
}

@Composable
private fun LessonLinks(info: LeafInfo, cards: Boolean) {
    Div(Modifier.styleModifier { property("margin-top", space8Var.value()) }.toAttrs()) {
        if (cards) PrevNextCards(info)
        Div(Modifier.styleModifier { property("margin-top", space4Var.value()) }.toAttrs()) {
            NavLink("Back to the tree", "/#${info.leaf.id}", info.branch.id)
        }
    }
}

@Composable
private fun NavLink(text: String, href: String, branch: String) {
    A(
        href,
        Modifier.styleModifier {
            property("display", "inline-flex")
            property("align-items", "center")
            property("min-height", "${Tokens.Spacing.minTargetPx}px")
            property("color", branchInk(branch).value().toString())
            property("text-decoration", "underline")
        }.toAttrs()
    ) { Text(text) }
}

/**
 * Not found, or a leaf that has not grown yet (docs/03 section 7.4): reads the compiled tree data, no fetch.
 * [path] is the address bar path, such as `/kotlin/null-safety/what-is-null`.
 */
@Composable
fun NotFoundContent(path: String) {
    val info = findLeaf(path.trim('/'))
    DisposableEffect(path) {
        document.title = "Not found · Kotree"
        onDispose { }
    }
    SiteHeader(NavItem.Lesson)
    Main(
        Modifier.styleModifier {
            property("box-sizing", "border-box")
            property("max-width", layoutLessonTextVar.value())
            property("margin", "0 auto")
            property("padding", "${space12Var.value()} ${layoutPageGutterVar.value()}")
            property("font-family", fontUiVar.value())
            property("color", colorOnSurfaceVar.value())
            typo(Typo.Body)
        }.toAttrs()
    ) {
        if (info != null) Breadcrumb(crumbs = info.crumbs, branch = info.branch.id)
        P(Modifier.styleModifier { property("margin", "0 0 ${space3Var.value()}"); typo(Typo.Headline) }.toAttrs()) {
            Text(if (info != null) "This leaf has not grown yet." else "Not found")
        }
        if (info != null) {
            P { Text(info.stem.why) }
            NavLink("See the stem on the tree", "/#${info.branch.id}/${info.stem.slug}", info.branch.id)
        } else {
            NavLink("Back to the tree", "/", "foundation")
        }
    }
}
