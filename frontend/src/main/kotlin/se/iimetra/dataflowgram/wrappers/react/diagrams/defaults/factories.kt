@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams.defaults

import se.iimetra.dataflowgram.wrappers.react.diagrams.*
import react.ReactElement

@JsName("DefaultNodeFactory")
external class DefaultNodeFactory : AbstractNodeFactory<DefaultNodeModel> {
    override fun getNewInstance(initialConfig: dynamic): DefaultNodeModel = definedExternally

    override fun generateReactWidget(diagramEngine: DiagramEngine, node: DefaultNodeModel): ReactElement = definedExternally
}

@JsName("DefaultPortFactory")
external class DefaultPortFactory : AbstractPortFactory<DefaultPortModel> {
    override fun getNewInstance(initialConfig: dynamic): DefaultPortModel = definedExternally
}

@JsName("DefaultLabelFactory")
external class DefaultLabelFactory : AbstractLabelFactory<DefaultLabelModel> {
    override fun getNewInstance(initialConfig: dynamic): DefaultLabelModel = definedExternally

    override fun generateReactWidget(diagramEngine: DiagramEngine, link: DefaultLabelModel): ReactElement = definedExternally
}

@JsName("DefaultLinkFactory")
external class DefaultLinkFactory : AbstractLinkFactory<DefaultLinkModel> {
    override fun getNewInstance(initialConfig: dynamic): DefaultLinkModel = definedExternally

    override fun generateReactWidget(diagramEngine: DiagramEngine, link: DefaultLinkModel): ReactElement = definedExternally

    fun generateLinkSegment(model: DefaultLinkModel, widget: DefaultLinkWidget, selected: Boolean, path: String)
}