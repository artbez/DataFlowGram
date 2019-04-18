package se.iimetra.dataflowgram.home.diagram.editor.element.existing

import react.*
import react.dom.b
import react.dom.div
import react.dom.span
import se.iimetra.dataflow.SystemFunction
import se.iimetra.dataflow.toMathText

class SystemDiaElemView : RComponent<SystemDiaElemView.Props, RState>() {

  interface Props : RProps {
    var function: SystemFunction
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

fun RBuilder.systemDiaElemView(handler: RHandler<SystemDiaElemView.Props>)
    = child(SystemDiaElemView::class, handler)