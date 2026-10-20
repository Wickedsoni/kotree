#!/usr/bin/env bash
# Wave gate: validateContent + CI-style export + kotree.js gzip size vs budget (docs/07 sections 3 and 8).
# Run from anywhere: bash scripts/wave-check.sh. Needs scripts/setup-cloud.sh to have run once.
# The export exit code is NOT trusted (docs/07 section 3): we check files and the log.
set -u
BUDGET=600000
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT/site"
fail=0
step() { echo "== $1"; }

step "validateContent"
../gradlew :site:validateContent || { echo "FAIL validateContent"; exit 1; }

step "export (kobweb export --notty --layout static)"
rm -rf .kobweb/site
kobweb export --notty --layout static 2>&1 | tee export.log
if [ ! -f .kobweb/site/index.html ]; then echo "FAIL export: index.html missing"; fail=1; fi
if grep -q "Failed to export" export.log; then echo "FAIL export: log has 'Failed to export'"; fail=1; fi

step "kotree.js size"
JS=.kobweb/site/kotree.js
if [ -f "$JS" ]; then
  bytes=$(gzip -9 -c "$JS" | wc -c)
  echo "kotree.js gzip -9: $bytes B (budget $BUDGET B)"
  if [ "$bytes" -gt "$BUDGET" ]; then echo "FAIL over budget by $((bytes - BUDGET)) B"; fail=1; else echo "OK headroom $((BUDGET - bytes)) B"; fi
else
  echo "FAIL $JS missing"; fail=1
fi

[ "$fail" -eq 0 ] && echo "wave-check: PASS" || echo "wave-check: FAIL"
exit "$fail"
