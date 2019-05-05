package se.iimetra.dataflowgram.home.diagram.executor

import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

abstract class AbstractNodeExecutor(val node: NodeModel, var panel: DiagramExecutionPanel) {

  val withoutInput = node.inPorts().isEmpty()

  var dataPoints = node.inPorts().size

  var dataValue: String? = null

  var valueHolders =
    node.inPorts().sortedBy { it.index }.map { it.getID() to ValueHolderPort<dynamic>(this) }.toMap()

  fun updateValueHolders(cache: Boolean) {
    valueHolders = node.inPorts().sortedBy { it.index }.map { it.getID() to ValueHolderPort<String?>(this) }.toMap()
    dataPoints = node.inPorts().size
    valueHolders.forEach { console.log(it.key); console.log(it.value.getValue()) }
    clearOutData()
  }

  protected abstract fun clearOutData()

  protected abstract suspend fun execute(cache: Boolean = false)

  abstract fun getPortById(portId: String): ValueHolderPort<dynamic>

  abstract fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor)

  abstract suspend fun innerClear()

  suspend fun clear() {
    innerClear()
  }

  suspend fun tryExecute() {
    if (dataPoints == 0) {
      execute()
    }
  }

  suspend fun dataReceived() {
    valueHolders.forEach { console.log(it.key); console.log(it.value.getValue()) }
    if (dataPoints == 0) {
      throw IllegalStateException("All data already received")
    }
    dataPoints--
    tryExecute()
  }
}
