package se.iimetra.dataflowgram

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import py4j.GatewayServer
import se.iimetra.dataflowgram.git.FunctionDescription
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.lib.LibHolder
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

//fun main(args: Array<String>) = runBlocking {
//    println("Enter git repository")
//    val repo = readLine()!!
//    val gitConnector = GitConnector(repo)
//    val libHolder = LibHolder()
//    gitConnector.addListener(libHolder)
//    gitConnector.start()
//
//    val gatewayServer = GatewayServer()
//    gatewayServer.start()
//    try {
//        while (true) {
//            val lib = libHolder.getLib()
//            if (lib.toFile().exists()) {
//              val client = HelloWorldClient("localhost", 50051)
//              try {
//                var user = "world"
//                client.greet(user)
//              } finally {
//                client.shutdown()
//              }
//            } else {
//                delay(1000)
//            }
//        }
//    } finally {
//        gatewayServer.shutdown()
//    }
//}