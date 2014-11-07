package algsmeister

import algsmeister.ir._

package object semantics extends App{
	def evalProgram(ast: AST): Unit = {
		ast match {
		case Program(dimension, baseCases, dependencies) => {
			val DPTable = evalDimension(dimension);
		}
		case _ => {
			throw new MatchError("Malformed program.");
			}
		}
	}

	def evalDimension(dim: Dimension): DPTable = {
		dim match {
			case OneD() => new OneDTable(new Array[Int](10))
			case TwoD() => new TwoDTable(Array.ofDim[Int](10, 10))
		}
	}
	
	def generateGraph(dimension: Dimension, baseCases: BaseCases, dependency: Dependencies): Traversable[(Cell, Cell)] = {
		dimension match {
			case OneD() => {
				for (i <- 0 to 9) {
      				val cell = OneDCell(i)
      				if (!baseCases.baseCase.contains(cell))
      					generateEdges(cell, dependency)
      			}
			}
			
			case TwoD() => {
				// TODO placeholder
				Seq((OneDCell(1), OneDCell(1)))
			}		
		}
		
		
		
	}
	
	def generateEdges(cell: Cell, dependency: Dependencies): Traversable[(Cell, Cell)] = {
		dependency.dependencies.foldLeft(List[(Cell, Cell)]())((list, dep) => {
			(cell, dep) match {
				case (OneDCell(i), OneDDep(offset)) => {
					val newIndex = i + offset
					if (newIndex >= 0 && newIndex <10) (list :+ (OneDCell(i + offset), cell))
					else list
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
	            if (hasPreds.isEmpty) done else sys.error(hasPreds.toString)
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

	
	println(generateEdges(OneDCell(4), Dependencies(List(OneDDep(-1), OneDDep(10), OneDDep(-10)))))

	//evalProgram(Program(TwoD(), BaseCase(List()), Dependencies(List())))
}