package se.iimetra.dataflowgram.home

import kotlinx.serialization.ImplicitReflectionSerializer
import se.iimetra.dataflowgram.home.controllers.ConfigController
import se.iimetra.dataflowgram.home.controllers.EventController
import se.iimetra.dataflowgram.home.controllers.PaletteChoseController
import se.iimetra.dataflowgram.home.diagram.node.DefaultNodeFactory
import se.iimetra.dataflowgram.wrappers.react.diagrams.DiagramEngine
import se.iimetra.dataflowgram.wrappers.react.diagrams.models.DiagramModel

fun DiagramEngine.setup(): DiagramEngine {

  installDefaultFactories()

  registerNodeFactory(DefaultNodeFactory.instance)

  val model = DiagramModel()
  setDiagramModel(model)

  return this
}

val configController = ConfigController()
val paletteChoseController = PaletteChoseController()
@ImplicitReflectionSerializer
val eventController = EventController()