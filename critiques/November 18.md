# Critique - 11/18
## By Emily Blatter

* I like the idea of using commas instead of ||. As I said in class, I think your language should still be clear, especially because it's intended to look like pseudocode. However, are you keeping the parentheses as an option in your base cases? It seems like they're unnecessary now, but it's unclear from your notebook whether or not they're still in the grammar. If they are, you might want to get rid of them to simplify things.

* It seems like you could have a lot of error handling you'll have to start thinking about, though I suppose it may depend a bit on how much trust you want to give to your users and how helpful you want to be to them. For example, what happens if they pass too many arguments or something that isn't i, j, or k? Make sure you think of all the ways a user can abuse your language.

* Checking to make sure everything is satisfiable and it actually makes a DP table might also be tricky. This does seem like logic that should be moved to the interpreter, though. The parser can handle just figuring out whether the user used the right syntax while the interpreter can figure out if the user's program actually makes any sense.

* Good work refactoring things! It seems like the changes you made should be very useful to future work.

* It seems like you're making good progress getting things set up so far! Keep up the good work!