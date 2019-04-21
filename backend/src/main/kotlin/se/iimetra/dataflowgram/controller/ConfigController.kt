//package se.iimetra.dataflowgram.controller
//
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import se.iimetra.dataflow.GitContent
//import se.iimetra.dataflow.SpaceContent
//
//@RestController
//@RequestMapping("/api/config")
//class ConfigController {
//
//  @Volatile
//  private var currentConfig: GitContent = GitContent(-1, SpaceContent(emptyList()), SpaceContent(emptyList()))
//
//  override suspend fun parseUpdate(newContent: GitContent) {
//    currentConfig = newContent
//  }
//
//  @GetMapping("all")
//  fun getConfig() = currentConfig
//}