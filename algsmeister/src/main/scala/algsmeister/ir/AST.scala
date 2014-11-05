package algsmeister.ir

sealed abstract class AST
case class Program(dimension: Dimension, baseCases: BaseCase, dependencies: Dependencies) extends AST

sealed abstract class Dimension extends AST
case class OneD() extends Dimension
case class TwoD() extends Dimension
// extends to 3D DP tables
//case class ThreeD() extends Dimension

case class BaseCase(baseCase: List[Cell]) extends AST
case class Dependencies(dependencies: List[Dependency]) extends AST

sealed abstract class Cell extends AST
case class OneDCell(col: Int) extends Cell
case class TwoDCell(row: Int, col: Int) extends Cell
// extends to 3D DP tables
//case class ThreeDCell(slice: Int, row: Int, col: Int) extends Cell

sealed abstract class Dependency extends AST
case class OneDDep(colOffset: Int) extends Dependency
case class TwoDDep(rowOffset: Int, colOffset: Int) extends Dependency
// extends to 3D DP tables
//case class ThreeDDep(sliceOffset: Int, rowOffset: Int, colOffset: Int) extends Dependency

// extends to non-constant numbers of dependencies
// case class cellsAbove() extends Dependency

/*
What a program looks like in AST:





*/