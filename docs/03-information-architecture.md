# 03 — Information Architecture

> **Purpose:** the shape of the tree as data and as a place: semantic zoom levels, the grouped-to-split bough behaviour (D4 amendment), the tree data schema (compiled into the app as generated `TreeData.kt`, also emitted as the `/tree.json` tool artifact), the URL scheme (site root `/`, no base path), leaf states, "Start here", leaf popup, progress counter and local-storage checkmarks.
> **Read this when:** you build or review the tree canvas, camera, overlays, navigation, the content generator, the lesson page's links back to the tree, anything that reads `TreeData`, or a script or agent that reads `tree.json`.
> **Depends on:** `docs/00-decisions.md` (wins every conflict; §2 L4, L6, L25, L26; §3 D1–D4, D10–D12, D15–D17; §5; §7; §8), `docs/01-vision-and-principles.md` (P1, P2, P4), `docs/02-design-system.md` (all colour, type, spacing and motion tokens), `docs/07-technical-architecture.md` (build pipeline, export, deploy).
> **Principles used:** P1 (open by default), P2 (honest empty space), P4 (the map explains itself: at Canopy zoom the visible labels are bough names).
> **Revised:** 18 Oct 2026 for L25 (subdomain `https://kotree.garvitmaheshwari.in`, root `/`, no base path), L26 (optional video), D15 (up-next override), D16 (live list = leaf frontmatter). Bough titles in §1 approved by the author. Verified 18 Oct 2026 (§7.2): no decision changed; one consequence added (Kobweb export skips dynamic routes, so each live lesson is exported as its own route). Revised again 18 Oct 2026 (author decision): the app reads tree data compiled in as generated `TreeData.kt` (no runtime fetch); the same generator also emits `tree.json` as a tool artifact (§4.1).

This doc defines **no colours, fonts or spacing values**. Colours are referenced by token name only: the branch accent is `color.branch.<slug>`; leaf-state, bark and shadow colours are the tokens `docs/02` defines for those roles.

---

## 1. Hierarchy recap

```
Tree (the site, https://kotree.garvitmaheshwari.in/)
├── Bough  (5: roots + 4 limbs, docs/00 §5)
│   └── Branch  (17: foundation + 16)
│       └── Stem   (topic group, from _roadmap.md)
│           └── Leaf  (one lesson; may carry a Fruit = capstone, D12)
```

Bough display titles (the roadmap only stores the slug, so this table is the source; approved by the author 18 Oct 2026):

| Bough slug | Display title | Branches (order = docs/00 §5) |
|---|---|---|
| `roots` | Foundation | `foundation` |
| `kotlin-android` | Kotlin and Android | `kotlin`, `android-core`, `ui`, `concurrency` |
| `architecture-kmp` | Architecture and KMP | `architecture-data`, `app-networking`, `kmp` |
| `cs-theory` | CS theory | `dsa`, `daa`, `os`, `networking` |
| `craft` | Craft | `testing`, `profiling`, `native-ndk`, `security`, `tools` |

Branch display titles come from docs/00 §5. Boughs and branches are a fixed registry (D4); the generator holds this registry and fails the build if a `_roadmap.md` names a bough or branch that is not in it, or names the wrong bough for its branch.

---

## 2. Semantic zoom levels

### 2.1 The zoom unit

`z` = current camera scale divided by the scale at which the **whole tree fits the viewport** (roots included). `z = 1` is "whole tree fits". Allowed range: `0.8 ≤ z ≤ 24`.

Using a relative `z` (not raw pixels-per-unit) keeps thresholds the same on every screen size.

### 2.2 The five levels

Names are fixed: **Canopy, Bough, Branch, Stem, Leaf**. Code IDs: `Z0`–`Z4` (Phase 2 in `docs/10` builds Z0–Z2).

| Level | ID | Enter when `z ≥` | Leave downward when `z <` | What it is for |
|---|---|---|---|---|
| Canopy | Z0 | 0.8 (min) | — | Whole tree. Boughs **grouped**; the only tree labels are bough names (P4). Orientation. |
| Bough | Z1 | 1.8 | 1.62 | One bough fills the view; its branches have **split** apart. |
| Branch | Z2 | 3.5 | 3.15 | One branch: its stems and their names. Branch accent appears (D5). |
| Stem | Z3 | 7.0 | 6.3 | One stem: its visible leaves are clickable. |
| Leaf | Z4 | 14.0 | 12.6 | Leaf titles readable next to each leaf. |

Hysteresis: going up switches at the threshold `T`; going down switches at `0.9 × T`. This stops labels flickering when the wheel stops right on a boundary.

### 2.3 What each level shows and does

