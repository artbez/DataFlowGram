package se.iimetra.dataflowgram.home.diagram.executor

class ValueHolderPort<T>(private val nodeExecutor: AbstractNodeExecutor) {
  private var value: T? = null

  fun getValue(): T? = value

  suspend fun setValue(newValue: T?) {
    value = newValue
    nodeExecutor.dataReceived()
  }
}