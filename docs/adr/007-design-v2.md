# ADR 007: Design v2 (page shell, even topics grid, three-track lesson, pill scrollbars)

Status: proposed · Date: 2026-10-20 · Decided by: the author (review of the live ADR 006 build), specified by `architect`
Amends ADR 006: §WP-B "Bento grid" (spans, hero 2×2, inline stem expand), §WP-C "Layout" (one rail only), "Rejected: left and right rails", the `/topics#<branch>/<stem>` behaviour (expand → side sheet), card hover morph on non-clickable cards. Everything else in ADR 006 stands (vector tree, hash grammar, anchor ids, node-card wording rule).
Docs to align after the wave (`architect`): `docs/02` §3, §4, §6; `docs/03` §6.2.

## Context

| # | Seen on the live site | Root cause in the code |
|---|---|---|
| 1 | Header overlaps content on `/topics` and lesson pages | `AppEntry.kt` wraps every page in Kobweb `Box`. Kobweb's stylesheet sets `.kobweb-box > * { grid-area: 1 / 1 }` (read in `KobwebComposeStyleSheet.kt`), so every top-level sibling sits in **one grid cell**. `TopicsPage` emits `SiteHeader` + `Main` + `BentoGrid`, and `LessonPage` emits `SiteHeader` + `Main`, so they are drawn on top of each other. This is not a z-index or offset problem. |
| 2 | `/topics` is uneven, with empty areas | Hero 2×2 holds 6 stems; wide cards are chosen by stem count (6 to 16 per branch), so card heights range from 6 to 16 rows; `auto-fill` leaves empty tracks after each bough row (4, 3, 4 and 5 branches); `align-items: stretch` stretches short cards |
| 2b | Cluttered card rows | Every leaf row has a status chip, a difficulty and a "Needs: ..." line; every stem has a gradient strip; leaves expand inline |
| 3 | Lesson page has dead space | Prose (544) is **left-aligned** in the 720 track (`docs/02` §4.5 says centred), so 176 px of dead space plus the 48 px gap sit before the rail; rail cards use `type.title` (22 px) headings and mixed row styles; `rememberWideLayout()` switches the layout in JS after hydration |
| 3b | Old wording | `LessonRail.kt:195` "Coming next: …", `LessonLookup.comingNext`, and `A11yNodes.kt:75` aria "coming next". "The author is learning this now" is **not in the source or content any more** (grep, 20 Oct), so the live site is running an older deploy. Checked again on the export. |
| 4 | Tree card is a long list | `BranchNodeCard` lists every stem (up to 16) as text buttons; `LinkChip`s are 44 px bordered pills; actions sit left with Close |
| 5 | Plain browser scrollbars | No scrollbar styling |
| 6 | No shared component set | Every page builds rows, cards and progress bars by hand with its own spacing |

Facts checked (20 Oct 2026):
- Kobweb `Box`: `.kobweb-box { grid: minmax(0,1fr) / minmax(0,1fr) }`, `.kobweb-box > * { gridArea("1","1") }`. Source: `github.com/varabyte/kobweb/.../compose/style/KobwebComposeStyleSheet.kt`.
- Android window size classes (width): Compact < 600, Medium 600-839, Expanded 840-1199, **Large 1200-1599, Extra-large ≥ 1600** dp (`developer.android.com/develop/ui/compose/layouts/adaptive/use-window-size-classes`).
- Canonical layouts: list-detail, feed (adaptive grid, `GridCells.Adaptive(180.dp)` example, full-width headers), supporting pane (about 70/30 at expanded) (`.../large-screens/large-screen-canonical-layouts`).
- Desktop guidance (`/design/ui/desktop/...`): use the space for density and do not scale things up; set a max width on content; short copy about 60 characters; panes, not screens; progressive disclosure as the window grows; hover states plus cursor changes on everything interactive; hover may reveal extra information; visual target = hit target (do not let invisible padding grow targets); Tab order follows reading order; arrow keys move inside composite widgets; focus stays inside modal dialogs; Esc is a local cancel for transient UI; focus styles distinct and consistent; shortcuts must be discoverable; "displays a scrollbar while content is being scrolled". Cursors: hand pointer = clickable, grab / grabbing = draggable, text = editable, zoom-in/out for zoomable content.
- Scrollbars: since Chrome 121 the standard `scrollbar-color` / `scrollbar-width` **override** `::-webkit-scrollbar*` rules on the same element. `scrollbar-width` takes only `auto | thin | none` (no length). Source: Chrome "Scrollbar styling" doc, quoted in search results; the page itself could not be fetched. ⚠️ VERIFY in the A probe (Chrome 121+: set both and check that the thumb radius is lost).
- `garvitmaheshwari.in`: DNS did not resolve from our tools, so **no brand cues are applied** (Q4).

