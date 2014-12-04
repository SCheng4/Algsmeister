package algsmeister

import algsmeister.ir._

package object semantics {
    
    val TABLE_SIZE = 10;
    
    class DPTable()
	case class OneDTable(table: Array[Int]) extends DPTable
	case class TwoDTable(table: Array[Array[Int]]) extends DPTable
	
	class Runtime()
	case class constant() extends Runtime
	case class linear() extends Runtime
	case class quad() extends Runtime
    
	def evalProgram(ast: AST): Unit = {
		ast match {
		case Program(dimension, baseCases, dependencies) => {
		    printRuntime(dimension, dependencies)
		    //println(ast)
			val evaluatedBaseCases = evalBaseCases(dimension, baseCases)
			//println(evaluatedBaseCases)
			val graph = generateGraph(dimension, evaluatedBaseCases, dependencies)
			//println(graph)
			val orderedCells = tsort(graph).toList
			//println(orderedCells)
			val DPTable = fillInTable(dimension, evaluatedBaseCases, orderedCells)
			printDPTable(DPTable)
		}
		case _ => {
			throw new MatchError("Malformed program.");
			}
		}
	}
	
    def printRuntime(dim: Dimension, dep: Dependencies) = {
        val runtime = getRuntime(dim, dep)
        val actualRuntime = dim match {
            case OneD() => if (runtime == constant()) "O(n)" else "O(n^2)"
            case TwoD() => if (runtime == constant()) "O(n^2)" else if (runtime == linear()) "O(n^3)" else "O(n^4)"
        }
        println("It takes " + actualRuntime + " to fill out the DP table")
    }
    
    def getRuntime(dim: Dimension, dep: Dependencies): Runtime = {
        dim match {
            case OneD() => {
                var isLinear = false
                for (dependency <- dep.dependencies) {
                    (dependency.start, dependency.end) match {
                        case (OneDIndices(i), OneDIndices(j)) => {
                            if (compareIndices(i,j)) isLinear = true
                        }
                    	case (_, _) => {sys.error("Incorrectly formatted dependencies")}
                    }}
                if (isLinear) new linear() else new constant()
            }
            
            case TwoD() => {
                var isLinear = false
                var isQuad = false
                for (dependency <- dep.dependencies) {
                    (dependency.start, dependency.end) match {
                        case (TwoDIndices(i1, j1), TwoDIndices(i2, j2)) => {
                            if (compareIndices(i1, i2) && compareIndices(j1, j2)) isQuad = true
                            else if (compareIndices(i1, i2) || compareIndices(j1, j2)) isLinear = true
                        }
                        case (_, _) => {sys.error("Incorrectly formatted dependencies")}
                    }
                }
                if (isQuad) new quad() else if (isLinear) new linear() else new constant() 
    }}}
    
    def compareIndices(i: Index, j: Index): Boolean = {
        (i, j) match {
            case (RelativeIndex(_), AbsIndex(_)) => true
            case (AbsIndex(_), RelativeIndex(_)) => true
            case (_, _) => false
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
	        case TwoD() => {
	            val table = new TwoDTable(Array.ofDim[Int](TABLE_SIZE, TABLE_SIZE))
	            var k = 1
	            for (x <- 0 until cells.length) {
	                val cell = cells(x)
	                cell match {
	                    case TwoDCell(i, j) => {
	                        if (!baseCases.contains(cell)) {table.table(i)(j) = k; k += 1}
	                    }
	                    case OneDCell(i) => sys.error("TwoDCell expected.")
	                }
	            }
	            table
	        }
	    }
	}
	
	def printDPTable(table: DPTable): Unit = {
	    table match {
	        case OneDTable(cells) => {
	            println("The DP table has dimension 1 x n.")
	            println{"+---+---+---+---+---+---+---+---+---+---+"}
	            print("|")
	            for (i <- 0 until cells.length) {
	                print(" " + cells(i) + " |")
	            }
	            println{"\n+---+---+---+---+---+---+---+---+---+---+"}
	        }
	        
	        case TwoDTable(cells) => {
	            println("The DP table has dimension n x n.")
	            println("+---+---+---+---+---+---+---+---+---+---+")
	            
	            for (i <- 0 until (TABLE_SIZE)) {
	                print("|")
	                for (j <- 0 until (TABLE_SIZE)) {
	                    if (cells(i)(j) < 10) print(" " + cells(i)(j) + " |")
	                    else print(" " + cells(i)(j) + "|")
	                }
	                println("\n+---+---+---+---+---+---+---+---+---+---+")
	            }
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
	            (0 to (TABLE_SIZE * TABLE_SIZE - 1)).toList.foldLeft(List[Cell]())((list, i) => {
	                val cell = TwoDCell(i / TABLE_SIZE, i % TABLE_SIZE)
	                val evaluation = baseCase.clauses.foldLeft(true)((bool, clause) => {
	                	evalClause(cell, clause) && bool
	                })
	                
	                if (evaluation) list ::: List(cell) else list
	            })     
	}}}
	
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
	        
	        case (TwoDCell(i, j), iVal()) => {
	            // TODO: implement 2D baseCase evaluations
	        	val condition = clause.value match {
	                case intValue(k) => k
	                case n() => TABLE_SIZE - 1
	                case iVal() => sys.error("cannot use same value on both side of base case condition!")
	                case jVal() => j
	            }
	            
	            clause.comparator match {
	                case isL() => {i < condition}
	                case isG() => {i > condition}
	                case isLE() => {i <= condition}
	                case isGE() => {i >= condition}
	                case isE() => {i == condition}
	            }
	        }
	        
	        case (TwoDCell(i, j), jVal()) => {
	        	val condition = clause.value match {
	                case intValue(k) => k
	                case n() => TABLE_SIZE - 1
	                case iVal() => i
	                case jVal() => sys.error("cannot use same value on both side of base case condition!")
	            }
	            
	            clause.comparator match {
	                case isL() => {j < condition}
	                case isG() => {j > condition}
	                case isLE() => {j <= condition}
	                case isGE() => {j >= condition}
	                case isE() => {j == condition}
	            }
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
      			})
			}
			
			case TwoD() => {
				(0 to (TABLE_SIZE * TABLE_SIZE - 1)).toList.foldLeft(List[(Cell, Cell)]())((list, i) => {
	                val cell = TwoDCell(i / TABLE_SIZE, i % TABLE_SIZE)
	                if (!baseCases.contains(cell)) list ::: generateEdgesForCell(cell, dependency)
	                else list
				})	
	}}}
	
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
				    
				    list ::: (startIndex to endIndex).toList.foldLeft(List[(Cell, Cell)]())((list, index) => {
				        if (index >= 0 && index < TABLE_SIZE) (list :+ (OneDCell(index), cell))
				        else sys.error("The DP table cannot be filled out. Check your base cases and dependencies!")
				    })
				    
				}
				
				case (TwoDCell(i, j), TwoDIndices(iStart, jStart), TwoDIndices(iEnd, jEnd)) => {
					
				    val iStartIndex = iStart match {
				        case AbsIndex(index) => index
				        case RelativeIndex(offset) => i + offset
				    }
				    
				    val iEndIndex = iEnd match {
				        case AbsIndex(index) => index
				        case RelativeIndex(offset) => i + offset
				    }
					
				    val jStartIndex = jStart match {
				        case AbsIndex(index) => index
				        case RelativeIndex(offset) => j + offset
				    }
				    
				    val jEndIndex = jEnd match {
				        case AbsIndex(index) => index
				        case RelativeIndex(offset) => j + offset
				    }
				    
				    val start = iStartIndex * TABLE_SIZE + jStartIndex
				    val end = iEndIndex * TABLE_SIZE + jEndIndex
				    
				    list ::: (start to end).toList.foldLeft(List[(Cell, Cell)]())((list, index) => {
				        val iIndex = index / TABLE_SIZE
				        val jIndex = index % TABLE_SIZE
				        if (iIndex >= 0 && iIndex < TABLE_SIZE && jIndex >= 0 && jIndex < TABLE_SIZE)
				            (list :+ (TwoDCell(iIndex, jIndex), cell))
				        else sys.error("The DP table cannot be filled out. Check your base cases and dependencies!")
				    })				}
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