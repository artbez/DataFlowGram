package se.iimetra.dataflowgram.home.diagram.node.ports

import react.*
import react.dom.div
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.LinkModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.PortWidget
import se.iimetra.dataflowgram.wrappers.react.diagrams.defaults.DefaultLinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.LinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel
import se.iimetra.dataflowgram.wrappers.react.tippy.Tooltip

enum class PortType {
  In, Out;

  var label = this.name.toLowerCase()
}

enum class PortPosition {
  Left, Right, Top, Bottom;

  val label = this.name.toLowerCase()
}

class InitialPortModel(val sname: String, val type: PortType, val index: Int) : PortModel("$sname-$index (${type.label})", type.label) {

  val unique = "$sname-$index (${type.label})"
  val label = "$sname (${type.label})"

  init {
    setMaximumLinks(1)
  }

  override fun createLinkModel(): LinkModel<out LinkModelListener>? = DefaultLinkModel()

  override fun canLinkToPort(port: InitialPortModel): Boolean {
    console.log(getLinks().toMap())
    console.log(port.getLinks().toMap())
    return type != port.type && sname == port.sname && getNode() != port.getNode()
       // && getLinks().toMap().size == 1 && port.getLinks().toMap().size == 1
  }
}

class InitialPortModelWidget : RComponent<InitialPortModelWidget.Props, RState>() {

  override fun RBuilder.render() {

    div(
      "diagram-net__port " +
        "diagram-net__port__${props.port.type.label} " +
        "diagram-net__port__${props.position.label}"
    ) {

      Tooltip {
        attrs {
          title = props.port.label
          position = "down"
          trigger = "mouseenter"
          theme = "light"
          animation = "none"
          sticky = true
          animateFill = false
          unmountHTMLWhenHide = false
          duration = 100
        }
        child(PortWidget::class) {
          attrs {
            this.node = props.node
            this.name = props.port.unique
          }
        }
      }
    }

  }

  interface Props : RProps {
    var node: NodeModel
    var position: PortPosition
    var port: InitialPortModel
  }
}

fun RBuilder.portModelWidget(handler: RHandler<InitialPortModelWidget.Props>) =
  child(InitialPortModelWidget::class, handler)