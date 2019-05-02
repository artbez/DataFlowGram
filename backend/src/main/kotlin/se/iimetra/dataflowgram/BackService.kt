package se.iimetra.dataflowgram

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.codec.multipart.Part
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyExtractors
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import se.iimetra.dataflowgram.files.FileSystemConnector
import java.io.File


@SpringBootApplication
class BackService

@Configuration
class DefaultRouter{

  @Bean
  fun route(homeHandler: HomeHandler): RouterFunction<ServerResponse> {

    return RouterFunctions
      .route(RequestPredicates.POST("/api/fileupload").and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)),
        HandlerFunction<ServerResponse> { homeHandler.upload(it) })

  }

}

@Component
class HomeHandler(private val fileSystemConnector: FileSystemConnector) {

  fun upload(request: ServerRequest): Mono<ServerResponse> {

    // Not used in this example, but useful for logging, etc
    //val id = request.pathVariable("id")

    return request.body(BodyExtractors.toMultipartData()).flatMap { parts ->
      val map: Map<String, Part> = parts.toSingleValueMap()
      val filePart : FilePart = map["file"]!! as FilePart
      // Note cast to "FilePart" above

      // Save file to disk - in this example, in the "tmp" folder of a *nix system
      val fileName = filePart.filename()
      filePart.transferTo(File("${fileSystemConnector.userDir.toAbsolutePath()}/$fileName"))

      ServerResponse.ok().body(BodyInserters.fromObject("OK"))
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(BackService::class.java, *args)
}