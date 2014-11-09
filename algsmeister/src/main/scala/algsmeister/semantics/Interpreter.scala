package algsmeister

import algsmeister.ir._

package object semantics extends App{
    
    val TABLE_SIZE = 10;
    
	def evalProgram(ast: AST): Unit = {
		ast match {
		case Program(dimension, baseCases, dependencies) => {
			val DPTable = evalDimension(dimension);
			println(tsort(generateGraph(dimension, baseCases, dependencies)))
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
      				if (!baseCases.baseCase.contains(cell)) list ::: generateEdges(cell, dependency)
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
	
	def generateEdges(cell: Cell, dependency: Dependencies): List[(Cell, Cell)] = {
		dependency.dependencies.foldLeft(List[(Cell, Cell)]())((list, dep) => {
			(cell, dep) match {
				case (OneDCell(i), OneDDep(offset)) => {
					val newIndex = i + offset
					if (newIndex >= 0 && newIndex < TABLE_SIZE) (list :+ (OneDCell(i + offset), cell))
					else sys.error("The DP table cannot be filled out. Check your base cases and dependencies!")
				}
				case (TwoDCell(i, j), TwoDDep(ioffset, joffset)) => {
					// TODO: placeholder
					List((OneDCell(1), OneDCell(1)))
				}
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

	evalProgram(Program(OneD(), BaseCases(List(OneDCell(0))), Dependencies(List(OneDDep(-1)))))
	//println(generateEdges(OneDCell(4), Dependencies(List(OneDDep(-1), OneDDep(10), OneDDep(-10)))))
	//evalProgram(Program(TwoD(), BaseCase(List()), Dependencies(List())))
}