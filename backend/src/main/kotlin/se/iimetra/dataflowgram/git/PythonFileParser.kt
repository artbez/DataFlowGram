package se.iimetra.dataflowgram.git

import se.iimetra.dataflow.CustomFunction
import se.iimetra.dataflow.FunctionSignature
import java.util.regex.Matcher
import java.util.regex.Pattern

class PythonFileParser {
  private val signaturePattern = Pattern.compile("# signature=\\((.*?)\\)->(.+?)$")
  private val paramsPattern = Pattern.compile("# params=(.*?)")
  private val descriptionPattern = Pattern.compile("# description=")
  private val functionNamePattern = Pattern.compile("def (.*?)\\((.*?)\\):")

  fun parse(lines: List<String>): List<CustomFunction> {
    val imports = lines.filter { it.startsWith("import ") || it.startsWith("from ") }
    val functionStartIndexes = lines
      .mapIndexed { index, s ->  index to s.replace(" ","").startsWith("#signature") }
      .filter { it.second }
      .map { it.first }

    val functionBodyIndexes = functionStartIndexes.zip(functionStartIndexes.drop(1).plus(lines.size))
    return functionBodyIndexes.map { pair ->
      extractFunction(lines.subList(pair.first, pair.second), imports)
    }
  }

  private fun extractFunction(lines: List<String>, imports: List<String>): CustomFunction {
    val signature = find(signaturePattern, lines) {
      FunctionSignature(group(1).split(",").map { it.trim() }.toList(), group(2))
    }
    val params = find(paramsPattern, lines) { group(1) }
    val description = find(descriptionPattern, lines) { group(1) }
    val firstNotNullIndex = lines.indexOfFirst { !it.isBlank() && !it.contains("#") }
    val (name, arguments) = find(functionNamePattern, listOf(lines[firstNotNullIndex])) {
      group(1) to group(2)
    }!!

    return CustomFunction(signature!!, params, description, name, arguments, lines, imports)
  }

  private fun <T : Any> find(pattern: Pattern, lines: List<String>, match: Matcher.() -> T) = lines.mapNotNull {
    val m = pattern.matcher(it)
    if (m.find()) m.match() else null
  }.firstOrNull()
}