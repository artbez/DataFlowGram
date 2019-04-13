package se.iimetra.dataflowgram.home

import kotlinext.js.invoke
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import react.*
import react.dom.div
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflowgram.header.header
import se.iimetra.dataflowgram.home.diagram.editor.editor
import se.iimetra.dataflowgram.home.diagram.palette.palette
import se.iimetra.dataflowgram.home.diagram.scene
import se.iimetra.dataflowgram.utils.get
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine

class HomeComponent : RComponent<RProps, RState>() {

  private lateinit var engine: DiagramEngine

  companion object {
    init {
      kotlinext.js.require("styles/home.scss")
    }
  }

  override fun RState.init() {
    GlobalScope.launch {
      val config = get("/api/config/all")
      val gitConfig = Json.parse(GitContent.serializer(), config)
      console.log(gitConfig)
      println(gitConfig)
    }
  }

  override fun componentWillMount() {
    engine = DiagramEngine().setup()
  }

  override fun RBuilder.render() {
    header
    div("row home-all") {
      div("col-md-2") {
        editor {

        }
      }
      div("main-scene col-md-7") {
        scene {
          attrs {
            engine = this@HomeComponent.engine
            updateDiagram = { forceUpdate {} }
          }
        }
      }
      div("col-md-3") {
        palette {
          attrs { }
        }
      }
    }
    paletteChoseController.addListener {
      console.log("H")
      console.log(it)
    }
  }
}