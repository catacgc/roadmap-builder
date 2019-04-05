@file:JsModule("parser")
package typemaps.peg

/**
end: {date: "2019-01-23"}
meta: {links: Array(1), labels: Array(2), desc: "A critical milestone↵More important than Milestone B"}
start: {date: "2019-01-10", delta: "9w"}
teams: ["Team A"]
title: {project: "Project A", task: "Task A"}
 */

external interface DateWithDelta {
    val date: String?
    val delta: String?
}

external interface Title {
    val project: String
    val task: String
}

external interface Link {
    val name: String
    val url: String
}

external interface Meta {
    val links: Array<Link>
    val desc: String
    val labels: Array<String>
}

external interface ParserResult {
    val title: Title
    val start: DateWithDelta
    val end: DateWithDelta?
    val teams: Array<String>
    val meta: Meta
}

external fun parse(parser: String): Array<ParserResult> = definedExternally
