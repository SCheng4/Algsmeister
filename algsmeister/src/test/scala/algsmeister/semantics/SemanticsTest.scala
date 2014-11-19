package algsmeister.semantics

import org.scalatest._

import algsmeister.ir._
import algsmeister.parser._
import algsmeister.semantics._
import edu.hmc.langtools._

class AlgsSemanticsTests extends FunSpec
    with LangInterpretMatchers[AST, Unit] {
  override val parser = funcParser.apply _
  override val interpreter = evalProgram _
  
  describe("A program") {
      it("should work for a recursive fib function") {
          program("def function(i): if (i == 0, i == 1): return else consider: function(i - 1), function(i - 2)") should compute (
               List(OneDCell(0), OneDCell(1), OneDCell(2), OneDCell(3), OneDCell(4), OneDCell(5), OneDCell(6), OneDCell(7), OneDCell(8), OneDCell(9))   
          )
      }
      
      
      
  }
}
