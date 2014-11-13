package algsmeister

import algsmeister.ir._

package object semantics extends App{
    
    val TABLE_SIZE = 10;
    
	def evalProgram(ast: AST): Unit = {
		ast match {
		case Program(dimension, baseCases, dependencies) => {
			val DPTable = evalDimension(dimension);
			val graph = generateGraph(dimension, baseCases, dependencies)
			println(graph)
			println(tsort(graph))
		}
		case _ => {
			throw new MatchError("Malformed program.");
			}
		}
	}

	def evalDimension(dim: Dimension): DPTable = {
		dim match {
			case OneD() => new OneDTable(new Array[Int](TABLE_SIZE))
			case TwoD() => new TwoDTable(Array.ofDim[Int](TABLE_SIZE, TABLE_SIZE))
		}
	}
	
	def generateGraph(dimension: Dimension, baseCases: BaseCases, dependency: Dependencies): Traversable[(Cell, Cell)] = {
		dimension match {
			case OneD() => {
				(0 to TABLE_SIZE - 1).toList.foldLeft(List[(Cell, Cell)]())((list, i) => {
      				val cell = OneDCell(i)
      				if (!baseCases.baseCase.contains(cell)) list ::: generateEdgesForCell(cell, dependency)
      				else list
      					//graph = graph ::: generateEdges(cell, dependency)
      			})
			}
			
			case TwoD() => {
				// TODO placeholder
				Seq((OneDCell(1), OneDCell(1)))
			}		
		}
	}
	
	def generateEdgesForCell(cell: Cell, dependency: Dependencies): List[(Cell, Cell)] = {
		dependency.dependencies.foldLeft(List[(Cell, Cell)]())((list, dep) => {
			(cell, dep.start, dep.end) match {
				case (OneDCell(i), OneDIndices(start), OneDIndices(end)) => {
				    
				    val startIndex = start match {
				        case AbsIndex(index) => index
				        case RelativeIndex(offset) => i + offset
				    }
				    
				    val endIndex = end match {
				        case AbsIndex(index) => index
				        case RelativeIndex(offset) => i + offset
				    }
				    
				    list ::: (startIndex to endIndex).toList.foldLeft(List[(Cell, Cell)]())((list, i) => {
				        if (i >= 0 && i < TABLE_SIZE) (list :+ (OneDCell(i), cell))
				        else sys.error("The DP table cannot be filled out. Check your base cases and dependencies!")
				    })
				    
				}
//				case (TwoDCell(i, j), TwoDDep(ioffset, joffset)) => {
//					// TODO: placeholder
//					List((OneDCell(1), OneDCell(1)))
//				}
				case _ => throw new MatchError("Mismatched parameters.");
			
			}}
		)
	}
	
	
	def evalBaseCases(dpTable: DPTable, baseCases: BaseCases): DPTable = {
		dpTable match {
			case OneDTable(table) => {
				dpTable
			}
			
			case TwoDTable(table) => {
				dpTable
			}
		}
	}

	class DPTable()
	
	case class OneDTable(table: Array[Int]) extends DPTable {
	}
	
	case class TwoDTable(table: Array[Array[Int]]) extends DPTable {
	}

	// A function for topological sort
	// Credit: https://gist.github.com/ThiporKong/4399695
	def tsort[A](edges: Traversable[(A, A)]): Iterable[A] = {
	    def tsort(toPreds: Map[A, Set[A]], done: Iterable[A]): Iterable[A] = {
	        val (noPreds, hasPreds) = toPreds.partition { _._2.isEmpty }
	        if (noPreds.isEmpty) {
	            if (hasPreds.isEmpty) done else sys.error("The recursive function has circular dependency and a DP solution is not possible")
	        } else {
	            val found = noPreds.map { _._1 }
	            tsort(hasPreds.mapValues { _ -- found }, done ++ found)    
	        }
	    }
	 
	    val toPred = edges.foldLeft(Map[A, Set[A]]()) { (acc, e) =>
	        acc + (e._1 -> acc.getOrElse(e._1, Set())) + (e._2 -> (acc.getOrElse(e._2, Set()) + e._1))
	    }
	    tsort(toPred, Seq())
	}

	//evalProgram(Program(OneD(), BaseCases(List(OneDCell(0))), Dependencies(List(Dependency(OneDIndices(relativeIndex(-1)), OneDIndices(relativeIndex(-1)))))))
	evalProgram(Program(OneD(), BaseCases(List(OneDCell(0), OneDCell(1))), Dependencies(List(Dependency(OneDIndices(AbsIndex(0)), OneDIndices(RelativeIndex(-1)))))))

	
	//println(generateEdges(OneDCell(4), Dependencies(List(OneDDep(-1), OneDDep(10), OneDDep(-10)))))
	//evalProgram(Program(TwoD(), BaseCase(List()), Dependencies(List())))
}