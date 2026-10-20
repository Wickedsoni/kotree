# DR WP-B (topics bento) - unverified items

Not compiled locally (dl.google.com blocked). CI must compile with no `w:` warnings.

Unverified:
- `ctx.stylesheet.registerStyle(".kt-bento-wide")` with a class selector plus `cssRule(MediaFeature("min-width", ...))` (ui/Base.kt). Pattern copied from the existing `"*"` rule.
- `classes(...)` inside `toAttrs { }` (BranchCard), `window.requestAnimationFrame { }` and `asDynamic()?.scrollIntoView()` (Topics).
- `Chip(..., ink, fill)` new optional parameters; `LeafStatus.Shadow` label changed "Planned" to "Coming soon" (KitchenSink shows the new label).
- Stem leaf strip is one node with hard-stop `linear-gradient` (keeps DOM small); DOM <= 1,500 with stems closed is an estimate. Up-next stems start open (17 of them), so the default page is above the closed-stem count.
- Column counts 3/4/4 at 1024/1440/1920 come from auto-fill math, not measured.
- Hero "Start here" links to the start leaf when live, else `/#foundation`.

Missing tokens: none. `elevation.1` is the border string (`elevation1Var`); the 4 px top band uses `space.1`, progress bar height `space.2`.
