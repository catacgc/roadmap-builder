package roadmapview.views

import roadmap.*
import roadmapview.RoadmapViewPropsObj

object TeamView {
    fun build(data: Blocks, options: VisJsOptions): RoadmapViewPropsObj {
        val teamGroups = data.teamsByName.values

        val teamItems = data.items
                .flatMap {
                    it.teams.map { team -> createItem(it, data, team) }
                }
                .mapIndexed { index, item -> item.copy(id = index) }

        return RoadmapViewPropsObj("By Team", teamItems.toTypedArray(), teamGroups.toTypedArray(), options)
    }

    private fun createItem(it: ParsedItem, data: Blocks, team: String): Item {
        val content = """
                <span>${it.group} - ${it.title}</span>
                <span class="links"> ${it.htmlLinks()} </span>
            """.trimIndent()

        return Item(0,
                content,
                it.start,
                it.end,
                color = Colors.getColor(it.group ?: it.title),
                description = it.description,
                labels = it.labels,
                visGroup = data.teamsByName.getValue(team)
        )
    }
}