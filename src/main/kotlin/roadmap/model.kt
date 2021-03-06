package roadmap

import typemaps.datefns.format
import kotlin.js.Date
import kotlin.math.floor

data class Group(val id: Int, val content: String, val order: Int = 10) {
    val subgroupStack = true
    val subgroupOrder = "subgroup"
}

data class Item(val id: Int,
                val content: String,
                val start: Date,
                val end: Date? = null,
                val visGroup: Group? = null,
                val color: Color? = null,
                val labels: List<String> = listOf(),
                val description: String = "") {

    val group = visGroup?.id

    val isMilestone = end == null
    val isBackground = !isMilestone && labels.contains("background")
    val isBox = isMilestone && labels.contains("box")

    val className = labels.joinToString(",")

    val style = when {
        isMilestone -> "border-color: $color"
        else -> "background-color: ${color?.transparent()}"
    }

    val type = when(true) {
        isMilestone -> "point"
        isBackground -> "background"
        isBox -> "box"
        else -> "range"
    }

    val subgroup = if (isMilestone || isBox) 1 else 0

    val title = createDescription()

    private fun createDescription(): String {
        val startstr = format(start, "MMM Do")
        val endstr = end?.let { format(end, "MMM Do") }

        val duration = if (isMilestone) startstr else "${countSprints(start)} ($startstr - $endstr)"

        val text = listOf(duration) + description.split("\n")

        return """<div class="tooltip">${text.joinToString("") { "<p>$it</p>" }}</div>"""
    }

    private fun countSprints(starting: Date): String {
        if (end == null) {
            return "milestone"
        }

        fun p(item: Int, unit: String): String? {
            if (item < 1) return null
            return "$item $unit" + if(item > 1) "s" else ""
        }

        val sprints = floor((end.getTime() - starting.getTime()) / 1000 / 3600 / 24 / 14).toInt()
        val weeks = floor((end.getTime() - starting.getTime()) / 1000 / 3600 / 24 / 7 - sprints * 2).toInt()
        val days = floor((end.getTime() - starting.getTime()) / 1000 / 3600 / 24 - sprints * 14 - weeks * 7).toInt()

        val date = mutableListOf<String?>()
        date.add(p(sprints, "sprint"))
        date.add(p(weeks, "week"))
        date.add(p(days, "day"))

        return date.filterNotNull().joinToString(", ")
    }
}

interface VisJsOptions {
    var width: String
    var zoomable: Boolean
    var zoomKey: String
    var start: String?
    var end: String?
    var autoResize: Boolean
    var clickToUse: Boolean
}

fun VisJsOptions(width: String = "100%",
                        zoomable: Boolean = true,
                        zoomKey: String = "ctrlKey",
                        start: String? = null,
                        end: String? = null,
                        autoResize: Boolean = true,
                        clickToUse: Boolean = true
): VisJsOptions {
    val o = js("{}")
    o.width = width
    o.zoomable = zoomable
    o.zoomKey = zoomKey
    start?.let { o.start = start }
    end?.let { o.end = end }
    o.autoResize = autoResize
    o.clickToUse = clickToUse

    return o
}