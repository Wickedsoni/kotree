package dev.kotree.build

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ValidateTest {
    private fun assertOnly(rule: String, vs: List<Violation>, contains: String? = null) {
        assertTrue(rule in vs.rules(), "expected $rule, got ${vs.map { it.format() }}")
        if (contains != null) {
            assertTrue(vs.of(rule).any { contains in it.message }, "expected '$contains' in ${vs.of(rule).map { it.message }}")
        }
    }

    @Test
    fun goodFixtureHasNoViolations() {
        val c = loadContent(Fx().content)
        assertEquals(1, c.roadmaps.size)
        assertEquals(2, c.leafFiles.size)
        assertEquals(emptyList(), validate(c, null).map { it.format() })
    }

    @Test
    fun messageFormat() {
        val v = Violation("V3", "content/a/_roadmap.md", 12, "a/b/c", "x")
        assertEquals("V3 content/a/_roadmap.md:12 [a/b/c] x", v.format())
    }

    // ---- V1 ----
    @Test
    fun v1UnknownBranch() {
        val vs = Fx().edit(Fx.RM, "branch: foundation", "branch: nonsense").violations()
        assertOnly("V1", vs, "not in the registry")
    }

    @Test
    fun v1WrongBough() {
        val vs = Fx().edit(Fx.RM, "bough: roots", "bough: craft").violations()
        assertOnly("V1", vs, "belongs to bough `roots`")
    }

    @Test
    fun v1UnknownFolder() {
        val vs = Fx().write("mystery/_roadmap.md", "x").violations()
        assertOnly("V1", vs, "not a branch in the registry")
    }

    // ---- V2 ----
    @Test
    fun v2BadSlug() {
        val vs = Fx().edit(Fx.RM, "- id: first-leaf", "- id: First_Leaf").violations()
        assertOnly("V2", vs, "kebab-case")
    }

    @Test
    fun v2DuplicateStem() {
        val vs = Fx().edit(Fx.RM, "- id: beta", "- id: alpha").violations()
        assertOnly("V2", vs, "duplicate stem")
    }

    @Test
    fun v2DuplicateLeaf() {
        val vs = Fx().edit(Fx.RM, "- id: third-leaf", "- id: first-leaf").violations()
        assertOnly("V2", vs, "duplicate leaf")
    }

    // ---- V3 ----
    @Test
    fun v3UnknownLeaf() {
        val vs = Fx().edit(Fx.RM, "requires: [foundation/alpha/second-leaf]", "requires: [foundation/alpha/ghost]").violations()
        assertOnly("V3", vs, "not a leaf of this roadmap")
    }

    @Test
    fun v3OtherBranch() {
        val vs = Fx().edit(Fx.RM, "requires: [foundation/alpha/second-leaf]", "requires: [kotlin/alpha/second-leaf]").violations()
        assertOnly("V3", vs, "another branch")
    }

    @Test
    fun v3ListedLater() {
        val vs = Fx().edit(Fx.RM, "requires: [foundation/alpha/first-leaf]", "requires: [foundation/beta/third-leaf]").violations()
        assertOnly("V3", vs, "listed later")
    }

    // ---- V4 ----
    @Test
    fun v4FileWithoutRow() {
        val fx = Fx()
        fx.write("foundation/alpha/orphan.md", fx.file(Fx.FIRST).readText().replace("foundation/alpha/first-leaf", "foundation/alpha/orphan"))
        assertOnly("V4", fx.violations(), "no row")
    }

    @Test
    fun v4IdNotEqualPath() {
        val vs = Fx().edit(Fx.FIRST, "id: foundation/alpha/first-leaf", "id: foundation/alpha/other").violations()
        assertOnly("V4", vs, "does not equal the path")
    }

    // ---- V5 ----
    @Test
    fun v5Title() {
        assertOnly("V5", Fx().edit(Fx.FIRST, "title: What is the first leaf?", "title: Other").violations(), "title")
    }

    @Test
    fun v5Difficulty() {
        assertOnly("V5", Fx().edit(Fx.FIRST, "difficulty: easy", "difficulty: hard").violations(), "difficulty")
    }

    @Test
    fun v5Requires() {
        assertOnly("V5", Fx().edit(Fx.SECOND, "requires: [foundation/alpha/first-leaf]", "requires: []").violations(), "requires")
    }

    @Test
    fun v5Fruit() {
        assertOnly("V5", Fx().edit(Fx.FIRST, "fruit: false", "fruit: true").violations(), "fruit")
    }

    // ---- V6 sub-cases ----
    @Test
    fun v6FirstBlockNotYaml() {
        val vs = Fx().edit(Fx.RM, "```yaml", "```text").violations()
        assertOnly("V6", vs, "```yaml")
    }

    @Test
    fun v6TextBeforeYaml() {
        val vs = Fx().edit(Fx.RM, "```yaml\nbranch", "intro\n\n```yaml\nbranch").violations()
        assertOnly("V6", vs)
    }

    @Test
    fun v6StemOrderDiffers() {
        val vs = Fx().edit(Fx.RM, "### alpha · Alpha Stem", "### gamma · Alpha Stem").violations()
        assertOnly("V6", vs, "stem order or ids differ")
    }

    @Test
    fun v6HeadingTitleDiffers() {
        assertOnly("V6", Fx().edit(Fx.RM, "### alpha · Alpha Stem", "### alpha · Alpha Different").violations(), "heading title")
    }

    @Test
    fun v6LeafMissingInTable() {
        assertOnly("V6", Fx().edit(Fx.RM, "| `second-leaf` | medium | - |\n", "").violations(), "missing leaf rows")
    }

    @Test
    fun v6LeafExtraInTable() {
        val vs = Fx().edit(Fx.RM, "| `second-leaf` | medium | - |\n", "| `second-leaf` | medium | - |\n| `extra-leaf` | easy | - |\n").violations()
        assertOnly("V6", vs, "extra leaf rows")
    }

    @Test
    fun v6LeafReordered() {
        val vs = Fx().edit(Fx.RM, "| `first-leaf` | easy | has a | pipe |\n| `second-leaf` | medium | - |",
            "| `second-leaf` | medium | - |\n| `first-leaf` | easy | has a | pipe |").violations()
        assertOnly("V6", vs, "different order")
    }

    @Test
    fun v6DiffCellDiffers() {
        assertOnly("V6", Fx().edit(Fx.RM, "| `second-leaf` | medium |", "| `second-leaf` | hard |").violations(), "Diff")
    }

    @Test
    fun v6FruitsDiffer() {
        assertOnly("V6", Fx().edit(Fx.RM, "| `foundation/beta/third-leaf` | capstone |", "| `foundation/beta/ghost` | capstone |").violations(), "fruit set")
    }

    @Test
    fun v6LaterHasTableOrSlug() {
        assertOnly("V6", Fx().edit(Fx.RM, "- Something postponed", "| `x` | easy |\n- Something postponed").violations(), "## Later")
        assertOnly("V6", Fx().edit(Fx.RM, "- Something postponed", "- Something `postponed-slug`").violations(), "backticked slug")
    }

    @Test
    fun v6RoadmapChangesRowStartsWithBacktick() {
        assertOnly("V6", Fx().edit(Fx.RM, "| 2026-10-20 | created |", "| `first-leaf` | created |").violations(), "date")
    }

    @Test
    fun v6HandlesCrlf() {
        val fx = Fx()
        val f = fx.file(Fx.RM)
        f.writeText(f.readText().replace("\n", "\r\n"))
        assertEquals(emptyList(), fx.violations().map { it.format() })
    }

    // ---- V7 ----
    @Test
    fun v7ReservedAndUnderscoreFolders() {
        val vs = Fx().write("assets/x.txt", "x").write("_secret/x.txt", "x").violations()
        assertEquals(2, vs.of("V7").size, vs.map { it.format() }.toString())
    }

    // ---- V8 ----
    @Test
    fun v8NoLongerLiveRemovedAndRenamed() {
        val fx = Fx()
        val base = setOf("foundation/alpha/second-leaf", "foundation/alpha/first-leaf", "foundation/alpha/renamed-away")
        val vs = fx.violations(base).of("V8")
        assertEquals(2, vs.size, vs.map { it.format() }.toString())
        assertTrue(vs.any { "no longer live" in it.message && it.id == "foundation/alpha/second-leaf" })
        assertTrue(vs.any { "removed or renamed" in it.message && it.id == "foundation/alpha/renamed-away" })
    }

    @Test
    fun v8SkippedWhenBaselineIsNull() {
        assertTrue("V8" !in Fx().violations(null).rules())
    }

    // ---- V9 ----
    @Test
    fun v9TwoUpnextInOneBranch() {
        val fx = Fx()
        fx.write("foundation/beta/third-leaf.md", fx.file(Fx.SECOND).readText().replace("alpha/second-leaf", "beta/third-leaf"))
        assertOnly("V9", fx.violations(), "more than one")
    }

    @Test
    fun v9UpnextOnLiveLeaf() {
        assertOnly("V9", Fx().edit(Fx.FIRST, "upnext: false", "upnext: true").violations(), "live leaf")
    }

    // ---- V10 ----
    @Test
    fun v10RoadmapUnknownKey() {
        assertOnly("V10", Fx().edit(Fx.RM, "    why: Why alpha matters.", "    why: Why alpha matters.\n    mood: happy").violations(), "unknown key")
    }

    @Test
    fun v10RoadmapMissingKey() {
        assertOnly("V10", Fx().edit(Fx.RM, "    why: Why beta matters.\n", "").violations(), "missing the key `why`")
    }

    @Test
    fun v10MissingFrontmatterKey() {
        assertOnly("V10", Fx().edit(Fx.FIRST, "playground: true\n", "").violations(), "missing the key `playground`")
    }

    @Test
    fun v10BadStatus() {
        assertOnly("V10", Fx().edit(Fx.FIRST, "status: live", "status: done").violations(), "status")
    }

    @Test
    fun v10LiveWithoutSummary() {
        assertOnly("V10", Fx().edit(Fx.FIRST, "summary: One sentence for the popup.", "summary: \"\"").violations(), "summary")
    }

    @Test
    fun v10BadVideo() {
        assertOnly("V10", Fx().edit(Fx.FIRST, "video: \"\"", "video: \"http://example.com/v\"").violations(), "video")
    }

    @Test
    fun v10GoodVideoForms() {
        val a = Fx().edit(Fx.FIRST, "video: \"\"", "video: \"https://www.youtube.com/watch?v=abc123\"").violations()
        val b = Fx().edit(Fx.FIRST, "video: \"\"", "video: \"https://youtu.be/abc123\"").violations()
        assertEquals(emptyList(), a)
        assertEquals(emptyList(), b)
    }

    @Test
    fun v10BadDate() {
        assertOnly("V10", Fx().edit(Fx.FIRST, "publishedOn: 2026-10-20", "publishedOn: 20-10-2026").violations(), "YYYY-MM-DD")
        assertOnly("V10", Fx().edit(Fx.FIRST, "publishedOn: 2026-10-20", "publishedOn: 2026-02-31").violations(), "YYYY-MM-DD")
    }

    @Test
    fun v10DatesStayStrings() {
        // `publishedOn: 2026-10-20` must read as the text, never a Date (compose, docs/14 section 3.1).
        val c = loadContent(Fx().content)
        assertEquals("2026-10-20", c.leafFiles.first { it.leaf == "first-leaf" }.fm["publishedOn"])
    }

    // ---- V11 ----
    @Test
    fun v11IndexOrLeadingDigit() {
        assertOnly("V11", Fx().edit(Fx.RM, "- id: first-leaf", "- id: index").violations(), "index")
        assertOnly("V11", Fx().edit(Fx.RM, "- id: beta", "- id: 3d-things").violations(), "digit")
    }

    // ---- V12 ----
    @Test
    fun v12RawHtmlInLiveBodyOnly() {
        val live = Fx().edit(Fx.FIRST, "## The question", "## The question\n\n<div>hi</div>").violations()
        assertOnly("V12", live, "raw HTML")
        val shadow = Fx().edit(Fx.SECOND, "Body.", "<!-- VERIFY -->\n<div>hi</div>").violations()
        assertTrue("V12" !in shadow.rules())
    }

    @Test
    fun v12SimMarkerAndCodeAreAllowed() {
        // the good fixture's live body has a SIM marker, inline `<b>` and a fenced <div>
        assertTrue("V12" !in Fx().violations().rules())
    }
}
