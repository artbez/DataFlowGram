package se.iimetra.dataflowgram.home.diagram.editor.element.new

import react.*
import react.dom.*
import se.iimetra.dataflow.SystemFunction
import se.iimetra.dataflow.toMathText
import se.iimetra.dataflowgram.home.diagram.SceneTransferObject
import se.iimetra.dataflowgram.home.diagram.node.*

class SystemDescriptionView : RComponent<SystemDescriptionView.Props, RState>() {

  interface Props : RProps {
    var function: SystemFunction
    var sceneTransferObject: SceneTransferObject
  }

  override fun RBuilder.render() {
    div("configurer-props") {
      div("configurer-props__group") {
        b { +"Function:" }
        span { +props.function.name }
      }
      div("configurer-props__group") {
        b { +"Signature:" }
        span { +props.function.functionSignature.toMathText() }
      }
    }
  }
}

fun RBuilder.systemDescriptionView(handler: RHandler<SystemDescriptionView.Props>)
    = child(SystemDescriptionView::class, handler)