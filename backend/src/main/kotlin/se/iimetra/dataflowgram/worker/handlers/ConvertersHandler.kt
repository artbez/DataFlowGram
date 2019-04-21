package se.iimetra.dataflowgram.worker.handlers

import se.iimetra.dataflowgram.root.ValueTypePair
import se.iimetra.dataflowgram.worker.PythonServerClient
import se.iimetra.dataflowgram.worker.WorkerAction
import java.lang.IllegalStateException

class ConvertersHandler(private val client: PythonServerClient) {

  fun processConversion(action: WorkerAction) {
    when (action) {
      is WorkerAction.InData -> {
        val value = client.inCommand(action.data)
        action.result.complete(ValueTypePair(value, action.data.type))
      }
      is WorkerAction.OutData -> {
        val value = client.outCommand(action.ref, action.type)
        action.result.complete(ValueTypePair(value, action.type))
      }

      else -> throw IllegalStateException("Action must be a converter")
    }
  }
}