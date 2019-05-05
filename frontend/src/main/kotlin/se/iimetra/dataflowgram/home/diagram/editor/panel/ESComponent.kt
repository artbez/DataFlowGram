package se.iimetra.dataflowgram.home.diagram.editor.panel

import react.*
import react.dom.*
import se.iimetra.dataflow.toMathText
import se.iimetra.dataflowgram.dom.logIcon
import se.iimetra.dataflowgram.home.diagram.node.*
import se.iimetra.dataflowgram.wrappers.react.bootstrap.OverlayTrigger
import se.iimetra.dataflowgram.wrappers.react.bootstrap.Popover
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ESComponent : RComponent<ESComponent.Props, RState>() {
  override fun RBuilder.render() {
    val signature = when (props.state.node) {
      is InitDefaultNode -> (props.state.node as InitDefaultNode).function.meta.signature
      else -> throw IllegalStateException("Unsupported node type")
    }
    div("configurer-props") {
      div("configurer-props__group") {
        b { +"Signature:" }
        span { +signature.toMathText() }
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
          when {
            props.state.errors != null -> +"Rejected"
            props.state.completed -> +"Completed"
            else -> +"Executing"
          }
        }
      }
    }
    div("configurer-props__group") {
      b { +(if (props.state.cache) "Output (cached):" else "Output:") }
      OverlayTrigger {
        attrs {
          trigger = "click"
          placement = "right"
          overlay = buildElement {
            Popover {
              attrs {
                id = "popover-select-output"
                title = "Output"
              }
              if (props.state.renderPng != null) {
                img {
                  attrs {
                    src = props.state.renderPng!!
                  }
                }
              } else if (props.state.renderHtml != null) {
                iframe {
                  attrs {
                    src = props.state.renderHtml!!
                  }
                }
              } else {
                div("log-box") {
                  if (props.state.errors != null) {
                    pre("log-error") {
                      +props.state.errors!!
                    }
                  } else {
                    pre {
                      +(props.state.logs ?: emptyList()).joinToString("\n")
                    }
                  }
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
    when (nodeModel) {
      is InitDefaultNode ->
        defaultNodeWidget {
          attrs {
            this.defaultNode = DefaultNodeFactory.instance.getNewInstance().initAll(nodeModel.function)
            isView = true
          }
        }
      is ConverterNode -> { }
    }
  }
}

fun RBuilder.executionState(handler: RHandler<ESComponent.Props>) = child(ESComponent::class, handler)