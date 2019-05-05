package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.selectAll2
import se.iimetra.dataflowgram.home.selectAllNodes
import se.iimetra.dataflowgram.home.selectAllNodes2
import se.iimetra.dataflowgram.utils.get
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import kotlin.collections.MutableMap
import kotlin.collections.any
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class ExecutionService {

  val executionMap = mutableMapOf<String, DiagramExecutionPanel>()
  private val allExecutors: MutableMap<String, AbstractNodeExecutor> = mutableMapOf()

  init {
    GlobalScope.launch {
      get("/api/dactions/delete_all")
    }
  }

  fun cached(node: NodeModel, full: Boolean) {
    val nodes = if (full) node.selectAll2() else node.selectAllNodes()
    if (nodes.any { executionMap.keys.contains(it.getID()) }) {
      throw Exception("Impossible to take one node in several executions!")
    }

    val panel = DiagramExecutionPanel({}) {
      nodes.forEach { executionMap.remove(it.getID()) }
    }
    nodes.forEach { executionMap[it.getID()] = panel }

    val cgraph = ComputationGraph(allExecutors, nodes, panel, true)
    if (cgraph.executors.isNotEmpty()) {
      GlobalScope.launch {
        cgraph.execute(node, false) {}
      }
    }
  }

  fun all(node: NodeModel, full: Boolean) {
    val nodes = if (full) node.selectAll2() else node.selectAllNodes()
    if (nodes.any { executionMap.keys.contains(it.getID()) }) {
      throw Exception("Impossible to take one node in several executions!")
    }

    val panel = DiagramExecutionPanel({}) {
      nodes.forEach { executionMap.remove(it.getID()) }
    }
    nodes.forEach { executionMap[it.getID()] = panel }

    val cgraph = ComputationGraph(allExecutors, nodes, panel)
    if (cgraph.executors.isNotEmpty()) {
      GlobalScope.launch {
        cgraph.execute(node) {}
      }
    }
  }
}