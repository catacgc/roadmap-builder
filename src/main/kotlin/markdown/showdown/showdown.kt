@file:JsModule("showdown")
package markdown.showdown

external class Converter {
    fun makeHtml(text: String): String
}