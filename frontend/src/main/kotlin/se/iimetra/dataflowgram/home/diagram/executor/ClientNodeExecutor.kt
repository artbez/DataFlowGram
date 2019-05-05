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

class ClientNodeExecutor(node: NodeModel, panel: DiagramExecutionPanel) : AbstractNodeExecutor(node, panel) {
  override fun clearOutData() {
    outData = null
  }

  private var executed = false
  private var lastError: String? = null

  override suspend fun innerClear() {
    dataValue?.let { name ->
      GlobalScope.launch {
        get("/api/dactions/delete_res?name=$name")
      }
    }
    lastError = null
    executed = false
    outData = null
  }
   private var outData: ValueHolderPort<dynamic>? = null

  @UseExperimental(ImplicitReflectionSerializer::class)
  override suspend fun execute(cache: Boolean) {
    if (executed) {
      if (panel.canProcess) {
        panel.startNode(node, true)
        if (lastError != null) {
          panel.stopNode(node, lastError)
        } else {
          panel.render(node, (node as InitDefaultNode).function.meta.signature.output, dataValue!!)
          panel.stopNode(node)
          dataValue = outData?.getValue()
          outData?.setValue(dataValue)
        }
        executed = true
      }
      outData?.setValue(outData?.getValue())
    } else {
      val defNode = node as InitDefaultNode
      if (panel.canProcess) {
        panel.startNode(node)
        val panelId = panel.panelId
        val executionIndex = panel.executionList.size
        serverEventController.addListener({ it.executionPanelId == panelId && it.blockIndex == executionIndex }) { response ->
          when {
            response.error != null -> GlobalScope.launch {
              lastError = response.error
              panel.stopNode(node, response.error)
            }

            response.msg != null -> {
            }

            else -> GlobalScope.launch {
              panel.render(node, "png", response.ref!!)
              panel.stopNode(node)
              dataValue = response.ref
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
        executed = true
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