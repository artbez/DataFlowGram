package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.serialization.ImplicitReflectionSerializer
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.ConverterNode
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

class ConverterExecutor(node: ConverterNode, var panel: DiagramExecutionPanel) : AbstractNodeExecutor(node) {

  private val inData = ValueHolderPort<dynamic>(this)
  private var outData: ValueHolderPort<dynamic>? = null

  @UseExperimental(ImplicitReflectionSerializer::class)
  override suspend fun execute() {
  }

  override fun getPortById(portId: String): ValueHolderPort<String?> {
    return inData
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData = targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>()
  }
}