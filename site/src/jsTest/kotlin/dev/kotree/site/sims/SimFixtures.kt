package dev.kotree.site.sims

import dev.kotree.site.sims.core.SimSpec
import dev.kotree.site.sims.core.SimTryStep
import dev.kotree.site.sims.core.readSimSpec

/** The ADR 009 section 2 worked example in the shape WP1 writes (cases for every action, keys normalised). */
internal const val SNAPSHOT_TRAY_JSON = """{
"title":"Working folder, tray and snapshot shelf",
"intro":"Files A and B sit in the working folder. The tray and the shelf are empty.",
"zones":[{"id":"folder","label":"Working folder"},{"id":"tray","label":"Tray (the frame)"},{"id":"shelf","label":"Snapshot shelf"}],
"items":[{"id":"a","label":"File A","at":"folder"},{"id":"b","label":"File B","at":"folder"}],
"vars":{"snaps":0},
"show":[{"var":"snaps","label":"Snapshots"}],
"actions":[
 {"id":"edit-a","label":"Edit file A","cases":[{"do":[{"mark":{"a":"changed"}},{"say":"File A is changed. It is still in the working folder."}]}]},
 {"id":"edit-b","label":"Edit file B","cases":[{"do":[{"mark":{"b":"changed"}},{"say":"File B is changed. It is still in the working folder."}]}]},
 {"id":"snap","label":"Take snapshot","cases":[{"when":{"at":{"a":"tray"}},"do":[{"add":{"snaps":1}},{"spawn":{"id":"s{snaps}","label":"Snapshot {snaps}: new A, B carried over","at":"shelf"}},{"move":{"a":"folder"}},{"mark":{"a":"plain"}},{"say":"Snapshot {snaps} holds the new A. B is carried over unchanged."}]}],"refuse":"Nothing is in the tray, so no snapshot is taken. Changed files stay in the folder."}
],
"drops":[
 {"item":"a","to":"tray","when":{"mark":{"a":"changed"}},"do":[{"say":"File A is in the tray."}],"refuse":"File A has no change to stage."},
 {"item":"b","to":"tray","when":{"mark":{"b":"changed"}},"do":[{"say":"File B is in the tray."}],"refuse":"File B has no change to stage."}
],
"script":["edit-a","a>tray","snap","edit-b","snap"]
}"""

internal fun trayTry(): List<SimTryStep> = listOf(
    SimTryStep("Edit file A.", listOf("edit-a"), false),
    SimTryStep("Drag file A into the tray, then take a snapshot.", listOf("a>tray", "snap"), false),
    SimTryStep("Edit file B and leave it out. Snapshot again.", listOf("edit-b", "snap"), false),
)

internal fun traySpec(): SimSpec = readSimSpec("snapshot-tray", "zones", JSON.parse<dynamic>(SNAPSHOT_TRAY_JSON), trayTry())!!

internal fun specOf(template: String, json: String): SimSpec =
    readSimSpec("t", template, JSON.parse<dynamic>(json), emptyList())!!
