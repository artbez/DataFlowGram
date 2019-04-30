package se.iimetra.dataflowgram.worker.handlers

import org.slf4j.LoggerFactory
import se.iimetra.dataflow.FunctionDescription
import se.iimetra.dataflowgram.worker.PythonServerClient
import se.iimetra.dataflowgram.worker.WorkerAction

class ExecutionHandler(private val client: PythonServerClient) {

  private val logger = LoggerFactory.getLogger(ExecutionHandler::class.java)

  fun processExecution(action: WorkerAction.Execute, description: FunctionDescription) {
    logger.info("Executing ${action.function.name}")
    try {
      val ref = client.executeCommand(action.function, description.meta.signature, action.arguments, action.params, action.onMessageReceive)
      logger.info("${action.function.name} executed")
      action.result.complete(ref)
    } catch (ex: Exception) {
      action.result.completeExceptionally(ex)
    }
  }
}