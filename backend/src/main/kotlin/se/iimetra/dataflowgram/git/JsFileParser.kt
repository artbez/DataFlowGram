package se.iimetra.dataflowgram.git

import se.iimetra.dataflow.CustomFunction
import java.util.regex.Pattern

class JsFileParser : AbstractParser() {
  override val language: String = "js"
  override val comment: String = "//"
  override val functionCodeName: Pattern = Pattern.compile("function (.*?)\\((.*?)\\)")

  override fun parseBodyLine(
    state: ParserState.FunctionBodyState,
    line: String,
    functionList: MutableList<CustomFunction>
  ): BodyIterationResult {
    if (line.startsWith("}")) {
      state.builder.body.add(line)
      functionList.add(CustomFunction(state.functionMeta, state.builder.realName!!, state.builder.args, state.builder.body))
      return BodyIterationResult(ParserState.SkipState, false)
    }
    state.builder.body.add(line)
    return BodyIterationResult(state, false)
  }
}