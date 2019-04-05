package markdown

import typemaps.showdown.Converter
import org.w3c.dom.Element
import react.*
import react.dom.div

interface MarkdownProps : RProps {
    var content: String
}

class Markdown(props: MarkdownProps) : RComponent<MarkdownProps, RState>(props) {

    private val markdownContianer = createRef<Element>()

    override fun componentDidMount() {
        markdownContianer.current!!.innerHTML = renderMarkdown()
    }

    private fun renderMarkdown(): String {
        val content = props.content.trimIndent()
        return Converter().makeHtml(content)
    }

    override fun RBuilder.render() {
        div("rendered-markdown") {
            ref = markdownContianer
        }
    }
}

fun RBuilder.markdown(content: String) = child(Markdown::class) {
    attrs.content = content
}