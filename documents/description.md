# Project description and plan

## Motivation

For students in an algorithm class, dynamic programming (DP) is an important problem-solving technique. The method for applying DP to a problem, as taught by Prof. Libeskind-Hadas of HMC, begins by writing a recursive function for the problem, a fairly intuitive process. Through a relatively mechanical series of steps, this recursive function is transformed into a corresponding DP table.

With this method, the majority of the creative process of problem-solving is achieved by arriving at a correct, DP-able recursive function. However, many students still have considerable trouble converting the recursive function into a DP table. Challenges usually arise from difficulties with mentally picturing what the DP table looks like from the recursive calls embeded in the function.

Because deriving the DP table from a valid recursive function is an uncreative process in many cases, a programming solution is possible. A DSL is a great tool for this problem because many students have a good idea of what the recursive function look like and frequently already state it in a code-like language (pseudocode, Ran++, or straight-up Python). A DSL can take in a simplified and standardized recursive function, derive the DP solution, and help the students in visualizing the DP table and other information about the DP solution.

## Language domain

The domain of this language is tools that help CS students learn concepts. Students who struggle with applying DP to a recursive function can use this DSL to guide and reinforce their learning. They can use the tool to visualize the DP table and to check their own solutions against. An extension of DSL is to help the student check if DP can be applied to their original function, thus also helping the student come up with good recursive functions.

To my knowledge, there are no tools or DSL currently that accomplishes this. Although I may need to do further research on the landscape of DP problems in order to design my DSL in an extensible way.

## Language design

In one sentence: my DSL parses a pseudocode-like recursive function, performs analysis on the intermediate representation (IR), and outputs a visualization of the DP solution of the function along with additional helpful information.

A program is a recursive function that the user writes to solve a problem. The syntax will be designed to encapsulate only enough information needed to perform the analysis and generate the DP solution. Therefore a program need not be valid code in any existing language, or even be the correct solution to the problem.

When the program runs in the DSL, it will produce some form of visualization of the valid DP table, if one exists. The graphics may be simple, such as an ASCII rendition of the table, but one can easily imagine how it can be extended and made into much more elaborate graphics. The table should include information on the order in which cells in the DP table should be filled in. In addition, the program can produce an estimate of the overall running time of the DP solution.

The main sources of errors in programs are syntax errors and compile-time errors. The user is required to use a specialized syntax to represent their recursive function, so the parser must handle syntax errors and produce meaningful error messages back to the user. In addition, the user may input a recursive function that does not have a DP solution. The language may be designed to not support syntax for certain recursive functions, and report these functions as syntax errors. The language can also abort during the analysis of such a function and report a compile-time error.

## Example computations

The most basic program the DSL can handle is the recursive function for calculating the nth Fibonacci number:

```
def fib(n):
	if n < 2:
		return 1
	else:
		return fib(n-1) + fib(n-2)
```

The naive recursive function has a DP solution which runs in linear time instead of exponential time. When this program is reformatted in the syntax of the DSL and run, it should produce a representation of a one-dimensional DP table, indicate that the table should be filled in from left to right, and inform the user that the DP solution runs in O(n).

A more complex program might be the recursive function to find the length of the longest common subsequence between two strings. (Code credited to Prof. Ran's lecture notes for Algorithms)

```
# where i and j are indices in the two strings, S1 and S2
def LCS(i, j):
	if i == 0 or j == 0:
		return 0
	elif S1[i] == S1[j]:
		return 1 + LCS(i-1, j-1)
	else:
		option1 = LCS(i, j-1)
		option2 = LCS(i-1, j)
		return mas(option1, option2)
```

This program should produce a representation of a 2D DP table, indicate one order in which the DP table can be filled out (there are several possibilities: diagonally from top left to bottom right, row-by-row, or column-by-column), and indicate that the DP solution is O(n^2).

There are, of course, much more complex recursive functions. A recursive function can have a more complex base case, contain O(n) recursive calls, return non-integral answers, or contain three or more arguments (which leads to higher dimension DP tables). I can begin the project by restricting the scope of recursive functions handled by the DSL to simpler ones such as the ones in the example above, but design the implementation with the possibility of extending the DSL to include more and more complex recursive functions.



