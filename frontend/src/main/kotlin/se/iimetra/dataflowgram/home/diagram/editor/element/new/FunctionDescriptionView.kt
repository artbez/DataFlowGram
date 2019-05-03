package se.iimetra.dataflowgram.home.diagram.editor.element.new

import kotlinx.html.InputType
import react.*
import react.dom.*
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.toMathText
import se.iimetra.dataflowgram.dom.logIcon
import se.iimetra.dataflowgram.home.diagram.SceneTransferObject
import se.iimetra.dataflowgram.home.diagram.node.DefaultNodeFactory
import se.iimetra.dataflowgram.home.diagram.node.defaultNodeWidget
import se.iimetra.dataflowgram.wrappers.react.bootstrap.OverlayTrigger
import se.iimetra.dataflowgram.wrappers.react.bootstrap.Popover

class FunctionDescriptionView : RComponent<FunctionDescriptionView.Props, RState>() {

  interface Props : RProps {
    var function: FunctionDescription
    var sceneTransferObject: SceneTransferObject
  }

  override fun RBuilder.render() {
    div("configurer-props") {
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
                span { +param.key }
                span { +":" }
                i (classes = "params-input") { +param.value }
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
      div("configurer-props__group") {
        b { +"Drag to Scene:" }
        editorNode {
          attrs {
            label = props.function.view.id.name
            sceneTransfer = props.sceneTransferObject
            this.nodeProducer = { DefaultNodeFactory.instance.getNewInstance().initAll(props.function) }
          }
          defaultNodeWidget {
            attrs {
              this.defaultNode = DefaultNodeFactory.instance.getNewInstance().initAll(props.function)
              isView = true
            }
          }
        }
      }
    }
  }
}

fun RBuilder.functionDescriptionView(handler: RHandler<FunctionDescriptionView.Props>) =
  child(FunctionDescriptionView::class, handler)