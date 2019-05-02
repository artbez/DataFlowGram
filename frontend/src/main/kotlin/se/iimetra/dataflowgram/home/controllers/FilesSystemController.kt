package se.iimetra.dataflowgram.home.controllers

import se.iimetra.dataflow.AllFunctions
import se.iimetra.dataflow.GitContent
import se.iimetra.dataflow.SpaceContent
import se.iimetra.dataflow.UserFilesInfo

class FilesSystemController {
  var files = UserFilesInfo(emptyList())
  private val listeners = mutableListOf<(UserFilesInfo) -> Unit>()

  fun push(newFiles: UserFilesInfo) {
    files = newFiles
    listeners.forEach { it.invoke(files) }
  }

  fun addListener(listener: (UserFilesInfo) -> Unit) {
    listeners.add(listener)
    listener(files)
  }
}