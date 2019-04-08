package se.iimetra.dataflowgram

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.python.util.PythonInterpreter
import py4j.GatewayServer
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun funk(command: String) {

}

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
                val pythonProgram = """
                  from py4j.java_gateway import JavaGateway
                  exec(compile(open("lib/all.py", "rb").read(), "lib/all.py", 'exec'))
                  gateway = JavaGateway()
                """.trimIndent()
                println("Enter command")
                val command = readLine()
                Files.write(Paths.get("tmp.py"), (pythonProgram + "\n" + command).toByteArray())

                val processBuilder = ProcessBuilder("python", "tmp.py")
                val process = processBuilder.start()

                try {
                    process.waitFor()
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }


                val inputStream = process.inputStream
                val scanner = Scanner(inputStream)
                while (scanner.hasNext()) {
                    println(scanner.next())
                }
            } else {
                delay(1000)
            }
        }
    } finally {
        gatewayServer.shutdown()
    }

}