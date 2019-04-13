package se.iimetra.dataflowgram.utils

import kotlinext.js.Object
import kotlinext.js.toPlainObjectStripNull
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify

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

@ImplicitReflectionSerializer
fun Any.toJson() = kotlin.js.JSON.parse<dynamic>(JSON.stringify(this))