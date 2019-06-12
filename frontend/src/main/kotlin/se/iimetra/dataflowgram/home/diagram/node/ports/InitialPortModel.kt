package se.iimetra.dataflowgram.home.diagram.node.ports

import kotlinext.js.jsObject
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import react.*
import react.dom.div
import se.iimetra.dataflowgram.home.diagram.node.nodeId
import se.iimetra.dataflowgram.utils.JsMap
import se.iimetra.dataflowgram.wrappers.lodash.lodash
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.LinkModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.PortWidget
import se.iimetra.dataflowgram.wrappers.react.diagrams.defaults.DefaultLinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.LinkModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel
import se.iimetra.dataflowgram.wrappers.react.tippy.Tooltip
import kotlin.random.Random

enum class PortType {
  In, Out;

  var label = this.name.toLowerCase()
}

enum class PortPosition {
  Left, Right, Top, Bottom;

  val label = this.name.toLowerCase()
}

class InitialPortModel constructor(): PortModel(type = "default") {

  constructor(sname: String, type: PortType, index: Int): this() {
    initAll(PortParams(sname, type.name, index.toString()))
  }

  var sname: String? = null
  var portType: PortType? = null
  var index: Int? = null
  var name: String? = null

  lateinit var unique: String
  lateinit var label: String

  fun initAll(params: PortParams) {
    sname = params.sname
    portType = PortType.valueOf(params.type)
    index = params.index.toInt()
    unique = "$sname-$index (${portType!!.label})"
    label = "$sname (${portType!!.label})"
    name = "$sname-$index (${portType!!.label})"
  }

  init {
    setMaximumLinks(1)
  }

  override fun createLinkModel(): LinkModel<out LinkModelListener>? = DefaultLinkModel()

  override fun canLinkToPort(port: InitialPortModel): Boolean {
    val curNode = getNode()
    val otherNode = port.getNode()
//    if (curNode is InitDefaultNode && otherNode is InitDefaultNode) {
//      if (curNode.function.meta.language != otherNode.function.meta.language) {
//        return false
//      }
//    }

    return portType != port.portType && sname == port.sname && curNode != otherNode
       // && getLinks().toMap().size == 1 && port.getLinks().toMap().size == 1
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  override fun serialize(): dynamic {
    val map = kotlin.js.JSON.parse<JsMap<dynamic>>(kotlin.js.JSON.stringify(super.serialize()))
    val params = PortParams(sname!!, portType!!.name, index!!.toString())
    val obj = jsObject<dynamic> {
      this.params = params
    }
    val jsParams = kotlin.js.JSON.parse<JsMap<dynamic>>(kotlin.js.JSON.stringify(obj))
    val res = lodash.merge(map, jsParams)
    return res
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  override fun deSerialize(ob: dynamic, engine: DiagramEngine) {
    super.deSerialize(ob, engine)
    val params = Json.plain.parse<PortParams>(kotlin.js.JSON.stringify(ob.params))
    initAll(params)
  }
}

@Serializable
data class PortParams(val sname: String, val type: String, val index: String)

class InitialPortModelWidget : RComponent<InitialPortModelWidget.Props, RState>() {

  override fun RBuilder.render() {

    div(
      "diagram-net__port " +
        "diagram-net__port__${props.port.portType!!.label} " +
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