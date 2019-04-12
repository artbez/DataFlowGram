package se.iimetra.dataflowgram

import kotlinx.coroutines.runBlocking
import se.iimetra.dataflowgram.git.FunctionId
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.root.RootDispatcher
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

fun main(args: Array<String>) = runBlocking {
  val repo = "/Users/artemii.bezguzikov/project/pywork"
  val connector = GitConnector(repo)
  connector.start()
  val dispatcher = RootDispatcher()
  dispatcher.start()
  connector.addListener(dispatcher)
  while (true) {
    println("Enter function to execute")
    val command = readLine()!!
    try {
      val job = dispatcher.processCommand(command).join()
      println("Result $job")
    } catch (ex: Exception) {
      println("Cant't execute $command")
    }
  }
}

suspend fun RootDispatcher.processCommand(command: String): CompletableFuture<String> {
  if (command.startsWith("upload")) {
    val arg = command.split(" ")[1]
    return uploadFile(Paths.get(arg))
  }
  if (command.startsWith("initFile")) {
    val arg = command.split(" ")[1]
    return initFile(arg)
  }
  if (command.startsWith("getJson")) {
    val arg = command.split(" ")[1]
    return loadJson(arg)
  }
  println("Enter version")
  val version = readLine()!!.toLong()
  val commandList = command.split("$")
  val function = FunctionId(commandList[0], commandList[1], commandList[2])
  return execute(function, commandList[3].split(" ").toList(), version)
}

//data$DataFrame$read_csv$'/Users/artemii.bezguzikov/project/tmp/test.csv'

