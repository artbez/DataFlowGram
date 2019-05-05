package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.serialization.ImplicitReflectionSerializer
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.ConverterNode
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

class ConverterExecutor(node: ConverterNode, panel: DiagramExecutionPanel) : AbstractNodeExecutor(node, panel) {
  override fun clearOutData() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override suspend fun innerClear() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private val inData = ValueHolderPort<dynamic>(this)
  private var outData: ValueHolderPort<dynamic>? = null

  @UseExperimental(ImplicitReflectionSerializer::class)
  override suspend fun execute(cache: Boolean) {
  }

  override fun getPortById(portId: String): ValueHolderPort<String?> {
    return inData
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData = targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>()
  }
}