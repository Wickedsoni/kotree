```yaml
branch: foundation
bough: roots
stems:
  - id: alpha
    title: Alpha Stem
    why: Why alpha matters.
    leaves:
      - id: first-leaf
        title: What is the first leaf?
        difficulty: easy
        requires: []
        fruit: false
      - id: second-leaf
        title: What is the second leaf?
        difficulty: medium
        requires: [foundation/alpha/first-leaf]
        fruit: false
  - id: beta
    title: Beta Stem
    why: Why beta matters.
    leaves:
      - id: third-leaf
        title: What is the third leaf?
        difficulty: hard
        requires: [foundation/alpha/second-leaf]
        fruit: true
```

## Stems

### alpha · Alpha Stem

| Leaf | Diff | Notes |
|---|---|---|
| `first-leaf` | easy | has a | pipe |
| `second-leaf` | medium | - |

### beta · Beta Stem

| Leaf | Diff | Notes |
|---|---|---|
| `third-leaf` | hard | - |

## Fruits

| Fruit | Notes |
|---|---|
| `foundation/beta/third-leaf` | capstone |

## Later

- Something postponed, with no table and no slug.

## Roadmap changes

| Date | Change |
|---|---|
| 2026-10-20 | created |
