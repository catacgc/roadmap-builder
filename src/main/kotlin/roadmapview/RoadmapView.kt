package roadmapview

import org.w3c.dom.Element
import react.*
import react.dom.div
import react.dom.h3
import roadmap.*
import roadmap.types.DataSet
import roadmap.types.Timeline
import roadmap.types.format
import kotlin.js.Date

interface RoadmapViewProps : RProps {
    var name: String
    var items: Array<Item>
    var groups: Array<Group>
    var options: VisJsOptions
}

interface RoadmapViewState : RState {
    var refresh: Boolean
    var timeline: Timeline?
}

class RoadmapViewPropsObj(override var name: String,
               override var items: Array<Item>,
               override var groups: Array<Group>,
               override var options: VisJsOptions) : RoadmapViewProps

class RoadmapView(props: RoadmapViewProps) : RComponent<RoadmapViewProps, RoadmapViewState>(props) {

    private val visJsContainer = createRef<Element>()

    override fun RoadmapViewState.init(props: RoadmapViewProps) {
        refresh = false
    }

    override fun componentDidMount() {
        console.log("did mount")
        renderVisJs(props)
    }

    override fun componentWillReceiveProps(nextProps: RoadmapViewProps) {
        state.refresh = true

        state.timeline?.destroy()

        renderVisJs(nextProps)
    }

    private fun renderVisJs(nextProps: RoadmapViewProps) {
        val items = DataSet(nextProps.items)
        val timeline = Timeline(visJsContainer.current!!, items, nextProps.options)
        timeline.setGroups(nextProps.groups)
        state.timeline = timeline

        timeline.addCustomTime(Date(2007, 3, 15), 1)
        timeline.on("click") { ev ->
            val itemId = ev.item
            if (itemId != null) {
                nextProps.items.find { it.id == itemId }?.let { item ->
                    timeline.setCustomTime(item.start, 1)
                    timeline.setCustomTimeTitle(format(item.start), 1)
                }
            }
        }
    }

    override fun RBuilder.render() {
        console.log("view render")

        div("timeline") {
            h3("") { +props.name }
            div("timeline-container") { ref = visJsContainer }
        }

        if (state.refresh) {

        }
    }

}

fun RBuilder.timeline(props: RoadmapViewProps) = child(RoadmapView::class) {
    console.log(props)
    attrs.name = props.name
    attrs.items = props.items
    attrs.groups = props.groups
    attrs.options = props.options
}