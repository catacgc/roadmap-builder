package roadmap

import typemaps.duration.Duration
import typemaps.datefns.parse
import typemaps.peg.DateWithDelta
import kotlin.js.Date
import typemaps.peg.parse as pegParse

class ParseError(errorMessage: String, line: Int, roadmap: String) {
    private val split = roadmap.split("\n")

    val message = {
        val context = 10
        val contextMin = if (line - context >= 1) line - context else 1
        val contextMax = if (line + context <= split.size) line + context else split.size

        val lines = (contextMin until contextMax + 1).map {
            "$it: ${split[it - 1]}"
        }.joinToString("\n")

        """
Error: $errorMessage
Line: $line

Context
-----
$lines
        """
    }()

}

class ParseResult<T>(val value: T? = null, val error: ParseError? = null)

data class ParsedItem(val group: String?,
                      val title: String,
                      val start: Date,
                      val end: Date?,
                      val teams: List<String>,
                      val description: String = "",
                      val labels: List<String> = listOf(),
                      val links: List<Pair<String, String>> = listOf()) {

    fun htmlLinks() = links.joinToString(", ") { """<a class="grow" target="_blank" href="${it.second}"/>${it.first}</a>""" }
}

class Blocks(val items: List<ParsedItem>) {

    val groupsByName = items
            .mapNotNull { it.group }
            .toSet()
            .mapIndexed { index, s -> s to Group(index, s) }
            .toMap()

    val teamsByName = items
            .flatMap { it.teams }
            .toSet()
            .mapIndexed { index, s -> s to Group(index, s) }
            .toMap()
}

private fun parseStart(date: DateWithDelta): Date {
    val start = parse(date.date!!)
    val delta = date.delta

    delta ?: return start

    console.log(start, Duration.parse(delta), Duration.parse(delta).after(start))

    return Duration.parse(delta).after(start)
}

private fun parseEnd(start: DateWithDelta, end: DateWithDelta?): Date? {
    end ?: return null

    val start = parseStart(start)

    val endDate = if (end.date != null) parse(end.date!!) else start

    return if (end.delta != null) {
        Duration.parse(end.delta!!).after(start)
    } else {
        return endDate
    }
}

fun parseRoadmap(roadmap: String): ParseResult<Blocks> {
    try {
        val items = pegParse(roadmap)
        val parsedItems = items.map {
            ParsedItem(
                    group = it.title.project,
                    title = it.title.task,
                    start = parseStart(it.start),
                    end = parseEnd(it.start, it.end),
                    links = it.meta.links.map { l -> l.name to l.url },
                    description = it.meta.desc,
                    labels = it.meta.labels.toList(),
                    teams = it.teams.toList()
            )
        }

        return ParseResult(Blocks(parsedItems))
    } catch (ex: dynamic) {
        console.log(ex)
        val error = ParseError(ex.message, ex.location.start.line as Int, roadmap)
        return ParseResult(error = error)
    }
}
