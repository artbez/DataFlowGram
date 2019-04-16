package se.iimetra.dataflowgram


import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.stereotype.Component
import se.iimetra.dataflowgram.controller.ws.ConfigWsHandler
import se.iimetra.dataflowgram.controller.ws.MainWSHandler
import se.iimetra.dataflowgram.controller.ws.ServerEventWsHandler
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.root.RootDispatcher

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
  override fun initialize(applicationContext: GenericApplicationContext) = beans {
    bean {
      val repo = "/Users/artemii.bezguzikov/project/pywork"
      GitConnector(repo)
    }
    bean<RootDispatcher>()
    bean<ConfigWsHandler>()
    bean<ServerEventWsHandler>()
    bean<MainWSHandler>()
  }.initialize(applicationContext)
}

@Component
class AfterInitializationHook(
  val gitConnector: GitConnector,
  val dispatcher: RootDispatcher,
  val configHandler: ConfigWsHandler
) {

  @EventListener
  fun handleContextRefresh(event: ContextRefreshedEvent) {
    gitConnector.addListener(dispatcher)
    gitConnector.addListener(configHandler)
    gitConnector.start()
  }
}