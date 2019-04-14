package se.iimetra.dataflowgram.home.diagram.editor

import kotlinx.html.Draggable
import kotlinx.html.draggable
import kotlinx.html.js.onDragEndFunction
import kotlinx.html.js.onDragStartFunction
import react.*
import react.dom.div
import se.iimetra.dataflowgram.home.diagram.SceneTransferObject
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class NewElementNode : RComponent<NewElementNode.Props, RState>() {

  override fun RBuilder.render() {
    div("node_item") {
      div("node_item__widget") {
        attrs {
          draggable = Draggable.htmlTrue
          onDragStartFunction = { props.sceneTransfer.putDto(props.nodeProducer()) }
          onDragEndFunction = { props.sceneTransfer.cleanDto() }
        }
        this.children()
      }
    }
  }

  interface Props : RProps {
    var label: String
    var sceneTransfer: SceneTransferObject
    var nodeProducer: () -> NodeModel
  }
}

fun RBuilder.editorNode(handler: RHandler<NewElementNode.Props>) = child(NewElementNode::class, handler)

