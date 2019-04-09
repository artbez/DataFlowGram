package se.iimetra.dataflowgram

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import py4j.GatewayServer


fun main(args: Array<String>) = runBlocking {
    println("Enter git repository")
    val repo = readLine()!!
    val gitConnector = GitConnector(repo)
    val libHolder = LibHolder()
    gitConnector.addListener(libHolder)
    gitConnector.start()

    val gatewayServer = GatewayServer()
    gatewayServer.start()
    try {
        while (true) {
            val lib = libHolder.getLib()
            if (lib.toFile().exists()) {
              val client = HelloWorldClient("localhost", 50051)
              try {
                var user = "world"
                client.greet(user)
              } finally {
                client.shutdown()
              }
            } else {
                delay(1000)
            }
        }
    } finally {
        gatewayServer.shutdown()
    }
}