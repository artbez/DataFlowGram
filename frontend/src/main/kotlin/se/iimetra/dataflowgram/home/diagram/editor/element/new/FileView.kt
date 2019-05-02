package se.iimetra.dataflowgram.home.diagram.editor.element.new

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import se.iimetra.dataflowgram.home.paletteFileChoseController
import se.iimetra.dataflowgram.utils.get
import se.iimetra.dataflowgram.utils.request
import kotlin.js.json

class FileView : RComponent<FileView.Props, RState>() {
  override fun RBuilder.render() {
    div("configurer-props") {
      div("configurer-props__group") {
        b { +"File name:" }
        span { +props.filename }
      }
      div("configurer-props__group") {
        a {
          attrs {
            href = "/api/file/download?fileName=${props.filename}"
            set("download", props.filename)
          }
          button(classes = "editor_button btn-primary") {
            +"Download"
          }
        }
        button(classes = "editor_button btn-danger") {
          attrs.onClickFunction = { event ->
            event.stopPropagation()
            event.preventDefault()
            GlobalScope.launch {
              get("/api/file/delete?fileName=${props.filename}")
              paletteFileChoseController.newChoose(null)
            }
          }
          +"Remove"
        }
      }
    }
  }

  interface Props : RProps {
    var filename: String
  }
}

fun RBuilder.fileView(handler: RHandler<FileView.Props>) = child(FileView::class, handler)