package se.iimetra.dataflowgram.controller

import org.springframework.web.bind.annotation.*
import se.iimetra.dataflow.AllDiagrams
import se.iimetra.dataflow.DiagramDto
import se.iimetra.dataflow.DiagramSaveRequest
import se.iimetra.dataflowgram.data.DiagramEntity
import se.iimetra.dataflowgram.data.DiagramRepository
import java.util.concurrent.atomic.AtomicLong

@RestController
@RequestMapping("/api/diagram")
class DiagramController(val repo: DiagramRepository) {

  private val nextId: AtomicLong

  init {
    val all = repo.findAll()
    nextId = if (all.isEmpty()) {
      AtomicLong(0)
    } else {
      AtomicLong(all.map { it.id }.max() ?: 0)
    }
  }

  @PostMapping("/save")
  fun save(@RequestBody diagram: DiagramSaveRequest) {
    repo.save(DiagramEntity(diagram.name, nextId.incrementAndGet(), diagram.diagram))
  }

  @GetMapping("/all")
  fun all(): AllDiagrams {
    return AllDiagrams(repo.findAll().map { it.name })
  }

  @GetMapping("/get")
  fun get(@RequestParam("name") name: String): DiagramDto {
    return repo.findById(name).get().let { DiagramDto(it.id, it.name, it.diagram) }
  }

  @GetMapping("/delete")
  fun delete(@RequestParam("name") name: String) {
    repo.deleteById(name)
  }
}