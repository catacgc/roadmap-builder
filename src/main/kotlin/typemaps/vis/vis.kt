@file:JsModule("vis")
package typemaps.vis

import org.w3c.dom.Element
import roadmap.Group
import roadmap.Item
import roadmap.VisJsOptions
import kotlin.js.Date


external class DataSet(items: Array<Item>)

external class Timeline(container: Element, items: DataSet, options: VisJsOptions) {
    fun setGroups(groups: Array<Group>): dynamic
    fun destroy()
    fun setCustomTime(date: Date, id: Int)
    fun setCustomTimeTitle(title: String, id: Int)
    fun addCustomTime(date: Date, id: Int)
    fun on(ev: String, callback: (props: MouseEvent) -> Unit)
}

external interface MouseEvent {
    val item: Int?
    val group: Int?
    val what: String
}