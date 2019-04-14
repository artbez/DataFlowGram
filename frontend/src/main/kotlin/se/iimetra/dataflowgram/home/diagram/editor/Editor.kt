package se.iimetra.dataflowgram.home.diagram.editor

import kotlinext.js.invoke
import org.w3c.dom.Node
import react.*
import react.dom.div
import react.dom.h4
import react.dom.hr
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflowgram.home.diagram.SceneTransferObject
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.paletteChoseController
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel

class Editor : RComponent<Editor.Props, Editor.State>() {

  companion object {
    init {
      kotlinext.js.require("styles/configurer.scss")
    }
  }

  override fun componentWillReceiveProps(nextProps: Props) {
    val selectedNodes = nextProps.engine.getDiagramModel().getNodes().toMap().values.filter { it.isSelected() }
    if (selectedNodes.isNotEmpty()) {
      val elem = selectedNodes[0]
      setState {
        paletteChosen = null
        selectedNode = elem
      }
    } else {
      setState { selectedNode = null }
    }
  }

  override fun Editor.State.init() {
    paletteChosen = null
    selectedNode = null
  }

  override fun componentDidMount() {
    paletteChoseController.addListener {
      setState {
        selectedNode = null
        paletteChosen = it
      }
    }
  }

  interface State : RState {
    var paletteChosen: FunctionDescription?
    var selectedNode: NodeModel?
  }

  override fun RBuilder.render() {
    div("home-left") {
      h4("home-left__title") {
        +"Configurer"
      }
      hr("home-left__line") { }
      if (state.paletteChosen != null) {
        functionDescriptionView {
          attrs {
            function = state.paletteChosen!!
            sceneTransferObject = props.sceneTransfer
          }
        }
      } else if (state.selectedNode != null) {
        diaElemView {
          attrs {
            function = (state.selectedNode as InitDefaultNode).function
            selectedNode = state.selectedNode!!
            updateDiagram = props.updateDiagram
            engine = props.engine
          }
        }
      }
    }
  }

  interface Props : RProps {
    var sceneTransfer: SceneTransferObject
    var engine: DiagramEngine
    var updateDiagram: () -> Unit
  }
}

fun RBuilder.editor(handler: RHandler<Editor.Props>) = child(Editor::class, handler)