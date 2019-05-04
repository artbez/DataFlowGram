package se.iimetra.dataflowgram.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository

data class DiagramEntity(
  @Id val name: String,
  val id: Long,
  val diagram: String
)

interface DiagramRepository : MongoRepository<DiagramEntity, String>