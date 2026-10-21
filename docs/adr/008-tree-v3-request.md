# ADR 008 (request): Tree v3 — airy layout, canopy dots, backdrop, autumn tracker

Status: requested by the author on 21 Oct 2026; `architect` writes the plan below this brief, then `ui-engineer` builds. Not started.
Author reference images (described): (1) a huge banyan tree with a dense round green canopy, thick trunk and hanging roots, dramatic cloudy sky, red earth and grass; (2) the Kotree sketch: black trunk, roots labelled Foundation, four boughs (Kotlin and Android, Architecture and KMP, CS theory, Craft).

## Author's requests (verbatim intent)
1. **More space.** Increase the gap between stems and leaves, and between branches. Leaves and stems must have "plenty of space"; nothing crowded.
2. **Unzoomed tree looks empty.** When zoomed out, add many small dotted leaves (a soft canopy of dots) so the tree reads as a full, lush tree like the banyan photo. Dots fade out and real leaf nodes (clickable) fade in as the camera zooms in (semantic zoom; no pop).
3. **Backdrop.** The background is plain white and boring. Add a tasteful design (inspired by the banyan photo: soft sky gradient, light clouds, distant hills or ground, subtle texture). Keep the page readable; Hard rule 2 (white background, no dark mode) is relaxed by the author for the tree canvas only, via a new decision L35 recorded by the architect in docs/00-decisions.md. Lesson and topics pages stay white.
4. **Progress tracker with autumn effect.** When the author marks a topic (leaf) as done, that leaf transitions from green to autumn (yellow then orange, dried look), smoothly animated. The more topics done, the more autumn the canopy: branches and the unzoomed dot-canopy tint by the fraction done. Marking is a click on the leaf's sheet ("Mark as done" / "Undo"). State is stored in the browser (localStorage, wrapped in try/catch, works without it), keyed by leaf path; no login, no server. Include a small legend and a counter ("N of M done"). Done is separate from "live": only live leaves can be marked.

5. **Motion while zooming (added by the author).** As the camera zooms in or out, done leaves visibly shift colour (green to yellow to orange, with a gentle sway or flutter), while leaves not marked done stay green and still. The motion is tied to zoom/pan activity (and a short ease when a leaf is first marked done), not a constant animation loop, so idle CPU stays near zero. Reduced-motion: no sway, colours switch instantly.

## Constraints
- Keep smooth zoom (frame budget in ADR 006/007); dots are drawn from a precomputed buffer or an offscreen cache, not per-frame random.
- Everything token-driven (Tokens.kt); autumn palette added as tokens. Respect prefers-reduced-motion (no animated transition, instant colour).
- Layout changes must keep `LayoutCheck.kt` invariants and the GeometryHash seed stable per decision.
- kotree.js gzip budget 600,000 B (wave-check). Last measured 334,045 B.
- a11y: done state exposed in A11yNodes (aria text), not by colour alone (also a check-mark glyph in the sheet).
- Verification: GitHub Actions build (validateContent + export + wave-check). One on-demand build at the end (CLAUDE.md CI rule).
