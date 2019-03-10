package roadmap

import editor.editor
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import roadmapview.timeline
import roadmapview.views.ProjectView
import roadmapview.views.TeamAndMilestonesView
import roadmapview.views.TeamView

interface RoadmapProps : RProps {
    var roadmapString: String
    var views: List<String>
    var start: String?
    var end: String?
}

interface RoadmapState : RState {
    var blocks: ParseResult<Blocks>
}

class Roadmap(props: RoadmapProps) : RComponent<RoadmapProps, RoadmapState>(props) {

    override fun RoadmapState.init(props: RoadmapProps) {
        console.log("init")
        blocks = parseRoadmap(props.roadmapString)
    }

    override fun RBuilder.render() {
        if (state.blocks.error != null) {
            val text = state.blocks.error!!

            div("error") { +text }
            return
        }

        val parsed = state.blocks.value!!

        val options = VisJsOptions(start = props.start, end = props.end)
        val viewsMap = linkedMapOf(
                "project" to ProjectView.build(parsed, options),
                "team" to TeamView.build(parsed, options),
                "milestones" to TeamAndMilestonesView.build(parsed, options)
        )

        div("roadmap-controls") {
            editor(props.roadmapString, ::onChange)
        }

        // render all views in the data-template attribute
        props.views.forEach {
            timeline(viewsMap[it]!!)
        }
    }

    private fun onChange(newRoadmap: String) {
        state.blocks = parseRoadmap(newRoadmap)
        console.log(state.blocks)
        setState(state)
    }
}

fun RBuilder.roadmap(props: RoadmapProps) = child(Roadmap::class) {
    attrs.roadmapString = props.roadmapString
    attrs.views = props.views
    attrs.start = props.start
    attrs.end = props.end
}