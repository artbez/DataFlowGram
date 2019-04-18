package se.iimetra.dataflowgram.home.diagram.palette.blocks


import kotlinext.js.invoke
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import react.*
import react.dom.div
import react.dom.h4
import react.dom.hr
import se.iimetra.dataflowgram.home.diagram.palette.ViewCell
import se.iimetra.dataflowgram.home.paletteDefaultChoseController
import se.iimetra.dataflowgram.wrappers.react.devextreme.TreeView

class CategoryBlock : RComponent<CategoryBlock.Props, RState>() {

  companion object {
    init {
      kotlinext.js.require("styles/category-block-styles.scss")
    }
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
          items = kotlin.js.JSON.parse(Json.plain.stringify(props.content))
          searchEnabled = true
          onItemClick = {
            if (it.itemData.items == null) {
              paletteDefaultChoseController.newChoose(it.itemData.id as String)
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
}

fun RBuilder.categoryBlock(handler: RHandler<CategoryBlock.Props>) =
  child(CategoryBlock::class, handler)