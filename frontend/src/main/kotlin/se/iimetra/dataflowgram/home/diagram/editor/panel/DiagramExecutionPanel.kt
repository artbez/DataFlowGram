package se.iimetra.dataflowgram.home.diagram.editor.panel

import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class DiagramExecutionPanel(val onChange: (List<ExecutionState>) -> Unit) {

  private val executionList: MutableList<ExecutionState> = mutableListOf()

  fun <T : NodeModel> startNode(node: T) {
    executionList.add(ExecutionState(node))
    onChange(executionList)
  }

  fun <T : NodeModel> stopNode(node: T) {
    val state = executionList.find { it.node == node } ?: return
    state.completed = true
    onChange(executionList)
  }

  fun <T : NodeModel> update(node: T, msg: String) {
    val state = executionList.find { it.node == node } ?: return
    val currentLogs = state.logs ?: emptyList()
    state.logs = currentLogs.plus(msg)
    onChange(executionList)
  }
}

class ExecutionState(
  val node: NodeModel,
  var completed: Boolean = false,
  var logs: List<String>? = null
)