"Visible" means drawn; "clickable" means it has a hit area (§2.6). Shadow leaves are never drawn (D3).

| Level | Pixel world (backdrop, D2) | Labels (overlay) | Clickable | Click result |
|---|---|---|---|---|
| Canopy | Trunk, roots, 4 grouped bough limbs, fruits that are live | Bough names only: the 4 bough titles plus "Foundation" (the `roots` bough title) on the roots; no branch, stem or leaf names. Plus the "Start here" marker at the trunk base | Each bough, the roots | Camera flies to that bough (lands at `z ≥ 2.4`, fully split) |
| Bough | Bough limbs split into branch limbs; stems as short twigs, no labels | Branch titles of the focused bough; other bough titles stay | Each branch | Camera flies to that branch (lands at `z ≥ 4.0`) |
| Branch | Stems as twigs; live and up-next leaves drawn small | Stem titles (shadow stems in the shadow-text token); branch panel opens in UI chrome | Each stem | Camera flies to that stem (lands at `z ≥ 8.0`) |
| Stem | Leaves of the stem at full size, spaced for clicking | Stem title; leaf titles appear on hover/focus only | Each live or up-next leaf | Leaf popup opens (§6) |
| Leaf | Same as Stem, larger | Every visible leaf's title next to it | Each live or up-next leaf | Leaf popup opens (§6) |

Rules that apply at every level:

- The **focused branch** is the branch whose bounding box contains the viewport centre, once `z ≥ 3.5`. Only then does `color.branch.<slug>` appear in panels, buttons and the breadcrumb (D5). The tree itself never takes the accent.
- **Breadcrumb** (UI chrome, top-left): `Kotree › <Bough> › <Branch> › <Stem>`, built from the focused nodes at the current level. Each crumb flies the camera to that node.
- **Shadow stems** (no live leaves) are drawn as gray twigs with their title from Branch level up. Hover or focus shows: `Coming soon: <stem.why>` (P2). A stem with live leaves uses the normal stem-label token.
- **Fruits** (D12) are drawn at the branch tip only when their leaf is `live`; clicking a fruit opens that leaf's popup.
- Clicking empty space does nothing. Double-click zooms in one level around the pointer. `Esc` closes a popup; a second `Esc` zooms out one level.

Rejected alternatives:
- Continuous zoom with no named levels: labels would need per-pixel rules; harder to test.
- Separate pages per level (`/kotlin`): breaks the "one standing tree" model of L6 and needs extra exported pages.
- Three levels only (tree, branch, leaf): 16 branches in one view is the crowd D4 exists to avoid.

### 2.4 Grouped-to-split boughs (D4 amendment)

At Canopy each bough is **one fused limb** carrying **one label** (the bough title). As the camera zooms in, the limb splits into its branch limbs, and the bough label hands over to the branch labels. Inside a bough, only that bough's branch labels (plus Foundation's) are shown; the other boughs' branch labels are not rendered, so they also leave the tab order (author decision, 20 Oct 2026).

Split progress for every bough:

```
p = smoothstep( clamp( (z - 1.4) / (2.4 - 1.4), 0, 1 ) )
p_drawn = round(p * 8) / 8          // 9 discrete steps
```

| `z` | `p` | What the visitor sees |
|---|---|---|
| ≤ 1.4 | 0 | Fused bough limb, bough title only |
| 1.4 – 1.8 | 0 → ~0.5 | Branch limbs start to fan out from the bough fork point; bough title still shown |
| 1.8 (enter Bough) | ~0.5 | Branch titles fade in (opacity = `2p − 1`, clamped); bough title moves to the breadcrumb |
| ≥ 2.4 | 1 | Branch limbs fully separate at their own anchors |

How the split is drawn (for the generator and renderer, D1):
- Each branch has a final anchor path from the seeded layout. At `p = 0` every branch path of a bough is drawn **on the bough's centre path**; at `p = 1` on its own path. Intermediate steps linearly interpolate the control points.
- `p_drawn` is quantised to 8 steps so the pixel limbs move in visible pixel steps (fits the pixel look) and each step can be **cached as one bitmap per bough** (9 bitmaps × 4 boughs). No per-frame re-rasterisation while zooming.
- `roots` has one branch (`foundation`), so it never splits; its label reads "Foundation" at every level.
- Camera "fly to bough" always lands at `z ≥ 2.4`, so a click never leaves the visitor in a half-split state.
- With `prefers-reduced-motion`, the camera cuts instead of flying; split steps still follow `z`.

Rejected: splitting only the bough under the cursor (camera focus changes would make other boughs pop); splitting by time animation after a click (wheel zoom would not split at all).

### 2.5 Camera landing targets

