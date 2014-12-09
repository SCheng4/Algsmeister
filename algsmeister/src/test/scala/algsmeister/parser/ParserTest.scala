package algsmeister.parser

import org.scalatest._

import algsmeister.ir._
import algsmeister.parser._
import edu.hmc.langtools._

class ParserTest extends FunSpec with LangParseMatchers[AST] {
    override val parser = funcParser.apply _
 
    describe("A program") {

        it("should be able to parse the basic 1D fib function") {
            program("def function(i): if (i == 0, i == 1): return else consider: function(i - 1), function(i - 2)") should parseAs (
            	Program(
            	    OneD(10), 
            	    BaseCases(List(
            	        BaseCase(List(Clause(iVal(),isE(),intValue(0)))),
            	        BaseCase(List(Clause(iVal(),isE(),intValue(1)))))),
            	    Dependencies(List(
            	        Dependency(OneDIndices(RelativeIndex(-1)),OneDIndices(RelativeIndex(-1))),
            	        Dependency(OneDIndices(RelativeIndex(-2)),OneDIndices(RelativeIndex(-2))))))
            )
        }
        
    	it("should be able to have all types of 1D dependencies") {
    		program("def function(i): if (i == 0): return else consider: function(0), function(1), function(i-1), function(0) ~ function(i-1)") should parseAs (
    		    Program(
    		    	OneD(10), 
    		    	BaseCases(List(BaseCase(List(Clause(iVal(),isE(),intValue(0)))))),
    		    	Dependencies(List(
    		    			Dependency(OneDIndices(AbsIndex(0)),OneDIndices(AbsIndex(0))),
    		    			Dependency(OneDIndices(AbsIndex(1)),OneDIndices(AbsIndex(1))),
    		    			Dependency(OneDIndices(RelativeIndex(-1)),OneDIndices(RelativeIndex(-1))),
    		    			Dependency(OneDIndices(AbsIndex(0)),OneDIndices(RelativeIndex(-1)))))))
    	}
    	
    	it("should be able to parse lots of different 2D dependencies") {
    	    program("def function(i, j): if (i == 0): return else consider: function(0,0), function(i-1, j-1), function(1,1) ~ function(5,5)") should parseAs (
    	    	Program(
    	    		TwoD(10,10), 
    	    		BaseCases(List(BaseCase(List(Clause(iVal(),isE(),intValue(0)))))),
    	    		Dependencies(List(
    	    			Dependency(TwoDIndices(AbsIndex(0),AbsIndex(0)),TwoDIndices(AbsIndex(0),AbsIndex(0))),
    	    			Dependency(TwoDIndices(RelativeIndex(-1),RelativeIndex(-1)),TwoDIndices(RelativeIndex(-1),RelativeIndex(-1))),
    	    			Dependency(TwoDIndices(AbsIndex(1),AbsIndex(1)),TwoDIndices(AbsIndex(5),AbsIndex(5)))))))
    	}
    	
    	it("should be able to parse lots of different 1D base cases") {
    	    program("def function(i): if (i <= 5, i < 5, i == 5, i > 5, i >= 5, i == n): return else consider: function(i - 1)") should parseAs (
    	        Program(
    	            OneD(10), 
    	            BaseCases(List(
    	                BaseCase(List(Clause(iVal(),isLE(),intValue(5)))),
    	                BaseCase(List(Clause(iVal(),isL(),intValue(5)))),
    	                BaseCase(List(Clause(iVal(),isE(),intValue(5)))),
    	                BaseCase(List(Clause(iVal(),isG(),intValue(5)))),
    	                BaseCase(List(Clause(iVal(),isGE(),intValue(5)))),
    	                BaseCase(List(Clause(iVal(),isE(),n()))))),
    	            Dependencies(List(Dependency(OneDIndices(RelativeIndex(-1)),OneDIndices(RelativeIndex(-1))))))
    	    )
    	}
    	
    	it("should be able to parse lots of different 2D base cases") {
    	    program("def function(i,j): if (i == 5, j == 5, i == j, j == i, i == n, j == n): return else consider: function(i - 1)") should parseAs (
    	        Program(
    	            TwoD(10,10), 
    	            BaseCases(List(
    	                BaseCase(List(Clause(iVal(),isE(),intValue(5)))),
    	                BaseCase(List(Clause(jVal(),isE(),intValue(5)))),
    	                BaseCase(List(Clause(iVal(),isE(),jVal()))),
    	                BaseCase(List(Clause(jVal(),isE(),iVal()))),
    	                BaseCase(List(Clause(iVal(),isE(),n()))),
    	                BaseCase(List(Clause(jVal(),isE(),n()))))),
    	                Dependencies(List(Dependency(OneDIndices(RelativeIndex(-1)),OneDIndices(RelativeIndex(-1))))))
    	    )
    	}
    	
    	it("should be able to parse 1D custom table size") {
    	    program("i <= 15 def function(i): if (i == 0, i == 1): return else consider: function(i - 1), function(i - 2)") should parseAs (
            	Program(
            	    OneD(15),
            	    BaseCases(List(
            	        BaseCase(List(Clause(iVal(),isE(),intValue(0)))),
            	        BaseCase(List(Clause(iVal(),isE(),intValue(1)))))),
            	    Dependencies(List(
            	        Dependency(OneDIndices(RelativeIndex(-1)),OneDIndices(RelativeIndex(-1))),
            	        Dependency(OneDIndices(RelativeIndex(-2)),OneDIndices(RelativeIndex(-2))))))
            )
        }
    	
    	
    }
}
