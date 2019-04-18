package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
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
      panel.startNode(node)
      val result = clientEventController.pushEvent(defNode.function, valueHolders.values.map { it.getValue()!! })
      GlobalScope.launch {
        panel.stopNode(node)
        outData?.setValue(result)
      }
    }
  }

  override fun getPortById(portId: String): ValueHolderPort<String?> {
    return valueHolders[portId]!!
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData = targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>()
  }
}