# Design and Implementation Notes

## Information needed to evaluate a program

* Array to represent DP table

* Dimension of DP table (number of arguments in recursive function)
  * Int, enum, or case class (there are only a few cases)

* Which cells correspond to the base case(s)?
  * List of tuples

* Which cells each cell is dependent on
  * List of tuples

* How to do a topo sort using above info
  * Build edge list, then use given topo sort from here: https://gist.github.com/ThiporKong/4399695

* How to represent how many cells each cell depends on? (could be constant, n, log(n), etc...)

## DP table representation design

* How to handle arbitrary sizes? Pick one?

* 1D

```
+---+---+---+---+---+---+---+---+---+---+
| n |...|...|...|...|...|...| 3 | 2 | 1 |
+---+---+---+---+---+---+---+---+---+---+
```

* 2D

```
+---+---+---+---+---+---+---+---+---+---+
| 0 | 1 | 2 | 3 |...|...|...|...|...| A |
+---+---+---+---+---+---+---+---+---+---+
|   | 0 | 1 | 2 | 3 |...|...|...|...|...|
+---+---+---+---+---+---+---+---+---+---+
|   |   | 0 | 1 | 2 | 3 |...|...|...|...|
+---+---+---+---+---+---+---+---+---+---+
|   |   |   | 0 | 1 | 2 | 3 |...|...|...|
+---+---+---+---+---+---+---+---+---+---+
|   |   |   |   | 0 | 1 | 2 | 3 |...|...|
+---+---+---+---+---+---+---+---+---+---+
|   |   |   |   |   | 0 | 1 | 2 | 3 |...|
+---+---+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   | 0 | 1 | 2 | 3 |
+---+---+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   |   | 0 | 1 | 2 |
+---+---+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   |   |   | 0 | 1 |
+---+---+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   |   |   |   | 0 |
+---+---+---+---+---+---+---+---+---+---+
```

```
+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
+---+---+---+---+---+---+---+---+---+---+
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
+---+---+---+---+---+---+---+---+---+---+
| 0 | 10| 11| 12| 13| 14| 15| 16| 17| 18|
+---+---+---+---+---+---+---+---+---+---+
| 0 |...|...|   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+---+---+
| 0 |   |   |   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+---+---+
| 0 |   |   |   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+---+---+
| 0 |   |   |   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+---+---+
| 0 |   |   |   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+---+---+
| 0 |   |   |   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+---+---+
| 0 |   |   |   |   |   |   |   |   | A |
+---+---+---+---+---+---+---+---+---+---+
```

## Ideas for 3D

* Display 2 slices, one in the middle and the final slice

* Display 1 slice, then display other information using text

* Ideal vision: fancy 3D graphic, allow user to swipe through slices.



