package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.ConverterNode
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.inPort
import se.iimetra.dataflowgram.home.outLinks
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ComputationGraph(nodes: List<NodeModel>, panel: DiagramExecutionPanel) {

  val executors: List<AbstractNodeExecutor>

  init {
    val executorMap = nodes
      .map { it.getID() to createExecutor(it, panel) }
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

  private fun createExecutor(node: NodeModel, panel: DiagramExecutionPanel): AbstractNodeExecutor {
    return when (node) {
      is InitDefaultNode -> if (node.function.meta.language == "render") ClientNodeExecutor(node, panel)
                            else ServerNodeExecutor(node, panel)
      is ConverterNode -> ConverterExecutor(node, panel)
      else -> throw IllegalStateException("Wrong executor")
    }
  }
}