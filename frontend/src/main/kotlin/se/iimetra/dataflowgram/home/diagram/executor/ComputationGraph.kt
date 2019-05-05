package se.iimetra.dataflowgram.home.diagram.executor

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.node.ConverterNode
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.inPort
import se.iimetra.dataflowgram.home.inPorts
import se.iimetra.dataflowgram.home.outLinks
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ComputationGraph(allExecutors: MutableMap<String, AbstractNodeExecutor>, nodes: List<NodeModel>, panel: DiagramExecutionPanel, cache: Boolean = false) {

  val executors: List<AbstractNodeExecutor>
  private val oldExecutors: Map<String, AbstractNodeExecutor?>

  init {
    oldExecutors = nodes.map { it.getID() }.map { it to allExecutors[it] }.toMap()
    oldExecutors.values.forEach { it?.updateValueHolders(cache) }
    val executorMap = nodes
      .map { it.getID() to (oldExecutors[it.getID()] ?: createExecutor(it, panel)) }
      .toMap()

    executorMap.values.forEach { it.panel = panel }
    allExecutors.putAll(executorMap)

    nodes.forEach { sourceNode ->
      sourceNode.outLinks().forEach {
        val targetPort = it.inPort()
        val targetExecutor = executorMap[targetPort.getNode().getID()]
          //?: throw IllegalStateException("Node must have executor")
        val sourceExecutor = executorMap[sourceNode.getID()]!!
        targetExecutor?.valueHolders?.forEach { console.log(it.key); console.log(it.value.getValue()) }
        targetExecutor?.let {
          console.log("Here")
          console.log(targetExecutor)
          sourceExecutor.attachPort(targetPort, it)
        }
        targetExecutor?.valueHolders?.forEach { console.log(it.key); console.log(it.value.getValue()) }
      }
    }
    executors = executorMap.values.filter { it.withoutInput }.toList()
  }

  suspend fun execute(node: NodeModel, clear: Boolean = true, callback: () -> Unit) {
    try {
      oldExecutors.values.filter { it?.node?.getID() == node.getID() }.forEach {
        it?.clear()
      }
      if (clear) {
        oldExecutors.values.forEach { it?.clear() }
      }
      executors.map {
        console.log("Executing $it")
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