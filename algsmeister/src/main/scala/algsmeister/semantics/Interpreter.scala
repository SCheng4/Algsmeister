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
		    println(ast)
			val evaluatedBaseCases = evalBaseCases(dimension, baseCases)
			val DPTable = fillDPTable(dimension, evaluatedBaseCases, dependencies)
			printDPTable(DPTable)
			printRuntime(dimension, dependencies)
		}
		case _ => {
			throw new MatchError("Malformed program.");
			}
		}
	}
	
    def printRuntime(dim: Dimension, dep: Dependencies) = {
        val runtime = getRuntime(dim, dep)
        val actualRuntime = dim match {
            case OneD(maxI) => if (runtime == constant()) "O(n)" else "O(n^2)"
            case TwoD(maxI, maxJ) => if (runtime == constant()) "O(n^2)" else if (runtime == linear()) "O(n^3)" else "O(n^4)"
        }
        val perCell = if (runtime == constant()) "O(1)" else if (runtime == linear()) "O(n)" else "O(n^2)"
        println("It takes up to " + perCell + " time to fill out each cell in the table, and up to a total of " + actualRuntime + " to fill out the entire table")
    }
    
    def getRuntime(dim: Dimension, dep: Dependencies): Runtime = {
        dim match {
            case OneD(maxI) => {
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
            
            case TwoD(maxI, maxJ) => {
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
    
    def fillDPTable(dim: Dimension, baseCases: List[Cell], dep: Dependencies): DPTable = {
        dim match {
            case OneD(maxI) => {
                var table = new OneDTable(new Array[Int](TABLE_SIZE))
                var k = 1
                var filledCells = baseCases;
                var unfilledCells = (0 to TABLE_SIZE - 1).toList.map(x => OneDCell(x)).filter(x => !filledCells.contains(x))
                
                while (unfilledCells.length > 0) {
                	val cellsToFill = unfilledCells.filter(x => dependencyForCell(x, dep).foldLeft(true)(
                	        (satisfied, cell) => if (filledCells.contains(cell)) satisfied else false))
                	if (cellsToFill.length == 0) sys.error("DP table cannot be filled out.")
                	for (cell <- cellsToFill) {
                		table.table(cell.col) = k
                	}
                	filledCells = filledCells ::: cellsToFill
                	unfilledCells = unfilledCells.filter(x => !cellsToFill.contains(x))
                	k += 1
                }
                table
            }
            case TwoD(maxI, maxJ) => {
                val table = new TwoDTable(Array.ofDim[Int](TABLE_SIZE, TABLE_SIZE))
                var k = 1
                var filledCells = baseCases;
                var unfilledCells = (0 to (TABLE_SIZE * TABLE_SIZE - 1)).toList.map(x => 
                    TwoDCell((x/TABLE_SIZE),(x%TABLE_SIZE))).filter(x => !filledCells.contains(x))
                
                 while (unfilledCells.length > 0) {                     
                	val cellsToFill = unfilledCells.filter(x => dependencyForCell(x, dep).foldLeft(true)(
                	        (satisfied, cell) => if (filledCells.contains(cell)) satisfied else false))
                	if (cellsToFill.length == 0) sys.error("DP table cannot be filled out.")
                	for (cell <- cellsToFill) {
                		table.table(cell.row)(cell.col) = k
                	}
                	filledCells = filledCells ::: cellsToFill
                	unfilledCells = unfilledCells.filter(x => !cellsToFill.contains(x))
                	k += 1
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
	
	def evalBaseCases(dimension: Dimension, baseCases: BaseCases): List[Cell] = {
		dimension match {
		    case OneD(maxI) => {
		        (0 to TABLE_SIZE - 1).toList.foldLeft(List[Cell]())((list, i) => {
		        	val cell = OneDCell(i)
		        	if (isBaseCase(cell, baseCases)) list :+ cell else list
		        })
		    }
		    case TwoD(maxI, maxJ) => {
		        (0 to (TABLE_SIZE * TABLE_SIZE - 1)).toList.foldLeft(List[Cell]())((list, i) => {
	                val cell = TwoDCell(i / TABLE_SIZE, i % TABLE_SIZE)
	                if (isBaseCase(cell, baseCases)) list :+ cell else list
		        })     
	}}}
		        
	
	def isBaseCase(cell: Cell, baseCases: BaseCases): Boolean = {
	    baseCases.baseCases.foldLeft(false)((bool, baseCase) => {
	    	bool || baseCase.clauses.foldLeft(true)((bool, clause) => {
			    evalClause(cell, clause) && bool
            })})
	}
	
	def evalClause(cell: Cell, clause: Clause): Boolean = {
	    (cell, clause.variable) match {
	        case (OneDCell(i), iVal()) => {
	            val condition = clause.value match {
	                case intValue(k) => k
	                case n() => TABLE_SIZE - 1
	                case iVal() => sys.error("You cannot use same value on both side of base case condition!")
	                case jVal() => sys.error("j is not a valid value in a 1D function")
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
	                case iVal() => sys.error("You cannot use same value on both side of base case condition!")
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
	                case jVal() => sys.error("You cannot use same value on both side of base case condition!")
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
	            sys.error("A base case does not match dimension of the DP table")
	        }
	    }
	}
	
	def dependencyForCell(cell: Cell, dependency: Dependencies): List[Cell] = {
		dependency.dependencies.foldLeft(List[Cell]())((list, dep) => {
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
				    
				    list ::: (startIndex to endIndex).toList.foldLeft(List[Cell]())((list, index) => {
				        if (index >= 0 && index < TABLE_SIZE) (list :+ OneDCell(index))
				        else sys.error("The DP table cannot be filled out: " + cell + " depends on " + OneDCell(index) + ", which is outside the bounds of the DP table.")
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
				    
				    var cellsToAdd = List[Cell]()
				    for (iIndex <- iStartIndex until iEndIndex + 1) {
				        for (jIndex <- jStartIndex until jEndIndex + 1) {
				            if (iIndex >= 0 && iIndex < TABLE_SIZE && jIndex >= 0 && jIndex < TABLE_SIZE)
				                (cellsToAdd = cellsToAdd :+ TwoDCell(iIndex, jIndex))
				            else sys.error("The DP table cannot be filled out: " + cell + " depends on " + TwoDCell(iIndex, jIndex) + ", which is outside the bounds of the DP table.")
				        }
				    }
				    
				    list ::: cellsToAdd			
				}
				case _ => throw new MatchError("A recursive call has incorrect parameters.");
			
			}}
		)
	}

}