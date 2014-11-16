# Language design and implementation overview

## Language design

My DSL is designed to be a tool to help algorithm students understand how to perform dynamic programming on a recursive function. There are currently no other languages for this domain.

The user input to the program is a textfile with a modified version of a recursive function that encapsulates the recurrence relation of the function. The syntax is Python-like, but is much simpler than a working Python program. A draft of the design of the syntax of the DSL is on [the Github wiki of my project](https://github.com/SCheng4/DSL-Project-Sisi-Cheng/wiki).

The program parses the user's input into an intermediary representation of the function, including information on the dimension of the function, the base cases, and the dependencies of each recursive call. The basic computation performed by the program is a topological sort on the dependencies of the function, and the program produces an order in which the DP table can be successfully filled out and prints an ascii representation of the DP table.

The user does not have control over the data structures or the control structure of the program.

The program will handle basic input syntax errors such as a typo in a keyword. It's also possible that the user can attempt to apply the program on a recursive function that cannot be DP-ed. The syntax of the language is designed so that user is unable to enter a recursive function with dependencies that are inappropriate for DP. During evaluation, the program will also output helpful error messages if the function the user enters has incorrect dependencies such as a missing base case.

## Language implementation

I choose to implement my DSL as an external DSL because it allows me much greater freedom in choosing the syntax of the language. I'm able to easily make the syntax much closer to Python pseudocode. I choose Scala as the host language because I'm already familiar with how to set up a program in Scala, and because the PackratParser provides moderate support for custom error messages on syntax errors. However, the choice of the host language is not crucial to my project, and the DSL can be implemented using a variety of available parsers and languages.

The architecture of my program is similar to the structure of the external Piconot project, and consists of three main parts: the parser, the intermediate representation, and the evaluator. The parser parses the user input into the the IR. The evaluator performs computation on the IR and produces the output that the user sees.

At this stage of my project, I've determined most of the concrete syntax of the language and all of the intermediate representation. The IR was determined with consideration for extensibility for higher-dimensional DP tables: I have case classes representing DP table of every dimension along the computation and there are TODO notes indicating where to add implementation for DP tables of other dimensions. The syntax was then designed for the user to most intuitively convey all the needed information about the recursive function to the program, while preventing the user from entering a recursive function that cannot be DP-ed.


