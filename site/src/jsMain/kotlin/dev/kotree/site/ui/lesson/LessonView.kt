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
import dev.kotree.site.colorOutlineVar
import dev.kotree.site.layoutLessonTextVar
import dev.kotree.site.layoutLessonWideVar
import dev.kotree.site.lessons.BlockData
import dev.kotree.site.lessons.LeafInfo
import dev.kotree.site.lessons.LessonData
import dev.kotree.site.lessons.LessonState
import dev.kotree.site.lessons.findLeaf
import dev.kotree.site.lessons.isExporting
import dev.kotree.site.lessons.rememberLesson
import dev.kotree.site.sims.ReadOnlyTry
import dev.kotree.site.sims.TemplateSimHost
import dev.kotree.site.sims.core.SimTryStep
import dev.kotree.site.sims.core.readSimSpec
import dev.kotree.site.sims.templateLayouts
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
import dev.kotree.site.ui.core.EmptyState
import dev.kotree.site.ui.core.Skeleton
import dev.kotree.site.ui.layout.NavItem
import dev.kotree.site.ui.shell.PageShell
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Li
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
    val exporting = isExporting()
    val ready = state is LessonState.Ready && !exporting
    PageShell(NavItem.Lesson, info.branch.id) {
        Div(Modifier.toAttrs { classes(LESSON_GRID) }) {
            LessonStemColumn(info)
            Div(Modifier.toAttrs { classes(LESSON_MAIN) }) {
                Breadcrumb(crumbs = info.crumbs, branch = info.branch.id)
                if (preview) PreviewBanner(leaf.status)
                Div(Modifier.styleModifier { property("margin-top", space4Var.value()) }.toAttrs()) {
                    LessonHeader(
                        title = leaf.title,
                        difficulty = leaf.difficulty.chip(),
                        branch = info.branch.id,
                        progressLine = info.progressLine,
                        videoUrl = leaf.video.orEmpty(),
                    )
                }
                OutlineDisclosure(info, ready)
                Div(
                    Modifier.styleModifier {
                        property("box-sizing", "border-box")
                        property("margin-top", space6Var.value())
                    }.toAttrs { id(LESSON_BODY_ID) }
                ) {
                    when {
                        exporting -> Unit // the export bakes the static prose here
                        state is LessonState.Ready -> LessonBody(state.data, info, preview)
                        state is LessonState.Failed -> LoadFailed { retries += 1 }
                        else -> Div(Modifier.styleModifier { property("min-height", "calc(${space12Var.value()} * 8)") }.toAttrs()) {
                            Skeleton(8)
                        }
                    }
                }
                if (state is LessonState.Ready && !exporting) {
                    if (preview && !state.data.notes.isNullOrEmpty()) ReviewerNotes(state.data.notes!!.toList())
                    NextSteps(info)
                }
            }
            LessonRail(info, ready)
        }
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
            // Centred in the 720 track; a sim frame is 720 wide itself.
            Div(
                Modifier.styleModifier {
                    property("max-width", if (block.t == "sim") layoutLessonWideVar.value() else layoutLessonTextVar.value())
                    property("margin", "0 auto ${space6Var.value()}")
                }.toAttrs()
            ) {
                Block(block, data, branch, preview)
            }
        }
    }
}

@Composable
private fun Block(block: BlockData, data: LessonData, branch: String, preview: Boolean) {
    when (block.t) {
        "html" -> Prose(block.html.orEmpty())
        "sim" -> SimHost(block, branch, preview)
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
 * Bespoke sims by id (later physics sims, ADR 009 section 1, docs/05). Empty today. A registered id wins over a template.
 */
private val simRegistry: Map<String, @Composable () -> Unit> = emptyMap()

/**
 * Resolution order of ADR 009 section 1: (1) a bespoke sim registered for the id; (2) the template layout of `tpl`;
 * (3) a template whose layout is not built yet: the Try this steps read-only on a live page, a labelled working frame in
 * a preview; a legacy bare marker (no `tpl`) draws nothing on a live page and a labelled frame in a preview.
 */
@Composable
private fun SimHost(block: BlockData, branch: String, preview: Boolean) {
    val id = block.id.orEmpty()
    val bespoke = simRegistry[id]
    if (bespoke != null) {
        SimFrame(title = "Simulation: $id", branch = branch) { bespoke() }
        return
    }
    val spec = remember(block) {
        val tpl = block.tpl
        if (tpl == null) null
        else readSimSpec(id, tpl, block.spec, block.`try`.orEmpty().map { SimTryStep(it.html, it.done.toList(), it.optional) })
    }
    val layout = spec?.let { templateLayouts[it.template] }
    when {
        spec != null && layout != null -> TemplateSimHost(spec, branch, layout)
        spec != null && preview -> TemplateSimHost(spec, branch, null)
        spec != null -> ReadOnlyTry(spec, branch)
        preview -> SimFrame(title = "Simulation: $id", branch = branch) { Text("Simulation \"$id\" is not built yet. Preview only.") }
    }
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
    PageShell(NavItem.Lesson, info?.branch?.id) {
        Div(
            Modifier.styleModifier {
                property("box-sizing", "border-box")
                property("max-width", layoutLessonTextVar.value())
                property("margin", "0 auto")
            }.toAttrs()
        ) {
            if (info != null) Breadcrumb(crumbs = info.crumbs, branch = info.branch.id)
            Div(Modifier.styleModifier { property("margin-top", space8Var.value()) }.toAttrs()) {
                if (info != null) {
                    EmptyState(
                        title = "This leaf has not grown yet.",
                        body = info.stem.why,
                        action = { TextLink("See the stem on the tree", "/#${info.branch.id}/${info.stem.slug}", info.branch.id) },
                    )
                } else {
                    EmptyState(title = "Not found", action = { TextLink("Back to the tree", "/", "foundation") })
                }
            }
        }
    }
}
