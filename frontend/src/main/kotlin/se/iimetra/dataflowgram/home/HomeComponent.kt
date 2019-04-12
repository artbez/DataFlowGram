package se.iimetra.dataflowgram.home

import kotlinext.js.invoke
import react.*
import react.dom.div
import se.iimetra.dataflowgram.header.header
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine

class HomeComponent : RComponent<RProps, RState>() {

  private lateinit var engine: DiagramEngine

  companion object {
    init {
      kotlinext.js.require("styles/home.scss")
    }
  }

  override fun componentWillMount() {
    engine = DiagramEngine()
  }

  override fun RBuilder.render() {
    header
    div("row home-all") {
      div("col-md-2") {
      }
      div("main-scene col-md-7") {
      }
      div("col-md-3") {
      }
    }
  }
}