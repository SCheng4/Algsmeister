# Algsmeister

## Running and Testing the Prototype

* Download the project, as a zip or clone

* Run sbt eclipse to create the Eclipse project, open the project in Eclipse

* Currently, the file \algsmeister\test is where you can input a program. Right now it contains a sample program for a recursive fib function.

* Open \algsmeister\src\main\scala\algsmeister\scala\main.scala file in Eclipse.

* in Right-click > Run As > Run Configurations, open the Arguments tab, and make sure the textbox for "Program arguments:" contains "test" (or path to whichever input file you decide to create).

* Now you should be able to run main.scala as a Scala program.


## Syntax

The formal syntax of the DSL is as following:

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
