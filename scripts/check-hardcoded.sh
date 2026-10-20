#!/usr/bin/env bash
# No hardcoded design values outside Tokens.kt (docs/13 section 5, docs/02 section 7). Run from the repo root. Exit 0 = clean.
# Scans site/src/jsMain/kotlin and site/src/jsMain/resources, excluding Tokens.kt and public/fonts/fonts.css.
set -u
cd "$(dirname "$0")/.."
status=0
files=$(find site/src/jsMain/kotlin site/src/jsMain/resources -type f \( -name '*.kt' -o -name '*.css' -o -name '*.html' \) \
  ! -name 'Tokens.kt' ! -path '*/public/fonts/fonts.css')
# Reports the matched token only (grep -o), so the 0.px / 100.percent exemption applies to the token, not the whole line.
check() {
  local label="$1" pattern="$2"
  local hits
  hits=$(echo "$files" | xargs grep -noE "$pattern" 2>/dev/null | grep -vE ':(0|100)\.(px|percent)$' | grep -vE ':"0(px|ms|s)( [^"]*)?"$' || true)
  if [ -n "$hits" ]; then
    echo "FAIL $label"; echo "$hits" | head -20; status=1
  fi
}
check "hex colour"            '#[0-9A-Fa-f]{3,8}\b'
check "0x colour"             '\b0x([0-9A-Fa-f]{6}|[0-9A-Fa-f]{8})\b'
check "rgb/rgba/hsl literal"  '(rgba?|hsla?)\([0-9]'
check "number with unit"      '\b[0-9]+(\.[0-9]+)?\.(px|em|rem|vh|vw|ms)\b'
check "string length/time"    '"[^"]*[0-9](px|rem|em|ms|s)\b[^"]*"'
check "Colors.X"              '\bColors?\.[A-Z][A-Za-z]*'
check "quoted colour name"    '"(red|green|blue|black|white|gray|grey|orange|yellow|purple|pink|brown|cyan|magenta|navy|teal|silver|gold|maroon|lime|olive|aqua|fuchsia)"'
check "font name"             '"(Roboto|JetBrains)'
# A CSS property name alone is not a value: flag only a quoted literal value.
check "font-family literal"   'font-family"?,? *"[A-Za-z]'
check "border-radius literal" 'border-radius"?,? *"[0-9]'
[ $status -eq 0 ] && echo "check-hardcoded: clean"
exit $status
