package roadmapview.views

import roadmap.*
import roadmapview.RoadmapViewPropsObj

object ProjectView {
    fun build(data: Blocks, options: VisJsOptions): RoadmapViewPropsObj {
        val groups = data.groupsByName.values.toTypedArray()
        val items = data.items
                .mapIndexed { index, block ->
                    createItem(index, block, data)
                }
                .toTypedArray()

        return RoadmapViewPropsObj("By Project", items, groups, options)
    }

    private fun createItem(index: Int, parsedItem: ParsedItem, data: Blocks): Item {
        val content = """
                <span> ${parsedItem.title} </span>
                <span class="task-labels">( ${parsedItem.teams.joinToString(", ")} )</span>
                <span class="task-links"> ${parsedItem.htmlLinks()} </span>
            """.trimIndent()

        return Item(index,
                content,
                parsedItem.start,
                parsedItem.end,
                color = Colors.getColor(parsedItem.group ?: parsedItem.title),
                visGroup = data.groupsByName[parsedItem.group],
                description = parsedItem.description,
                labels = parsedItem.labels
        )
    }
}