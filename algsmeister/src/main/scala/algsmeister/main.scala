package algsmeister

import scala.tools.nsc.EvalLoop
import algsmeister.parser.funcParser
import algsmeister.semantics.eval

object Algsmeister extends EvalLoop with App {
  override def prompt = "> "
    // TODO
}
