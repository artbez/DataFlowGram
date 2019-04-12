package se.iimetra.dataflowgram.home

interface EventListener {
  fun processEvent(event: Any)
}

class EventDispatcher {

  private val listeners: MutableList<EventListenerEntity> = mutableListOf()

  fun fireEvent(event: Any) {
    listeners.filter { it.filter(event) }.forEach { it.inner.processEvent(event) }
  }

  fun addListner(filter: (Any) -> Boolean, listener: EventListener) {
    listeners.add(EventListenerEntity(filter, listener))
  }

}

class EventListenerEntity(val filter: (Any) -> Boolean = { true }, val inner: EventListener)