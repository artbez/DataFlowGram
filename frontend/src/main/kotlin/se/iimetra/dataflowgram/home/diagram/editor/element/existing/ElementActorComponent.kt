package se.iimetra.dataflowgram.home.diagram.editor.element.existing

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import se.iimetra.dataflowgram.home.executionService
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ElementActorComponent : RComponent<ElementActorComponent.Props, RState>() {

  interface Props : RProps {
    var selectedNode: NodeModel
    var engine: DiagramEngine
    var updateDiagram: () -> Unit
  }

  override fun RBuilder.render() {
      button(classes = "editor_button btn-success") {
        attrs.onClickFunction = { event ->
          event.stopPropagation()
          event.preventDefault()
          executionService.cached(props.selectedNode)
          props.updateDiagram()
        }
        +"Cached"
      }
      button(classes = "editor_button btn-warning") {
        attrs.onClickFunction = { event ->
          event.stopPropagation()
          event.preventDefault()
          executionService.all(props.selectedNode)
          props.updateDiagram()
        }
        +"Full"
      }
      button(classes = "editor_button btn-danger") {
        attrs.onClickFunction = { event ->
          event.stopPropagation()
          event.preventDefault()
          props.selectedNode.getPorts().toMap().forEach { it ->
            it.value.getLinks().toMap().forEach {
              val source = it.value.getSourcePort()
              val target = it.value.getTargetPort()
              source.removeLink(it.value)
              target.removeLink(it.value)
              props.engine.getDiagramModel().removeLink(it.value)
            }
          }
          props.engine.getDiagramModel().removeNode(props.selectedNode)
          props.updateDiagram()
        }
        +"Remove"
      }
  }
}

fun RBuilder.elementActor(handler: RHandler<ElementActorComponent.Props>) = child(ElementActorComponent::class, handler)