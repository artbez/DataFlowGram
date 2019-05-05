package se.iimetra.dataflowgram.home.diagram.node

import kotlinext.js.invoke
import kotlinext.js.jsObject
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import kotlinx.serialization.stringify
import react.*
import react.dom.div
import react.dom.span
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflowgram.home.diagram.node.ports.*
import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.home.outPorts
import se.iimetra.dataflowgram.utils.JsMap
import se.iimetra.dataflowgram.wrappers.lodash.lodash
import se.iimetra.dataflowgram.wrappers.react.diagrams.AbstractNodeFactory
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import kotlin.js.JSON as KJSON
var nodeId = 0

class InitDefaultNode constructor() : NodeModel("default") {

  constructor(function: FunctionDescription): this() {
    initAll(function.copy(paramValues = mutableMapOf()))
  }

  lateinit var function: FunctionDescription
  val portsList = mutableListOf<InitialPortModel>()
  var outPort: InitialPortModel? = null

  fun simpleInit(func: FunctionDescription): InitDefaultNode {
    function = func
    inPorts().forEach { portsList.add(it) }
    outPort = outPorts().let { if (it.isEmpty()) null else it[0]}
    return this
  }

  fun initAll(func: FunctionDescription): InitDefaultNode {
    function = func
    function.meta.signature.input.filter { it.isNotBlank() }.forEachIndexed { index, s ->
      val model = InitialPortModel(s, PortType.In, index)
      portsList.add(model)
      addPort(model)
    }
    if (function.meta.signature.output != "Unit" && function.meta.language != "render") {
      outPort = InitialPortModel(function.meta.signature.output, PortType.Out, -1)
      addPort(outPort!!)
    }
    return this
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  override fun serialize(): dynamic {
    val map = kotlin.js.JSON.parse<JsMap<dynamic>>(kotlin.js.JSON.stringify(super.serialize()))
    val obj = jsObject<dynamic> {
      this.mfunction = KJSON.parse<JsMap<dynamic>>(Json.stringify(function))
    }
    val jsParams = kotlin.js.JSON.parse<JsMap<dynamic>>(kotlin.js.JSON.stringify(obj))
    val res = lodash.merge(map, jsParams)
    return res
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  override fun deSerialize(ob: dynamic, engine: DiagramEngine) {
    super.deSerialize(ob, engine)
    val function = Json.plain.parse<FunctionDescription>(kotlin.js.JSON.stringify(ob.mfunction))
    simpleInit(function)
  }
}

class DefNodeWidget : RComponent<DefNodeWidget.Props, RState>() {

  companion object {
    init {
      kotlinext.js.require("styles/custom-nodes.scss")
    }
  }

  override fun RBuilder.render() {
    val add_class = props.defaultNode.function.meta.language
    div("diagram-net__node_$add_class") {
      span("diagram-net__node__span") { +props.defaultNode.function.meta.label }
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

  override fun getNewInstance(): InitDefaultNode {
    return InitDefaultNode()
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
