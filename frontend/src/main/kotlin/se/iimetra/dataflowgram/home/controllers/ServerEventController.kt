package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.ServerResultEventResponse

typealias EventFilterPair = Pair<(ServerResultEventResponse) -> Unit, (ServerResultEventResponse) -> Boolean>

class ServerEventController {
  private val listeners = mutableListOf<EventFilterPair>()

  fun push(response: ServerResultEventResponse) {
    val iter = listeners.iterator()
    while (iter.hasNext()) {
      val (func, filter) = iter.next()
      if (filter.invoke(response)) {
        func.invoke(response)
        if (response.ref != null) {
          iter.remove()
        }
      }
    }
  }

  fun addListener(filter: (ServerResultEventResponse) -> Boolean, listener: (ServerResultEventResponse) -> Unit) {
    listeners.add(EventFilterPair(listener, filter))
  }
}