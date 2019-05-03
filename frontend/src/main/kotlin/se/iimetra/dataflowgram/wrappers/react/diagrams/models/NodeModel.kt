@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.models

import se.iimetra.dataflowgram.utils.JsMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine

@JsName("NodeModel")
open external class NodeModel(nodeType: String = definedExternally, id: String? = definedExternally) :
    BaseModel<DiagramModel, BaseModelListener> {

    open fun serialize(): dynamic
    open fun deSerialize(ob: dynamic, engine: DiagramEngine)
    fun setPosition(x: Double, y: Double)
    fun getPortFromID(id: String): PortModel?
    fun getPort(name: String): PortModel
    fun getPorts(): JsMap<PortModel>
    fun removePort(port: PortModel)
    fun <T : PortModel> addPort(port: T): T
    fun updateDimensions(dimension: Dimension): Dimension

    interface Dimension {
        var width: Double
        var height: Double
    }
}