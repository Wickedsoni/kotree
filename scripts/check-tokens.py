#!/usr/bin/env python3
"""Contrast check for the design tokens (docs/13 section 1 step 7). Stdlib only.

Reads every `const val name = 0xRRGGBB` (also object Status, ADR 006) of site/src/jsMain/kotlin/dev/kotree/site/Tokens.kt and applies the
WCAG 2.x ratio. Tints are mixed in sRGB into white with the same rounding as Tokens.Branch.tint (round half to even).
Run from the repo root: python scripts/check-tokens.py   (exit 0 = all pass)
"""
import re
import sys
from pathlib import Path

TOKENS = Path(__file__).resolve().parent.parent / "site/src/jsMain/kotlin/dev/kotree/site/Tokens.kt"
TEXT_MIN = 4.5
UI_MIN = 3.0
DOC_TOLERANCE = 0.1
TINT_RE = re.compile(r"const val tintPercent = (\d+)")
HOVER_RE = re.compile(r"const val hoverLayerPercent = (\d+)")
PRESSED_RE = re.compile(r"const val pressedLayerPercent = (\d+)")
DARKEN_RE = re.compile(r"const val tonalStateInkDarkenPercent = (\d+)")
# docs/02 section 2.2, ratio on white.
DOC_RATIO = {
    "foundation": 7.6, "kotlin": 5.7, "androidCore": 12.1, "ui": 5.9, "concurrency": 6.4, "architectureData": 5.5,
    "appNetworking": 7.4, "kmp": 5.8, "dsa": 5.8, "daa": 6.3, "os": 6.5, "networking": 5.5, "testing": 5.4,
    "profiling": 5.9, "nativeNdk": 6.7, "security": 9.7, "tools": 5.4,
}


def objects(src):
    """Map object name -> {const name: int} for the hex constants."""
    out, current = {}, None
    for line in src.splitlines():
        m = re.match(r"\s*object (\w+)", line)
        if m:
            current = m.group(1)
            out.setdefault(current, {})
        m = re.search(r"const val (\w+) = 0x([0-9A-Fa-f]{6})\b", line)
        if m and current:
            out[current][m.group(1)] = int(m.group(2), 16)
    return out


def channels(c):
    return (c >> 16) & 255, (c >> 8) & 255, c & 255


def lum(c):
    def f(v):
        v /= 255
        return v / 12.92 if v <= 0.03928 else ((v + 0.055) / 1.055) ** 2.4
    r, g, b = channels(c)
    return 0.2126 * f(r) + 0.7152 * f(g) + 0.0722 * f(b)


def ratio(a, b):
    la, lb = sorted((lum(a), lum(b)), reverse=True)
    return (la + 0.05) / (lb + 0.05)


def tint(ink, pct):
    def mix(v):
        return int(round((v * pct + 255 * (100 - pct)) / 100.0))
    r, g, b = channels(ink)
    return (mix(r) << 16) | (mix(g) << 8) | mix(b)


def blend(base, top, pct):
    """Top colour laid over base at pct percent (what Button.kt's gradient layer does). Unrounded channels, as the browser."""
    a, b = channels(base), channels(top)
    return tuple(a[i] * (100 - pct) / 100.0 + b[i] * pct / 100.0 for i in range(3))


def lum_rgb(rgb):
    def f(v):
        v /= 255
        return v / 12.92 if v <= 0.03928 else ((v + 0.055) / 1.055) ** 2.4
    return 0.2126 * f(rgb[0]) + 0.7152 * f(rgb[1]) + 0.0722 * f(rgb[2])


def ratio_rgb(a, b):
    la, lb = sorted((lum_rgb(a), lum_rgb(b)), reverse=True)
    return (la + 0.05) / (lb + 0.05)


