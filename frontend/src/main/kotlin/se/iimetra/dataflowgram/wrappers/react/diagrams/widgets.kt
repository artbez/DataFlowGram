@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams

import se.iimetra.dataflowgram.wrappers.react.diagrams.models.LinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PointModel
import org.w3c.dom.events.MouseEvent
import react.RProps
import react.React
import react.ReactElement

@JsName("BaseWidget")
open external class BaseWidget<T : BaseWidgetProps> : React.Component<T, dynamic> {
    override fun render(): ReactElement
}

external interface BaseWidgetProps : RProps {
    var baseClass: String
    var className: String
    var extraProps: dynamic
}

@JsName("DiagramWidget")
external class DiagramWidget : BaseWidget<DiagramWidgetProps>

external interface DiagramWidgetProps : BaseWidgetProps {
    var diagramEngine: DiagramEngine
    var allowLooseLinks: Boolean
    var allowCanvasTranslation: Boolean
    var allowCanvasZoom: Boolean
    var inverseZoom: Boolean
    var maxNumberPointsPerLink: Int
    var smartRouting: Boolean

    var actionStartedFiring: (dynamic) -> Boolean
    var actionStillFiring: (dynamic) -> Unit
    var actionStoppedFiring: (dynamic) -> Unit

    var deleteKeys: Array<Int>
}

external interface NodeProps : BaseWidgetProps {
    var node: NodeModel
    var children: dynamic
    var diagramEngine: DiagramEngine
}

@JsName("NodeWidget")
external class NodeWidget : BaseWidget<NodeProps>

external interface PortProps : BaseWidgetProps {
    var name: String
    var node: NodeModel
}

@JsName("PortWidget")
external class PortWidget : BaseWidget<PortProps>

external interface LinkProps : BaseWidgetProps {
    var link: LinkModel<LinkModelListener>
    var diagramEngine: DiagramEngine
    var children: dynamic
}

@JsName("LinkWidget")
external class LinkWidget : BaseWidget<LinkProps>

external interface NodeLayerProps : BaseWidgetProps {
    var diagramEngine: DiagramEngine
}

@JsName("NodeLayerWidget")
external class NodeLayerWidget : BaseWidget<NodeLayerProps>

external interface LinkLayerProps : BaseWidgetProps {
    var diagramEngine: DiagramEngine
    var pointAdded: (point: PointModel, event: MouseEvent) -> dynamic
}

@JsName("LinkLayerWidget")
external class LinkLayerWidget : BaseWidget<LinkLayerProps>