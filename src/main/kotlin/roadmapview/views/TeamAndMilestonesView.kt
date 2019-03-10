package roadmapview.views

import roadmap.*
import roadmapview.RoadmapViewPropsObj

object TeamAndMilestonesView {
    fun build(data: Blocks, options: VisJsOptions): RoadmapViewPropsObj {
        // create a new group for milestones
        val milestone = Group(-1000, "Milestones", order = -1000)
        val teamGroups = listOf(milestone) + data.teamsByName.values

        val teamItems = data.items
                .flatMap {
                    val items = it.teams.map { team -> createItem(it, data, team) }

                    // don't duplicate milestones as we'll move them on a single row
                    if (items.first().isMilestone) {
                        items.take(1)
                    } else {
                        items
                    }
                }
                .mapIndexed { index, item -> item.copy(id = index) }

        // move all milestones to top group called milestones for better visibility
        val moveMilestones = teamItems.map { if (it.isMilestone) it.copy(visGroup = milestone) else it }

        return RoadmapViewPropsObj("By Team", moveMilestones.toTypedArray(), teamGroups.toTypedArray(), options)
    }

    private fun createItem(it: ParsedItem, data: Blocks, team: String): Item {
        val milestoneTeams = if (it.end == null)
            """</br> <span class="task-labels">${it.teams.joinToString(", ")}</span>"""
        else ""

        val content = """
                <span>${it.group} - ${it.title}</span>
                <span class="links"> ${it.htmlLinks()} </span>
                $milestoneTeams
            """.trimIndent()

        return Item(0,
                content,
                it.start,
                it.end,
                color = Colors.getColor(it.group ?: ""),
                description = it.description,
                labels = it.labels,
                visGroup = data.teamsByName.getValue(team)
        )
    }
}