# ADR 001 — Kobweb as the framework

Status: accepted · Date: 20 Oct 2026 · Decided by: the author (L18)

## Context
Kotree is a public, no-login learning site shaped like one pixel-art tree. The author works in Kotlin and wants one language for the site, the tree renderer and later the sims. The site is static, desktop-first, served at https://kotree.garvitmaheshwari.in with no base path (L25). Content is Markdown in /content (L17).

## Decision
- Framework: Kobweb (Kotlin/JS, Compose HTML, Silk), pinned `kobweb = "0.25.1"` (latest stable on 18 Oct 2026; re-checked on <date of gate G2>). It compiles with the Kotlin version that release names (2.4.10 at that pin).
- Site project in /site, package `dev.kotree.site`, JDK Temurin 17 locally and in CI.
- `LESSON_KOTLIN_VERSION = "2.4.21"` for lesson code and the Playground `data-version`, re-checked on <date of gate G3>. It may differ from the Kotlin version that compiles the site.
- Static export: `kobweb export --layout static`.
- Build order (L28): the scaffold and the pixel tree are one milestone (docs/12) with all values in one Tokens.kt; design tokens are applied in Phase 1, content pipeline in Phase 3.

## Rejected options
None are recorded in docs/00 to docs/11; the stack is an author lock (L18). Add rows here only with their reason if the author names alternatives.

## Consequences
- One main JS bundle for every page; lesson text will land in it (docs/07 §8). The Phase 0b bundle spike (P0-7) decides whether ADR 005 is needed.
- Kobweb version bumps are deliberate and go through a new ADR or an edit here with the date.
- Verified-command table lives in docs/07 §3; no other flags are used.
