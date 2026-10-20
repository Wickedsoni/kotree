#!/usr/bin/env bash
# Installs the pinned Kotlin compiler (LESSON_KOTLIN_VERSION) to $HOME/.kotlinc and prints its bin path.
# Usage: bash scripts/kt-setup.sh   then   $HOME/.kotlinc/kotlinc/bin/kotlinc file.kt -include-runtime -d out.jar && java -jar out.jar
set -eu
V=2.4.21
D="$HOME/.kotlinc"
if [ ! -x "$D/kotlinc/bin/kotlinc" ]; then
  mkdir -p "$D"
  curl -fsSL -o "$D/k.zip" "https://github.com/JetBrains/kotlin/releases/download/v$V/kotlin-compiler-$V.zip"
  unzip -q -o "$D/k.zip" -d "$D"
  rm -f "$D/k.zip"
fi
echo "$D/kotlinc/bin/kotlinc"
