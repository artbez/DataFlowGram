package se.iimetra.dataflowgram.home.diagram.palette

import kotlinext.js.invoke
import kotlinx.serialization.Serializable
import react.*
import react.dom.div
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflow.FunctionId
import se.iimetra.dataflow.fullId
import se.iimetra.dataflowgram.home.configController
import se.iimetra.dataflowgram.home.diagram.palette.blocks.categoryBlock
import se.iimetra.dataflowgram.wrappers.react.tabs.Tab
import se.iimetra.dataflowgram.wrappers.react.tabs.TabList
import se.iimetra.dataflowgram.wrappers.react.tabs.TabPanel
import se.iimetra.dataflowgram.wrappers.react.tabs.Tabs

class Palette : RComponent<RProps, Palette.State>() {

  override fun State.init() {
    categories = emptyList()
  }

  override fun componentDidMount() {
    configController.addListener { newConfig ->
      setState {
        categories = toViewCategories(newConfig.functions)
      }
    }
  }

  companion object {
    init {
      kotlinext.js.require("styles/palette-styles.scss")
    }
  }

  override fun RBuilder.render() {
    div("palette") {
      Tabs {
        TabList {
          Tab {
            +"Server"
          }
          Tab {
            +"Client"
          }
        }
        TabPanel {
          if (state.categories.isNotEmpty()) {
            state.categories.forEach {
              categoryBlock {
                attrs {
                  name = it.category
                  content = it.cells
                }
              }
            }
          }
        }
        TabPanel {
          +"b2"
        }
      }
    }
  }

  interface State : RState {
    var categories: List<ViewCategory>
  }

  private fun toViewCategories(functions: List<FunctionDescription>): List<ViewCategory> {
    return functions
      .groupBy { it.view.id.category }
      .map { entry ->
        val groupByFile = entry.value.groupBy { it.view.id.file }

        val fileCells = groupByFile.map { en ->
          ViewCell(
            "${entry.key}__${en.key}", en.key, true,
            items = en.value.map {
              ViewCell(
                it.fullId(),
                "${it.view.id.name} : (${it.meta.signature.input.joinToString(",")}) -> ${it.meta.signature.output}",
                expanded = false
              )
            }
          )
        }
        return@map ViewCategory(entry.key, fileCells)
      }
  }
}

@Serializable
data class ViewCell(val id: String, val text: String, val expanded: Boolean, val items: List<ViewCell>? = null)

@Serializable
data class ViewCategory(val category: String, val cells: List<ViewCell>)

fun RBuilder.palette(handler: RHandler<RProps>) = child(Palette::class, handler)