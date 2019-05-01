package se.iimetra.dataflowgram.home.diagram.editor.element.existing

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onInputFunction
import kotlinx.html.js.onVolumeChangeFunction
import react.*
import react.dom.*
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.toMathText
import se.iimetra.dataflowgram.dom.logIcon
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.bootstrap.OverlayTrigger
import se.iimetra.dataflowgram.wrappers.react.bootstrap.Popover
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class DiaElemView(props: Props) : RComponent<DiaElemView.Props, DiaElemView.State>(props) {

  interface Props : RProps {
    var function: FunctionDescription
    var selectedNode: NodeModel
    var engine: DiagramEngine
    var updateDiagram: () -> Unit
  }

  interface State : RState {
    var paramValues: MutableMap<String, String>
  }

  override fun State.init(props: Props) {
    paramValues = props.function.paramValues
  }

  override fun RBuilder.render() {
    div("configurer-props") {
      div("configurer-props__group") {
        elementActor {
          attrs {
            selectedNode = props.selectedNode
            engine = props.engine
            updateDiagram = props.updateDiagram
          }
        }
      }
      div("configurer-props__group") {
        b { +"Function:" }
        span { +"${props.function.view.id.name}(${props.function.view.args})" }
      }
      div("configurer-props__group") {
        b { +"Signature:" }
        span { +props.function.meta.signature.toMathText() }
      }
      if (props.function.meta.paramsMap.isNotEmpty()) {
        div("configurer-props__group") {
          b { +"Params:" }
          span {
            props.function.meta.paramsMap.forEach { param ->
              div("params-pair") {
                div {
                  span { +param.key }
                  i { +" (${param.value})" }
                }
                input(classes = "params-input") {
                  attrs {
                    type = when (param.value) {
                      "string" -> InputType.text
                      "int", "float" -> InputType.number
                      else -> TODO()
                    }
                    value = props.function.paramValues[param.key] ?: if (param.value == "string") "" else "0"
                    onInputFunction = {
                      val newValue = it.target.asDynamic().value
                      print(newValue)
                      setState {
                        paramValues[param.key] = newValue
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      if (props.function.meta.description.isNotEmpty()) {
        div("configurer-props__group") {
          b { +"Description:" }
          span("descr-span") { +props.function.meta.description.joinToString("\n") }
        }
      }
      div("configurer-props__group") {
        b { +"Code:" }
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
                    +props.function.view.content.filter { !it.isBlank() }.joinToString("\n")
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
  }
}

fun RBuilder.diaElemView(handler: RHandler<DiaElemView.Props>)
    = child(DiaElemView::class, handler)