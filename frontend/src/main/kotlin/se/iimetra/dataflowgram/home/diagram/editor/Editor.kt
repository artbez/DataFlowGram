package se.iimetra.dataflowgram.home.diagram.editor

import kotlinext.js.invoke
import react.*
import react.dom.div
import react.dom.h4
import react.dom.hr
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflowgram.home.paletteChoseController

class Editor : RComponent<RProps, Editor.State>() {

  companion object {
    init {
      kotlinext.js.require("styles/configurer.scss")
    }
  }

  override fun Editor.State.init() {
    paletteChosen = null
  }

  override fun componentDidMount() {
    paletteChoseController.addListener {
      setState { paletteChosen = it }
    }
  }

  interface State: RState {
    var paletteChosen: FunctionDescription?
  }

  override fun RBuilder.render() {
    div("home-left") {
      h4("home-left__title") {
       +"Configurer"
      }
      hr("home-left__line") { }
      if (state.paletteChosen != null) {
        functionDescriptionView { attrs.function = state.paletteChosen!! }
      }
    }
  }
}

fun RBuilder.editor(handler: RHandler<RProps>) = child(Editor::class, handler)