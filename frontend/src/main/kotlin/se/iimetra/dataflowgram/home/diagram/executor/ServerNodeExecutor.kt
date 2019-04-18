package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.eventController
import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.home.serverEventController
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

class ServerNodeExecutor(node: NodeModel, var panel: DiagramExecutionPanel) : AbstractNodeExecutor(node) {

  private val valueHolders = node.inPorts().sortedBy { it.index }.map { it.getID() to ValueHolderPort<String?>(this) }.toMap()
  private var outData: ValueHolderPort<String?>? = null

  @UseExperimental(ImplicitReflectionSerializer::class)
  override suspend fun execute() {
    val defNode = node as InitDefaultNode
    if (panel.canProcess) {
      panel.startNode(node)
      val panelId = panel.panelId
      val executionIndex = panel.executionList.size
      serverEventController.addListener({ it.executionPanelId == panelId && it.blockIndex == executionIndex }) { response ->
        if (response.msg != null) {
          panel.update(node, response.msg)
        } else {
          GlobalScope.launch {
            panel.stopNode(node)
            outData?.setValue(response.ref)
          }
        }
      }
      eventController.pushServerEvent(
        defNode.function,
        valueHolders.values.map { it.getValue()!! },
        panelId,
        executionIndex
      )
    }
  }

  override fun getPortById(portId: String): ValueHolderPort<String?> {
    return valueHolders[portId]!!
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData = targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>()
  }
}