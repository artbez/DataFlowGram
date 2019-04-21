package se.iimetra.dataflowgram.home.diagram.editor.element.existing

import kotlinx.html.js.onClickFunction
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

class DiaElemView : RComponent<DiaElemView.Props, RState>() {

  interface Props : RProps {
    var function: FunctionDescription
    var selectedNode: NodeModel
    var engine: DiagramEngine
    var updateDiagram: () -> Unit
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
      props.function.meta.paramsMap.forEach { (key, value) ->
        div("configurer-props__group") {
          b { +"Param@ $key : $value" }
          // TODO
          input { }
        }
      }
      props.function.meta.description?.let { desc ->
        div("configurer-props__group") {
          b { +"Description:" }
          span { +desc }
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