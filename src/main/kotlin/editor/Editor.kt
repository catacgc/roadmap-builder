package editor

import kotlinext.js.asJsObject
import kotlinx.html.asFlowContent
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import roadmap.RoadmapProps

interface EditorProps : RProps {
    var editedContent: String
    var onChange: (String) -> Unit
}

interface EditorState : RState {
    var isActive: Boolean
    var edited: String
}

class Editor(props: EditorProps) : RComponent<EditorProps, EditorState>(props) {

    override fun EditorState.init(props: EditorProps) {
        isActive = false
        edited = props.editedContent
    }

    override fun RBuilder.render() {
        div("edit-button") {
            button {
                +"Edit"
                attrs {
                    onClickFunction = { ev ->
                        state.isActive = !state.isActive
                        setState(state)
                    }
                }
            }
        }

        if (state.isActive) {
            p {
                +"There's no save button in here; embed this back in the page;"
            }

            textArea("20", "120") {
                +state.edited
                attrs {
                    onChangeFunction = ::captureEdited
                }
            }

            button {
                +"Refresh"
                attrs {
                    onClickFunction = { ev ->
                        props.onChange(state.edited)
                    }
                }
            }
        }


    }

    private fun captureEdited(ev: Event) {
        state.edited = ev.target?.asDynamic().value as String
    }

}

fun RBuilder.editor(roadmap: String, onChange: (String) -> Unit) = child(Editor::class) {
    attrs.onChange = onChange
    attrs.editedContent = roadmap
}