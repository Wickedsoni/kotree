# DR-wpc: WP-C lesson page, unverified items

Not compiled locally (dl.google.com blocked). CI compile is the first check. `scripts/check-hardcoded.sh`: clean.

## Unverified API use
- `window.matchMedia(...)` + `MediaQueryList.addEventListener("change", (Event) -> Unit)` in `LessonRail.kt` (`rememberWideLayout`). `matches` is used in `Fly.kt`; the EventTarget supertype is assumed.
- `NodeList.item(i)`, `querySelectorAll`, `el.asDynamic().scrollIntoView(options)` with `js("({})")` in `Outline.kt`.
- `window.clearTimeout`, `cancelAnimationFrame` signatures (Int handles).
- `Aside`, `Section`, `H2` from compose-web DOM (Section/H2 used elsewhere; Aside not used elsewhere yet).

## Deviations from ADR 006
- Media query: a `matchMedia` listener instead of a Silk `CssStyle` (the ADR's own VERIFY item). Initial value is read synchronously. The static export is built at the exporter's viewport; if that is below 1200 px the exported HTML is the one-column layout and switches after load (possible CLS; measure).
- Outline: scroll listener (rAF-throttled, `getBoundingClientRect`) instead of `IntersectionObserver`, because the Kotlin/JS binding is unverified. Outline scans via `setTimeout(0)` after the prose sets `innerHTML`.
- Narrow layout: prev/next sit in the rail section under the body; the bottom block there is only "Back to the tree". Wide layout: bottom block has the same cards plus the back link.
- Rail cards show an empty "On this page" card in the export snapshot and while loading (heading only).
- `LessonHeader` max width widened from `layout.lesson.text` to `layout.lesson.wide` to fill the column; prose and `#kt-lesson-body` stay at `layout.lesson.text`.
- Status chips use `ui/core/Chip.kt` (label "Planned" for shadow); WP-B owns the wording.
- Breadcrumb deep links `/#<branch>` and `/#<branch>/<stem>` need WP-A's `DeepLink.kt` to land.

## Not checked
Screenshots at 1024/1440/1920, sticky behaviour, CLS, `verifyLessonExport`, gzip size.
