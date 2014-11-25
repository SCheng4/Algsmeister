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
  
  describe("A program for 1D inputs") {
      it("should work for a recursive fib function") {
          program("def function(i): if (i == 0, i == 1): return else consider: function(i - 1), function(i - 2)") should compute (
               List(OneDCell(0), OneDCell(1), OneDCell(2), OneDCell(3), OneDCell(4), OneDCell(5), OneDCell(6), OneDCell(7), OneDCell(8), OneDCell(9)))
      }
      
      it("should work for a function with continuous base case range") {
          program("def function(i): if (i >= 0 && i <= 5): return else consider: function(i - 1), function(i - 2)") should compute (
               List(OneDCell(4), OneDCell(5), OneDCell(6), OneDCell(7), OneDCell(8), OneDCell(9)))
      }
      
      it("should work for a function with continuous absolute dependency range") {
          program("def function(i): if (i == 0, i == 1): return else consider: function(0) ~ function(1)") should compute (
              List(OneDCell(0), OneDCell(1), OneDCell(2), OneDCell(3), OneDCell(4), OneDCell(5), OneDCell(6), OneDCell(7), OneDCell(8), OneDCell(9)))
      }
      
      it("should work for a function with continuous relative dependency range") {
    	  program("def function(i): if (i == 0, i == 1): return else consider: function(i - 2) ~ function(i - 1)") should compute (
    	      List(OneDCell(0), OneDCell(1), OneDCell(2), OneDCell(3), OneDCell(4), OneDCell(5), OneDCell(6), OneDCell(7), OneDCell(8), OneDCell(9)))
      }
      
      it("should work for a function with multiple base cases") {
          program("def function(i): if (i == 0, i == n): return else consider: function(i - 1)") should compute (
              List(OneDCell(0), OneDCell(1), OneDCell(2), OneDCell(3), OneDCell(4), OneDCell(5), OneDCell(6), OneDCell(7), OneDCell(8), OneDCell(9)))
      }
      
      it("should work for a function with weird base cases") {
          program("def function(i): if (i <= 5 && i <= 4 && i == 0): return else consider: function(i - 1)") should compute (
              List(OneDCell(0), OneDCell(1), OneDCell(2), OneDCell(3), OneDCell(4), OneDCell(5), OneDCell(6), OneDCell(7), OneDCell(8), OneDCell(9)))
      }
      
      it("should work for a function evaluted in a different order") {
          program("def function(i): if (i == n): return else consider: function(i + 1)") should compute (
              List(OneDCell(9), OneDCell(8), OneDCell(7), OneDCell(6), OneDCell(5), OneDCell(4), OneDCell(3), OneDCell(2), OneDCell(1), OneDCell(0)))
      }
  }
}
