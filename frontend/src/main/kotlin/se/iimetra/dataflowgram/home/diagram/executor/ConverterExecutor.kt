package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import se.iimetra.dataflowgram.home.converterController
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.ConverterNode
import se.iimetra.dataflowgram.home.eventController
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

class ConverterExecutor(node: ConverterNode, var panel: DiagramExecutionPanel) : AbstractNodeExecutor(node) {

  private val inData = ValueHolderPort<dynamic>(this)
  private var outData: ValueHolderPort<dynamic>? = null

  @UseExperimental(ImplicitReflectionSerializer::class)
  override suspend fun execute() {
    val defNode = node as ConverterNode
    val dataType = defNode.function.functionSignature.output
    if (panel.canProcess) {
      panel.startNode(node)
      val panelId = panel.panelId
      val executionIndex = panel.executionList.size
      converterController.addListener({ it.executionPanelId == panelId && it.blockIndex == executionIndex }) { response ->
        if (response.error != null) {
          GlobalScope.launch {
            panel.stopNode(node, response.error)
          }
        } else {
          val res = response.result!!.let {
            if (defNode.function.params["from"] == "client") it else
              when (dataType) {
                "String" -> it
                "Int" -> it.toInt()
                "Float" -> it.toFloat()
                "Json" -> JSON.parse(it)
                else -> throw IllegalStateException("Wrong converter type")
              }
          }
          GlobalScope.launch {
            panel.stopNode(node)
            outData?.setValue(res)
          }
        }
      }

      val strValue = if (dataType == "Json") {
        JSON.stringify(inData.getValue())
      } else {
        inData.getValue().toString()
      }

      eventController.pushConverterEvent(
        defNode.function,
        strValue,
        panelId,
        executionIndex
      )
    }
  }

  override fun getPortById(portId: String): ValueHolderPort<String?> {
    return inData
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData = targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>()
  }
}