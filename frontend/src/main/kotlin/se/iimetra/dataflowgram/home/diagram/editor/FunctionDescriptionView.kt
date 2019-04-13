package se.iimetra.dataflowgram.home.diagram.editor

import kotlinext.js.js
import kotlinx.html.id
import react.*
import react.dom.*
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.toMathText
import se.iimetra.dataflowgram.dom.logIcon
import se.iimetra.dataflowgram.wrappers.react.bootstrap.OverlayTrigger
import se.iimetra.dataflowgram.wrappers.react.bootstrap.Popover
import se.iimetra.dataflowgram.wrappers.svgtext.SvgText
import kotlin.browser.document
import kotlin.js.json
import kotlin.reflect.KClass

class FunctionDescriptionView : RComponent<FunctionDescriptionView.Props, RState>() {

  interface Props : RProps {
    var function: FunctionDescription
  }

  override fun componentDidMount() {
    val t = SvgText.default
    val el = document.getElementById("tmp_id")!!
    val ops = json(
      "text" to "| ${props.function.view.id.name} |",
      "rect" to json("fill" to "#FFF", "class" to "my-rect"),
      "element" to el
    )
    val s = js("new t(ops)")
    el.asDynamic().style.width = s.bounds.width
    el.asDynamic().style.height = s.bounds.height
  }

  override fun componentDidUpdate(prevProps: Props, prevState: RState) {
    val t = SvgText.default
    val el = document.getElementById("tmp_id")!!
    val ops = json(
      "text" to "| ${props.function.view.id.name} |",
      "rect" to json("fill" to "#FFF", "class" to "my-rect"),
      "element" to el
    )
    val s = js("new t(ops)")
    el.asDynamic().style.width = s.bounds.width
    el.asDynamic().style.height = s.bounds.height
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
      props.function.meta.params?.let { params ->
        div("configurer-props__group") {
          b { +"Params:" }
          span { +params }
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
      div("configurer-props__group") {
        b { +"Drag to Scene:" }
        svg { attrs.id = "tmp_id" }
      }
    }
  }
}

fun RBuilder.functionDescriptionView(handler: RHandler<FunctionDescriptionView.Props>)
    = child(FunctionDescriptionView::class, handler)