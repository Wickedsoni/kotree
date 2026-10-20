#!/usr/bin/env bash
# Idempotent toolchain setup for a fresh Linux cloud session (docs/07 section 2.1, section 3).
# Installs JDK >= 17 (if missing) and the pinned Kobweb CLI, then chmod +x gradlew.
# Usage: bash scripts/setup-cloud.sh
set -eu
KOBWEB_VERSION="0.9.23"
KOBWEB_HOME="${KOBWEB_HOME:-$HOME/.local/kobweb}"
cd "$(dirname "$0")/.."

SUDO=""; [ "$(id -u)" -ne 0 ] && SUDO="sudo"

java_major() { java -version 2>&1 | sed -n 's/.*version "\([0-9]*\).*/\1/p' | head -1; }
if ! command -v java >/dev/null 2>&1 || [ "$(java_major)" -lt 17 ]; then
  echo "Installing JDK 17"
  $SUDO apt-get update -qq && $SUDO apt-get install -y -qq openjdk-17-jdk-headless unzip curl
fi
echo "java: $(java -version 2>&1 | grep -m1 version)"

if ! "$KOBWEB_HOME/kobweb-$KOBWEB_VERSION/bin/kobweb" version 2>/dev/null | grep -q "$KOBWEB_VERSION"; then
  echo "Installing Kobweb CLI $KOBWEB_VERSION"
  tmp="$(mktemp -d)"
  curl -fsSL -o "$tmp/kobweb.zip" \
    "https://github.com/varabyte/kobweb-cli/releases/download/v$KOBWEB_VERSION/kobweb-$KOBWEB_VERSION.zip"
  mkdir -p "$KOBWEB_HOME"
  unzip -qo "$tmp/kobweb.zip" -d "$KOBWEB_HOME"
  rm -rf "$tmp"
fi
BIN="$KOBWEB_HOME/kobweb-$KOBWEB_VERSION/bin"
chmod +x "$BIN/kobweb"
# Make it available to later shells and to this one's callers.
if [ -d /usr/local/bin ] && [ -w /usr/local/bin -o "$(id -u)" -eq 0 ]; then
  ln -sf "$BIN/kobweb" /usr/local/bin/kobweb
else
  grep -qs "$BIN" "$HOME/.bashrc" || echo "export PATH=\"$BIN:\$PATH\"" >> "$HOME/.bashrc"
  export PATH="$BIN:$PATH"
fi
chmod +x gradlew
echo "kobweb: $("$BIN/kobweb" version 2>&1 | head -1)"
echo "setup-cloud: OK"
