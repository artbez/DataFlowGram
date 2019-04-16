package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.neighbors
import se.iimetra.dataflowgram.home.selectAllNodes
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ExecutionService {

  val executionMap = mutableMapOf<String, DiagramExecutionPanel>()

  fun cached(node: NodeModel) {
    val nodes = node.selectAllNodes()
    if (nodes.any { executionMap.keys.contains(it.getID()) }) {
      throw Exception("Impossible to take one node in several executions")
    }
  }

  fun all(node: NodeModel) {
    val nodes = node.selectAllNodes()
    if (nodes.any { executionMap.keys.contains(it.getID()) }) {
      throw Exception("Impossible to take one node in several executions!")
    }

    val panel = DiagramExecutionPanel({}) {
      nodes.forEach { executionMap.remove(it.getID()) }
    }
    nodes.forEach { executionMap[it.getID()] = panel }

    val cgraph = ComputationGraph(nodes, panel)
    if (cgraph.executors.isNotEmpty()) {
      GlobalScope.launch {
        cgraph.execute {}
      }
    } else {
      nodes.forEach { executionMap.remove(it.getID()) }
    }
  }
}