#!/usr/bin/env python3
"""Radial build planner (decision L34).

Prints the next layer: for every branch, the first leaf in roadmap order that is
not live yet and whose `requires` are all live. Usage: scripts/next-layer.py [N]
(N leaves per branch, default 1). Read-only; it never edits files.
"""
import glob, os, re, sys

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "content")
PER_BRANCH = int(sys.argv[1]) if len(sys.argv) > 1 else 1


def live_ids():
    out = set()
    for f in glob.glob(os.path.join(ROOT, "*", "*", "*.md")):
        if os.path.basename(f).startswith("_"):
            continue
        head = open(f, encoding="utf-8").read(800)
        if re.search(r"^status:\s*live\s*$", head, re.M):
            rel = os.path.relpath(f, ROOT)[:-3]
            out.add(rel)
    return out


def leaves(branch):
    path = os.path.join(ROOT, branch, "_roadmap.md")
    if not os.path.exists(path):
        return []
    blocks, cur = [], None
    for line in open(path, encoding="utf-8"):
        m = re.match(r"^(\s*)- id:\s*(\S+)", line)
        if m:
            cur = {"indent": len(m.group(1)), "id": m.group(2), "lines": []}
            blocks.append(cur)
        elif cur is not None:
            cur["lines"].append(line)
    out, stem = [], None
    for b in blocks:
        text = "".join(b["lines"])
        if "difficulty:" in text:
            req = re.search(r"requires:\s*\[(.*?)\]", text)
            reqs = [r.strip() for r in req.group(1).split(",") if r.strip()] if req else []
            title = re.search(r"title:\s*(.+)", text)
            out.append({"path": f"{branch}/{stem}/{b['id']}", "title": title.group(1).strip() if title else "", "requires": reqs})
        else:
            stem = b["id"]
    return out


live = live_ids()
branches = sorted(d for d in os.listdir(ROOT) if not d.startswith("_") and os.path.isdir(os.path.join(ROOT, d)))
layer, blocked = [], []
for br in branches:
    picked = 0
    todo = [l for l in leaves(br) if l["path"] not in live]
    for l in todo:
        if picked >= PER_BRANCH:
            break
        missing = [r for r in l["requires"] if r not in live]
        if missing:
            continue
        layer.append((br, l, os.path.exists(os.path.join(ROOT, l["path"] + ".md"))))
        picked += 1
    if picked == 0:
        blocked.append((br, len(todo)))

print(f"# Next layer: {len(layer)} leaves ({PER_BRANCH} per branch)")
for br, l, has_file in layer:
    tag = "finish draft" if has_file else "write"
    print(f"- [{tag}] {l['path']} :: {l['title']}")
if blocked:
    print("# Branches with nothing eligible (all done, or prerequisites not live):")
    for br, n in blocked:
        print(f"- {br}: {n} leaves left")
