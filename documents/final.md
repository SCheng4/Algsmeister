# Final Write-Up

## Algsmeister - Sisi Cheng

### Introduction

For students in an algorithm class, dynamic programming (DP) is an important problem-solving technique. The method for applying DP to a problem, as taught by Prof. Ran Libeskind-Hadas of HMC, begins by writing a recursive function for the problem, a fairly intuitive process. Through a relatively mechanical series of steps, this recursive function is transformed into a corresponding DP table.

With this method, the majority of the creative process of problem-solving is achieved by arriving at a correct, DP-able recursive function. However, many students still have considerable trouble converting the recursive function into a DP table. Challenges usually arise from difficulties with mentally picturing what the DP table looks like from the recursive calls embeded in the function.

Because deriving the DP table from a valid recursive function is an uncreative process in many cases, a programming solution is possible. A DSL is a great tool for this problem because many students have a good idea of what the recursive function look like and frequently already state it in a code-like language (English pseudocode, Ran++, or simple Python). A DSL can take in a simplified and standardized recursive function, derive the DP solution, and help the students in visualizing the DP table and provide other information about the DP implementation. Furthermore, computer science students who study DP are presumably already familiar with many programming languages. A simple DSL with well-designed syntax should be intuitive to use and would not be a burden to learn and use. There are currently no existing tool for this domain.

### Language Design Details

Algsmeister is a DSL tool designed to help algorithm students understand how to perform dynamic programming on a recursive function. The DSL parses a pseudocode-like recursive function from a given text file, generates an abstract syntax tree (AST), performs analysis on this intermediate representation (IR) of the program, and outputs information to help the student. There are currently no other languages or tools in this domain.

The syntax for the DSL is formally specified as following:

```
Input =>
    tablesize
    def foo(arguments):
        if (baseCases):
            return
        else consider:
            recursiveCases

tablesize => "" | "i <= " number | "j <= " number | "i <= " number ", j <= " number

arguments => "i" | "i, j"

baseCases => baseCase "," [baseCases]
baseCase => clause "&&" [baseCase]
clause => variable comparator value
comparator => "==" | "<" | "<=" | ">" | ">="
variable => "i" | "j"
value => variable | number | "n"

recursiveCases => recursiveCase "," [recursiveCases]
recursiveCase => foo(index) | foo(index, index) ~ foo(index, index)
index => number |
         "i+" number |
         "j+" number |
         "i-" number |
         "j-" number
```

The basic computation performed by the DSL is a topological sort on the dependencies of the recursive function calls, and it then produces an intuitive order in which the DP table can be successfully filled out and prints it as an ASCII table. The DSL also outputs helpful textual information including the dimension of the table and the running time of the DP solution.
The user does not have control over the data structures or the control structure of the program.

The DSL will handle a variety of user input errors and printing helpful messages to help the user locate these errors. A list of common errors supported by the language is as following:

* Typos in keywords (ex. "funtion" instead of "function")

* Unsupported function and variable names - handled like a misspelled keyword

* Base cases and dependencies that are not in the range of the size of the DP table (ex. base case of `i = 42` when the DP table is smaller)

* Circular dependencies (i.e. `cell(i)` depends on `cell(i)`)

* Base cases and dependencies that does not produce an evaluation order (ex. the last cell is the base case, but all other cells depend on the cells before them)

* Base case and dependency that does not match the dimension of the function.

* No base case or no dependency provided

* A base case that does not match any cells (ex. `i == 0 && i == 1`)

### Example Programs
A Fibonacci program demonstrates the most basic features of the program. The base cases of the recursive function go in the `if` statement. The recursive calls is formatted as a list in the `consider` block. The syntax of the DSL is not whitespace-sensitive.

```
def function(i):
	if (i == 0, i == 1):
		return
	else consider:
		function(i - 1), function (i - 2)
```

This program produces the output

```
The DP table has dimension 1 x n.
+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
+---+---+---+---+---+---+---+---+---+---+
It takes up to O(1) time to fill out each cell in the table, and up to a total of O(n) to fill out the entire table.
```

The cells filled with `0` indicate a base case cell that can be filled in first for free. The other numbers indicate an order in which the cells can be filled in. Cells with the same number can be filled in at the same time.

The user can enter a dependency clause which specifies a range of cells using `~`:

```
def function(i):
	if (i == 0):
		return
	else consider:
		function(0) ~ function(i-1)
```
This program produces the output:
```
The DP table has dimension 1 x n.
+---+---+---+---+---+---+---+---+---+---+
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
+---+---+---+---+---+---+---+---+---+---+
It takes up to O(n) time to fill out each cell in the table, and up to a total of O(n^2) to fill out the entire table.
```

Note that the running time the DSL produces is an upper-bound. Depending on the specific recursive algorithm, the actual DP solution can be asymptotically faster, but not slower.

The user can enter a table size of their preference by specifying the maximum values for `i` and `j` as in the following program (if unspecified, the language defaults to using `10` as the table size):

```
i <= 15, j <= 15
def function(i, j):
	if (i >= j):
		return
	else consider:
		function(i, j-1)
```

