package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.eventController
import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.home.serverEventController
import se.iimetra.dataflowgram.utils.get
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

class ServerNodeExecutor(node: NodeModel, panel: DiagramExecutionPanel) : AbstractNodeExecutor(node, panel) {
  override fun clearOutData() {
    outData.clear()
  }

  private val messages = mutableListOf<String>()
  private var executed = false
  private var lastError: String? = null

  override suspend fun innerClear() {
    dataValue?.let { ref ->
      GlobalScope.launch {
        get("/api/dactions/delete?ref=$ref")
      }
    }
    executed = false
    lastError = null
  }

  private var outData: MutableList<ValueHolderPort<String?>> = mutableListOf()

  @UseExperimental(ImplicitReflectionSerializer::class)
  override suspend fun execute(cache: Boolean) {
    if (executed) {
      if (panel.canProcess) {
        panel.startNode(node, true)
        messages.forEach {
          panel.update(node, it)
        }
        if (lastError != null) {
          panel.stopNode(node, lastError)
        } else {
          panel.stopNode(node)
          outData.forEach {
            it.setValue(dataValue)
          }
        }
        executed = true
      }
    } else {
      val defNode = node as InitDefaultNode
      if (panel.canProcess) {
        panel.startNode(node)
        val panelId = panel.panelId
        val executionIndex = panel.executionList.size
        serverEventController.addListener({ it.executionPanelId == panelId && it.blockIndex == executionIndex }) { response ->
          when {
            response.msg != null -> {
              messages.add(response.msg)
              panel.update(node, response.msg)
            }

            response.error != null -> GlobalScope.launch {
              lastError = response.error
              panel.stopNode(node, response.error)
            }

            else -> GlobalScope.launch {
              panel.stopNode(node)
              dataValue = response.ref
              outData.forEach { it.setValue(response.ref) }
            }
          }
        }
        valueHolders.forEach { console.log(it.key); console.log(it.value.getValue()) }
        eventController.pushServerEvent(
          defNode.function,
          valueHolders.values.map { it.getValue()!! },
          panelId,
          executionIndex
        )
        executed = true
      }
    }
  }

  override fun getPortById(portId: String): ValueHolderPort<String?> {
    return valueHolders[portId]!!
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData.add(targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>())
  }
}