## Decision (summary)

1. **One page shell.** `AppEntry` renders a plain block `Div` (not `Box`). Every non-tree page uses `PageShell`: skip link, a **sticky in-flow** header (not fixed), then `<main id="kt-main">` on a max-width grid. Hash and outline targets clear the header through `html { scroll-padding-top }`.
2. **A small component set** (A builds, B and C use): `PageShell`, `SectionHeader`, `SurfaceCard` (3 variants), `ListRow`, `Meter`, `StatusIcon`, compact `StatusChip`, `EmptyState`, `Skeleton`. `RailPanel` (C) and `SideSheet` (B) are built on top of them.
3. **`/topics` = feed canonical layout with even rows.** A full-width Foundation hero banner, a bough filter, then **one** grid of the 16 branch cards at fixed column counts (1 / 2 / 4), so every row is full. Each card shows 6 stems, plus a "Show all" disclosure. A stem opens a **side sheet** with its leaves (list-detail).
4. **Lesson = supporting-pane layout.** Prose is centred in the 720 track. Large widths add a right rail; extra-large widths add a left stem navigation as well. Layout switches by CSS media queries, not JS.
5. **Slim pill scrollbars everywhere**, token-driven, with hover and active states.
6. **Copy:** "Coming next" and the `comingNext` field are deleted; aria text "up next"; "Coming soon" (L4) stays only as a status label in the legend, sheets and cards, not repeated on every row.

## Tokens (A adds them first; nothing is renamed)

Colour values are typed only in `Tokens.kt`. "CSS" = also a `StyleVariable` in `TokenVars.kt`; "Kotlin" = constant only.

| Token | Value | Kind | Use / check |
|---|---|---|---|
| `color.scrollbar.thumb` | = `color.tree.stem` `0x80868B` (3.7:1) | CSS | thumb at rest; `check-tokens.py`: ≥ 3.0 on white |
| `color.scrollbar.thumb.hover` | = `on-surface-muted` | CSS | thumb hover |
| `color.scrollbar.thumb.active` | = `on-surface` | CSS | thumb while dragging |
| `color.skeleton` | `on-surface` mixed 6 % into white, computed by `tint()` | CSS | skeleton bars |
| `color.scrim` | `on-surface` at 32 % alpha (⚠️ VERIFY: M3 `ScrimTokens.ContainerOpacity` 0.32) | CSS | side-sheet scrim |
| `scrollbar.size` / `.inset` / `.inset.hover` / `.thumb.min` | 12 / 3 / 2 / 48 px | Kotlin | gutter, transparent border (thumb 6 px, 8 px on hover), minimum thumb length |
| `layout.header.height` | 64 px (M3 small top app bar, ⚠️ VERIFY) | CSS + Kotlin | sticky header, scroll padding, rail `top` |
| `layout.bp.expanded` / `.large` / `.xlarge` | 840 / 1200 / 1600 px | Kotlin | Android size classes (verified); `.large` = the existing `railMinViewport` (alias) |
| `layout.nav.width` | 256 px | CSS | lesson left stem nav (extra-large) |
| `layout.dialog.width` | 400 px | CSS + Kotlin | tree node card and side sheet (replaces `CARD_WIDTH_PX`) |
| `layout.topics.cols` | 1 / 2 / 4 (below 840 / 840-1199 / from 1200) | Kotlin | branch grid column counts |
| `layout.measure.short` | 60 ch | CSS | intro paragraphs and card copy (desktop guide: about 60 characters) |
| `space.inset.card` / `.panel` | = `space.6` 24 / `space.4` 16 | CSS | padding of cards and of rail / sheet panels |
| `space.stack.page` / `.section` | = `space.8` 32 / `space.12` 48 | CSS | header to first content; between page sections |
| `space.gap.grid` | = `space.6` 24 | CSS | gap of every grid (topics, lesson tracks use `space.8`) |
| `type.title.small` | 16 / 24 / 600 | CSS | card section titles, rail and sheet headings, row titles in bold |
| `type.body.small` | 16 / 24 / 400 | CSS | card copy, row titles, supporting text (14 px floor kept) |
| `size.chip.compact` / `size.status.icon` | 24 / 12 px | Kotlin | non-interactive status chip height; status glyph |
| `z.header` / `z.sheet` / `z.skip` | 10 / 20 / 30 | Kotlin | stacking |
| `motion.tooltip.delay` | 500 ms (our value) | Kotlin | delay before the zoom-control tooltip |

