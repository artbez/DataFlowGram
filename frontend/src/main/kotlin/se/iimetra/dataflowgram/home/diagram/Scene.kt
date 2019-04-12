package se.iimetra.dataflowgram.home.diagram

import kotlinext.js.invoke
import kotlinx.html.js.onContextMenuFunction
import kotlinx.html.js.onDragOverFunction
import kotlinx.html.js.onDropFunction
import react.*
import react.dom.div
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.diagramWidget

class Scene : RComponent<Scene.Props, RState>() {

  companion object {
    init {
      kotlinext.js.require("styles/scene.scss")
    }
  }

  override fun RBuilder.render() {
    div("scene") {
      div("scene__inner") {
        attrs {
          onDropFunction = { e ->
            props.updateDiagram()
          }
          onDragOverFunction = { it.preventDefault() }
          onContextMenuFunction = { it.stopPropagation(); it.preventDefault() }
        }
        diagramWidget {
          attrs {
            deleteKeys = emptyArray()
            className = "srd-demo-canvas"
            diagramEngine = props.engine
            allowLooseLinks = false
            maxNumberPointsPerLink = 0
          }
        }
      }
    }
  }

  interface Props : RProps {
    var engine: DiagramEngine
    var updateDiagram: () -> Unit
  }
}


fun RBuilder.scene(handler: RHandler<Scene.Props>) = child(Scene::class, handler)