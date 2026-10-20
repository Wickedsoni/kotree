# DV2 WP-C: unverified items (nothing compiled here)

Scripts run: `check-hardcoded.sh` clean, `check-tokens.py` 0 failures. No screenshots taken. Not committed by me; a `wip:` commit (7f74bb3, `Prose.kt` only) appeared in the repo during the task.

## Needs a CI compile / browser check
1. Kotlin: `Modifier.attrsModifier { ... }.toAttrs { classes(...) }` on `Aside` / `Nav`; `p.classList.add` and `first.nodeName` in `Prose.kt` (`markBoldLeads`); `smart cast of state` inside the `PageShell` lambda in `LessonView.kt`.
2. Tracks: 1 / 2 / 3 by media query in `LessonStyles.kt` (`minmax(0, lesson.wide)` centred with `justify-content: center`). Sticky rails use padding plus an equal negative margin so the focus ring is not clipped by `overflow-y: auto`; check that nothing shifts by 8 px.
3. `SurfaceCard` next / previous cards use `height: 100%` inside a stretched grid item.
4. Outline is mounted twice (rail, and the collapsed disclosure below bp.large only when open); both listen to scroll.
5. Interview Q&A: `markBoldLeads` marks a `<p>` that starts with `<strong>` followed by a newline (soft break) or `<br>` and more text. If the build renders the line break differently, the question stays inline (still readable). Generic, no heading match.
6. Export: `#kt-lesson-body` stays an empty div while exporting; the outline skeleton and the closed disclosure are in the snapshot, so the first client frame has the same boxes (CLS). `verifyLessonExport` only checks the body, but is untested here.
7. Neighbour cards put `on-surface-muted` on the branch tint; `check-tokens.py` was not checked for that pair.

## Deviations
- Wide blocks do NOT break out 88 px: `LessonBlock` / code / predict keep `max-width: lesson.text` (their files are not WP-C). Every block is centred at 544; sim frames at 720.
- Below bp.large the outline is a collapsible "On this page" disclosure above the body (lead's request; ADR says hidden). Previous and next are not in the rail there; the bottom cards serve.
- Rail "Previous and next" uses compact cards built in `LessonRail.kt` (`SurfaceCard` has fixed 24 px padding); the bottom Next / Previous cards use `SurfaceCard(Tonal, href)`.
- `NotFound.kt`, `LessonPreview.kt`, `Breadcrumb.kt`: no change needed. `comingNext` was already gone.
- `NotFoundContent` title is a `<p>` (EmptyState), as before; no `h1`.

## Missing tokens
None. The disclosure uses existing vars.
