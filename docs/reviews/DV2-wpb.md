# DV2 WP-B: unverified items (nothing compiled here)

Scripts run: `check-hardcoded.sh` clean, `check-tokens.py` 0 failures. No screenshots taken. Grep of `site/src` finds no "Needs:", "Coming next", "learning this".

## Needs a CI compile / browser check
1. Kotlin risks: `ref { element -> ... }` and `onKeyDown { event -> ... }` in `SideSheet`; anonymous `fun() { ... }` passed as `ListRow.onClick`; `window.history.replaceState(null, "", url)` in `Topics.kt`; composable lambdas passed to the nullable `eyebrow` / `chips` parameters of `NodeCard`.
2. Equal card heights and one footer baseline per row: `SurfaceCard` forces `display:block`, so each card has an inner flex column with `height:100%` inside a stretched grid item. If the percentage does not resolve, footers will not align (fallback: `min-height:100%`, or a `display` parameter on `SurfaceCard`).
3. Filter hides a card with `display:none` on a wrapper `Div` (SurfaceCard sets its own display). DOM budget (1,500 nodes) not measured.
4. Hero height (<= 320 px at 1440) not measured; the 5fr/7fr split and the 3-column tiles are my reading of the ADR.
5. Sheet: focus trap, Esc, scrim click, focus return to the opener (the element focused at open time; Safari does not focus buttons on click, so it falls back to nothing). Page behind the scrim still scrolls with the wheel (no scroll lock).
6. `/topics#<branch>/<stem>` loads with the sheet open and the disclosure opened for stems past the 6th. Open / close use `replaceState`, so `hashchange` does not fire.
7. Node card: top clamp is done with `margin-top` + `max-height` calc inside the centred host. The host (`CardHost`) is unchanged.
8. Bough filter: radiogroup with roving tabindex; arrows move focus with `getElementById(...).focus()`.

## Needs a change in a file I do not own
- `tree/TreeView.kt`: `CARD_WIDTH_PX` is 384 (`s16 * 6`); the card is now `layout.dialog.width` (400) and sets its own width, so the right/left fit test is 16 px off. Use `Tokens.LayoutW.dialogWidth`. Optionally drop the vertical centring in `CardHost`.
- `ZoomControls.kt` titles, Shortcuts button, `ShortcutsDialog.kt`, canvas cursors, `A11yNodes.kt` wording: not part of this task.

## Deviations and additions
- New file `ui/layout/LinkButton.kt` (an `<a>` styled as a Button, same kinds). Used for Start here, Open lesson, Open in Topics, See on the tree. Not in the ADR component table.
- `SideSheet` has an extra `branch` parameter (tint, ink, focus ring); `NodeCard` has `clearHeader` (false for `LeafPopup` and the kitchen sink) and nullable `eyebrow` / `chips`.
- `LeafPopup` now wraps `NodeCard` (same signature; no longer `shape.blob`).
- `BentoGrid.kt` now holds `BranchGrid`; `BoughHeader`, `CardKind`, `WIDE_STEM_COUNT`, `BENTO_*` are gone. `layout.bento.col.min` is unused now (Tokens untouched).
- Hero tiles show "n of m live" as a second line; the stem rows in cards show "n/m" with an aria-label in words.
- Prerequisites appear in the sheet as "Builds on: ...", not "Needs:".

## Missing tokens (not added, Tokens.kt is not mine)
- Hero tile columns (2 below / 3 from `bp.large`) and the hero split `5fr 7fr`: constants in `TopicsStyles.kt`.
- Collapsed stem count 6, node-card row caps 6 / 8, `why` clamp 3 lines: constants in `BranchCard.kt` and `NodeCard.kt`.
