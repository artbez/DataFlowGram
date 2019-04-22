package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import react.buildElement
import se.iimetra.dataflowgram.home.clientEventController
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.eventController
import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.home.serverEventController
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

class ClientNodeExecutor(node: NodeModel, var panel: DiagramExecutionPanel) : AbstractNodeExecutor(node) {

  private val valueHolders =
    node.inPorts().sortedBy { it.index }.map { it.getID() to ValueHolderPort<dynamic>(this) }.toMap()
  private var outData: ValueHolderPort<dynamic>? = null

  @UseExperimental(ImplicitReflectionSerializer::class)
  override suspend fun execute() {
    val defNode = node as InitDefaultNode
    if (panel.canProcess) {
      val state = panel.startNode(node)
      try {
        val result =
          clientEventController.pushEvent(defNode.function, valueHolders.values.map { it.getValue()!! }, state)
        if (result != null) {
          console.log(result.size)
          when {
            result.size == 1 && node.outPort != null -> GlobalScope.launch {
              panel.stopNode(node)
              outData!!.setValue(result[0])
            }
            result.size == 1 && node.outPort == null -> {
              state.component = result[0]
              GlobalScope.launch {
                panel.stopNode(node)
              }
            }
            result.size == 2 -> {
              state.component = result[1]
              GlobalScope.launch {
                panel.stopNode(node)
                outData!!.setValue(result[0])
              }
            }
            else -> throw Error("Can't parse result $result")
          }
        } else {
          GlobalScope.launch {
            panel.stopNode(node)
          }
        }
      } catch (ex: Throwable) {
        console.log(ex)
        GlobalScope.launch {
          panel.stopNode(node, ex.message)
        }
      }
    }
  }

  override fun getPortById(portId: String): ValueHolderPort<dynamic> {
    return valueHolders[portId]!!
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData = targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>()
  }
}