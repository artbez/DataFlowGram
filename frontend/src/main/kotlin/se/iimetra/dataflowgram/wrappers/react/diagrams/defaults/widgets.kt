@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.defaults

import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseWidget
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseWidgetProps
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PointModel

external interface DefaultNodeProps : BaseWidgetProps {
    var node: DefaultNodeModel
    var diagramEngine: DiagramEngine
}

@JsName("DefaultNodeWidget")
external class DefaultNodeWidget : BaseWidget<DefaultNodeProps>

external interface DefaultPortLabelProps : BaseWidgetProps {
    var model: DefaultPortModel
}

@JsName("DefaultPortLabel")
external class DefaultPortLabel : BaseWidget<DefaultPortLabelProps>

external interface DefaultLabelWidgetProps : BaseWidgetProps {
    var model: DefaultLabelModel
}

@JsName("DefaultLabelWidget")
external class DefaultLabelWidget : BaseWidget<DefaultLabelWidgetProps>

external interface DefaultLinkProps : BaseWidgetProps {
    var color: String
    var width: Double
    var smooth: Boolean
    var link: DefaultLinkModel
    var diagramEngine: DiagramEngine
    var pointAdded: (point: PointModel, event: dynamic) -> dynamic
}

@JsName("DefaultLinkWidget")
external class DefaultLinkWidget : BaseWidget<DefaultLinkProps>
