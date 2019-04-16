package se.iimetra.dataflowgram.home.diagram.editor.panel

import react.*
import react.dom.*
import se.iimetra.dataflow.toMathText
import se.iimetra.dataflowgram.dom.logIcon
import se.iimetra.dataflowgram.home.diagram.node.DefaultNodeFactory
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.diagram.node.defaultNodeWidget
import se.iimetra.dataflowgram.wrappers.react.bootstrap.OverlayTrigger
import se.iimetra.dataflowgram.wrappers.react.bootstrap.Popover
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ESComponent : RComponent<ESComponent.Props, RState>() {
  override fun RBuilder.render() {
    val function = (props.state.node as InitDefaultNode).function
    div("configurer-props") {
      div("configurer-props__group") {
        b { +"Signature:" }
        span { +function.meta.signature.toMathText() }
      }
      div("configurer-props__group") {
        b { +"Node:" }
        div("node_item") {
          div("node_item__widget") {
            createWidget(props.state.node)
          }
        }
      }
      div("configurer-props__group") {
        b { +"Status:" }
        div("execution-state-element__state") {
          if (props.state.completed) +"Completed" else +"Executing..."
        }
      }
    }
    div("configurer-props__group") {
      b { +"Output:" }
      OverlayTrigger {
        attrs {
          trigger = "click"
          placement = "right"
          overlay = buildElement {
            Popover {
              attrs {
                id = "popover-select-pretrained"
                title = "Code"
              }
              div("log-box") {
                pre {
                  +function.view.content.filter { !it.isBlank() }.joinToString("\n")
                }
              }
            }
          }!!
        }
        button(classes = "log-section") {
          logIcon("log-section__icon") { }
        }
      }
    }
  }

  interface Props : RProps {
    var state: ExecutionState
  }

  private fun RBuilder.createWidget(nodeModel: NodeModel) {
    defaultNodeWidget {
      attrs {
        this.defaultNode = DefaultNodeFactory.instance.getNewInstance((props.state.node as InitDefaultNode).function)
        isView = true
      }
    }
  }
}

fun RBuilder.executionState(handler: RHandler<ESComponent.Props>) = child(ESComponent::class, handler)