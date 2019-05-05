package se.iimetra.dataflowgram.controller

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import se.iimetra.dataflowgram.root.RootDispatcher

@RestController
@RequestMapping("/api/dactions")
class DActionController(val dispatcher: RootDispatcher) {

  @GetMapping("/delete_all")
  fun deleteAll() {
    GlobalScope.future { dispatcher.deleteAll() }
  }

  @GetMapping("/delete")
  fun deleteRef(@RequestParam("ref") ref: String) {
    GlobalScope.future { dispatcher.deleteRef(ref) }
  }

  @GetMapping("/delete_res")
  fun deleteRes(@RequestParam("name") name: String) {
    GlobalScope.future { dispatcher.deleteResource(name) }
  }
}