package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflow.ConverterEventResponse

typealias ConverterFilterPair = Pair<(ConverterEventResponse) -> Unit, (ConverterEventResponse) -> Boolean>

class ConverterEventController {
  private val listeners = mutableListOf<ConverterFilterPair>()

  fun push(response: ConverterEventResponse) {
    val iter = listeners.iterator()
    while (iter.hasNext()) {
      val (func, filter) = iter.next()
      if (filter.invoke(response)) {
        func.invoke(response)
        iter.remove()
      }
    }
  }

  fun addListener(filter: (ConverterEventResponse) -> Boolean, listener: (ConverterEventResponse) -> Unit) {
    listeners.add(ConverterFilterPair(listener, filter))
  }
}