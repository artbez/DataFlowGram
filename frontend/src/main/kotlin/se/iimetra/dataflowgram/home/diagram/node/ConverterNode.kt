package se.iimetra.dataflowgram.home.diagram.node

import kotlinext.js.invoke
import react.*
import react.dom.div
import react.dom.span
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.SystemFunction
import se.iimetra.dataflowgram.dom.converterDown
import se.iimetra.dataflowgram.dom.converterUp
import se.iimetra.dataflowgram.home.diagram.node.ports.InitialPortModel
import se.iimetra.dataflowgram.home.diagram.node.ports.PortPosition
import se.iimetra.dataflowgram.home.diagram.node.ports.PortType
import se.iimetra.dataflowgram.home.diagram.node.ports.portModelWidget
import se.iimetra.dataflowgram.wrappers.react.diagrams.AbstractNodeFactory
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import kotlin.js.Math
import kotlin.random.Random

class ConverterNode : NodeModel("system", Random(42).nextDouble().toString()) {

  val inPort: InitialPortModel = TODO()//InitialPortModel(function.functionSignature.input[0], PortType.In, 0)
  val outPort: InitialPortModel = TODO()// InitialPortModel(function.functionSignature.output, PortType.Out, -1)

  init {
    addPort(inPort)
    addPort(outPort)
  }
}

class ConverterNodeWidget : RComponent<ConverterNodeWidget.Props, RState>() {

  companion object {
    init {
      kotlinext.js.require("styles/custom-nodes.scss")
    }
  }

  override fun RBuilder.render() {
//    div {
//      if (props.converterNode.function.params["from"]!! == "server") {
//        converterUp("converter-icon_up") { }
//      } else {
//        converterDown("converter-icon_down") {  }
//      }
//      if (props.isView != true) {
//        portModelWidget {
//          attrs {
//            node = props.converterNode
//            port = props.converterNode.inPort
//            position = PortPosition.Left
//          }
//        }
//        portModelWidget {
//          attrs {
//            node = props.converterNode
//            port = props.converterNode.outPort
//            position = PortPosition.Right
//          }
//        }
//      }
//    }
  }

  interface Props : RProps {
    var converterNode: ConverterNode
    var isView: Boolean?
  }
}

fun RBuilder.converterNodeWidget(handler: RHandler<ConverterNodeWidget.Props>) = child(ConverterNodeWidget::class, handler)

class ConverterNodeFactory : AbstractNodeFactory<ConverterNode>("system") {

  companion object {
    val instance = ConverterNodeFactory()
  }

  override fun getNewInstance(): ConverterNode {
    return ConverterNode()
  }

  override fun generateReactWidget(diagramEngine: DiagramEngine, node: ConverterNode): ReactElement = buildElement {
    converterNodeWidget {
      attrs {
        this.converterNode = node
        isView = false
      }
    }
  }!!
}
