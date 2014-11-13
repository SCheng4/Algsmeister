package algsmeister.ir

sealed abstract class AST
case class Program(dimension: Dimension, baseCases: BaseCases, dependencies: Dependencies) extends AST

sealed abstract class Dimension extends AST
case class OneD() extends Dimension
case class TwoD() extends Dimension
// extends to 3D DP tables
//case class ThreeD() extends Dimension

case class BaseCases(baseCase: List[Cell]) extends AST
case class Dependencies(dependencies: List[Dependency]) extends AST

sealed abstract class Cell extends AST
case class OneDCell(col: Int) extends Cell
case class TwoDCell(row: Int, col: Int) extends Cell
// extends to 3D DP tables
//case class ThreeDCell(slice: Int, row: Int, col: Int) extends Cell

case class Dependency(start: Indices, end: Indices) extends AST

sealed abstract class Indices extends AST
case class OneDIndices(i: Index) extends Indices
case class TwoDIndices(i: Index, j:Index) extends Indices

sealed abstract class Index extends AST
case class AbsIndex(index: Int) extends Index
case class RelativeIndex(offset: Int) extends Index

// extends to 3D DP tables
//case class ThreeDDep(sliceOffset: Int, rowOffset: Int, colOffset: Int) extends Dependency

// extends to non-constant numbers of dependencies
// case class cellsAbove() extends Dependency
