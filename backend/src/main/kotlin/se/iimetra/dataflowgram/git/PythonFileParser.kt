package se.iimetra.dataflowgram.git

import se.iimetra.dataflow.CustomFunction
import java.util.regex.Pattern

class PythonFileParser(override val language: String) : AbstractParser() {

  override val comment: String = "#"
  override val functionCodeName: Pattern = Pattern.compile("def (.*?)\\((.*?)\\):")

  override fun parseBodyLine(
    state: ParserState.FunctionBodyState,
    line: String,
    functionList: MutableList<CustomFunction>
  ): BodyIterationResult {
    if (line.isNotBlank() && line[0] != ' ') {
      functionList.add(CustomFunction(state.functionMeta, state.builder.realName!!, state.builder.args, state.builder.body))
      return BodyIterationResult(ParserState.SkipState, true)
    }
    state.builder.body.add(line)
    return BodyIterationResult(state, false)
  }
}