package se.iimetra.dataflowgram.header

import se.iimetra.dataflowgram.wrappers.reactRouter
import kotlinext.js.invoke
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import react.*
import react.dom.*
import se.iimetra.dataflow.AllDiagrams
import se.iimetra.dataflow.DiagramSaveRequest
import se.iimetra.dataflowgram.dom.logIcon
import se.iimetra.dataflowgram.utils.get
import se.iimetra.dataflowgram.utils.post
import se.iimetra.dataflowgram.utils.toMap
import se.iimetra.dataflowgram.wrappers.react.bootstrap.OverlayTrigger
import se.iimetra.dataflowgram.wrappers.react.bootstrap.Popover
import se.iimetra.dataflowgram.wrappers.react.diagrams.BaseModelListener
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.DiagramModel
import kotlin.browser.window

class HeaderComponent : RComponent<HeaderComponent.Props, HeaderComponent.State>() {

  companion object {
    init {
      kotlinext.js.require("styles/header.scss")
    }
  }

  override fun State.init() {
    diaName = null
    tmpName = ""
    showOpenPopup = false
  }

  override fun RBuilder.render() {
    nav("navbar navbar-default header-nav navbar-fixed-top") {
      div("container-fluid") {
        div("navbar-header") {
          homeLink("navbar-brand", state.diaName ?: "Diagram executor")
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
                  setState {
                    diaName = null
                  }
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
                  if (state.diaName == null) {
                    window.alert("Please, pick saveAs option in order to chose a name")
                  } else {
                    state.diaName?.let {
                      GlobalScope.launch {
                        val finalName = state.diaName
                        val diagramString = JSON.stringify(props.diagramModel.serializeDiagram())
                        post(
                          "/api/diagram/save",
                          Json.plain.stringify(
                            DiagramSaveRequest.serializer(),
                            DiagramSaveRequest(it, diagramString)
                          )
                        )
                        setState {
                          diaName = finalName
                        }
                      }
                    }
                  }
                }
              }
              +"Save"
            }
          }
          li {
            OverlayTrigger {
              attrs {
                trigger = "click"
                placement = "bottom"
                overlay = buildElement {
                  Popover {
                    attrs {
                      placement = "bottom"
                      id = "popover-select-output"
                      title = "Output"
                    }
                    span { +"Enter diagram name" }
                    input {
                      attrs {
                        value = state.tmpName
                        onChangeFunction = {
                          val newValue = it.target.asDynamic().value
                          setState {
                            tmpName = newValue
                          }
                        }
                      }
                    }
                    button(classes = "editor_button btn-success") {
                      attrs {
                        onClickFunction = {
                          if (state.tmpName.isBlank()) {
                            window.alert("Can't save blunk diagram name")
                          } else {
                            val finalName = state.tmpName
                            val diagramString = JSON.stringify(props.diagramModel.serializeDiagram())
                            val request = Json.plain.stringify(
                              DiagramSaveRequest.serializer(),
                              DiagramSaveRequest(finalName, diagramString)
                            )
                            GlobalScope.launch {
                              post("/api/diagram/save", request)
                              setState {
                                diaName = finalName
                              }
                            }
                          }
                        }
                      }
                      +"Save"
                    }
                  }
                }!!
              }
              a {
                attrs {
                  href = "#"
                }
                +"Save As"
              }
            }
          }
          li {
            OverlayTrigger {
              attrs {
                trigger = "click"
                placement = "bottom"
                overlay = buildElement {
                  Popover {
                    attrs {
                      placement = "bottom"
                      id = "popover-select-output"
                      title = "Output"
                    }
                    diaListComponent {
                      attrs {
                        currentId = state.diaName
                        onDiagramUpload = {
                          setState {
                            diaName = it.name
                          }
                          props.diagramModel.getNodes().toMap().forEach { (_, value) ->
                            props.diagramModel.removeNode(value)
                          }
                          props.diagramModel.getLinks().toMap().forEach { props.diagramModel.removeLink(it.value) }
                          props.diagramModel.deSerializeDiagram(kotlin.js.JSON.parse(it.diagram), props.diaEngine)
                          props.diagramModel.getNodes().toMap().forEach {
                            it.value.addListener(
                              BaseModelListener().events {
                                this.selectionChanged = {
                                  props.updateDiagram()
                                }
                              }
                            )
                          }
                        }
                      }
                    }
                  }
                }!!
                a {
                  attrs {
                    href = "#"
                  }
                  +"Open"
                }
              }
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

  interface State : RState {
    var diaName: String?
    var tmpName: String
    var showOpenPopup: Boolean
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