| Fly to | Fit rule | Then clamp `z` to at least |
|---|---|---|
| Whole tree (Home key, "Kotree" crumb) | Fit tree bounds, 5% margin | `z = 1` |
| Bough | Fit bough bounds (all its branches at `p = 1`), 8% margin | 2.4 |
| Branch | Fit branch bounds, 8% margin | 4.0 |
| Stem | Fit stem bounds, 12% margin | 8.0 |
| Leaf (from deep link or "Start here") | Centre on leaf | 14.0 |

Fly duration and easing come from the motion tokens in `docs/02`; budget ≤ 600 ms (§10).

### 2.6 Click targets and keyboard

- Every clickable node has a hit area of **at least 24 × 24 CSS px** at the level where it becomes clickable (matches WCAG 2.2 target-size minimum). If the drawn shape is smaller, the hit area is padded.
- Hit areas at the same level never overlap; the layout guarantees **≥ 8 CSS px gap** between leaf hit areas at `z = 7` (Stem entry). Leaves are not clickable below Stem level; clicking their stem zooms in instead.
- Clickable nodes are DOM elements in the overlay (D2), so they get focus, `aria-label` and a visible focus ring for free.
- Keys: `Tab`/`Shift+Tab` move through clickable nodes of the current level in data order; `Enter`/`Space` activate (same as click); `Esc` close popup, then zoom out one level; `+`/`-` zoom around viewport centre; arrow keys pan; `Home` fly to whole tree.

### 2.7 Acceptance criteria (zoom)

- [ ] At `z = 1` exactly 5 tree labels are visible, all bough names: 4 bough titles + "Foundation" (plus the "Start here" marker). No branch title is visible.
- [ ] Wheel-zooming from `z = 1` to `z = 2.4` over any bough shows its branch limbs separating in 8 visible steps, with no frame-time spike above budget (§10).
- [ ] Clicking a bough at Canopy lands at Bough level, fully split, branch titles visible.
- [ ] Stopping the wheel at `z = 1.80 ± 0.02` does not flicker labels.
- [ ] `color.branch.<slug>` appears in chrome only when `z ≥ 3.5` and that branch is focused.
- [ ] Every branch, stem and visible leaf is reachable with the keyboard alone.
- [ ] No clickable hit area is smaller than 24 × 24 CSS px or overlaps another at its level.

---

## 3. Leaf states

Values are fixed by docs/00 §7: `shadow` | `upnext` | `live`. The **generator computes** the state in the tree data (`TreeData.kt` and `tree.json`, §4). The only hand-typed inputs are frontmatter `status: live` (D16) and the optional `upnext: true` override (D15).

### 3.1 Derivation (build time)

The list of live leaves is the leaves' own frontmatter (D16). There is **no separate published-IDs file**.

| State | Rule |
|---|---|
| `live` | `content/<branch>/<stem>/<leaf>.md` exists, its frontmatter `id` equals the roadmap ID, and its frontmatter `status` is `live`. |
| `upnext` | Per branch, exactly one leaf, or none when every leaf of the branch is live (D3). Chosen in this order (D15): **1.** the non-live leaf whose file has frontmatter `upnext: true` (override); **2.** otherwise the **first leaf in roadmap YAML order** that is not `live` (auto). |
| `shadow` | Every other leaf. |

Override rules (D15):
- `upnext: true` is optional; absent or `false` means "use the auto rule". It only needs a draft leaf file (any non-live `status`) to exist.
- At most one leaf per branch may carry `upnext: true`; two or more fail the build (V9).
- `upnext: true` on a `live` leaf fails the build (V9), so the flag is removed in the same commit that publishes the leaf (`housekeeper` does this).

A leaf file whose frontmatter `status` is not `live` (a draft inside pipeline P1) gets **no page** and changes nothing in the tree except through its `upnext` flag. This is how "do not set live before reviewer PASS" is enforced by the build.

### 3.2 What each state looks like and does

| State | Drawn? | Colour (token role from docs/02) | Popup | Lesson URL |
|---|---|---|---|---|
| `shadow` | No (D3). Its stem still shows in gray with a "Coming soon" hint. | — | — | 404 (P2: no stub pages) |
| `upnext` | Yes, from Branch level | Shadow gray + up-next glow | "Coming next" popup (§6.2) | 404 |
| `live`, `easy` | Yes, from Branch level | Light green leaf | Full popup (§6.1) | Lesson page |
| `live`, `medium` | Yes | Mid green leaf | Full popup | Lesson page |
| `live`, `hard` | Yes | Dark green leaf (L7) | Full popup | Lesson page |

Leaf colour reflects **topic state and difficulty only**, never the visitor's checkmark (§9 draws checkmarks as a separate badge).

### 3.3 Acceptance criteria (states)

