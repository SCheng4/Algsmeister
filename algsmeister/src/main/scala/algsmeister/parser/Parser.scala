package algsmeister.parser

import scala.util.parsing.combinator._
import algsmeister.ir._

object funcParser extends JavaTokenParsers with PackratParsers {
	// parsing interface
    def apply(s: String): ParseResult[AST] = parseAll(program, s)

    lazy val program: PackratParser[Program] =
    	("def"~"function"~arguments~":"~
      		"if"~"("~baseCases~")"~":"~"return"~
      		"else"~"consider"~":"~
      			recursiveCases
      	^^ {case "def"~"function"~arguments~":"~
      			     "if"~"("~baseCases~")"~":"~"return"~
      			     "else"~"consider"~":"~
      			     	recursiveCases
      			 => Program(arguments, baseCases, recursiveCases)}
    	)
    
    lazy val arguments: PackratParser[Dimension] = (
          "("~"i"~")" ^^ {case "("~"i"~")" => OneD()}
        | "("~"i"~","~"j"~")" ^^ {case "("~"i"~","~"j"~")" => TwoD()} 
    )
     
    lazy val baseCases: PackratParser[BaseCases] = ( 
       "TODO" ^^ {case _ => BaseCases(List.empty)}
    )
     
    //lazy val baseCase: PackratParser[Cell] = ()
     
    lazy val recursiveCases: PackratParser[Dependencies] = (
        opt(repsep(recursiveCase, ",")) ^^ {
            case Some(rules) => Dependencies(rules)
            case None => throw new MatchError("Please provide at least one dependency.") 
        }
    )
     
    lazy val recursiveCase: PackratParser[Dependency] = (
        opt(repsep(indices, "~")) ^^ {
            case None => throw new MatchError("Incorrectly formatted dependency.")
            case Some(indices) => 
                if (indices.length == 1) Dependency(indices(0), indices(0))
                else if (indices.length == 2) Dependency(indices(0), indices(1))
                else throw new MatchError("Incorrectly formatted dependency.")
        }
    )
    
    lazy val indices: PackratParser[Indices] = (
        "function"~"("~rep1sep(index, ",")~")" ^^ {
            case "function"~"("~index~")" =>
                if (index.length == 1) OneDIndices(index(0))
                else if (index.length == 2) TwoDIndices(index(0), index(1))
                // add 3D case here if needed
                else throw new MatchError("Incorrectly formatted recursive call") 
        }
    )
    
    lazy val index: PackratParser[Index] = (
        number ^^ {case num => AbsIndex(num)}
        | "i"~"+"~number ^^ {case "i"~"+"~num => RelativeIndex(num)}
        | "i"~"-"~number ^^ {case "i"~"-"~num => RelativeIndex(-num)}
        | "j"~"+"~number ^^ {case "j"~"+"~num => RelativeIndex(num)}
        | "j"~"-"~number ^^ {case "j"~"-"~num => RelativeIndex(-num)}
    )
    
    def number: Parser[Int] = wholeNumber ^^ {s => (s.toInt)}

    
}
