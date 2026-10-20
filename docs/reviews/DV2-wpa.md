# DV2 WP-A: unverified items (nothing compiled here, dl.google.com blocked)

Scripts run: `check-hardcoded.sh` clean, `check-tokens.py` 0 failures. No screenshots taken.

## Needs a CI compile / browser check
1. Silk emits `::-webkit-scrollbar*` and `::-webkit-scrollbar-thumb:hover` selectors verbatim (ADR VERIFY). Fallback: one `<style id="kt-scrollbar">` in `ui/Base.kt`.
2. Scrollbar colours and `scroll-padding-top` use plain values from `Tokens`, not CSS variables: `tokenVariables()` sits on the AppEntry Div, so `html` and the viewport scrollbar cannot see the variables.
3. Firefox rules are registered on `:root, :root *` only when `CSS.supports('selector(::-webkit-scrollbar)')` is false.
4. `h1, h2, h3 { text-wrap: balance }` support; M3 values flagged in the ADR (scrim 0.32, header 64) unchanged.
5. Kotlin risks: `this.onClick` in `ListRow` (parameter name clash), `id(id)` in `SectionHeader`, `classes()` in `SiteHeader`, `js()` in `supportsWebkitScrollbar`.
6. Sticky header needs that no ancestor sets `overflow`; the root Div has no fixed height (fillMaxWidth + min-height).

## Deviations
- Button state layers NOT moved to `StateLayer` 8/10/10: at 10 % pressed, `architectureData` and `tools` filled buttons fall to 4.45 / 4.36 (< 4.5). Inks untouched; needs a decision. `check-tokens.py` regexes unchanged, plus new scrollbar thumb checks (3.68 / 6.05 / 17.09).
- Pages are not wired to `PageShell` (Topics, LessonView, NotFoundContent stay as they are, B and C adopt it). They now work because the root is a plain Div. The skip-link acceptance check passes only after that.
- Stale copy removed outside WP-A files (text only): `A11yNodes.kt` ("up next"), `LessonRail.kt` (Coming next block), `LessonLookup.kt` (`comingNext`). Grep of the repo finds no "learning this".
- Header label "Home" is now "Tree"; tagline is D14 (working text), shown from 1200 px.
- `KitchenSink` shows `PageShell` as a note only (cannot nest).
- `EmptyState` and `Meter` follow the ADR parameter order; `modifier` is last (Meter) or absent (EmptyState).
