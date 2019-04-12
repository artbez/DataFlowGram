@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.models

import se.iimetra.dataflowgram.utils.JsMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.*

@JsName("DiagramListener")
external interface DiagramListener : BaseListener {
    fun <T> nodesUpdated(event: T) where T : BaseEvent<NodeModel>, T : NodeUpdate
    fun <T> linksUpdated(event: T) where T : BaseEvent<LinkModel<LinkModelListener>>, T : LinkModel<LinkModelListener>
    fun <T> offsetUpdated(event: T) where T : BaseEvent<DiagramModel>, T : OffsetUpdate
    fun <T> zoomUpdated(event: T) where T : BaseEvent<DiagramModel>, T : ZoomUpdate
    fun <T> gridUpdated(event: T) where T : BaseEvent<DiagramModel>, T : GridUpdate

    interface NodeUpdate {
        var node: NodeModel
        var isCreated: Boolean
    }

    interface LinkUpdate {
        var link: LinkModel<LinkModelListener>
        var isCreated: Boolean
    }

    interface OffsetUpdate {
        var offsetX: Double
        var offsetY: Double
    }

    interface ZoomUpdate {
        var zoom: Double
    }

    interface GridUpdate {
        var size: Double
    }
}

@JsName("DiagramModel")
open external class DiagramModel : BaseEntity<DiagramListener> {
    fun setGridSize(size: Double = definedExternally)
    fun getGridPosition(pos: dynamic)
    fun clearSelection(ignore: BaseModel<BaseEntity<dynamic>, BaseModelListener>)
    fun getSelectedItems(vararg filters: String): BaseModel<BaseEntity<dynamic>, BaseModelListener>
    fun setZoomLevel(zoom: Double)
    fun setOffset(offsetX: Double, offsetY: Double)
    fun setOffsetX(offsetX: Double)
    fun setOffsetY(offsetY: Double)
    fun getOffsetY(): Double
    fun getOffsetX(): Double
    fun getNode(node: String): NodeModel?
    fun getNode(node: NodeModel): NodeModel?
    fun getLink(link: String): LinkModel<LinkModelListener>?
    fun getLink(link: LinkModel<LinkModelListener>): LinkModel<LinkModelListener>?
    fun addAll(vararg models: BaseModel<dynamic, dynamic>): BaseModel<dynamic, dynamic>
    fun addLink(link: LinkModel<LinkModelListener>): LinkModel<LinkModelListener>
    fun addNode(node: NodeModel): NodeModel
    fun removeLink(link: LinkModel<LinkModelListener>)
    fun removeLink(link: String)
    fun removeNode(node: NodeModel)
    fun removeNode(node: String)
    fun getLinks(): JsMap<LinkModel<LinkModelListener>>
    fun getNodes(): JsMap<NodeModel>
}