def main():
    src = TOKENS.read_text(encoding="utf-8")
    obj = objects(src)
    pct = int(TINT_RE.search(src).group(1))
    white = obj["Color"]["surface"]
    fails, rows = [], []

    def check(name, fg, bg, minimum, extra=""):
        r = ratio(fg, bg)
        ok = r >= minimum
        rows.append(f"{'ok  ' if ok else 'FAIL'} {name:46} {r:5.2f} (min {minimum}) {extra}")
        if not ok:
            fails.append(name)
        return r

    on_branch = obj["Chrome"]["onBranch"]
    for name, ink in obj["Branch"].items():
        t = tint(ink, pct)
        r = check(f"branch.{name} on white", ink, white, TEXT_MIN)
        if name in DOC_RATIO and abs(r - DOC_RATIO[name]) > DOC_TOLERANCE:
            rows.append(f"FAIL branch.{name} differs from docs/02 table {DOC_RATIO[name]}: {r:.2f}")
            fails.append(f"branch.{name} doc value")
        check(f"branch.{name} on its {pct}% tint #{t:06X}", ink, t, TEXT_MIN)
        check(f"on-branch on branch.{name} ink", on_branch, ink, TEXT_MIN)
    hover, pressed, darken = (int(r.search(src).group(1)) for r in (HOVER_RE, PRESSED_RE, DARKEN_RE))
    for name, ink in obj["Branch"].items():
        t = tint(ink, pct)
        for state, layer in (("hover", hover), ("pressed", pressed)):
            # Filled: white label, white layer over the ink. Text: ink label, ink layer over white.
            fill = blend(ink, on_branch, layer)
            checks = [("filled", ratio_rgb(channels(on_branch), fill)),
                      ("text", ratio_rgb(channels(ink), blend(white, ink, layer)))]
            # Tonal: label darkened by `darken` percent towards black, layer in that same colour over the tint.
            label = tuple(v * (100 - darken) / 100.0 for v in channels(ink))
            tone = tuple(t_ * (100 - layer) / 100.0 + l * layer / 100.0 for t_, l in zip(channels(t), label))
            checks.append(("tonal", ratio_rgb(label, tone)))
            for kind, r in checks:
                ok = r >= TEXT_MIN
                rows.append(f"{'ok  ' if ok else 'FAIL'} {'branch.' + name + ' ' + kind + ' ' + state:46} {r:5.2f} (min {TEXT_MIN})")
                if not ok:
                    fails.append(f"branch.{name} {kind} {state}")
    check("on-surface on white", obj["Color"]["onSurface"], white, TEXT_MIN)
    check("on-surface-muted on white", obj["Color"]["onSurfaceMuted"], white, TEXT_MIN)
    check("leaf.shadow.label on white", obj["Color"]["leafShadowLabel"], white, TEXT_MIN)
    for key in ("feedbackCorrect", "feedbackWrong", "feedbackHint"):
        ink = obj["Chrome"][key]
        check(f"{key} on white", ink, white, TEXT_MIN)
        check(f"{key} on its tint", ink, tint(ink, pct), TEXT_MIN)
    check("focus ring default on white", obj["Branch"]["kotlin"], white, UI_MIN)
    # ADR 006: status inks on white and on their own container; tree stem and shadow stroke are non-text.
    for key in ("live", "upnext", "shadow"):
        ink = obj["Status"][key]
        check(f"status.{key} on white", ink, white, TEXT_MIN)
        check(f"status.{key} on its container #{tint(ink, pct):06X}", ink, tint(ink, pct), TEXT_MIN)
    for key in ("treeStem", "leafShadowStroke"):
        check(f"status.{key} on white", obj["Status"][key], white, UI_MIN)
    for key in ("easy", "medium", "hard", "upnext", "shadow"):
        # Swatches repeat the chip text, so they need only the non-text 3:1 against white when they carry meaning alone.
        rows.append(f"info {'leaf.' + key + ' swatch on white':46} {ratio(obj['Leaf'][key], white):5.2f} (not gated: text carries the meaning)")
    print("\n".join(rows))
    print(f"\n{len(fails)} failure(s)" + (": " + ", ".join(fails) if fails else ""))
    return 1 if fails else 0


if __name__ == "__main__":
    sys.exit(main())
