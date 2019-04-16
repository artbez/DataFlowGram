//package se.iimetra.dataflowgram.home.controllers
//
//import se.iimetra.dataflow.FunctionDescription
//import se.iimetra.dataflowgram.wrappers.react.diagrams.models.NodeModel
//
//class ExecutionController {
//  private val listeners = mutableListOf<(List<NodeModel>) -> Unit>()
//  private var executionList: List<NodeModel> = emptyList()
//
//
//
//  fun addListener(listener: (List<NodeModel>) -> Unit) {
//    listeners.add(listener)
//    choosen?.let { listener.invoke(it) }
//  }
//}