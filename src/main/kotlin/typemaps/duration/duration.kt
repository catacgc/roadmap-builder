@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS", "EXTERNAL_DELEGATION", "NESTED_CLASS_IN_EXTERNAL_INTERFACE")
package typemaps.duration

import kotlin.js.*

@JsModule("duration-js")
open external class Duration {
    constructor(value: String? = definedExternally /* null */)
    constructor(value: Number? = definedExternally /* null */)
    constructor(value: Duration? = definedExternally /* null */)
    open var _milliseconds: Number = definedExternally
    open fun nanoseconds(): Number = definedExternally
    open fun microseconds(): Number = definedExternally
    open fun milliseconds(): Number = definedExternally
    open fun seconds(): Number = definedExternally
    open fun minutes(): Number = definedExternally
    open fun hours(): Number = definedExternally
    open fun days(): Number = definedExternally
    open fun weeks(): Number = definedExternally
    override fun toString(): String = definedExternally
    open fun valueOf(): Number = definedExternally
    open fun isGreaterThan(duration: String): Boolean = definedExternally
    open fun isGreaterThan(duration: Number): Boolean = definedExternally
    open fun isGreaterThan(duration: Duration): Boolean = definedExternally
    open fun isLessThan(duration: String): Boolean = definedExternally
    open fun isLessThan(duration: Number): Boolean = definedExternally
    open fun isLessThan(duration: Duration): Boolean = definedExternally
    open fun isEqualTo(duration: String): Boolean = definedExternally
    open fun isEqualTo(duration: Number): Boolean = definedExternally
    open fun isEqualTo(duration: Duration): Boolean = definedExternally
    open fun roundTo(duration: String): Unit = definedExternally
    open fun roundTo(duration: Number): Unit = definedExternally
    open fun roundTo(duration: Duration): Unit = definedExternally
    open fun after(date: Number): Date = definedExternally
    open fun after(date: Date): Date = definedExternally
    companion object {
        var millisecond: Duration = definedExternally
        var second: Duration = definedExternally
        var minute: Duration = definedExternally
        var hour: Duration = definedExternally
        var day: Duration = definedExternally
        var week: Duration = definedExternally
        fun since(date: Number): Duration = definedExternally
        fun since(date: Date): Duration = definedExternally
        fun until(date: Number): Duration = definedExternally
        fun until(date: Date): Duration = definedExternally
        fun between(a: Number, b: Number): Duration = definedExternally
        fun between(a: Number, b: Date): Duration = definedExternally
        fun between(a: Date, b: Number): Duration = definedExternally
        fun between(a: Date, b: Date): Duration = definedExternally
        fun parse(duration: String): Duration = definedExternally
        fun fromMicroseconds(us: Number): Duration = definedExternally
        fun fromNanoseconds(ns: Number): Duration = definedExternally
        fun add(a: Duration, b: Duration): Duration = definedExternally
        fun subtract(a: Duration, b: Duration): Duration = definedExternally
        fun multiply(a: Duration, b: Number): Duration = definedExternally
        fun multiply(a: Number, b: Duration): Duration = definedExternally
        fun divide(a: Duration, b: Duration): Number = definedExternally
    }
}
