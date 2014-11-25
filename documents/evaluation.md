# Preliminary evaluation
** Sisi Cheng - Algsmeister

**What works well? What are you particularly pleased with?**

The program's main computation, the calculation of a dependency graph, has worked well for DP problems. The application of DP is inherently a dependency problem. Modeling recursive function calls as a graph then applying a topological sort is both appropriate and a good design choice. In addition, I'm satisfied that I designed my DSL to be extensible. So far, the language only supports DP table of one dimension, but at each step of parsing and evaluation of a program, it's easy to see where to add new logic to support additional dimensions. The implementation of one dimensional programs does not involve any hard-coding of dimensions.

**What could be improved? For example, how could the user's experience be better? How might your implementation be simpler or more cohesive?**

There are a few areas that can be improved. Some aspects of the user interface could be better. The user is currently limited to use hardcoded function name and variable names. While this does not take away from the usibility of the program, it can be somewhat unnatural. The syntax of the language is also designed to be similar to Python, but the function is stripped down to just what is needed for the program. The language may be a lot better if it can parse real Python code, then calculate the dependency directly from a working, recursive Python function. The current language also hard-codes the size of the DP table that will be constructed and printed. This makes the program useless for a recursive function that is not recursive for all cells in the DP table supported by the DSL. A better solution would be to not hard-code the DP table size, and either allow user to input the size suitable for them, or perhaps dynamically decide the appropriate size for a given input.

The implementation of the language could also be even more extensible if the I had chosen a more object-oriented host language. Because I can only do so much within this scope of the project, I'm most concerned about the extensibility of the DSL in the direction of supporting more different dimensions of DP functions. This involved adding more objects of different types, or "nouns". Object-oriented languages are therefore more suitable for this purpose. In Scala, to add a new dimension, the needed changes are spread through different parts of the project. In an object-oriented language, all the needed changes can be encapsulated in simply a new class.

**Re-visit your evaluation plan from the beginning of the project. Which tools have you used to evaluate the quality of your design? What have you learned from these evaluations? Have you made any significant changes as a result of these tools, the critiques, or user tests?**

I've extensively used the help of my critique partner, Emily, and other classmates, who are all potential users of the language. Their inputs were particularly important during the design stage of the language. My critique group helped come up with a design for encoding base cases and recursive calls in the language, and these changes results in two major refactoring of my entire code base. After their inputs, my AST became much more powerful and could represent a much great variety of dependencies. The base cases in the concrete syntax were free from unnecessary nesting and easier for the user to understand.

**Where did you run into trouble and why? For example, did you come up with some syntax that you found difficult to implement, given your host language choice? Did you want to support multiple features, but you had trouble getting them to play well together?**

The most difficult part of my project was error-handling. Because I chose to allow the user to input any text, there are many possibilities of errors. Furthermore, there is no single place in the program to check for errors: syntax errors emerge from the parser, some errors occur when the dependency graph is constructed, and some errors can't be checked until after I run the topological sort on the dependency graph. Handling all the errors involved being creative, thinking of all the ways a program can go wrong, and catching the error at the right place in the program to output the most helpful information to the user.

**What's left to accomplish before the end of the project?**

I need to implement evaluation support for 2D functions. I will also continue to tweak the user experience by handling errors as they arise. Finally, if time allows, I can either attempt to add support for 3D functions or 




