//package algsmeister.parser
//
//import scala.util.parsing.combinator._
//import algsmeister.ir._
//
//object funcParser extends JavaTokenParsers with PackratParsers {
//	// parsing interface
//
//    lazy val program: PackratParser[Program] =
//    	("def"~"function"~"("~arguments~")"~
//      		"if"~"("~baseCases~")"~":"~"return"~
//      		"else"~"consider"~":"~
//      			recursiveCases
//      	^^{case _ => Program(OneD(), BaseCases(List(OneDCell(0))), Dependencies(OneDDep(-1)))})
//    
//     lazy val arguments: PackratParser[Dimension] = 
//         ("i" ^^ {case "i" => OneD()}
//          | "i"~","~"j" ^^ {case "i"~","~"j" => TwoD()})
//     
//     lazy val baseCases: PackratParser[BaseCases] = ()
//     
//     lazy val recursiveCases: PackratParser[Dependencies] = ()
//     
// }
