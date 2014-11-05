package algsmeister

import algsmeister.ir._

package object semantics {
  def evalProgram(ast: AST): Unit = {
    var DP = new DPObject;
    ast match {
      case Program(dimension, baseCases, dependencies) => println("TODO")
      case _ => {
    	throw new MatchError("Malformed program.");
      }
    }
  }
  
  
  
  
  
  
  
  
  
  class DPObject();
}