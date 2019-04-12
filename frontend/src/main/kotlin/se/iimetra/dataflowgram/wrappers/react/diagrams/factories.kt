@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams

import se.iimetra.dataflowgram.wrappers.react.diagrams.models.*
import react.ReactElement

@JsName("AbstractFactory")
abstract external class AbstractFactory<out T : BaseModel<dynamic, dynamic>>(name: String) {

    fun getType(): String

    abstract fun getNewInstance(initialConfig: dynamic): T
}

@JsName("AbstractNodeFactory")
abstract external class AbstractNodeFactory<T : NodeModel>(name: String) : AbstractFactory<T> {
    abstract fun generateReactWidget(diagramEngine: DiagramEngine, node: T): ReactElement
}

@JsName("AbstractLinkFactory")
abstract external class AbstractLinkFactory<T : LinkModel<dynamic>> : AbstractFactory<T> {
    abstract fun generateReactWidget(diagramEngine: DiagramEngine, link: T): ReactElement
}

@JsName("AbstractLabelFactory")
abstract external class AbstractLabelFactory<T : LabelModel> : AbstractFactory<T> {
    abstract fun generateReactWidget(diagramEngine: DiagramEngine, link: T): ReactElement
}

@JsName("AbstractPortFactory")
abstract external class AbstractPortFactory<out T : PortModel> : AbstractFactory<T>