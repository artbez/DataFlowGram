package se.iimetra.dataflowgram.header

import se.iimetra.dataflowgram.wrappers.reactRouter
import kotlinext.js.invoke
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.DiagramModel

class HeaderComponent : RComponent<HeaderComponent.Props, RState>() {

  var tmp: dynamic = null

  companion object {
    init {
      kotlinext.js.require("styles/header.scss")
    }
  }

  override fun RBuilder.render() {
    nav("navbar navbar-default header-nav navbar-fixed-top") {
      div("container-fluid") {
        div("navbar-header") {
          homeLink("navbar-brand", "Diagram executor")
        }
        ul("nav navbar-nav") {
          li {
            a {
              attrs {
                href = "#"
                onClickFunction = {
                  props.diagramModel.getNodes().toMap().forEach { (_, value) ->
                    props.diagramModel.removeNode(value)
                  }
                  props.diagramModel.getLinks().toMap().forEach { props.diagramModel.removeLink(it.value) }
                }
              }
              +"New"
            }
          }
          li {
            a {
              attrs {
                href = "#"
                onClickFunction = {
                  console.log(props.diagramModel.serializeDiagram())
                  tmp = props.diagramModel.serializeDiagram()
                }
              }
              +"Save As"
            }
          }
          li {
            a {
              attrs {
                href = "#"
                onClickFunction = {
                  props.diagramModel.deSerializeDiagram(tmp, props.diaEngine)
                }
              }
              +"Open"
            }
          }
        }
      }
    }
  }

  interface Props : RProps {
    var diagramModel: DiagramModel
    var diaEngine: DiagramEngine
    var updateDiagram: () -> Unit
  }
}

fun RBuilder.header(handler: RHandler<HeaderComponent.Props>) = child(HeaderComponent::class, handler)

private fun RBuilder.homeLink(className: String = "", name: String = "Home") = reactRouter.Link {
  attrs {
    to = "/"
    this.className = className
  }
  +name
}