This program produces the output:
```
The DP table has dimension n x n.
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11| 12| 13| 14|
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11| 12| 13|
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11| 12|
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10| 11|
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10|
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 | 6 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 | 5 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 | 4 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 | 3 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 | 2 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 1 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+
It takes up to O(1) time to fill out each cell in the table, and up to a total of O(n^2) to fill out the entire table.
```

### Language Implementation

I chose to implement Algsmeister as an external DSL because it allows me much greater freedom in choosing the syntax of the language. I'm able to easily make the syntax much closer to Python pseudocode. I chose Scala as the host language because I'm already familiar with how to set up a project in Scala, and because the built-in PackratParser provides moderately good support for custom error messages on syntax errors. However, the choice of the host language is not crucial to my project, and the DSL can be implemented using a variety of available parsers and languages.

The architecture of my program consists of three main parts: the parser, the intermediate representation, and the evaluator. The parser parses the user input into the the IR. The evaluator performs computation on the IR and produces the output that the user sees.

The parsing and parser test components requires the standard Scala PackratParser library and the Harvey Mudd Langtools library. The parser is a simple recursive-descent parser that extracts the information needed from the user input and creates the IR.

A program in the IR is composed of 3 components, the dimension, base cases, and dependencies. The dimension indicates whether the input function is 1D or 2D and stores the size of the finite DP table to be evaluated, which can be the default size or a custom size entered by the user. The base cases contains a list of base case conditions of the input function. The dependencies contains a list of dependencies for any non-base-case cell in the DP table. In order to represent all possible forms of dependencies (ex. "cell(i,j) depends on all cells above it in the DP table"), each dependency is composed of two references to cells, a begin index and an end index. Below is a class diagram with more details on the data structures of the IR for a program in Algsmeister:

![image](http://i59.tinypic.com/30jhbtt.jpg)

Once the IR of a program is created, the evaluator performs computation on the representation to create the final output. Based on the dimension of the IR, evaluator creates an empty DP table, then finds all cells in the table that matches the base case conditions. Then the evaluator performs a topological breadth-first search. At each step of the search, the evaluator iterates through all the unfilled cells in the DP table and their dependencies, finding all cells that can now be evaluated, and adding them to the list of evaluated cells.

To determine the runtime of the DP solution, the evaluator determines the number of cells each cell in the DP depends on from the dependencies stored in the IR. Each cell depends on either `O(1)`, `O(n)`, or `O(n^2)` other cells.

### Evaluation

Algsmeister is a very domain-specific language. It has a limited and restrictive syntax; the user can only input one recursive function; the function can only have one or two integral arguments; the names of the function and the arguments are restricted to those defined by the DSL. Algsmeister is also designed for a very specific purpose, and performs analysis on the input function instead of treating the input as a function. In these ways, Algsmeister is far away from a general-purpose language.

Algsmeister's biggest strength is its computation model: the calculation of a dependency graph, has worked well for the DP domain. The application of DP is inherently a dependency problem. Modeling recursive function calls as a graph then applying a topological sort is both appropriate and a good design choice. Furthermore, I'm pleased with the architecture of the program, particularly the IR and the organization of the evaluator. The IR was designed with extensibility in mind. It was almost trivial to expand the program to support 2D functions in addition to 1D functions. The evaluator is also broken down into cohesive and useful subroutines which made refactoring of the program relatively easy during the development of the DSL.

There are a few areas that can be improved. Some aspects of the user interface could be better. The user is currently limited to use hardcoded function name and variable names. While this does not take away from the usibility of the DSL, it can be somewhat unnatural. The syntax of the language is also designed to be similar to Python, but the function is stripped down to just what is needed for the evaluation. The language may be a lot better if it can parse real Python code, then calculate the dependency directly from a working, recursive Python function.

The implementation of the language could also be even more extensible if the I had chosen a more object-oriented host language. It is most important for the implementation of the DSL to be extensible in the direction of supporting more different dimensions of DP functions. This involved adding more objects of different types, or "nouns". Object-oriented languages are therefore more suitable for this purpose. In Scala, to add a new dimension, the needed changes are spread through different parts of the project. In an object-oriented language, all the needed changes can be encapsulated in simply a new class.

The DSL is constrained by the need to evaluate only a finite DP table. There is a class of edge case inputs that the language cannot evaluate: if a function call in an input function depends on an evaluated cell which is outside the bounds of the finite DP table produced by the DSL, the DSL will return an error. Fixing this issue requires a drastically different evaluation model and significant changes to the implementation of the language. I was unable to find a solution for this problem.

Throughout this project, I've extensively used the help of my critique partner, Emily, and other classmates, who are all potential users of the language. Their inputs were particularly important during the design stage of the language. My critique group helped come up with a design for encoding base cases and recursive calls in the language, and these changes results in two major refactoring of my entire code base. After their inputs, my AST became much more powerful and could represent a much great variety of dependencies. The base cases in the concrete syntax were free from unnecessary nesting and easier for the user to understand.

The most difficult part of the project has been the implementation of error-handling. Because I chose to allow the user to input any text, there are many possibilities of errors. Furthermore, there is no single place in the code to check for errors: syntax errors emerge from the parser, some errors occur when the dependency graph is constructed, and some errors can't be checked until after I run the topological sort on the dependency graph. Handling all the errors and outputting the most helpful information to the user required me to be creative, to think of all the ways a program can go wrong, and to catch the errors at the right place.