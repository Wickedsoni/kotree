#!/usr/bin/env bash
# Phase 3 acceptance checks (docs/14 section 5). Run from the repo root: bash scripts/verify-phase3.sh
# Prints PASS / FAIL / SKIP per check. Exit 0 = no FAIL. SKIP = needs a running browser, Gradle run or manual step.
# The Kobweb export exit code is NOT trusted: artefacts are checked instead.
set -u
cd "$(dirname "$0")/.."

fails=0
pass() { echo "PASS  $1"; }
fail() { echo "FAIL  $1 -- $2"; fails=$((fails + 1)); }
skip() { echo "SKIP  $1 -- $2"; }

PY=""
for c in python3 python; do
  if "$c" -c "import sys" >/dev/null 2>&1; then PY="$c"; break; fi
done

TREE="site/build/tree.json"
GEN_KT="site/build/generated/kotree/kotlin/dev/kotree/site/tree/model/GeneratedTreeData.kt"
SITE_TREE="site/.kobweb/site/tree.json"
INDEX="site/.kobweb/site/index.html"
KOTREE_JS="site/.kobweb/site/kotree.js"

# 1. tree.json exists and carries the day-one counts (docs/14 section 5, "Counts match").
if [ ! -f "$TREE" ]; then
  fail "tree.json exists" "$TREE missing; run the build with generateTreeData and copyTreeJson first"
elif [ -z "$PY" ]; then
  skip "tree.json counts" "no python found to parse JSON"
else
  res=$("$PY" - "$TREE" <<'EOF'
import json, sys
t = json.load(open(sys.argv[1], encoding="utf-8"))
errs = []
if t.get("counts", {}).get("live") != 0: errs.append("live != 0")
if t.get("counts", {}).get("total") != 482: errs.append("total != 482")
want = [34,65,37,49,32,33,17,22,46,22,23,21,22,16,11,15,17]
brs = [b for bo in t.get("boughs", []) for b in bo.get("branches", [])]
got = sorted(b["counts"]["total"] for b in brs)
if len(brs) != 17: errs.append("branches=%d" % len(brs))
if got != sorted(want): errs.append("branch totals differ")
stems = sum(len(b["stems"]) for b in brs)
leaves = [l for b in brs for s in b["stems"] for l in s["leaves"]]
fruits = sum(1 for l in leaves if l.get("fruit"))
shadow = sum(1 for l in leaves if l.get("status") == "shadow")
upn = sum(1 for b in brs if b.get("upNext"))
if stems != 180: errs.append("stems=%d" % stems)
if fruits != 20: errs.append("fruits=%d" % fruits)
if upn != 17: errs.append("upNext=%d" % upn)
if shadow != 465: errs.append("shadow=%d" % shadow)
print("; ".join(errs) if errs else "ok")
EOF
)
  if [ "$res" = "ok" ]; then pass "tree.json counts (482 total, 0 live, 17 branches, 180 stems, 20 fruits, 17 upNext, 465 shadow)"
  else fail "tree.json counts" "$res"; fi
fi

# 2. Generated Kotlin output exists (the two outputs come from one model).
if [ -f "$GEN_KT" ]; then pass "GeneratedTreeData.kt exists"
else fail "GeneratedTreeData.kt exists" "$GEN_KT missing"; fi

# 3. Deterministic output: needs two runs of generateTreeData; compare sha256 of both outputs manually.
skip "deterministic outputs" "run generateTreeData twice, compare sha256sum of $TREE and $GEN_KT"

# 4. No leaf published: no leaf .md file in the diff against main, and tree.json has live 0.
base=""
if git rev-parse --verify -q main >/dev/null; then base="main...HEAD"; fi
if [ -n "$base" ]; then changed=$(git diff --name-only "$base" 2>/dev/null)
else changed=$(git diff --name-only HEAD 2>/dev/null); fi
leafhits=$(printf '%s\n' "$changed" | grep -E '^content/[^_/]+/[^_/]+/[^_/]+\.md$' || true)
if [ -z "$leafhits" ]; then pass "no leaf file changed in ${base:-HEAD}"
else fail "no leaf file changed" "$leafhits"; fi

# 5. addExtraRoute absent from site/build.gradle.kts.
if [ ! -f site/build.gradle.kts ]; then skip "addExtraRoute absent" "site/build.gradle.kts not found"
elif grep -q "addExtraRoute" site/build.gradle.kts; then fail "addExtraRoute absent" "found in site/build.gradle.kts"
else pass "addExtraRoute absent"; fi

# 6. Export artefacts (not the exit code). Index must exist; the log check needs KOBWEB_LOG=<path to export log>.
if [ -f "$INDEX" ]; then pass "export produced $INDEX"
else fail "export produced $INDEX" "missing; the export failed or was not run (see docs/07 section 3)"; fi

if [ -n "${KOBWEB_LOG:-}" ] && [ -f "$KOBWEB_LOG" ]; then
  if grep -q "Failed to export" "$KOBWEB_LOG"; then fail "export log clean" "'Failed to export' in $KOBWEB_LOG"
  else pass "export log has no 'Failed to export'"; fi
else
  skip "export log clean" "set KOBWEB_LOG to the export log path to check it"
fi

if [ -f "$SITE_TREE" ]; then pass "copyTreeJson copied tree.json into .kobweb"
else skip "copyTreeJson copied tree.json" "$SITE_TREE absent; run after export"; fi

# 7. Bundle size gate T <= 60000 (gzip -9 of kotree.js, after the 277,917 baseline is recorded in docs/07 section 8.1).
if [ -f "$KOTREE_JS" ]; then
  gz=$(gzip -9 -c "$KOTREE_JS" | wc -c | tr -d ' ')
  echo "INFO  gzip -9 kotree.js = $gz bytes (T = gz - 277917; cap 60000)"
  if [ $((gz - 277917)) -le 60000 ]; then pass "bundle T within cap"
  else fail "bundle T within cap" "T=$((gz - 277917)) > 60000"; fi
else
  skip "bundle T within cap" "$KOTREE_JS absent"
fi

# 8. Design tokens and hard-coded values.
if [ -f scripts/check-hardcoded.sh ]; then
  if bash scripts/check-hardcoded.sh >/dev/null 2>&1; then pass "check-hardcoded.sh exits 0"
  else fail "check-hardcoded.sh exits 0" "run bash scripts/check-hardcoded.sh for details"; fi
fi
if [ -f scripts/check-tokens.py ] && [ -n "$PY" ]; then
  if "$PY" scripts/check-tokens.py >/dev/null 2>&1; then pass "check-tokens.py exits 0"
  else fail "check-tokens.py exits 0" "run python scripts/check-tokens.py for details"; fi
fi

# 9. Browser and Gradle checks: need a running site or a manual step.
skip "limbHash cecb906380417dd8" "browser console check"
skip "layoutReport (1280x800, 24 CSS px, gaps 8 px)" "browser console check"
skip "performance (p95 <= 16.7 ms, DOM <= 200, generate <= 50 ms)" "browser trace"
skip "screenshots and accessibility (docs/reviews/S3-acceptance.md)" "manual headless Chrome review"
skip "validators fail on seeded bad input" "run: ../gradlew -p ../buildSrc test, then ../gradlew :site:validateContent in /site"

echo
if [ "$fails" -eq 0 ]; then echo "verify-phase3: no FAIL"; exit 0; fi
echo "verify-phase3: $fails FAIL"
exit 1
