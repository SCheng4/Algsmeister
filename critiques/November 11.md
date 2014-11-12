# Critique - 11/11
## By Emily Blatter

* I think the idea we came up with in class to deal with your dependency problem was to make dependencies have a range of values they could be from and a value for how many of those values it actually depends on. It'll take some refactoring, but you can do it!

* I think your decision to get rid of arbitrary function names was probably a good one. The only argument I can think of for keeping the function names is if a user wants to run multiple programs in a row to get results, they may want the option to name them differently. However, this seems like an uncommon use case, so I wouldn't worry too much about it, especially if this decision simplifies your code a lot.

* Naming variables might be useful for some users to better understand their own pseudocode and thus better understand the problem, but I don't think it's significant enough a difference to do something besides i, j, k if it makes it easier for you to code.

* The syntax looks good overall to me. It seems fairly intuitive. However, I'm a bit confused about how your recursive cases work. What if there are cases where the solution is the sum of some recursive cases or you're returning the max/min of multiple possible cases? Is this something your language would be able to handle?

* Overall, it looks like you're making a lot of good progress. Besides my questions about the input, your input and output both look good, and I think you have an AST that should work well for you (once you tweak things about the dependencies).