- [ ] On day one (no leaf files) every branch shows exactly one glowing up-next leaf and no other leaves.
- [ ] Publishing the up-next leaf of a branch makes it green and moves the glow to the next non-live leaf in YAML order after one build (no override present).
- [ ] A draft leaf file with `upnext: true` moves that branch's glow to it; removing the flag returns the glow to the auto leaf.
- [ ] A leaf file with `status: upnext` or `shadow` and no `upnext: true` produces no page and does not change the tree.
- [ ] Two `upnext: true` files in one branch, or `upnext: true` on a live leaf, fail the build (V9).
- [ ] `/<branch>/<stem>/<leaf>` for a shadow or up-next leaf returns the 404 page (§7.4).

---

## 4. Data model: tree data (`TreeData.kt` + `tree.json`)

### 4.1 Source, generation and the two outputs (author decision 18 Oct 2026)

- **Input:** the YAML block of every `content/<branch>/_roadmap.md` (docs/00 §8.1), plus frontmatter of every `content/<branch>/<stem>/<leaf>.md` (docs/00 §8.2), plus the fixed bough/branch registry (§1).
- **Generator:** one Gradle task, `generateTreeData` (`docs/07` §6.4). It parses the inputs once and writes **two outputs from the same in-memory model**:

| Output | What | Who reads it |
|---|---|---|
| `TreeData.kt` (generated Kotlin, compiled into the site) | The data the app uses. **No runtime fetch, no `kotlinx.serialization` in the bundle.** | The tree page (canvas + overlay), the lesson page (breadcrumb, previous/next, popup data for "back to tree"), the 404 page, the progress counter, the checkmark count `K`. |
| `site/build/tree.json`, published at `/tree.json` | A plain JSON artifact with the schema of §4.2. **The app never reads it at runtime.** | Tools and agents only: `housekeeper`'s `PROGRESS.md` generator, progress counter scripts, link checks, any agent that wants the tree without parsing Kotlin. |

- **Same data:** both outputs hold the same values with the same field names (§4.2). `TreeData.kt` writes the schema as Kotlin data classes and literals (`null` stays `null`; nested arrays become `listOf(...)`). Exact paths and the publish step are owned by `docs/07`.
- **When:** every build (dev and export). Neither output is committed; both are build artefacts.

Rejected: a runtime fetch of `tree.json` by the app (extra request before the first paint, a loading state, and `kotlinx.serialization` in the bundle); generated Kotlin only (scripts and agents would have to parse Kotlin source); JSON only plus a hand-written parser in the app (same request cost, more code); a flat list of leaves only (every consumer would rebuild the hierarchy). Cost accepted: every content change needs a rebuild, which lesson pages need anyway (Kobweb compiles Markdown to Kotlin).

### 4.2 Schema (version 1)

Shown as JSON (the `tree.json` form); `TreeData.kt` mirrors it field for field. Nested in tree order. All arrays are in recommended learning order. Field names are final.

```jsonc
{
  "schema": 1,                                  // bump on any breaking change
  "seed": 20261017,                             // procedural layout seed (D1); fixed constant
  "counts": { "live": 1, "total": 412 },        // public counter (§8)
  "startHere": "foundation/how-code-runs/what-is-a-program",   // §5; full leaf ID
  "boughs": [
    {
      "id": "kotlin-android",
      "title": "Kotlin and Android",
      "order": 1,                               // roots = 0
      "branches": [
        {
          "id": "kotlin",
          "title": "Kotlin language",
          "order": 1,                           // position across all branches, docs/00 §5
          "accent": "color.branch.kotlin",      // token name, never a hex value
          "counts": { "live": 1, "total": 48 },
          "upNext": "kotlin/null-safety/what-is-a-nullable-type",   // D15 result; null when branch is complete
          "stems": [
            {
              "id": "kotlin/null-safety",
              "slug": "null-safety",
              "title": "Null Safety",
              "why": "One sentence — why a fresher needs this stem.",
              "order": 1,
              "counts": { "live": 1, "total": 6 },
              "leaves": [
                {
                  "id": "kotlin/null-safety/what-is-null",
                  "slug": "what-is-null",
                  "title": "What is null and why does it crash apps?",
                  "status": "live",             // shadow | upnext | live
                  "difficulty": "easy",         // easy | medium | hard
                  "order": 1,                   // 1-based within the branch, YAML order
                  "requires": [],               // full leaf IDs, same branch
                  "fruit": false,
                  "path": "/kotlin/null-safety/what-is-null",   // site-root path; null unless live
                  "summary": "One sentence for the leaf popup.", // live only, else null
                  "video": "",                  // live only; "" = no video yet (L26, optional)
                  "publishedOn": "2026-10-20"   // live only, else null
                }
              ]
            }
          ]
        }
      ]
    }
  ]
}
```

