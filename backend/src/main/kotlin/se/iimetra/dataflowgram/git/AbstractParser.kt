package se.iimetra.dataflowgram.git

import se.iimetra.dataflow.CustomFunction
import se.iimetra.dataflow.FunctionMeta
import se.iimetra.dataflow.FunctionSignature
import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class AbstractParser {

  protected abstract val language: String
  protected abstract val comment: String
  protected abstract val functionCodeName: Pattern

  private val functionNamePattern = lazy { Pattern.compile("$comment function=(.*?)$") }
  private val signaturePattern = lazy { Pattern.compile("$comment signature=\\((.*?)\\)->(.+?)$") }
  private val paramsPattern = lazy { Pattern.compile("$comment param@(.*?):(.*?)$") }
  private val descriptionPattern = lazy { Pattern.compile("$comment description=(.*?)$") }

  protected abstract fun parseBodyLine(
    state: ParserState.FunctionBodyState,
    line: String,
    functionList: MutableList<CustomFunction>
  ): BodyIterationResult

  fun check(firstLine: String) = firstLine.contains("$comment exec")

  fun parse(version: Long, lines: List<String>): List<CustomFunction> {
    val functionList = mutableListOf<CustomFunction>()
    var state : ParserState = ParserState.SkipState
    for (line in lines) {
      var next = true
      while (next) {
        next = false
        when (state) {
          is ParserState.SkipState -> if (startFunctionMeta(line)) {
            state = ParserState.FunctionMetaState()
            state.builder.parseFunctionMeta(line)
          }

          is ParserState.FunctionMetaState -> when {
            line.startsWith(comment) -> state.builder.parseFunctionMeta(line)

            startFunctionBody(line) -> {
              val bodyBuilder = FunctionBodyBuilder()
              find(functionCodeName, line) { matcher ->
                bodyBuilder.apply {
                  realName = matcher.group(1)
                  args = matcher.group(2)
                  body.add(line)
                }
              }
              val meta = state.builder.build(bodyBuilder.realName!!, version, language)
              state = ParserState.FunctionBodyState(bodyBuilder, meta)

            }
            line.isNotBlank() -> state = ParserState.SkipState
          }

          is ParserState.FunctionBodyState -> {
            val result = parseBodyLine(state, line, functionList)
            next = result.next
            state = result.state
          }
        }
      }
    }

    if (state is ParserState.FunctionBodyState) {
      val customFunction = CustomFunction(
        state.functionMeta, state.builder.realName!!, state.builder.args, state.builder.body.filter { it.isNotBlank() }
      )
      functionList.add(customFunction)
    }
    return functionList
  }

  private fun startFunctionMeta(line: String): Boolean {
    val noSpaces = line
    val matchFunctionName = functionNamePattern.value.matcher(noSpaces)
    val matchFunctionSignature = signaturePattern.value.matcher(noSpaces)
    return matchFunctionName.find() || matchFunctionSignature.find()
  }

  private fun startFunctionBody(line: String): Boolean {
    val matcher = functionCodeName.matcher(line)
    return matcher.find()
  }

  private fun FunctionMetaBuilder.parseFunctionMeta(line: String) {
    val noSpace = line
    find(functionNamePattern.value, noSpace) { matcher ->
      label = matcher.group(1)
    }

    find(signaturePattern.value, noSpace) { matcher ->
      signature = FunctionSignature(
        matcher.group(1).split(",").map { it.trim() }.toList(),
        matcher.group(2)
      )
    }

    find(paramsPattern.value, noSpace) { matcher ->
      paramsMap[matcher.group(1)] = matcher.group(2)
    }

    find(descriptionPattern.value, line) { matcher ->
      description = matcher.group(1).split("\\n")
    }
  }

  private fun find(pattern: Pattern, line: String, handler: (Matcher) -> Unit) {
    val matcher = pattern.matcher(line)
    if (matcher.find()) {
      handler(matcher)
    }
  }
}

sealed class ParserState {
  object SkipState : ParserState()
  class FunctionMetaState(val builder: FunctionMetaBuilder = FunctionMetaBuilder()) : ParserState()
  class FunctionBodyState(val builder: FunctionBodyBuilder, val functionMeta: FunctionMeta) : ParserState()
}

data class FunctionMetaBuilder(
  var label: String? = null,
  var signature: FunctionSignature? = null,
  var paramsMap: MutableMap<String, String> = mutableMapOf(),
  var description: List<String> = emptyList()
) {
  fun build(realName: String, version: Long, language: String) =
    FunctionMeta(label ?: realName, signature!!, paramsMap, description, version, language)
}

data class FunctionBodyBuilder(
  var realName: String? = null,
  var args: String = "",
  val body: MutableList<String> = mutableListOf()
)

class BodyIterationResult(val state: ParserState, val next: Boolean)