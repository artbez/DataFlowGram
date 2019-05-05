package se.iimetra.dataflowgram.home.diagram.editor.element.existing

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import se.iimetra.dataflowgram.home.executionService
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class ElementActorComponent : RComponent<ElementActorComponent.Props, ElementActorComponent.State>() {

  interface Props : RProps {
    var selectedNode: NodeModel
    var engine: DiagramEngine
    var updateDiagram: () -> Unit
  }

  override fun State.init() {
    checked = true
  }

  interface State : RState {
    var checked: Boolean
  }

  override fun RBuilder.render() {
    button(classes = "editor_button btn-success") {
      attrs.onClickFunction = { event ->
        event.stopPropagation()
        event.preventDefault()
        executionService.cached(props.selectedNode, state.checked)
        props.updateDiagram()
      }
      +"Cached"
    }
    button(classes = "editor_button btn-warning") {
      attrs.onClickFunction = { event ->
        event.stopPropagation()
        event.preventDefault()
        executionService.all(props.selectedNode, state.checked)
        props.updateDiagram()
      }
      +"Run"
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
    div("dia-mega-2") {
      input(classes = "editor_button") {
        attrs {
          type = InputType.checkBox
          onChangeFunction = {
            val newValue = it.target.asDynamic().checked
            console.log(newValue)
            setState { checked = newValue }
          }
          set("defaultChecked", state.checked.toString())
        }
      }
      span(classes = "dia-mega") { +"Full" }
    }
  }
}

fun RBuilder.elementActor(handler: RHandler<ElementActorComponent.Props>) = child(ElementActorComponent::class, handler)