Field rules:

| Field | Rule |
|---|---|
| `id` | Leaf: `<branch>/<stem>/<leaf>`; stem: `<branch>/<stem>`; branch and bough: slug. Same as docs/00 §8. |
| `slug` | `^[a-z0-9]+(-[a-z0-9]+)*$`. Unique within its parent. |
| `title` | Leaf title from the roadmap. If the leaf file exists, its frontmatter `title` must be identical, else the build fails (roadmap is canonical; change it through `curriculum-designer`). |
| `difficulty`, `requires`, `fruit` | From the roadmap. Leaf frontmatter must match, else the build fails. |
| `order` (leaf) | 1-based position in the branch, counting across stems. Drives previous/next and up-next. |
| `path` | Absolute route from the site root: `/<branch>/<stem>/<leaf>`. No prefix of any kind (L25). `null` unless `live`. |
| `summary`, `publishedOn` | From frontmatter; `null` unless `live`. Shadow leaves carry no summary (P2). |
| `video` | From frontmatter; `null` unless `live`. For a live leaf, `""` means no video yet (L26). Any non-empty value must be an `https://` YouTube URL, else the build fails. |
| `upNext` (branch) | Full leaf ID chosen by §3.1 (D15); `null` when every leaf of the branch is live. |
| `accent` | The literal token name `color.branch.<slug>`; the renderer resolves it through the token file from `docs/02`. |
| `counts` | `live` = live leaves; `total` = all leaves in the roadmap(s) below this node. |

What is **not** in the tree data (either output): lesson bodies, sim configs, layout coordinates. Coordinates are computed in the browser from `seed` + order (D1), so the data stays small and the layout stays deterministic.

### 4.3 Validation (build fails with a clear message)

Owned by `housekeeper` and the generator (Phase 3). Each rule prints the file path and the offending ID.

| # | Rule |
|---|---|
| V1 | Unknown bough or branch, or branch under the wrong bough (§1). |
| V2 | Slug fails the regex, or a duplicate stem/leaf slug inside a branch. |
| V3 | `requires` names an unknown leaf, a leaf in another branch, or a leaf that appears later (docs/00 §8.1). |
| V4 | A leaf file with no matching roadmap row, or a frontmatter `id` that does not match its file path. |
| V5 | Frontmatter `title`, `difficulty`, `requires` or `fruit` differs from the roadmap. |
| V6 | The YAML block and the tables of a `_roadmap.md` list different leaves or a different order (docs/00 §8.1). |
| V7 | A first path segment is a reserved word (§7.3). |
| V8 | A previously published leaf ID is no longer `live` (slugs never change, CLAUDE.md rule 6). No separate published-IDs file (D16): the check compares the current leaf files with the leaf files that had `status: live` in git history. Open by design, not a fact to look up: the exact git command and where it runs (local pre-commit or CI) is a `docs/07` decision. |
| V9 | More than one leaf in a branch has `upnext: true`, or a `live` leaf has `upnext: true` (D15). |

### 4.4 Acceptance criteria (data)

- [ ] Two builds from the same content produce byte-identical `TreeData.kt` **and** byte-identical `tree.json`.
- [ ] `TreeData.kt` and `tree.json` from one build hold the same values: a test loads both and compares every field.
- [ ] Adding one live leaf file changes exactly, in both outputs: that leaf's `status`/`path`/`summary`/`video`/`publishedOn`, the next leaf's `status`, the branch `upNext`, and `counts` on the path to the root.
- [ ] Each of V1–V7 and V9 has a failing fixture that stops the build with a message naming the file.
- [ ] Neither output contains hex colours or lesson body text.
- [ ] The production bundle makes no request for `/tree.json` (Network panel) and does not include `kotlinx.serialization`.

Example IDs in this doc other than `kotlin/null-safety/what-is-null` are illustrative; the real ones live in each `_roadmap.md`.

---

## 5. "Start here" path

The Foundation is recommended before every other branch (docs/00 §8.1), but nothing is locked (P1, L2).

| Item | Rule |
|---|---|
| `startHere` (in the tree data, §4.2) | The first `live` leaf of `foundation` in order. If none is live, the `foundation` up-next leaf. |
| Marker | A "Start here" label at the trunk base, visible at every level where the trunk base is on screen. |
| Trail | A pixel trail on the ground from the trunk base along the roots through the Foundation leaves in order. Segments up to the last live leaf use the live-trail token; the rest use the shadow token (`docs/02`). |
| Button | "Start here" button in the top UI chrome, visible at every level. |
| Action (marker or button) | Fly to the `startHere` leaf (§2.5, lands at Leaf level) and open its popup. |
| After Foundation | The lesson page "Next" link follows Foundation order; after the last live Foundation leaf it shows the Foundation up-next leaf as "Coming next" (no link), plus "Back to the tree". No forced hand-off into another branch. |

