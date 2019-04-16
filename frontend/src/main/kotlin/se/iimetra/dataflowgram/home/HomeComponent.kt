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
import se.iimetra.dataflowgram.home.diagram.SceneTransferObject
import se.iimetra.dataflowgram.home.diagram.editor.editor
import se.iimetra.dataflowgram.home.diagram.palette.palette
import se.iimetra.dataflowgram.home.diagram.scene
import se.iimetra.dataflowgram.utils.get
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine

class HomeComponent : RComponent<RProps, RState>() {

  private lateinit var engine: DiagramEngine
  private lateinit var sceneTransferObject: SceneTransferObject

  companion object {
    init {
      kotlinext.js.require("styles/home.scss")
    }
  }

  override fun componentWillMount() {
    sceneTransferObject = SceneTransferObject()
    engine = DiagramEngine().setup()
  }

  override fun RBuilder.render() {
    header
    div("row home-all") {
      div("col-md-2") {
        editor {
          attrs {
            engine = this@HomeComponent.engine
            sceneTransfer = sceneTransferObject
            updateDiagram = { forceUpdate {} }
          }
        }
      }
      div("main-scene col-md-7") {
        scene {
          attrs {
            engine = this@HomeComponent.engine
            sceneTransfer = sceneTransferObject
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
  }
}