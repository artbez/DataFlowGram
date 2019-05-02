package se.iimetra.dataflow

import kotlinx.serialization.Serializable

@Serializable
data class UserFilesInfo(val files: List<FileInfo>)

@Serializable
data class FileInfo(val filename: String, val isDirectory: Boolean)