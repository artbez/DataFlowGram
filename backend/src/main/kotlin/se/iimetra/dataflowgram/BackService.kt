package se.iimetra.dataflowgram

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class BackService

fun main(args: Array<String>) {
  SpringApplication.run(BackService::class.java, *args)
}