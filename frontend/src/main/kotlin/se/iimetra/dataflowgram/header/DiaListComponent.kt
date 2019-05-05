package se.iimetra.dataflowgram.header

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import react.*
import react.dom.button
import react.dom.div
import react.dom.span
import se.iimetra.dataflow.AllDiagrams
import se.iimetra.dataflow.DiagramDto
import se.iimetra.dataflowgram.home.diagram.palette.ViewCell
import se.iimetra.dataflowgram.utils.get
import se.iimetra.dataflowgram.wrappers.react.devextreme.TreeView

class DiaListComponent : RComponent<DiaListComponent.Props, DiaListComponent.State>() {

  var choosenId:String? = null

  override fun State.init() {
    diaList = emptyList()
  }

  override fun componentDidMount() {
    GlobalScope.launch {
      val diagrams = Json.plain.parse(AllDiagrams.serializer(), get("/api/diagram/all"))
      setState { diaList = diagrams.diagrams }
    }
  }

  @UseExperimental(ImplicitReflectionSerializer::class)
  override fun RBuilder.render() {
    if (state.diaList.isEmpty()) {
      span { +"No saved diagrams" }
    } else {
      val content = state.diaList.map { ViewCell(it, it, false) }
      TreeView {
        attrs {
          id = "treeview"
          items = JSON.parse(Json.plain.stringify(content))
          searchEnabled = true
          onItemClick = {
            val chose = it.itemData.id
            choosenId = chose
          }
        }
      }
      div("btn-open-menu") {
        button(classes = "editor_button btn-success") {
          attrs {
            onClickFunction = {
              if (choosenId != null) {
                GlobalScope.launch {
                  val dia = get("/api/diagram/get?name=$choosenId")
                  val diaDto = Json.plain.parse(DiagramDto.serializer(), dia)
                  props.onDiagramUpload(diaDto)
                }
              }
            }
          }
          +"Open"
        }
        button(classes = "editor_button btn-danger") {
          attrs {
            onClickFunction = {
              if (choosenId != null && choosenId != props.currentId) {
                GlobalScope.launch {
                  get("/api/diagram/delete?name=$choosenId")
                  GlobalScope.launch {
                    val diagrams = Json.plain.parse(AllDiagrams.serializer(), get("/api/diagram/all"))
                    setState { diaList = diagrams.diagrams }
                  }
                }
              }
            }
          }
          +"Delete"
        }
      }
    }
  }

  interface State : RState {
    var diaList: List<String>
  }

  interface Props: RProps {
    var onDiagramUpload: (DiagramDto) -> Unit
    var currentId: String?
  }
}

fun RBuilder.diaListComponent(handler: RHandler<DiaListComponent.Props>) = child(DiaListComponent::class, handler)