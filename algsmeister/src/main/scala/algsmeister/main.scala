package algsmeister

import scala.tools.nsc.EvalLoop
import algsmeister.parser.funcParser
import algsmeister.semantics.evalProgram

object Algsmeister extends App {
    if (args.length != 1) {
        throw new IllegalArgumentException("Exactly one parameter expected")
    }
    
    val source = scala.io.Source.fromFile(args(0))
    val contents = source.mkString
    source.close()
    
    val parsed = funcParser(contents)

    parsed match {
        case e: funcParser.NoSuccess => println(e)
        case _ => {evalProgram(parsed.get)}
    }
}

