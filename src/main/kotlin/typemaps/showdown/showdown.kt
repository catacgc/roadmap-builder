@file:JsModule("showdown")
package typemaps.showdown

external class Converter {
    fun makeHtml(text: String): String
}