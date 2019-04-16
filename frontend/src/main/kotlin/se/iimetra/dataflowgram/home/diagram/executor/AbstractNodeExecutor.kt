package se.iimetra.dataflowgram.home.diagram.executor

import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.PortModel

abstract class AbstractNodeExecutor(val node: NodeModel) {

  val withoutInput = node.inPorts().isEmpty()

  private var dataPoints = node.inPorts().size
  protected abstract suspend fun execute()

  abstract fun getPortById(portId: String): ValueHolderPort<String?>

  abstract fun attachPort(port: PortModel, targetExecutor: AbstractNodeExecutor)

  suspend fun tryExecute() {
    if (dataPoints == 0) {
      execute()
    }
  }

  suspend fun dataReceived() {
    if (dataPoints == 0) {
      throw IllegalStateException("All data already received")
    }
    dataPoints--
    tryExecute()
  }
}
