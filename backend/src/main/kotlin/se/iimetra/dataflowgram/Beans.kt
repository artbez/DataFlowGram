package se.iimetra.dataflowgram


import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.stereotype.Component
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import se.iimetra.dataflowgram.controller.ws.*
import se.iimetra.dataflowgram.files.FileSystemConnector
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.root.RootDispatcher

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
  override fun initialize(applicationContext: GenericApplicationContext) = beans {
    bean {
      val repo = "/Users/artemii.bezguzikov/project/tt2"
      GitConnector(repo)
    }
    //bean<CommonsMultipartResolver>()
    bean<FileSystemConnector>()
    bean<FilesSystemHandler>()
    bean<ConfigWsHandler>()
    bean<RootDispatcher>()
    bean<ServerEventWsHandler>()
    bean<ConverterWsHandler>()
    bean<MainWSHandler>()
  }.initialize(applicationContext)
}