Spacing rhythm (rule, no new raw steps): inside a component 4/8/12; between rows 0 (a row carries its own 44 px); between siblings in a section 16; card padding 24; panel padding 16; between sections 48; header to content 32. Corner radii by role: page hero `shape.xxl`; grid cards and node card `shape.xl` / `shape.xl.increased`; panels and sheet `shape.lg.increased`; rows `shape.md`; chips `shape.xs`; buttons and meters `shape.full`. Counts use `font-variant-numeric: tabular-nums`.

## Component set

| Component (file) | Owner | Contract and look |
|---|---|---|
| `PageShell` (`ui/shell/PageShell.kt`) | A | `@Composable fun PageShell(current: NavItem, branch: String? = null, content: @Composable () -> Unit)`. It renders the skip link "Skip to content" (first Tab stop, visible on focus, `z.skip`), then `SiteHeader(current)`, then `<main id="kt-main" tabindex="-1">` with `max-width: layout.page.max`, inline padding `layout.page.gutter`, top `space.stack.page`, bottom `space.stack.section`. `branch` scopes `inBranch`. |
| `SiteHeader` (existing) | A | Same signature. In flow: `position: sticky; top: 0; z.header`, height `layout.header.height`, surface fill, 1 px `color.outline` bottom border, and an inner row aligned to the page max-width grid, so the wordmark lines up with the page content edge. Row: wordmark (+ tagline at ≥ `bp.large`) · nav pills **Tree**, **Topics** (`space.8` after the wordmark) · `ProgressCounter` at the end. Floating variant (tree) unchanged: pill at top-left. |
| `SectionHeader` (`ui/core/SectionHeader.kt`) | A | `(title: String, level: Int = 2, eyebrow: String? = null, meta: String? = null, id: String? = null, action: (@Composable () -> Unit)? = null)`: eyebrow (`type.label`, muted) over the title; `meta` and `action` at the end, baseline-aligned. |
| `SurfaceCard` (`ui/core/Card.kt`) | A | `enum class CardVariant { Outlined, Tonal, Hero }`; `(variant, branch: String, modifier = Modifier, inkBand: Boolean = false, href: String? = null, content)`. Outlined = surface + `elevation.1`, `shape.xl`, padding `space.inset.card`; Tonal = branch tint, `shape.lg.increased`; Hero = tint, `shape.xxl`. With `href`, the whole card is one `<a>` that gets the hover state layer (`state.hover`) and the morph to `shape.xl.increased`. Without `href` there is **no hover effect** (no false affordance). |
| `ListRow` (`ui/core/ListRow.kt`) | A | `(title: String, modifier = Modifier, branch = "kotlin", href: String? = null, onClick: (() -> Unit)? = null, supporting: String? = null, current = false, muted = false, clampLines: Int = 1, leading: (@Composable () -> Unit)? = null, trailing: (@Composable () -> Unit)? = null)`. A CSS grid `[24px] [1fr] [auto]`, column gap `space.3`, min-height 44, inline padding `space.3`, `shape.md`. Element: `<a>` with `href`, `<button type=button>` with `onClick`, else `<div>`. Hover adds `state.hover`, pressed adds `state.pressed`; `current` = tint fill + ink + `aria-current`; the title is clamped with ellipsis, and the full text goes in `title`. |
| `Meter` (`ui/core/Meter.kt`) | A | `(value: Int, max: Int, label: String, branch: String, showText: Boolean = true, onTint: Boolean = false)`: "N of M live" (`type.label`, tabular) and an 8 px `shape.full` bar, `role=progressbar` with values. Replaces the private `Progress` and the gradient strips. |
| `StatusIcon` (`ui/core/StatusIcon.kt`) | A | `(status: ui.core.LeafStatus)`. 12 px glyph in a 24 px box, the same shape cue as the tree: live = filled `status.live`, up next = `status.upnext.glow` fill + `status.upnext` ring, shadow = `leaf.shadow.stroke` outline. `aria-hidden`: the row text or the chip carries the meaning. |
| `StatusChip` / `Chip` (existing) | A | New parameter `compact: Boolean = false` (24 px, no swatch, ink on container). Existing calls unchanged. |
| `EmptyState` (`ui/core/EmptyState.kt`) | A | `(title: String, body: String? = null, action: (@Composable () -> Unit)? = null)`: muted, centred in its container, `space.4` stack. |
| `Skeleton` (`ui/core/Skeleton.kt`) | A | `enum class SkeletonLine { Body, Row }`; `(lines: Int, line: SkeletonLine = Body)`: `color.skeleton` bars at the type line height, last bar 60 % wide, static (no shimmer, so reduced motion is the same), `aria-hidden` plus one visually hidden "Loading". |
| `RailPanel` (`ui/lesson/RailPanel.kt`) | C | `(title: String, meta: String? = null, content)`: Outlined, `shape.lg.increased`, padding `space.inset.panel`, title `type.title.small`, meta at the end. Used by every rail block, so all rail cards look the same. |
| `SideSheet` (`ui/layout/SideSheet.kt`) | B | `(title: String, onClose: () -> Unit, eyebrow: String? = null, footer: @Composable () -> Unit = {}, content)`: a modal side sheet at the right, `layout.dialog.width`, full height, `z.sheet`, `color.scrim`. Enter: slide 24 px + fade with `motion.spatial` (a cut under reduced motion). Focus moves to the title, stays trapped inside, Esc or scrim click closes, focus returns to the opener. |

