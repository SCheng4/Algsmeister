# Project plan

## Language evaluation

The most important goal of my DSL is accessibility and usefulness to its users. A tool for learning can be evaluated through its user experience. I will mainly assess the success of my DSL through surveying any potential user (myself, Prof. Ran, fellow HMC students, etc.) to evaluate whether the language has enough computation and error-checking capability to help a student learn DP. During the design and implementation process, I can ask potential users to provide feedback on any major design decisions to get a clearer idea of the impact of the decision on the user experience.

It's likely that my implementation of the language will not be comprehensive. I also need to evaluate that the language implementation leaves enough room for future expansions of the language.

To ensure the success of my DSL on these fronts, during the design of the implementation of my language, I must pay extra attention to extensibility, the user experience, and user safety. I will develop error-checking code along-side my implementation to avoid sacrificing error-checking for ease of implementation.

## Implementation plan

I plan to spend the first week to explore host language options. Once the host language is determined, I will spend the next three weeks designing the abstract syntax as well as implement basic parser and evaluator. Next, I'll spend two weeks expanding the basic implementation to include more features. Finally, I will dedicate the final week to wrapping up the project and exploring some of the stretch goals if all goes well.

The project milestones and intermediate deadlines are as following:

By November 2:

* Finish project description and plan

* Research alternative host languages other than Scala

* Research feasibility of using Scala by setting up initial project

By November 9:

* Design the abstract syntax for the language

* Simultaneously implement evaluation of the abstract syntax

By November 16:

* Finish design and implementation overview

* Design the concrete syntax for the language

* Begin to implement the parser. Continue to develop the evaluator as design shifts

By November 23:

* Have a working prototype that works for a basic program with integer return value, one argument, and one simple recursive call. Prototype should return a representation of the DP table as well as the order for which the DP table should be filled in. Prototype should check for syntax errors.

By November 30:

* Finish preliminary evaluation

* Support programs with multiple but constant number of recursive calls

By December 7:

* Extend the prototype to either support programs with two arguments or 

* Develop support for estimating the running time of the DP solution

By December 12:

* Complete final write-up

* Work on stretch goals: extend the prototype to either support programs with up to three arguments or programs with non-constant number of recursive calls

