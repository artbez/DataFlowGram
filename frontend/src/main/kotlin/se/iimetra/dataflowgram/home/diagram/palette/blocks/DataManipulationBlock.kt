package se.iimetra.dataflowgram.home.diagram.palette.blocks


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.*
import react.dom.div
import react.dom.h4
import react.dom.hr
import se.iimetra.dataflowgram.home.diagram.palette.paletteNode

class DataManipulationBlock : RComponent<DataManipulationBlock.Props, DataManipulationBlock.State>() {

  override fun State.init() {
    datasets = emptyList()
  }

  override fun componentWillMount() {
    GlobalScope.launch {
      //val netResponse = JSON.parse(DatasetDescription.serializer().list, get("/api/net/info/datasets/all"))
      setState {
        //datasets = netResponse
      }
    }
  }

  override fun RBuilder.render() {
    div("palette__block") {
      h4("palette__block__title") {
        +"Data manipulation"
      }

      hr("palette__block__line") { }

      if (state.datasets.isNotEmpty()) {
        paletteNode {
          attrs {
            label = "Upload dataset"
            //this.nodeProducer = { UploadDatasetNodeFactory.instance.getNewInstance(null) }
          }
        }
      }
    }
  }

  interface Props : RProps {
  }

  interface State : RState {
    var datasets: List<String>
  }
}

fun RBuilder.dataManipulationBlock(handler: RHandler<DataManipulationBlock.Props>) =
  child(DataManipulationBlock::class, handler)