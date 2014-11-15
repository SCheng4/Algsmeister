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
    	| failure("Invalid syntax")
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
        rep1sep(recursiveCase, ",") ^^ {case rules => Dependencies(rules)}
        | failure("must provide 1 dependency")
    )
     
    lazy val recursiveCase: PackratParser[Dependency] = (
        beginIndices~"~"~endIndices ^^ {case beginIndices~"~"~endIndices => Dependency(beginIndices, endIndices)}
        | beginIndices ^^ {case beginIndices => Dependency(beginIndices, beginIndices)}
        | failure("Incorrectly formatted dependency")
    )
    
    lazy val beginIndices: PackratParser[Indices] = (
        "function"~"("~rep1sep(index, ",")~")" ^^ {
            case "function"~"("~index~")" =>
                if (index.length == 1) OneDIndices(index(0))
                else if (index.length == 2) TwoDIndices(index(0), index(1))
                // add 3D case here if needed
                else throw new MatchError("Incorrectly formatted recursive call") 
        }
    )
    
    lazy val endIndices: PackratParser[Indices] = (
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
