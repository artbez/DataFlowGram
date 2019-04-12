package se.iimetra.dataflowgram.git

import se.iimetra.dataflow.GitContent

interface GitListener {
  suspend fun parseUpdate(newContent: GitContent)
}