## Layout per page

**Shell (all pages but `/`).** Order: skip link → sticky header → `main`. `html { scroll-padding-top: calc(layout.header.height + space.4) }` (Silk `calc`), so `/topics#kotlin`, sheet hashes and outline links land below the header. No page sets `padding-top` to make room for the header. The sticky header is in the flow, so nothing needs offsetting.

**Home `/` (tree).** Full-bleed canvas, as in ADR 006. Floating header pill top-left (inset `space.4`); zoom controls bottom-right, each with `title` + `aria-label` naming its key ("Zoom in (+)"), plus a fifth tonal button **Shortcuts** that opens the shortcuts dialog. Node card (B):
- Width `layout.dialog.width`. It is placed right of the node, else left, else centred. Its top is clamped below the floating header (`layout.header.height + 2 × space.4`). Max height = viewport − that − `space.6`. The body scrolls (pill scrollbar) only past that height.
- Anatomy: a tint band holding an eyebrow line (leaf: `Branch › Stem` as two links; stem: `Branch`; branch: bough title), the title (`type.title`, ink) and, for leaves only, a row of compact chips (status, difficulty). Then the body. The footer is end-aligned: `[Close (text)] [primary]`. The 44 px bordered `LinkChip`s are removed.
- Leaf: live = summary + **Open lesson**. Otherwise: `NOT_PUBLISHED_TEXT`, `stem.why` (3-line clamp), live neighbours as `ListRow`s, and **See all topics**.
- Stem: `why` (3-line clamp), `Meter`, leaves as `ListRow` (leading `StatusIcon`, trailing compact chip for live / up next only), **at most 6** rows, then a "+N more in Topics" row link. Primary: **Open in Topics**.
- Branch: `Meter`, an up-next `ListRow` on `status.upnext.container`, stems as `ListRow` (trailing "n/m"), **at most 8** rows, then "All N stems in Topics". A stem row flies to the stem and opens its card.

