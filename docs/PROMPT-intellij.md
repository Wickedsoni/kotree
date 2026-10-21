# Prompt for the IntelliJ terminal session

Repo: Wickedsoni/kotree. Read CLAUDE.md, then docs/NEXT-STEPS.md (top handoff), docs/00-decisions.md (L31-L36), docs/adr/008-tree-v3-request.md.

State (21 Oct 2026):
- Branch feat/design-v2 has layer 1 live (112 of 482). Main has 96 (wave 10). Branch release/layer1 = layer 1 content + site code frozen at WP1 (commit 9bad25c); build run 39534387894 was cancelled, not failed.
- feat/design-v2 build 39534116051 FAILED only because WP2 (spacing) was committed half-done: GeometryTest.limbHashIsPinned / fullHashIsPinned / layoutReportDoesNotRegress fail. Content validators and compile passed.
- Tree v3: WP0 (test harness, CI proven) and WP1 (tokens) done. WP2 (spacing in tree/gen/Layout.kt, Tokens.kt, LayoutCheck.kt) is half done: finish it, run ./gradlew :site:jsNodeTest locally, copy the new limbHash/fullHash into GeometryTest.kt, layout report must pass strictly. Then WP3 backdrop, WP4 canopy dots with LOD cross-fade, WP5 done store + autumn colour, WP6 zoom-linked sway. Local machine can compile (the cloud sandbox could not).

Do first:
1. git fetch; git checkout feat/design-v2; finish WP2 and get ./gradlew :site:jsNodeTest, :site:validateContent, python3 scripts/check-tokens.py, bash scripts/check-hardcoded.sh green.
2. Continue WP3-WP6 locally with kobweb run to look at the tree.
3. One build only when a batch is done: gh workflow run build.yml --ref feat/design-v2, then push that sha to main (merge origin/main first if diverged). Do not run extra builds.
4. Content: "next" = python3 scripts/next-layer.py, same pipeline (write, 2 reviewers, fixer, re-check, housekeeper). Quiet mode: no status text, ask only about real decisions.
