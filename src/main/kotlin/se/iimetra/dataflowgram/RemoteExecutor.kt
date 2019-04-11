package se.iimetra.dataflowgram

import kotlinx.coroutines.runBlocking
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.root.FunctionMeta
import se.iimetra.dataflowgram.root.RootDispatcher

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
    println("Enter version")
    val version = readLine()!!.toLong()
    val commandList = command.split("/")
    val function = FunctionMeta(commandList[0], commandList[1], commandList[2], commandList[3].toInt())
    try {
      val job = dispatcher.execute(function, listOf(), version).join()
      println("Result $job")
    } catch (ex: Exception) {
      println("Cant't execute $function")
    }
  }
}