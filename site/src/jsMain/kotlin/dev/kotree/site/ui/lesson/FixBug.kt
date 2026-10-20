package dev.kotree.site.ui.lesson

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.toAttrs
import dev.kotree.site.colorOnSurfaceVar
import dev.kotree.site.space2Var
import dev.kotree.site.space3Var
import dev.kotree.site.space4Var
import dev.kotree.site.ui.core.Button
import dev.kotree.site.ui.core.ButtonKind
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Pre
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

/**
 * FixBug: a static Fix-the-bug card (docs/06 sections 3.3, 4, 6). The broken code and "It should print" are visible from
 * the start. Hints appear one at a time ("Want a nudge?"), and "Show me one fix" unlocks only after both hints.
 * The fix and the explanation stay out of the page until then. No scoring; "Reset" hides everything again.
 * Running the learner's edit is Phase 6: [onRun] is called, the shell shows [lastOutput] when it is not empty.
 */
@Composable
fun FixBug(
    code: String,
    expected: String,
    hints: List<String>,
    fix: String,
    modifier: Modifier = Modifier,
    explanation: String = "",
    branch: String = "kotlin",
    online: Boolean = true,
    lastOutput: String = "",
    onRun: () -> Unit = {},
    runEnabled: Boolean = true,
    runHint: String = "",
) {
    var hintsShown by remember { mutableStateOf(0) }
    var fixShown by remember { mutableStateOf(false) }
    var resets by remember { mutableStateOf(0) }
    var controls by remember { mutableStateOf<HTMLElement?>(null) }
    val allHints = hintsShown >= hints.size
    // Reset removes itself, which would drop focus: put it on the first control of the card (WCAG 2.4.3).
    LaunchedEffect(resets) {
        if (resets > 0) (controls?.querySelector("button") as? HTMLElement)?.focus()
    }
    Div(modifier.styleModifier { property("color", colorOnSurfaceVar.value()) }.toAttrs()) {
        EditorShell(
            title = "Fix the bug",
            branch = branch,
            online = online,
            output = lastOutput,
            onRun = onRun,
            runEnabled = runEnabled,
            runHint = runHint,
        ) {
            CodeBlock(code, branch = branch, label = "Code with a bug")
            P(Modifier.styleModifier { property("margin", "${space3Var.value()} 0 ${space2Var.value()}"); typo(Typo.Label) }.toAttrs()) {
                Text("It should print")
            }
            Pre(Modifier.styleModifier { property("margin", "0"); typo(Typo.Code) }.toAttrs()) { Text(expected) }
        }
        Div(Modifier.styleModifier { property("margin-top", space4Var.value()); property("display", "grid"); property("gap", space3Var.value()) }.toAttrs()) {
            hints.take(hintsShown).forEachIndexed { index, hint ->
                FeedbackBox(FeedbackKind.Hint, "Nudge ${index + 1} of ${hints.size}") {
                    P(Modifier.styleModifier { property("margin", "${space2Var.value()} 0 0") }.toAttrs()) { Text(hint) }
                }
            }
            if (fixShown) {
                FeedbackBox(FeedbackKind.Neutral, "Here is one way to fix it.", focusOnShow = true) {
                    Div(Modifier.styleModifier { property("margin-top", space2Var.value()) }.toAttrs()) {
                        CodeBlock(fix, branch = branch, label = "One fix")
                    }
                    if (explanation.isNotBlank()) {
                        P(Modifier.styleModifier { property("margin", "${space2Var.value()} 0 0") }.toAttrs()) { Text(explanation) }
                    }
                }
            }
            Div(
                Modifier.styleModifier { property("display", "flex"); property("flex-wrap", "wrap"); property("gap", space2Var.value()) }
                    .toAttrs { ref { element -> controls = element; onDispose { controls = null } } }
            ) {
                // One call site for both labels keeps the same DOM node, so focus stays put (WCAG 2.4.3). When the
                // fix appears the button goes away and focus moves to the fix box (focusOnShow).
                if (!fixShown) {
                    Button(
                        text = if (!allHints) "Want a nudge?" else "Show me one fix",
                        onClick = { if (!allHints) hintsShown += 1 else fixShown = true },
                        kind = ButtonKind.Tonal,
                        branch = branch,
                    )
                }
                if (hintsShown > 0) {
                    Button("Reset", { hintsShown = 0; fixShown = false; resets += 1 }, kind = ButtonKind.Text, branch = branch)
                }
            }
        }
    }
}
