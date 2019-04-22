package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflowgram.home.diagram.editor.panel.ExecutionState

class ClientEventController {

  fun pushEvent(function: FunctionDescription, args: List<String>, state: ExecutionState): Array<dynamic>? {
    val funBody = function.view.content.joinToString("\n")
    console.log(funBody)
   // eval(funBody)
    val argString = args.joinToString(",")
    val evaluation = eval(funBody + "\n" + function.view.id.name + "(" + argString + ")")
    if (evaluation == null) {
      return null
    }
    return evaluation as Array<dynamic>
  }
}