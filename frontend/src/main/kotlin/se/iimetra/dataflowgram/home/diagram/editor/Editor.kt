package se.iimetra.dataflowgram.home.diagram.editor

import kotlinext.js.invoke
import react.*
import react.dom.div
import react.dom.h4
import react.dom.hr
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.SystemFunction
import se.iimetra.dataflowgram.home.diagram.SceneTransferObject
import se.iimetra.dataflowgram.home.diagram.editor.element.existing.diaElemView
import se.iimetra.dataflowgram.home.diagram.editor.element.existing.systemDiaElemView
import se.iimetra.dataflowgram.home.diagram.editor.element.new.functionDescriptionView
import se.iimetra.dataflowgram.home.diagram.editor.element.new.systemDescriptionView
import se.iimetra.dataflowgram.home.diagram.editor.panel.DiagramExecutionPanel
import se.iimetra.dataflowgram.home.diagram.editor.panel.diagramPanel
import se.iimetra.dataflowgram.home.diagram.node.ConverterNode
import se.iimetra.dataflowgram.home.diagram.node.InitDefaultNode
import se.iimetra.dataflowgram.home.executionService
import se.iimetra.dataflowgram.home.paletteDefaultChoseController
import se.iimetra.dataflowgram.home.paletteSystemChoseController
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
    val execMap = executionService.executionMap
    if (selectedNodes.isNotEmpty()) {
      val elem = selectedNodes[0]
      setState {
        defaultPaletteChosen = null
        selectedNode = elem
        executionPanel = execMap[elem.getID()]
        systemPaletteChosen = null
      }
    } else {
      setState {
        selectedNode = null
        executionPanel = execMap.values.firstOrNull()
      }
    }
  }

  override fun Editor.State.init() {
    defaultPaletteChosen = null
    selectedNode = null
    systemPaletteChosen = null
  }

  override fun componentDidMount() {
    paletteDefaultChoseController.addListener {
      setState {
        executionPanel = null
        selectedNode = null
        systemPaletteChosen = null
        defaultPaletteChosen = it
      }
    }
    paletteSystemChoseController.addListener {
      setState {
        executionPanel = null
        selectedNode = null
        systemPaletteChosen = it
        defaultPaletteChosen = null
      }
    }
  }

  interface State : RState {
    var defaultPaletteChosen: FunctionDescription?
    var systemPaletteChosen: SystemFunction?
    var selectedNode: NodeModel?
    var executionPanel: DiagramExecutionPanel?
  }

  override fun RBuilder.render() {
    div("home-left") {
      h4("home-left__title") {
        if (state.executionPanel != null) +"ExecutionPanel" else +"Configurer"
      }
      hr("home-left__line") { }
      if (state.defaultPaletteChosen != null) {
        functionDescriptionView {
          attrs {
            function = state.defaultPaletteChosen!!
            sceneTransferObject = props.sceneTransfer
          }
        }
      } else if (state.systemPaletteChosen != null) {
        systemDescriptionView {
          attrs {
            function = state.systemPaletteChosen!!
            sceneTransferObject = props.sceneTransfer
          }
        }
      } else if (state.executionPanel != null) {
        diagramPanel {
          attrs {
            updateDiagram = props.updateDiagram
            diagramExecutionPanel = state.executionPanel!!
          }
        }
      } else if (state.selectedNode != null) {
        when (state.selectedNode) {
          is InitDefaultNode -> diaElemView {
            attrs {
              function = (state.selectedNode as InitDefaultNode).function
              selectedNode = state.selectedNode!!
              updateDiagram = props.updateDiagram
              engine = props.engine
            }
          }
          else -> throw IllegalStateException("Impossible")
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