package se.iimetra.dataflowgram.home.diagram.executor

import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

class DefaultNodeExecutor(node: NodeModel, var panel: DiagramExecutionPanel) : AbstractNodeExecutor(node) {

  private val valueHolders = node.inPorts().map { it.getID() to ValueHolderPort<String?>(this) }.toMap()
  private var outData: ValueHolderPort<String?>? = null

  override suspend fun execute() {
    panel.startNode(node)
    console.log("I am executing!!! " + (node as InitDefaultNode).function.view.id.name)
    panel.stopNode(node)
    outData?.setValue("hoho")
  }

  override fun getPortById(portId: String): ValueHolderPort<String?> {
    return valueHolders[portId]!!
  }

  override fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor) {
    outData = targetExecutor.getPortById(port.getID()).unsafeCast<ValueHolderPort<String?>>()
  }
}