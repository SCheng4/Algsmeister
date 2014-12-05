package algsmeister.ir

sealed abstract class AST
case class Program(dimension: Dimension, size: TableSize, baseCases: BaseCases, dependencies: Dependencies) extends AST

sealed abstract class Dimension extends AST
case class OneD() extends Dimension
case class TwoD() extends Dimension

sealed abstract class TableSize extends AST
case class OneDSize(maxI: Int) extends TableSize
case class TwoDSize(maxI: Int, maxJ: Int) extends TableSize
case class DefaultSize() extends TableSize

case class BaseCases(baseCases: List[BaseCase]) extends AST
case class BaseCase(clauses: List[Clause]) extends AST
case class Clause(variable: Variable, comparator: Comparator, value: Value) extends AST

sealed abstract class Value extends AST
case class intValue(value: Int) extends Value
case class n() extends Value

sealed abstract class Variable extends Value
case class jVal() extends Variable
case class iVal() extends Variable

sealed abstract class Comparator
case class isL() extends Comparator
case class isLE() extends Comparator
case class isE() extends Comparator
case class isG() extends Comparator
case class isGE() extends Comparator

case class Dependencies(dependencies: List[Dependency]) extends AST

sealed abstract class Cell extends AST
case class OneDCell(col: Int) extends Cell {
    override def toString = "cell(" + col + ")"
}
case class TwoDCell(row: Int, col: Int) extends Cell {
    override def toString = "cell(" + row + ", " + col + ")"
}

case class Dependency(start: Indices, end: Indices) extends AST

sealed abstract class Indices extends AST
case class OneDIndices(i: Index) extends Indices
case class TwoDIndices(i: Index, j:Index) extends Indices

sealed abstract class Index extends AST
case class AbsIndex(index: Int) extends Index
case class RelativeIndex(offset: Int) extends Index
