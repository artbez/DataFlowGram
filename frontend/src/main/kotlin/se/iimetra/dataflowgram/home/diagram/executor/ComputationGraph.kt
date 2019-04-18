package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.inPort
import se.iimetra.dataflowgram.home.outLinks
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ComputationGraph(val nodes: List<NodeModel>, panel: DiagramExecutionPanel) {

  val executors: List<AbstractNodeExecutor>

  init {
    val executorMap = nodes
      .map { it.getID() to if ((it as InitDefaultNode).function.meta.language == "js") ClientNodeExecutor(it, panel) else ServerNodeExecutor(it, panel) }
      .toMap()

    nodes.forEach { sourceNode ->
      sourceNode.outLinks().forEach {
        val targetPort = it.inPort()
        val targetExecutor = executorMap[targetPort.getNode().getID()]
          ?: throw IllegalStateException("Node must have executor")
        val sourceExecutor = executorMap[sourceNode.getID()]!!
        sourceExecutor.attachPort(targetPort, targetExecutor)
      }
    }
    executors = executorMap.values.filter { it.withoutInput }.toList()
  }

  suspend fun execute(callback: () -> Unit) {
    try {
      executors.map {
        GlobalScope.launch { it.tryExecute() }
      }.forEach { it.join() }
    } finally {
      callback()
    }
  }
}