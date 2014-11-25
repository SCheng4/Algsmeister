package algsmeister

import algsmeister.ir._

package object semantics {
    
    val TABLE_SIZE = 10;
    
    class DPTable()
	case class OneDTable(table: Array[Int]) extends DPTable
	case class TwoDTable(table: Array[Array[Int]]) extends DPTable
    
	def evalProgram(ast: AST): Unit = {
		ast match {
		case Program(dimension, baseCases, dependencies) => {
		    //println(ast)
			val evaluatedBaseCases = evalBaseCases(dimension, baseCases)
			println(evaluatedBaseCases)
			val graph = generateGraph(dimension, evaluatedBaseCases, dependencies)
			//println(graph)
			val orderedCells = tsort(graph).toList
			println(orderedCells)
			val DPTable = fillInTable(dimension, evaluatedBaseCases, orderedCells)
			printDPTable(DPTable)
		}
		case _ => {
			throw new MatchError("Malformed program.");
			}
		}
	}
	
	def fillInTable(dim: Dimension, baseCases: List[Cell], cells: List[Cell]): DPTable = {
	    dim match {
	        case OneD() => {
	            val table = new OneDTable(new Array[Int](TABLE_SIZE))
	            var k = 1
	            for (x <- 0 until cells.length) {
	                val cell = cells(x)
	                cell match {
	                    case OneDCell(i) => {
	                        if (!baseCases.contains(cell)) {table.table(i) = k; k += 1}
	                    }
	                    case TwoDCell(i, j) => sys.error("OneDCell expected.")
	                }
	            }
	            table
	        }
	        case TwoD() => new TwoDTable(Array.ofDim[Int](TABLE_SIZE, TABLE_SIZE)) // TODO
	    }
	}
	
	def printDPTable(table: DPTable): Unit = {
	    table match {
	        case OneDTable(cells) => {
	            println("The DP table has dimension 1 x n.")
	            printProgramInfo()
	            println{"+---+---+---+---+---+---+---+---+---+---+"}
	            print("|")
	            for (i <- 0 until cells.length) {
	                print(" " + cells(i) + " |")
	            }
	            println{"\n+---+---+---+---+---+---+---+---+---+---+"}
	        }
	        
	        case TwoDTable(cells) => { //TODO 
	        }
	    }
	}
	
	def printProgramInfo(): Unit = {
		println("These cells labelled with 0 are the base cases and can be filled in for free.")
	    println("The other numbers indicate one order in which the remaining cells can be filled in.")
	    println("(Note that there are frequently more than one order to fill the DP table.)")
	}
	
	def evalBaseCases(dimension: Dimension, baseCases: BaseCases): List[Cell] = {
		baseCases.baseCases.foldLeft(List[Cell]())((list, baseCase) => {
		    val evaluated = evalBaseCase(dimension, baseCase)
		    if (evaluated.size == 0) sys.error("Unsatisfiable base case condition found")
		    else list ::: evalBaseCase(dimension, baseCase)
		})
	}
	
	def evalBaseCase(dimension: Dimension, baseCase: BaseCase): List[Cell] = {
	    dimension match {
	        case OneD() => {
	            (0 to TABLE_SIZE - 1).toList.foldLeft(List[Cell]())((list, i) => {
	                val cell = OneDCell(i)
	                val evaluation = baseCase.clauses.foldLeft(true)((bool, clause) => {
	                	clause.variable match {
	                	    case iVal() => {evalClause(cell, clause) && bool}
	                	    case jVal() => {sys.error("Incorrectly formatted basecase")}
	                	}
	                })
	                
	                if (evaluation) list ::: List(cell) else list
	            })
	        }
	        
	        case TwoD() => {
	            sys.error("implement 2D stuff here")
	        }
	    }
	}
	
	def evalClause(cell: Cell, clause: Clause): Boolean = {
	    (cell, clause.variable) match {
	        case (OneDCell(i), iVal()) => {
	            val condition = clause.value match {
	                case intValue(k) => k
	                case n() => TABLE_SIZE - 1
	                case iVal() => sys.error("cannot use same value on both side of base case condition!")
	                case jVal() => sys.error("invalid condition value for 1D function")
	            }
	            
	            clause.comparator match {
	                case isL() => {i < condition}
	                case isG() => {i > condition}
	                case isLE() => {i <= condition}
	                case isGE() => {i >= condition}
	                case isE() => {i == condition}
	            }
	        }
	        
	        case (TwoDCell(i, j), _) => {
	            // TODO: implement 2D baseCase evaluations
	            sys.error("2D baseCase evaluations not yet implemented")
	        }
	        
	        case _ => {
	            sys.error("base case clause do not match dimension of the DP table")
	        }
	    }
	}
	
	def generateGraph(dimension: Dimension, baseCases: List[Cell], dependency: Dependencies): Traversable[(Cell, Cell)] = {
		dimension match {
			case OneD() => {
				(0 to TABLE_SIZE - 1).toList.foldLeft(List[(Cell, Cell)]())((list, i) => {
      				val cell = OneDCell(i)
      				if (!baseCases.contains(cell)) list ::: generateEdgesForCell(cell, dependency)
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
}