**`/topics` (feed + list-detail).** Order inside `PageShell(NavItem.Topics)`:
1. Intro: at ≥ `bp.large` a 2-column grid `minmax(0, 1fr) auto`. Left: H1 "Topics" (`type.headline`) and one intro sentence (`type.body`, muted, max `layout.measure.short`). Right: overall `Meter` (live of total) and a legend of three `StatusIcon` + label pairs (Live, Up next, Coming soon). Below `bp.large` these stack.
2. Foundation hero (`SurfaceCard(Hero, "foundation")`, full width, `space.stack.section` below it). At ≥ `bp.large` a grid `5fr 7fr`, gap `space.8`. Left: eyebrow "Start here", title, one line of UI copy, `Meter`, filled **Start here** button. Right: the 6 Foundation stems as stem tiles (3 columns, 2 below `bp.large`). Each tile is a `ListRow`-height button with title, "n of m live" and a `StatusIcon` when it holds the up-next leaf; it opens the sheet. Target height ≤ 320 px at 1440.
3. Bough filter: a single-select segmented group ("All" + the 4 bough titles), `role=radiogroup`. Arrow keys move the selection and Tab leaves the group. It filters cards by `display: none`; the default is All. No hash.
4. Branch grid: **one** grid for the 16 branches in tree order, `grid-template-columns: repeat(layout.topics.cols, minmax(0, 1fr))` by media query (1 / 2 / 4), gap `space.gap.grid`, `align-items: stretch`. 16 cards = 4 full rows at ≥ 1200 and 8 at 840-1199. Bough grouping is kept by order, the eyebrow and the filter; there are no full-row bough headers.
5. Branch card (`SurfaceCard(Outlined, inkBand = true)`, `id=<branch>`), in a fixed order: eyebrow (bough) · title (`type.title`, ink, 2-line clamp) · `Meter` · up-next `ListRow` (container fill, opens that stem's sheet) · 1 px divider · stems as `ListRow` (title ellipsis, trailing "n/m" tabular, `id=<branch>/<stem>`, opens the sheet) · the first **6** stems, then a disclosure row "Show all N stems" / "Show fewer" (`aria-expanded`) · footer link "See on the tree" (`margin-top: auto`, so footers line up across a row). Removed: gradient strips, per-leaf chips in cards, "Needs: …" lines, ▸/▾ glyphs, hover morph.
6. Stem sheet (`SideSheet`): eyebrow = branch title, title = stem title, `why`, `Meter`, then every leaf as `ListRow` (leading `StatusIcon`, title clamped to 2 lines, supporting = difficulty, trailing compact chip for Live and Up next only). Live rows link to the lesson; others are muted plain rows. Footer: "See on the tree" (`/#<branch>/<stem>`) and Close. Opening it calls `history.replaceState` to `#<branch>/<stem>`; closing sets `#<branch>`. Loading `/topics#<branch>/<stem>` (or a leaf id) opens the sheet and, if the stem row is hidden, also opens the card's disclosure. Anchor ids of ADR 006 are unchanged.

**Lesson page (supporting pane; `PageShell(NavItem.Lesson, branch)`).** Tracks by CSS media query (`ui/lesson/LessonStyles.kt`, the same `cssRule(CSSMediaQuery.MediaFeature(...))` route that `Base.kt` already uses). `rememberWideLayout()` is deleted.

| Viewport | `grid-template-columns` (gap `space.8`) | Track content |
|---|---|---|
| ≥ 1600 | `layout.nav.width` · `layout.lesson.wide` · `layout.rail.width` (256 + 720 + 320 + 64 = 1360 ≤ 1376 inner) | left: **In this stem** (`StemNav`); centre: breadcrumb, header, body, bottom links; right: **On this page**, **Previous and next** |
| 1200-1599 | `layout.lesson.wide` · `layout.rail.width` | right rail: On this page, In this stem, Previous and next |
| < 1200 | one track, `layout.lesson.wide` centred | after the body: In this stem, Previous and next (outline hidden) |

- The breadcrumb sits in the centre track, `space.stack.page` below the header, with `space.4` above `LessonHeader`. The header card is the full 720 width.
- `#kt-lesson-body`: the prose is **centred** at `layout.lesson.text` (`margin-inline: auto`), and wide blocks break out 88 px on each side (`docs/02` §4.5). Id and element are unchanged for `emitLessonPages` / `verifyLessonExport`.
- The rails are `position: sticky; top: calc(layout.header.height + space.6)`, `max-height: calc(100vh - layout.header.height - 2 × space.6)`, `overflow-y: auto`, `align-self: start`, with `space.4` between panels. Every block is a `RailPanel`.
- `StemNav`: `ListRow` per leaf (leading `StatusIcon`, 2-line clamp); current = tint + `aria-current="page"`; live rows link; meta "n of m live". The meter replaces the old "N of M grown in X" sentence.
- Previous / next: `SurfaceCard(Tonal, href)` cards (label, leaf title, stem title). At the bottom of the page they form a 2-column grid (previous at the start, next at the end). With no neighbours: `EmptyState("No other lessons are live in this branch yet.", action = link "See all topics")`. "Coming next" is removed.
- Loading: `Skeleton(8)` inside `#kt-lesson-body`, at the current reserved height. The outline panel renders `Skeleton(3, Row)` until the headings are scanned, so the rail does not shift. Outline click: smooth scroll, then focus the heading (`tabindex=-1`).
- `NotFoundContent` uses `PageShell` + `EmptyState`, keeping its texts and links.

## Keyboard and pointer

| Area | Behaviour |
|---|---|
| Everywhere | Tab order = reading order: skip link → wordmark → Tree → Topics → main. Every interactive element shows the 3 px `color.focus-ring` (existing global rule); nobody sets `outline: none` without a replacement. Esc closes the top transient UI (card, sheet, dialog) and nothing else. **No single-key page shortcuts** (WCAG 2.1.4). |
| Tree (focused) | Keys of `docs/03` §2.6 unchanged, plus `?` opens the **Shortcuts** dialog (B, `ui/layout/ShortcutsDialog.kt`, a modal that lists the tree keys and Esc). The dialog is also reachable by the Shortcuts button. |
| `/topics` | Filter = radio group (arrows). Rows and tiles: Enter / Space open the sheet. Sheet: focus trap, Esc returns focus to the opener. Disclosure rows: `aria-expanded`. |
| Lesson | Outline links move focus to their heading; StemNav and the cards are plain links. |
| Cursors | Links, buttons and rows: `pointer` (global rule on `a[href], button:not([disabled]), [role=button], [role=radio]`); disabled: `not-allowed`. Tree canvas: `grab` at rest, `grabbing` while dragging, `pointer` over a hit node, written to `canvas.style.cursor` only when it changes (B, in `camera/Input.kt` / `TreeView.kt`). |
| Hover | Rows, text buttons and link cards: `state.hover` layer; tonal buttons as today. Static cards: none. Truncated titles: the full text in `title`. Zoom controls: native `title` tooltip naming the key (`motion.tooltip.delay` applies only if a custom tooltip is later needed). |
| Targets | Visual box = hit box: rows, pills and buttons are 44 px tall visually; no invisible padding (desktop quality guideline). |

**Scrollbars (A, `ui/Base.kt`).** These rules are registered globally, so the page, rails, node-card body and sheet all match:
- `::-webkit-scrollbar` width and height `scrollbar.size`; `-track`, `-corner` transparent; `-button` `display: none`.
- `::-webkit-scrollbar-thumb`: `color.scrollbar.thumb`, `border: scrollbar.inset solid transparent`, `background-clip: padding-box`, `border-radius: shape.full`, `min-height` and `min-width` `scrollbar.thumb.min` (a 6 px pill).
- `:hover`: `.hover` colour, border `scrollbar.inset.hover` (an 8 px pill). `:active`: `.active` colour.
- Standard properties `scrollbar-width: thin; scrollbar-color: thumb transparent` are set **only when** `window.CSS.supports("selector(::-webkit-scrollbar)")` is false (Firefox). The check runs in the `@InitSilk` function, because Chrome 121+ would otherwise drop the pill.
- ⚠️ VERIFY: Silk `registerStyle` accepts `::-webkit-scrollbar-thumb:hover` as a selector. Fallback: `Base.kt` appends one `<style id="kt-scrollbar">` built from the same tokens at start-up.

Other global rules (A): `scroll-padding-top` (above); `h1, h2, h3 { text-wrap: balance }` (progressive; ⚠️ VERIFY support, harmless if ignored); `.kt-num { font-variant-numeric: tabular-nums }`.

## Work packages and file ownership

Order: **A first** (merged, compiles, kitchen sink shows every component) → **B and C in parallel**. B and C only read tokens and A's components. A missing token or parameter goes into the agent's reply, and the orchestrator adds it in one follow-up commit. Paths are under `site/src/jsMain/kotlin/dev/kotree/site/` unless they start with `scripts/`.

| File | Owner | Action |
|---|---|---|
| `AppEntry.kt` | **A** | `Box` → block `Div` with the same modifiers |
| `Tokens.kt`, `TokenVars.kt`, `scripts/check-tokens.py` | A | §Tokens; check `color.scrollbar.thumb` ≥ 3.0 on white |
| `ui/Base.kt` | A | scrollbars, scroll padding, cursor rule, tabular class; **move** the two BENTO rules unchanged into new `ui/topics/TopicsStyles.kt` (then B owns that file) |
| `ui/layout/SiteHeader.kt`, `ui/layout/ProgressCounter.kt` | A | sticky in-flow header, "Tree" label, aligned inner row |
| new `ui/shell/PageShell.kt`, `ui/core/SectionHeader.kt`, `Card.kt`, `ListRow.kt`, `Meter.kt`, `StatusIcon.kt`, `EmptyState.kt`, `Skeleton.kt` | A | new |
| `ui/core/Chip.kt`, `ui/core/Button.kt` | A | `compact` chip; Button state layers move to `state.*` (8/10/10) |
| `pages/KitchenSink.kt` | A | add one section that shows every new component and variant; existing sections untouched |
| `pages/Topics.kt`, `ui/topics/*` (`BentoGrid.kt` → the grid, `BranchCard.kt`, `StemRow.kt`, `TopicsStyles.kt`, new `FoundationHero.kt`, `BoughFilter.kt`, `StemSheet.kt`) | **B** | rewrite / new |
| `ui/layout/NodeCard.kt`, `ui/layout/ZoomControls.kt`, new `ui/layout/SideSheet.kt`, new `ui/layout/ShortcutsDialog.kt` | B | change / new |
| `tree/TreeView.kt` (only `CardHost` placement, the dialog and the cursor), `tree/camera/Input.kt` (cursor only), `tree/overlay/A11yNodes.kt` (wording) | B | change |
| `ui/lesson/LessonView.kt`, `LessonRail.kt`, `Outline.kt`, `LessonHeader.kt`, `Prose.kt` (centring only), new `RailPanel.kt`, `StemNav.kt`, `LessonStyles.kt` | **C** | change / new |
| `lessons/LessonLookup.kt` (delete `comingNext`), `ui/layout/Breadcrumb.kt`, `pages/NotFound.kt`, `pages/LessonPreview.kt` (shell only) | C | change |
| `tree/gen/*`, `tree/render/*`, `tree/hit/*`, `tree/camera/` other than the cursor, `buildSrc/**`, `content/**`, `pages/Index.kt`, `pages/Lesson.kt` | nobody | must compile unchanged; `limbHash` stays `cecb906380417dd8` |

Commits: A `feat: design v2 shell, components, scrollbars (ADR 007)`; B `feat: topics feed grid, stem sheet, node card v2 (ADR 007)`; C `feat: lesson three-track layout and rail panels (ADR 007)`.

## Budgets

| Item | Budget | How |
|---|---|---|
| `kotree.js` gzip | each WP ≤ +12 KB; wave ≤ +25 KB over the baseline A records at its start; hard limit 600 KB (L30) | `gzip -9 -c site/.kobweb/site/kotree.js \| wc -c` after a clean export |
| `/topics` DOM, sheet closed, disclosures closed | ≤ 1,500 nodes | `document.querySelectorAll('*').length` |
| CLS, `/topics` and lesson | ≤ 0.05 | Lighthouse |
| Sheet or node card open → visible | ≤ 100 ms | Performance panel |
| Tree draw | unchanged from ADR 006 (p95 ≤ 8 ms); B must not touch the frame path except cursor writes on change | `?perf=1` |
| JS-driven layout | none: every breakpoint is a CSS media query | code review |

## Acceptance checks

Run on the static export (`kobweb run --env prod --layout static`), headless Chrome over CDP, at 1024×768, 1280×800, 1440×900, 1600×900 and 1920×1080. Screenshots go to `docs/reviews/design-v2/`. Always: `scripts/check-hardcoded.sh` clean, `check-tokens.py` 0 failures, no `w:` warnings, `KitchenSink.kt` compiles, `limbHash` unchanged.

**A**
- [ ] `/topics`, a lesson preview and `/404`: at scrollY 0 and after scrolling 600 px, no element inside `main` intersects the header rect; the first content element's top ≥ header bottom + 32 (± 1).
- [ ] `/topics#kotlin` and an outline link: the target's top ≥ header bottom + 16.
- [ ] The direct child of the root `kobweb-box`, if any remains, is one element; pages have a single root.
- [ ] Scrollbar (Chrome): the thumb computes `border-radius` = `shape.full` and a 6 px visual width, 8 px on hover, a darker colour on hover and active; same on the rail, node-card body and sheet. In Firefox, if available, `scrollbar-width: thin`.
- [ ] Skip link: the first Tab shows it; Enter focuses `#kt-main`.
- [ ] Kitchen-sink section: every component and variant with hover, focus and current states; ListRow as a, button and div.

**B**
- [ ] At 1280, 1440 and 1920: the branch grid has 4 columns × 4 rows; the cards in a row have equal height (± 1 px) and their footers share one baseline. At 1024: 2 × 8. No horizontal scroll at any width.
- [ ] The Foundation hero spans the full width and is ≤ 320 px tall at 1440; its 6 stems are visible.
- [ ] Every stem of every branch is reachable (disclosure); every stem row and tile opens the sheet with all of its leaves in order; live rows link; Esc and scrim click close it and focus returns to the opener; `/topics#kotlin/null-safety` loads with that sheet open.
- [ ] `document.body.innerText` on `/topics`, `/` (each card type) and the export HTML contains none of "Needs:", "Coming next", "learning this".
- [ ] Filter: arrow keys change the selection; only that bough's cards show; DOM ≤ 1,500.
- [ ] Node cards at 1440×900 (kotlin and dsa branch, a 6+-leaf stem, a leaf): never overlap the floating header; stems are capped at 8 and leaves at 6, each with its "more" link; the footer is end-aligned.
- [ ] Canvas cursor: `grab` at rest, `grabbing` while dragging, `pointer` over a leaf (computed style after synthetic events); `?` on the focused tree opens the shortcuts dialog.

**C**
- [ ] At 1600 and 1920: three tracks (256 / 720 / 320); at 1280 and 1440: two; at 1024: one. Media queries only (`rememberWideLayout` gone).
- [ ] The prose box is 544 wide and centred in the 720 track (left offset 88 ± 1); the breadcrumb top ≥ header bottom + 32.
- [ ] All rail panels share padding, radius, border and heading style (compare computed styles); the rails stay sticky below the header while scrolling and scroll on their own when taller than the viewport.
- [ ] No "Coming next" in the DOM; with no live neighbours the EmptyState appears with its link.
- [ ] The skeleton shows while loading; CLS ≤ 0.05; `verifyLessonExport` passes on a scratch live leaf; `#kt-lesson-body` is in the snapshot.
- [ ] Outline: the current section has `aria-current`; a click moves focus to the heading.

## Risks

| Risk | Mitigation |
|---|---|
| `Box` → `Div` changes layout of pages that relied on grid stacking (`KitchenSink`, `LessonPreview`, `NotFound`) | A screenshots each before and after; the tree is one child, so it is unaffected |
| Silk rejects pseudo-element selectors, or Chrome's standard-property precedence bites | `CSS.supports` gate; `<style>` fallback in `Base.kt`; probe in A |
| 4 columns at 1200 px give cards of about 266 px; long stem titles truncate | ellipsis + `title`; full titles in the sheet; Q1 |
| Side sheet adds focus-trap code | one `SideSheet` component; same pattern as `NodeCard` (focus in, Esc, return) |
| Two rails at ≥ 1600 reverse an ADR 006 rejection | justified by L32 (use side space) and the supporting-pane pattern; Q2 |
| B touches `TreeView.kt` while the tree is perf-sensitive | B is limited to `CardHost`, the dialog and cursor writes on change; the draw p95 check is repeated |

## Rejected options

| Option | Why rejected |
|---|---|
| `position: fixed` header + `padding-top` offsets | Fragile and duplicated per page; it also does not fix the real cause (`Box` stacking). Sticky in flow needs no offset. |
| Keep bento spans, 2×2 hero | The cause of the empty areas; stem counts 6-16 make spans uneven |
| CSS masonry / `grid-template-rows: masonry` | Not shipped across browsers (⚠️ not checked; not needed) |
| Stems expanded inline by default | Uneven heights; ~3,000 DOM nodes |
| Full-row bough headers inside the grid | Leave part-empty rows after 3- and 5-branch boughs |
| A JS scrollbar library | Bundle, a11y and scroll-performance cost for a cosmetic gain |
| Single-key global shortcuts (`t`, `n`, `p`) | WCAG 2.1.4; they clash with the Kotlin editor |
| Hover lift on non-clickable cards | False affordance (desktop guide: hover signals interactivity) |
| Left-aligned prose in the 720 track | The dead-space complaint; contradicts `docs/02` §4.5 |
| JS `matchMedia` layout | Post-hydration layout jump; CSS media queries are simpler |

## Consequences

- `/topics#<branch>/<stem>` opens a sheet instead of an inline expansion; the anchor ids do not change, so the WP-A/C links of ADR 006 still work.
- Pages must use `PageShell`. A page with several root siblings would stack again if someone reintroduces `Box`; review rule: "no `Box` as a page root".
- `docs/02` gains §4.6 "Layout grid and size classes", the scrollbar tokens and the component table rows; `docs/03` §6.2 gets the node-card anatomy. `architect` writes these after the wave.

## Open questions

| # | Question | Default if no answer |
|---|---|---|
| Q1 | 4 columns from 1200 (even rows, narrower cards) or 3 columns at 1200-1439 (one card alone in the last row)? | 4 |
| Q2 | Left stem nav plus right rail at ≥ 1600? | Yes |
| Q3 | "Coming soon" shown only in the legend, sheets and node cards, not on every row? | Yes (L4 wording kept) |
| Q4 | `garvitmaheshwari.in` was unreachable: share the brand colours or wordmark if Kotree should echo them | No brand cues; Kotree tokens only |
