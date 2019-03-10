package roadmap

import roadmap.types.Duration
import roadmap.types.parse
import kotlin.js.Date

class ParseResult<T>(val value: T? = null, val error: String? = null)

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

private fun parseItem(item: String): ParseResult<ParsedItem> {
    val lines = item.trim().split("\n")

    if (lines.size < 2) {
        return ParseResult(error = "Cannot parse: $item")
    }

    val groupAndTitle = lines[0].split(",").map { it.trim() }
    val title = if (groupAndTitle.size == 2) groupAndTitle[1] else groupAndTitle[0]
    val group = if (groupAndTitle.size == 2) groupAndTitle[0] else null

    val (start, end) = parseDate(lines[1])

    val teams = lines[2].split(",").map { it.trim() }

    var parsed = ParsedItem(group, title, start, end, teams)

    lines.drop(3).forEach {
        parsed = parseMeta(it, parsed)
    }

    return ParseResult(parsed)
}

private fun parseMeta(meta: String, parsed: ParsedItem): ParsedItem {
    val metaTrimmed = meta.trim()
    val prefix = metaTrimmed[0]
    val value = metaTrimmed.drop(1).trim()

    return when (prefix) {
        '@' -> parsed.copy(labels = value.split(",").map { it.trim() })
        '>' -> parsed.copy(description = parsed.description + value + "\n")
        '^' -> parsed.copy(links = parsed.links + (value.split("|")[0] to value.split("|")[1]))
        else -> parsed
    }
}

private fun parseDate(line: String): Pair<Date, Date?> {
    val dates = line.split(",").map { it.trim() }

    val start = if (dates[0].contains("+")) {
        val (first, duration) = dates[0].split("+")
        parseEndDate(parse(first), duration)!!
    } else parse(dates[0])

    val end = if (dates.size > 1) {
        parseEndDate(start, dates[1])
    } else null

    return Pair(start, end)
}

private fun parseEndDate(start: Date, end: String): Date? {
    val endDate = try { parse(end) } catch (ex: Throwable) { null }
    val duration = try { Duration.parse(end).after(start) } catch (ex: Throwable) { null }

    if(endDate?.getSeconds()?.toDouble()?.isFinite() == true) {
        return endDate
    }

    return duration
}

fun parseRoadmap(roadmap: String): ParseResult<Blocks> {
    val items = roadmap.trim().split("\n\n")

    val parsed: List<ParseResult<ParsedItem>> = items.map {
        try {
            parseItem(it)
        } catch (ex: Throwable) {
            ParseResult<ParsedItem>(error = ex.message + " " +  it)
        }
    }

    val errors = parsed.mapNotNull { it.error }
    val noErrors = parsed.mapNotNull { it.value }

    val errorMessage = when (errors.size) {
        0 -> null
        else -> errors.joinToString("\n\n")
    }

    return ParseResult(Blocks(noErrors), errorMessage)
}