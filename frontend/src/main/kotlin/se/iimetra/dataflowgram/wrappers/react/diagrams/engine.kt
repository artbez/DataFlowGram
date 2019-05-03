@file:JsModule("storm-react-diagrams")

package se.iimetra.dataflowgram.wrappers.react.diagrams

import se.iimetra.dataflowgram.wrappers.react.diagrams.models.DiagramModel
import org.w3c.dom.events.Event

@JsName("DiagramEngine")
external class DiagramEngine {
    fun installDefaultFactories()
    fun setDiagramModel(model: DiagramModel)
    fun getRelativeMousePoint(event: Event): Point
    fun getDiagramModel(): DiagramModel
    fun registerNodeFactory(nodeFactory: AbstractNodeFactory<dynamic>)
    fun registerPortFactory(portFactory: AbstractPortFactory<dynamic>)
}

external class Point {
    val x: Double
    val y: Double
}