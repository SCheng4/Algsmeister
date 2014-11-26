# Critique - 11/25
## By Emily Blatter

* I think that you may be producing too much output. Though the info about the program that says, "These cells labelled with 0 are the base cases and can be filled in for free.
The other numbers indicate one order in which the remaining cells can be filled in.
(Note that there are frequently more than one order to fill the DP table.)" is useful the first time someone uses your language, it seems it would be excessive and unnecessary to have to see this every time I run a program. Perhaps it would be better to just put it in the documentation for your language.

* I'm excited to see that you have something functional! It looks great so far! I ran a couple of tests with it to see how you handle errors, and my notes on that are below:
	* When misspelling something, the error message says what it expected and then just the first letter of what it found instead. It might be helpful if you could have it return the whole word that was misspelled. Not entirely necessary because it shows where the problem is in the output line under it, but it would still be nice.
	* Not sure if this how realistic this is as a use case, but your program does not currently accept things like "i == n - 1" as a base case. It's something to consider.
	* If the base case doesn't have a comparator (ex: if (i)), then the error just says "Unrecognized comparator" which isn't the most helpful error? Might be more helpful if you could handle it similar to how missing base cases are handled.
	* Is it an error if the user uses a letter in their recursive cases that isn't used in the base cases? It seems that if I change the i's in the recursive cases of your fib program to j's, it still runs the same.
	* I think I tested most of the other errors you had in your wiki, and they all seem to be doing well!

* Overall, you've made a lot of good progress! It seems to be working well and I look forward to seeing it work with 2D problems!