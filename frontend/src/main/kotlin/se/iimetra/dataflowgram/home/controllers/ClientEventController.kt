package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflow.FunctionDescription

class ClientEventController {

  fun pushEvent(function: FunctionDescription, args: List<String>): dynamic {
    val funBody = function.view.content.joinToString("\n")
    console.log(funBody)
   // eval(funBody)
    val argString = args.joinToString(",")
    return eval(funBody + "\n" + function.view.id.name + "(" + argString + ")")
  }
}