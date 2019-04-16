package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.neighbors
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ExecutionService {
  fun cached(engine: DiagramEngine, node: NodeModel) {

  }

  fun all(engine: DiagramEngine, node: NodeModel) {
    val nodes = node.selectAllNodes()
    val cgraph = ComputationGraph(nodes, DiagramExecutionPanel {})
    GlobalScope.launch { cgraph.execute {  } }
  }

  private fun NodeModel.selectAllNodes(): List<NodeModel> {
    val nearNodes = neighbors()
      .filterNot { it.isSelected() }
      .map { it.also { it.setSelected(true) } }

    return nearNodes.plus(this).plus(nearNodes.flatMap { it.selectAllNodes() }).distinctBy { it.getID() }
  }
}