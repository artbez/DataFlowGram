package se.iimetra.dataflowgram.git

import java.util.regex.Pattern

class PythonFileParser {
  private val functionNamePattern = Pattern.compile("def (.*?)\\((.*?)\\):")

  fun parse(lines: List<String>): List<CustomFunction> {
    val imports = lines.filter { it.startsWith("import ") || it.startsWith("from ") }
    val functionStartIndexes = lines
      .mapIndexed { index, s ->  index to s.startsWith("def") }
      .filter { it.second }
      .map { it.first }

    val functionStartLines = functionStartIndexes.map { lines[it] }.filter { it.startsWith("def") }
    val functionDescription = functionStartLines.map {
      val m = functionNamePattern.matcher(it)
      m.find()
       m.group(1) to m.group(2)
    }

    val functionBodyIndexes = functionStartIndexes.zip(functionStartIndexes.drop(1).plus(lines.size))
    return functionBodyIndexes.mapIndexed { index, pair ->
      CustomFunction(
        functionDescription[index].first,
        functionDescription[index].second?.let { if (it.isBlank()) 0 else it.split(",").size } ?: 0,
        lines.subList(pair.first + 1, pair.second),
        imports
      )
    }
  }
}