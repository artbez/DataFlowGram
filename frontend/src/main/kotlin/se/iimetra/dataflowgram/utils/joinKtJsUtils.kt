package se.iimetra.dataflowgram.utils

import kotlinext.js.Object
import kotlinext.js.toPlainObjectStripNull

external class JsMap<T> {
    operator fun get(key: String): T
    operator fun set(key: String, value: T)
}

fun <T> JsMap<T>.toMap(): Map<String, T> {
    val resultMap = mutableMapOf<String, T>()
    val that = toPlainObjectStripNull(this)

    for (key in Object.keys(that)) {
        val value = this.asDynamic()[key]
        resultMap[key] = value as T
    }
    return resultMap
}