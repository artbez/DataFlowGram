package se.iimetra.dataflowgram.controller.ws

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class ReactiveWebSocketConfiguration(val mainWsHandler: MainWSHandler) {

  @Bean
  fun webSocketHandlerMapping(): HandlerMapping {
    val map = HashMap<String, WebSocketHandler?>()
    map["/api/ws/all"] = mainWsHandler

    val handlerMapping = SimpleUrlHandlerMapping()
    handlerMapping.order = 1
    handlerMapping.urlMap = map
    return handlerMapping
  }

  @Bean
  fun handlerAdapter(): WebSocketHandlerAdapter {
    return WebSocketHandlerAdapter()
  }
}
