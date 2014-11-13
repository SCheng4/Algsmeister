package algsmeister.parser

import org.scalatest._

import algsmeister.ir._
import algsmeister.parser._
import edu.hmc.langtools._

class ParserTests extends FunSpec with LangParseMatchers[AST] {
    override val parser = funcParser.apply _
 
    describe("A program") {
    	it("should be able to have all types of 1D dependencies") {
    		program("def function(i): if (TODO): return else consider: function(0), function(1), function(i-1), function(0) ~ function(i-1)") should parseAs (
    		    Program(
    		    	OneD(),
    		    	BaseCases(List()),
    		    	Dependencies(List(
    		    			Dependency(OneDIndices(AbsIndex(0)),OneDIndices(AbsIndex(0))),
    		    			Dependency(OneDIndices(AbsIndex(1)),OneDIndices(AbsIndex(1))),
    		    			Dependency(OneDIndices(RelativeIndex(-1)),OneDIndices(RelativeIndex(-1))),
    		    			Dependency(OneDIndices(AbsIndex(0)),OneDIndices(RelativeIndex(-1)))))))
    	}
    	
    	it("should be able to parse some 2D dependencies") {
    	    program("def function(i, j): if (TODO): return else consider: function(0,0), function(i-1, j-1), function(1,1) ~ function(5,5)") should parseAs (
    	    	Program(
    	    		TwoD(),
    	    		BaseCases(List()),
    	    		Dependencies(List(
    	    			Dependency(TwoDIndices(AbsIndex(0),AbsIndex(0)),TwoDIndices(AbsIndex(0),AbsIndex(0))),
    	    			Dependency(TwoDIndices(RelativeIndex(-1),RelativeIndex(-1)),TwoDIndices(RelativeIndex(-1),RelativeIndex(-1))),
    	    			Dependency(TwoDIndices(AbsIndex(1),AbsIndex(1)),TwoDIndices(AbsIndex(5),AbsIndex(5)))))))
    	}
    	
    	
    	
    	
    }
}
