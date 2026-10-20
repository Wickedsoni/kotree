#!/usr/bin/env bash
# Compile and run one Kotlin file with the pinned compiler: bash scripts/kt-run.sh snippet.kt
set -eu
K=$(bash "$(dirname "$0")/kt-setup.sh")
T=$(mktemp -d)
"$K" "$1" -include-runtime -d "$T/out.jar" 2>&1 | grep -v "^warning: .*Picked up" || true
java -jar "$T/out.jar"
