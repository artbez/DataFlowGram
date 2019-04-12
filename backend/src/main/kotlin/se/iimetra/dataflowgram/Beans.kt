package se.iimetra.dataflowgram


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import se.iimetra.dataflowgram.git.GitConnector
import se.iimetra.dataflowgram.root.RootDispatcher

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
  override fun initialize(applicationContext: GenericApplicationContext) = beans {
    bean {
      val repo = "/Users/artemii.bezguzikov/project/pywork"
      GitConnector(repo)
    }
    bean {
      val dispatcher = RootDispatcher()
      GlobalScope.launch {  ref<GitConnector>().addListener(dispatcher) }
      dispatcher
    }
  }.initialize(applicationContext)
}