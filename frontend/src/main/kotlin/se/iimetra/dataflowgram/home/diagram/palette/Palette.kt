package se.iimetra.dataflowgram.home.diagram.palette

import kotlinext.js.invoke
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import react.*
import react.dom.div
import se.iimetra.dataflow.*
import se.iimetra.dataflowgram.home.configController
import se.iimetra.dataflowgram.home.diagram.palette.blocks.categoryBlock
import se.iimetra.dataflowgram.home.diagram.palette.blocks.filesBlock
import se.iimetra.dataflowgram.home.filesSystemController
import se.iimetra.dataflowgram.home.paletteSystemChoseController
import se.iimetra.dataflowgram.wrappers.react.devextreme.TreeView
import se.iimetra.dataflowgram.wrappers.react.tabs.Tab
import se.iimetra.dataflowgram.wrappers.react.tabs.TabList
import se.iimetra.dataflowgram.wrappers.react.tabs.TabPanel
import se.iimetra.dataflowgram.wrappers.react.tabs.Tabs

class Palette : RComponent<RProps, Palette.State>() {

  override fun State.init() {
    pureCategories = emptyList()
    renderCategories = emptyList()
    resourceCategories = emptyList()
    files = emptyList()
  }

  override fun componentDidMount() {
    configController.addListener { newConfig ->
      setState {
        pureCategories = toViewCategories(newConfig.git.pureSpace.functions)
        renderCategories = toViewCategories(newConfig.git.renderSpace.functions)
        resourceCategories = toViewCategories(newConfig.git.resourceSpace.functions)
      }
    }
    filesSystemController.addListener { newFiles ->
      val viewFiles = toViewFiles(newFiles)
      if (state.files != viewFiles) {
        setState {
          files = viewFiles
        }
      }
    }
  }

  companion object {
    init {
      kotlinext.js.require("styles/palette-styles.scss")
    }
  }

  @ImplicitReflectionSerializer
  override fun RBuilder.render() {
    div("palette") {
      Tabs {
        TabList {
          Tab {
            +"Pure"
          }
          Tab {
            +"Render"
          }
          Tab {
            +"Resource"
          }
          Tab {
            +"File System"
          }
        }
        TabPanel {
          if (state.pureCategories.isNotEmpty()) {
            state.pureCategories.forEach {
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
          if (state.renderCategories.isNotEmpty()) {
            state.renderCategories.forEach {
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
          if (state.resourceCategories.isNotEmpty()) {
            state.resourceCategories.forEach {
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
          filesBlock {
            attrs {
              name = "All files in file system"
              content = state.files
            }
          }
        }
      }
    }
  }

  interface State : RState {
    var pureCategories: List<ViewCategory>
    var renderCategories: List<ViewCategory>
    var resourceCategories: List<ViewCategory>
    var files: List<ViewCell>
  }

  private fun systemToViewCells(functions: List<SystemFunction>): List<ViewCell> =
    functions.groupBy { it.params["from"] }.map {
      val items = it.value.map { ViewCell(it.id, it.name, false, null) }
      ViewCell(
        id = it.key!!,
        text = "From ${it.key!!}",
        expanded = true,
        items = items
      )
    }

  private fun toViewFiles(filesInfo: UserFilesInfo): List<ViewCell> {
    return filesInfo.files.map { ViewCell(it.filename, it.filename, false) }
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
                "${it.view.id.name} : ${it.meta.signature.toMathText()}",
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