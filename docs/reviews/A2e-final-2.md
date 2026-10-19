# A2e final consistency read, pass 2

Reviewer: independent. Date: 2026-10-20. Scope: new contradictions since `docs/reviews/A2e-final.md`. `docs/00` wins. No spec file was edited. VERIFY items were not settled.

VERDICT: FIX (no blocking contradiction; 1 medium, 4 low)

## Checked and consistent

- Lesson export: `docs/07` §6.7 (static `@Page` routes, no `addExtraRoute`, backup only under ADR 005) agrees with `docs/03` §7.2 (lines 393-394, 483), `docs/07` V7 (line 22) and §8 ADR 003 (line 481), `docs/10` Phases 3 and 4, and `docs/11` P0-3 and P0-7. Nothing in `.claude/`, `CLAUDE.md`, `docs/00` or the template mentions `addExtraRoute`.
- `docs/02` §3.1 rule, `docs/05` §3.3 (line 130) and `docs/11` P0-5 and P0-6 agree: `.` to `-`, `--` prefix, `space2Var`, `--space-2` = 8px, `#3B5BDB`.
- SimFrame 720 px: `docs/02` §4.5 (lines 185-188, 220, 240), `docs/05` lines 83, 300, 530, Q5. 624 + 2 x 48 = 720 and 720 + 64 = 784 < 1024 both hold.
- `content/kotlin/_roadmap.md` `what-is-null` requires (YAML line 108 and Notes line 458) equal `docs/04` line 448. All three target IDs exist.
- `docs/11` VERIFY coverage: every open VERIFY in docs 00-10 maps to a row (checked by line: 01:368-369, 02:6/109/128/129/177/184, 03:397, 05:64/338/339/598/633/663, 06:57/58/61/70/71/73/83/207, 07:133/204/220/221/224/262/291/387/403/444/453/454, 04:299/756, 09:176). Row count 30 (12/1/4/5/5/3) is correct. Left-out marks (§4 of `docs/11`) are authoring rules or roadmap-level, as stated.
- `docs/INDEX.md` rows exist for `docs/11`, `reviews/`, `sims/`, `adr/`, template. `physics-designer` effort: `docs/09:39` and the agent file both say `high` (earlier item 7 is closed).

## FIX list

1. MEDIUM. `docs/07-technical-architecture.md:407` (§6.7 "Check 1 (Phase 0)"): requires `./gradlew :site:verifyLessonExport` to pass in Phase 0. `verifyLessonExport` reads `site/build/tree.json`, which `generateTreeData` writes, and `docs/10` Phase 3 lists both tasks as Phase 3 deliverables. Phase 0 cannot run it; `docs/11` P0-3 correctly omits it. Fix: drop that sentence from Check 1 (the Phase 3 acceptance already covers it). Mechanical.
2. LOW. `docs/NEXT-STEPS.md:45` ("Known risks"): "each live lesson needs `addExtraRoute` (`docs/03` §7.2)". Stale; contradicts `docs/07` §6.7 and `docs/03` §7.2. Fix: "Kobweb skips dynamic routes on export; lessons are static pages, so no `addExtraRoute` (`docs/07` §6.7)". Mechanical.
3. LOW. `docs/07-technical-architecture.md:6` header: "Version: v2.3 · 18 Oct 2026" while line 10 describes v2.4 (20 Oct 2026) and §6.7. Fix: "v2.4 · 20 Oct 2026". Mechanical.
4. LOW. `docs/07` §6.7 line 390 "if check 1 below fails" (contingent path) versus `docs/11` P0-3 fallbacks: 07 sends a failure to the dynamic-page path (ADR 005); 11 sends it to safe file names plus `routeOverride`, or to updating `07` §5.5. Fix: make one of them the rule and cite it in the other. Needs the owner (`architect`).
5. LOW. `docs/07` §6.7 `verifyLessonExport` step 2 (line 379): "exactly three folders deep" while the path is `<branch>/<stem>/<leaf>.html` (two folders plus the file). Also `docs/05` Q4 (line 663) and the `docs/02` line 6 header say only the digit-name case stays open, but `docs/02` line 128 and V4 (line 238), and `docs/11` P0-6, also keep "`StyleVariable.name` is public" open. Fix: say "three path segments", and add the P0-6 case to both summaries. Mechanical.

## Not changed

All VERIFY items stay open. `docs/adr/` is listed in INDEX but not created yet (Phase 0 creates ADR 001).
