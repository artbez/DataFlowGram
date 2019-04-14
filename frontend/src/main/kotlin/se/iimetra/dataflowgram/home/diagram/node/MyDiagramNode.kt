package se.iimetra.dataflowgram.home.diagram.node

import kotlinext.js.invoke
import react.*
import react.dom.div
import react.dom.span
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflowgram.home.diagram.node.ports.InitialPortModel
import se.iimetra.dataflowgram.home.diagram.node.ports.PortPosition
import se.iimetra.dataflowgram.home.diagram.node.ports.PortType
import se.iimetra.dataflowgram.home.diagram.node.ports.portModelWidget
import se.iimetra.dataflowgram.wrappers.react.diagrams.AbstractNodeFactory
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class InitDefaultNode(val function: FunctionDescription) : NodeModel("default", "") {

  val portsList = mutableListOf<InitialPortModel>()
  var outPort: InitialPortModel? = null

  init {
    function.meta.signature.input.filter { it.isNotBlank() }.forEachIndexed { index, s ->
      val model = InitialPortModel(s, PortType.In, index)
      portsList.add(model)
      addPort(model)
    }
    if (function.meta.signature.output != "Unit") {
      outPort = InitialPortModel(function.meta.signature.output, PortType.Out, 5)
      addPort(outPort!!)
    }
  }
}

class DefNodeWidget : RComponent<DefNodeWidget.Props, RState>() {

  companion object {
    init {
      kotlinext.js.require("styles/custom-nodes.scss")
    }
  }

  override fun RBuilder.render() {
    div("diagram-net__node") {
      span("diagram-net__node__span") { +props.defaultNode.function.view.id.name }
      if (props.isView != true) {
        val sided = listOf(PortPosition.Left, PortPosition.Bottom, PortPosition.Top).subList(0, props.defaultNode.portsList.size)
        val positioned = props.defaultNode.portsList.zip(sided)
        positioned.forEach { (newPort, newPosition) ->
          portModelWidget {
            attrs {
              node = props.defaultNode
              port = newPort
              position = newPosition
            }
          }
        }

        val outModel = props.defaultNode.outPort
        if (outModel != null) {
          portModelWidget {
            attrs {
              node = props.defaultNode
              port = outModel
              position = PortPosition.Right
            }
          }
        }
      }
    }
  }

  interface Props : RProps {
    var defaultNode: InitDefaultNode
    var isView: Boolean?
  }
}

fun RBuilder.defaultNodeWidget(handler: RHandler<DefNodeWidget.Props>) = child(DefNodeWidget::class, handler)

class DefaultNodeFactory : AbstractNodeFactory<InitDefaultNode>("default") {

  companion object {
    val instance = DefaultNodeFactory()
  }

  override fun getNewInstance(initialConfig: FunctionDescription): InitDefaultNode {
    return InitDefaultNode(initialConfig)
  }

  override fun generateReactWidget(diagramEngine: DiagramEngine, node: InitDefaultNode): ReactElement = buildElement {
    defaultNodeWidget {
      attrs {
        this.defaultNode = node
        isView = false
      }
    }
  }!!
}