Acceptance:
- [ ] On a fresh visit, one click on "Start here" opens the popup of `startHere`.
- [ ] Any leaf of any branch is still reachable without touching "Start here" (P1).

---

## 6. Leaf popup, then lesson page

Clicking (or `Enter` on) a visible leaf always opens a **popup first**; the popup leads to the lesson page (L6). The popup is UI chrome: smooth Material 3 Expressive, not pixelated (D6). It is anchored next to the leaf, keeps the leaf visible, and closes on `Esc`, click outside, or camera move larger than one viewport.

### 6.1 Popup for a `live` leaf

| Element | Content | Notes |
|---|---|---|
| Overline | `<Branch title> › <Stem title>` | Uses `color.branch.<slug>` |
| Title | Leaf `title` | |
| Difficulty chip | "Easy topic" / "Medium topic" / "Hard topic" | Describes the topic, not the learner (docs/00 §7) |
| Summary | Leaf `summary` (one sentence) | |
| Primary button | "Open lesson" → `/<branch>/<stem>/<leaf>` | Default focus |
| Secondary button | "Watch the video" → `video` (new tab) | Rendered **only when `video` is non-empty** (L26). Not rendered at all (no disabled state, no placeholder) while `video` is `""`. Video is optional; a leaf can be live without one. |
| Checkmark | "I finished this" toggle (§9) | Hidden when local storage is unavailable |
| Fruit note | "Capstone at this leaf" | Only when `fruit: true` |

### 6.2 Popup for the `upnext` leaf

Overline as above; title; the line "Coming next. The author is learning this now."; no buttons except Close. No summary is shown (the author has not written it yet, P2).

### 6.3 Lesson page links (structure is owned by `docs/04`)

- Breadcrumb `Kotree › <Bough> › <Branch> › <Stem>`; each crumb links to the tree at that node (§7.2).
- Previous / Next = the nearest `live` leaves before/after this one in branch `order`. Shadow leaves are skipped; the up-next leaf may appear as "Coming next" without a link.
- Video (L26): the "Watch the video" button in the lesson header and the lesson's video section are rendered **only when the leaf's `video` is non-empty**. With `video: ""` neither exists on the page. Once the author adds the YouTube URL, one rebuild makes both appear (popup button too). Placement of the section is owned by `docs/04`.
- "Back to the tree" → `/#<leaf id>`; the tree restores the exact camera if a saved camera for that leaf exists in `sessionStorage` key `kotree.v1.camera` (`{"leaf": "<id>", "x": .., "y": .., "z": ..}`), otherwise it flies to the leaf (§2.5).

Acceptance:
- [ ] Click live leaf → popup; "Open lesson" → lesson page; "Back to the tree" → same camera as before.
- [ ] Up-next popup has no link to a page.
- [ ] With `video: ""`, neither the popup nor the lesson page contains a "Watch the video" button or a video section.
- [ ] Setting `video` to a YouTube URL and rebuilding shows the button in the popup and lesson header and the video section on the lesson page.

---

## 7. URL scheme

### 7.1 Origin and root

The site is `https://kotree.garvitmaheshwari.in` (L25), hosted on Vercel via a Hostinger DNS CNAME (D17). The site root is `/`. There is **no base path** (docs/00 O2 dropped). Rules:

- No `basePath` in `.kobweb/conf.yaml`; no `BasePath` calls in code.
- Every path in code and in the tree data is an absolute path from the root (`/tree.json`, `/kotlin/null-safety/what-is-null`). No `/kotree` prefix anywhere.
- Internal links use plain root-relative paths; navigation still goes through one small helper so routing changes live in one place.
- Export layout and Vercel settings are `docs/07` decisions.

### 7.2 Routes

| URL | What | Exists when |
|---|---|---|
| `/` | The tree, Canopy (`z = 1`) | Always |
| `/#<bough>` | Tree, flown to that bough | Always |
| `/#<branch>` | Tree, flown to that branch | Always |
| `/#<branch>/<stem>` | Tree, flown to that stem | Always |
| `/#<branch>/<stem>/<leaf>` | Tree, flown to that leaf, popup open | Leaf is `live` or `upnext`; for a shadow leaf, falls back to its stem |
| `/<branch>/<stem>/<leaf>` | Lesson page | Leaf is `live` |
| `/tree.json` | Tool artifact (§4.1); the app never fetches it | Always |
| anything else | 404 page | — |

Bough and branch slugs never collide (docs/00 §5), so `#<slug>` is unambiguous.

