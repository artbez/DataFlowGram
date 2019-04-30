package se.iimetra.dataflowgram.home.diagram.editor.panel

import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class DiagramExecutionPanel(
  var onChange: (List<ExecutionState>) -> Unit,
  val onClose: () -> Unit
) {

  companion object {
    var nextPanelId = 0L
  }

  var canProcess = true

  val panelId = nextPanelId++

  val executionList: MutableList<ExecutionState> = mutableListOf()

  fun <T : NodeModel> startNode(node: T): ExecutionState {
    val state = ExecutionState(node)
    executionList.add(state)
    onChange(executionList)
    return state
  }

  fun <T : NodeModel> stopNode(node: T, errors: String? = null) {
    val state = executionList.find { it.node == node } ?: return

    if (errors != null) {
      state.errors = errors
    }
    state.completed = true

    onChange(executionList)
  }

  fun <T : NodeModel> update(node: T, msg: String) {
    val state = executionList.find { it.node == node } ?: return
    val currentLogs = state.logs ?: emptyList()
    state.renderPng = null
    state.logs = currentLogs.plus(msg)
    onChange(executionList)
  }

  fun <T : NodeModel> render(node: T, type: String, value: String) {
    val state = executionList.find { it.node == node } ?: return
    if (type == "png") {
      state.renderPng = "imgs/$value"
      onChange(executionList)
    }
  }
}

data class ExecutionState(
  val node: NodeModel,
  var completed: Boolean = false,
  var logs: List<String>? = null,
  var errors: String? = null,
  var renderPng: String? = null
)