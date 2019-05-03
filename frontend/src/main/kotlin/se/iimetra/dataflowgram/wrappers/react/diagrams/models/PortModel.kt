@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.models

import se.iimetra.dataflowgram.home.diagram.node.ports.InitialPortModel
import se.iimetra.dataflowgram.utils.JsMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.LinkModelListener

@JsName("PortModel")
open external class PortModel(
    name: String = definedExternally,
    type: String = definedExternally,
    id: String = definedExternally,
    maximumLinks: Int = definedExternally
) : BaseModel<NodeModel, BaseModelListener> {

    fun getNode(): NodeModel
    fun getName(): String
    fun getMaximumLinks(): Int
    open fun serialize(): dynamic
    open fun deSerialize(ob: dynamic, engine: DiagramEngine)
    fun setMaximumLinks(maximumLinks: Int)
    fun removeLink(link: LinkModel<LinkModelListener>)
    fun addLink(link: LinkModel<LinkModelListener>)
    fun getLinks(): JsMap<LinkModel<LinkModelListener>>
    open fun createLinkModel(): LinkModel<out LinkModelListener>?
    fun updateCoords(coords: Coordinates): Coordinates
    open fun canLinkToPort(port: InitialPortModel): Boolean

    interface Coordinates {
        var x: Double
        var y: Double
        var width: Double
        var height: Double
    }
}