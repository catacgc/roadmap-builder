package index

import kotlinext.js.require
import kotlinext.js.requireAll
import markdown.markdown
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.get
import react.dom.render
import roadmap.RoadmapProps
import roadmap.roadmap
import kotlin.browser.document
import kotlin.browser.window

fun main() {
    requireAll(require.context("src", true, js("/\\.css$/")))

    window.onload = {
        renderRoadmap()
        renderMarkdown()
    }
}

private fun renderMarkdown() {
    document.selectByClass("markdown").forEach {
        render(it) {
            markdown(it.textContent!!)
        }
    }
}

private fun renderRoadmap() {
    val elements = document.selectByClass("roadmap")

    elements.forEach { element ->
        val views = element.attr("data-view", "project,team").split(",").map { it.trim() }
        val start = element.attr("data-start") { it.trim() }
        val end = element.attr("data-end") { it.trim() }

        render(element) {
            roadmap(object: RoadmapProps {
                override var roadmapString: String = element.textContent!!
                override var views: List<String> = views
                override var start: String? = start
                override var end: String? = end
            })
        }
    }
}

private fun Element.attr(name: String, default: String): String {
    return getAttribute(name) ?: default
}

private fun Element.attr(name: String, ifPresent: (String) -> String?): String? {
    return getAttribute(name)?.let(ifPresent)
}

private fun Document.selectByClass(className: String): List<Element> {
    val roadmaps = document.getElementsByClassName(className)
    return (0 until roadmaps.length).map { roadmaps[it]!! }
}
