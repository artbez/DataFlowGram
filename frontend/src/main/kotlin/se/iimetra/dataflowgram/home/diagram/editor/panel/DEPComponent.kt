package se.iimetra.dataflowgram.home.diagram.editor.panel

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div
import react.dom.hr

class DEPComponent(override var props: Props) : RComponent<DEPComponent.Props, DEPComponent.State>(props) {

  override fun State.init() {
    nodes = emptyList()
    done = false
  }

  override fun State.init(props: Props) {
    nodes = ArrayList(props.diagramExecutionPanel.executionList)
    done = props.diagramExecutionPanel.executionList.all { it.completed }
  }

  override fun componentDidMount() {
    props.diagramExecutionPanel.onChange = { updatePanel(it) }
  }

  override fun componentWillReceiveProps(nextProps: Props) {
    setState {
      nodes = ArrayList(nextProps.diagramExecutionPanel.executionList)
      done = nextProps.diagramExecutionPanel.executionList.all { it.completed }
    }
  }

  private fun updatePanel(nodes: List<ExecutionState>) {
    if (nodes.all { it.completed }) {
      setState {
        done = true
        this.nodes = nodes
      }
    } else {
      setState {
        this.nodes = nodes
      }
    }
  }

  override fun RBuilder.render() {
    div("execution-panel-block") {
      state.nodes.forEach {
        executionState {
          attrs {
            state = it
          }
        }
        hr("palette__block__line") { }
      }
      if (state.done) {
        button(classes = "editor_button btn-primary") {
          attrs {
            onClickFunction = {
              it.preventDefault()
              it.stopPropagation()
              props.diagramExecutionPanel.onClose()
              props.updateDiagram()
            }
          }
          +"Ok"
        }
      } else {
        button(classes = "editor_button btn-danger") {
          attrs {
            onClickFunction = {
              it.preventDefault()
              it.stopPropagation()
              props.diagramExecutionPanel.canProcess = false
              props.diagramExecutionPanel.onClose()
              props.updateDiagram()
            }
          }
          +"Cancel"
        }
      }
    }
  }

  interface Props : RProps {
    var diagramExecutionPanel: DiagramExecutionPanel
    var updateDiagram: () -> Unit
  }

  interface State : RState {
    var nodes: List<ExecutionState>
    var done: Boolean
  }
}

fun RBuilder.diagramPanel(handler: RHandler<DEPComponent.Props>) = child(DEPComponent::class, handler)