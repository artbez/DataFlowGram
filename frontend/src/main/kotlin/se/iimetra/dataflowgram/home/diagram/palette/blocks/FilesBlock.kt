package se.iimetra.dataflowgram.home.diagram.palette.blocks


import kotlinext.js.invoke
import kotlin.js.iterator as kiterator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import org.w3c.files.Blob
import org.w3c.xhr.FormData
import react.*
import react.dom.*
import se.iimetra.dataflowgram.home.diagram.palette.ViewCell
import se.iimetra.dataflowgram.home.paletteFileChoseController
import se.iimetra.dataflowgram.utils.fileHeaders
import se.iimetra.dataflowgram.utils.post
import se.iimetra.dataflowgram.wrappers.react.devextreme.TreeView

class FilesBlock : RComponent<FilesBlock.Props, FilesBlock.State>() {

  companion object {
    init {
      kotlinext.js.require("styles/category-block-styles.scss")
    }
  }

  override fun State.init() {
    chosen = null
  }

  @ImplicitReflectionSerializer
  override fun RBuilder.render() {
    div("palette__block") {
      h4("palette__block__title") {
        +props.name
      }

      hr("palette__block__line") {}
      TreeView {
        attrs {
          id = "treeview"
          items = JSON.parse(Json.plain.stringify(props.content))
          searchEnabled = true
          onItemClick = {
            val choosenId = it.itemData.id
            paletteFileChoseController.newChoose(choosenId)
          }
        }
      }
        input(classes = "files-btn-upload") {
          attrs {
            type = InputType.file
            name = "file"
            onChangeFunction = { event ->
              val values = event.target.asDynamic().files[0]
              setState {
                chosen = values
              }
            }
          }
        }
        input(classes = "editor_button btn-success upload_button") {
          attrs {
            type = InputType.submit
            value = "Upload"
            onClickFunction = {
              val body = state.chosen
              val formData = FormData()
              formData.set("file", body as Blob)
              val elems = formData.asDynamic().entries()
              //js("for (var key of elems) { console.log(el[0] + \", \" + el[1]) }")
              var t = elems.next()
              while (t.value != undefined) {
                console.log(t.value[0] + ", " + t.value[1])
                t = elems.next()
              }
              GlobalScope.launch {
                post("/api/fileupload", formData, fileHeaders)
              }
            }
          }
        }
      }
  }

  interface Props: RProps {
    var name: String
    var content: List<ViewCell>
  }

  interface State: RState {
    var chosen: dynamic
  }
}

fun RBuilder.filesBlock(handler: RHandler<FilesBlock.Props>) =
  child(FilesBlock::class, handler)