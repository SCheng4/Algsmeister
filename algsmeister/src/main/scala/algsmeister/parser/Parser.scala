package algsmeister.parser

import scala.util.parsing.combinator._
import algsmeister.ir._

object funcParser extends JavaTokenParsers with PackratParsers {
	// parsing interface
    def apply(s: String): ParseResult[AST] = parseAll(program, s)
    
    lazy val program: PackratParser[Program] =
    	(opt(tableSize)~"def"~"function"~arguments~":"~
      		"if"~"("~baseCases~")"~":"~"return"~
      		"else"~"consider"~":"~
      			recursiveCases
      	^^ {case tableSize~"def"~"function"~arguments~":"~
      			     "if"~"("~baseCases~")"~":"~"return"~
      			     "else"~"consider"~":"~
      			     	recursiveCases
      			 => {val size = tableSize match {case Some(parsedSize) => parsedSize case _ => DefaultSize()}
      			     Program(arguments, size, baseCases, recursiveCases)}
      			 }
    	| failure("Invalid syntax")
    	)
    
    lazy val tableSize: PackratParser[TableSize] = (
        ("i"~"<="~number~","~"j"~"<="~number) ^^ {case ("i"~"<="~iMax~","~"j"~"<="~jMax) => TwoDSize(iMax, jMax)}
        | ("i"~"<="~number) ^^ {case ("i"~"<="~number) => OneDSize(number)}
        | failure("Invalid syntax in custom table size.")
    )
    	
    lazy val arguments: PackratParser[Dimension] = (
          "("~"i"~")" ^^ {case "("~"i"~")" => OneD()}
        | "("~"i"~","~"j"~")" ^^ {case "("~"i"~","~"j"~")" => TwoD()}
        | failure("Function arguments must be i and j.")
    )
     
    lazy val baseCases: PackratParser[BaseCases] = ( 
        rep1sep(baseCase, ",") ^^ {case rules => BaseCases(rules)}
        | failure("Must provide 1 base case!")
    )
    
    lazy val baseCase: PackratParser[BaseCase] = (
        rep1sep(clause, "&&") ^^ {case clauses => BaseCase(clauses)}
        | failure("Must have at least one clause in a base case")
    )
    
    lazy val clause: PackratParser[Clause] = (
        variable~comparator~value ^^ {case variable~comparator~value => Clause(variable, comparator, value)}   
    )
    
    lazy val variable: PackratParser[Variable] = (
        "i" ^^ {case "i" => iVal()}
        | "j" ^^ {case "j" => jVal()}
    )
    
    lazy val comparator: PackratParser[Comparator] = (
        "<=" ^^ {case "<=" => isLE()}
        | ">=" ^^ {case ">=" => isGE()}
        | "<" ^^ {case "<" => isL()}
        | ">" ^^ {case ">" => isG()}
        | "==" ^^ {case "==" => isE()}
        | failure("A base case clause must have at least one recognized parameters (<, <=, ==, >=, or >).")
    )
    
    lazy val value: PackratParser[Value] = (
        variable ^^ {case variable => variable}
        | "n" ^^ {case "n" => n()}
        | number ^^ {case number => intValue(number)}
    )
    
    //lazy val baseCase: PackratParser[Cell] = ()
     
    lazy val recursiveCases: PackratParser[Dependencies] = (
        rep1sep(recursiveCase, ",") ^^ {case rules => Dependencies(rules)}
        | failure("must provide 1 dependency")
    )
     
    lazy val recursiveCase: PackratParser[Dependency] = (
        indices~"~"~indices ^^ {case beginIndices~"~"~endIndices => Dependency(beginIndices, endIndices)}
        | indices ^^ {case beginIndices => Dependency(beginIndices, beginIndices)}
        | failure("Incorrectly formatted dependency")
    )
    
    lazy val indices: PackratParser[Indices] = (
        "function"~"("~iIndex~","~jIndex~")" ^^ {case "function"~"("~iIndex~","~jIndex~")" => TwoDIndices(iIndex, jIndex)}
        | "function"~"("~iIndex~")" ^^ {case "function"~"("~iIndex~")" => OneDIndices(iIndex)}
    )
    
    lazy val iIndex: PackratParser[Index] = (
        number ^^ {case num => AbsIndex(num)}
        | "i"~"+"~number ^^ {case "i"~"+"~num => RelativeIndex(num)}
        | "i"~"-"~number ^^ {case "i"~"-"~num => RelativeIndex(-num)}
        | "i" ^^ {case "i" => RelativeIndex(0)}
    )
    
    lazy val jIndex: PackratParser[Index] = (
        number ^^ {case num => AbsIndex(num)}
        | "j"~"+"~number ^^ {case "j"~"+"~num => RelativeIndex(num)}
        | "j"~"-"~number ^^ {case "j"~"-"~num => RelativeIndex(-num)}
        | "j" ^^ {case "j" => RelativeIndex(0)}
    )
    
    def number: Parser[Int] = wholeNumber ^^ {
        s => (s.toInt)}
}
