@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams


@JsName("BaseEvent")
external interface BaseEvent<T : BaseEntity<dynamic>> {
    var entity: T
    var stopPropagation: () -> dynamic
    var firing: Boolean
    var id: String
}

@JsName("BaseListener")
external interface BaseListener {

    var lockChanged: ((event: BaseEvent<dynamic>, BaseListener.Locked) -> Unit)?

    interface Locked {
        var locked: Boolean
    }
}

@JsName("BaseEntityType")
external enum class BaseEntityType {
    NODE, LINK, PORT, POINT
}

@JsName("BaseEntity")
open external class BaseEntity<in T : BaseListener>(id: String? = definedExternally) {
    fun getID(): String
    fun clearListeners()
    fun removeListener(listener: String)
    fun addListener(listener: T)
    fun isLocked(): Boolean
    fun setLocked(locked: Boolean = definedExternally)
}