The tree updates the hash with `history.replaceState` when the focused node changes (no new history entry per zoom step). Pressing Back from a lesson page returns to the tree URL that opened it.

Canonical lesson URLs have **no trailing slash**.

Verified 18 Oct 2026:
- **Kobweb routes** come from files: package path plus kebab-cased file name, or `@Page(routeOverride = ...)`; dynamic segments use `{name}` and are read with `ctx.route.params.getValue("name")`. Navigation is `ctx.router.navigateTo("/path")` (inside widgets, `rememberPageContext()`). A custom 404 is set with `ctx.router.setErrorPage { }` in an `@InitKobweb` function. Source: https://kobweb.varabyte.com/docs/concepts/foundation/routing . The routing docs do not mention base paths; with root `/` none is needed (L25).
- **Static export** is `kobweb export --layout static`, output `.kobweb/site`; the exported HTML refers to root-absolute assets such as `/mysite.js`. **Dynamic routes are skipped by the export** unless added one by one with `addExtraRoute("/path", exportPath = "...")` in the build script (the docs' example maps `/users/default/posts/0` to `users/index.html`). Source: https://kobweb.varabyte.com/docs/concepts/foundation/exporting
- **Consequence (important):** a single dynamic page `/{branch}/{stem}/{leaf}` would produce no exported file for any lesson. So every live leaf needs its own exported file. `docs/07` §6.7 decided (20 Oct 2026): each lesson gets a fixed `@Page` route from the markdown plugin, so the export already writes it; `addExtraRoute` is only a backup if a dynamic lesson page is chosen (ADR 005). Shadow and up-next leaves get no file, which already matches the 404 rule in §6.
- **Vercel** serves `foo.html` at `/foo` when `"cleanUrls": true` is set in `vercel.json`, redirecting `/foo.html` to `/foo` with 308; `"trailingSlash": false` makes `/foo/` redirect to `/foo`. Source: https://vercel.com/docs/project-configuration/vercel-json . Decision: set both (`cleanUrls: true`, `trailingSlash: false`) so there is exactly one canonical URL; `docs/07` already carries `cleanUrls` (its §5.1).

⚠️ VERIFY (Phase 0, owned by `docs/07`): the file name Kobweb actually writes for a route such as `/kotlin/null-safety/what-is-null` (`kotlin/null-safety/what-is-null.html` or `.../what-is-null/index.html`). The docs we could read do not say; only the `exportPath = "users/index.html"` example hints at folders. Test (see `docs/07` §6.7 and `docs/11`): export a sample lesson page, run `kobweb export --layout static`, list `.kobweb/site`; with `exportPath` set explicitly we choose the shape ourselves (prefer the `foo.html` shape with `cleanUrls`). Then deploy a preview to Vercel and request `/kotlin/null-safety/what-is-null` with `curl -I`: expect 200 with no redirect, and `/…/what-is-null/` expect 308 to the non-slash form.

Rejected: query string `?at=kotlin` (reaches the server and CDN cache keys for no gain); real pages per branch/stem (`/kotlin`): extra exported pages and a second way to show the same tree (L6); leaf ID without stem (`/kotlin/what-is-null`): stem is part of the ID in docs/00 §8.

### 7.3 Reserved first segments

No branch slug may be any of: `assets`, `fonts`, `images`, `kitchen-sink`, `about`, `progress`, `api`, `tree.json`, `404`, or any name starting with `_`. Validation V7 enforces this.

### 7.4 404 page

Reads the compiled `TreeData` (no fetch). If the path matches a known shadow or up-next leaf, it says "This leaf has not grown yet." with the stem's `why` line and a link to `/#<branch>/<stem>`. Otherwise a plain "Not found" with a link to `/`.

Acceptance:
- [ ] Every link and asset works at `https://kotree.garvitmaheshwari.in/`, and the codebase contains no `/kotree` path prefix and no `BasePath` usage.
- [ ] Opening `https://kotree.garvitmaheshwari.in/#kotlin/null-safety` in a new tab lands at Stem level on that stem.
- [ ] Zooming around the tree does not add browser history entries.

---

## 8. Progress counter (D11)

| Where | Text | Source |
|---|---|---|
| Tree, bottom-left chrome, Canopy and Bough | `N of M leaves grown` | `counts.live`, `counts.total` (root) |
| Branch panel and lesson header, Branch level and deeper | `N of M leaves grown in <Branch title>` | branch `counts` |
| Stem label tooltip | `N of M` | stem `counts` |
| Visitor line, under the public counter | `You have finished K` | §9; shown only when `K > 0` |

- `M` counts every leaf in every roadmap, Foundation included. `M` may grow when a roadmap grows; that is expected.
- Counts are computed at build time (no runtime counting, no network calls).
- No percentage bars, streaks or celebrations (docs/01 §5.2: "the moment is quiet").

Acceptance:
- [ ] Public counter equals the number of leaves with `status: live` in `TreeData`, and the same number in `tree.json`.
- [ ] Sum of branch `counts.total` equals root `counts.total`.

---

## 9. Visitor checkmarks in local storage (D10)

| Item | Decision |
|---|---|
| Storage | `window.localStorage`, key `kotree.v1.done` |
| Value | `{"v":1,"ids":["kotlin/null-safety/what-is-null", ...]}` — full leaf IDs, sorted, no duplicates |
| Read | Once at page start; parse errors → treat as empty and overwrite on the next toggle |
| Write | On every toggle, whole value rewritten |
| Toggle points | Popup "I finished this" (§6.1) and a "I finished this" button at the end of the lesson page (`docs/04` places it) |
| Allowed IDs | Only `live` leaves can be toggled. Stored IDs that are not live are kept but not counted or shown. |
| `K` (§8) | Count of stored IDs that are `live` in the current compiled `TreeData` |
| Tree badge | A small check badge on the leaf, drawn in the UI overlay (smooth, not pixel). Leaf colour does not change (§3.2). |
| Other tabs | Listen to the `storage` event and refresh badges and `K` |
| Reset | "Clear my checkmarks" in the counter's menu, with a confirm step |
| Unavailable storage | Access wrapped in try/catch; on failure all toggles, badges and the visitor line are hidden. No error shown. |
| Privacy | Nothing leaves the browser. No cookies, no sync, no export in v1 (L2, docs/00 §9). |

Rejected: cookies (sent to the server with every request for no reason); IndexedDB (async API for a list of a few hundred strings); recolouring checked leaves (would hide the topic's real state).

Acceptance:
- [ ] Toggle a leaf, reload: badge and `K` persist. Clear browser data: both are gone.
- [ ] With local storage blocked, the tree and lessons work and show no checkmark UI.
- [ ] Toggling in one tab updates another open tab without a reload.

---

## 10. Performance budget

Measured on the author's laptop, Chrome, production build.

| Metric | Budget |
|---|---|
| `TreeData` share of the main bundle | Owned by `docs/07` §8 (≤ 60 KB gzip at 500 leaves) |
| `tree.json` artifact size at ~1,000 leaves (tools only, not loaded by the app) | ≤ 250 KB raw, ≤ 50 KB gzip |
| `TreeData` init + layout (D1) | ≤ 50 ms |
| Pan/zoom frame rate, every level | 60 fps (no frame > 16.7 ms in a 10 s wheel-zoom trace) |
| Bough split bitmaps (§2.4) | ≤ 36 cached bitmaps, built lazily the first time a step is needed |
| Overlay DOM nodes mounted at once | ≤ 200 (cull nodes outside the viewport and below their level) |
| Camera fly | ≤ 600 ms; instant with reduced motion |
| Local storage read | Once per page load |

---

## 11. Risks

| Risk | Mitigation |
|---|---|
| Vercel serving `/a/b/c` without `.html` | Settled in §7.2 by `cleanUrls: true` + `trailingSlash: false`; only Kobweb's emitted file name is still ⚠️ VERIFY (§7.2 Phase 0 test) |
| Kobweb skips dynamic routes on export (verified, §7.2) | Each live leaf gets a fixed `@Page` route so the export writes it; `addExtraRoute` is the backup (`docs/07` §6.7) |
| Strict title/difficulty match (V5) slows small wording fixes | Fix goes through `curriculum-designer`, one roadmap edit; keeps one source of truth |
| Stale `upnext: true` left on a leaf after publishing | V9 fails the build; `housekeeper` removes the flag in the publish commit |
| V8 relies on git history instead of a list file (D16) | Shallow CI clones may hide history; `docs/07` fixes where the check runs |
| Many stems in one branch could break the 8 px gap rule at `z = 7` | Layout may raise the Stem landing `z` per stem; the rule (24 px, 8 px gap) is fixed, the landing `z` is not |

---

## 12. Open questions

| # | Question | Default until answered |
|---|---|---|
| Q1 | ~~Can the author pick a different up-next leaf?~~ **Resolved 18 Oct 2026 (D15):** yes, with `upnext: true` in the leaf's frontmatter (§3.1). | — |
| Q2 | ~~Exact file for the published-ID list (V8).~~ **Resolved 18 Oct 2026 (D16):** no file; V8 reads git history. Where the check runs is for `docs/07`. | Run locally before commit. |
| Q3 | ~~Bough display titles.~~ **Resolved 18 Oct 2026:** approved as written in §1. | — |
