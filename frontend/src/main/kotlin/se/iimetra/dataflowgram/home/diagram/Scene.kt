package se.iimetra.dataflowgram.home.diagram

import jquery.JQuery
import kotlinext.js.invoke
import kotlinx.html.js.onContextMenuFunction
import kotlinx.html.js.onDragOverFunction
import kotlinx.html.js.onDropFunction
import org.w3c.dom.events.Event
import org.w3c.dom.get
import react.*
import react.dom.div
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.diagramWidget
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
import kotlin.browser.document
import kotlin.browser.window

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
            props.engine.addNode(e)
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

  private fun DiagramEngine.addNode(e: Event): NodeModel? {
    val node = props.sceneTransfer.getDto()
    val point = getRelativeMousePoint(e)

    node.setPosition(point.x, point.y)

    node.addListener(
      BaseModelListener().events {
        this.selectionChanged = {
          console.log("*****!*****")
          props.updateDiagram()
        }
      }
    )

    getDiagramModel().addNode(node)
    return node
  }

  interface Props : RProps {
    var engine: DiagramEngine
    var updateDiagram: () -> Unit
    var sceneTransfer: SceneTransferObject
  }
}


fun RBuilder.scene(handler: RHandler<Scene.Props>) = child(Scene::class, handler)