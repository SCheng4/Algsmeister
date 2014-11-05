# Critique - 11/4
## By Emily Blatter

* I like the general idea of your language. I've said this before, but I still think this is something that would have been useful to me when I took algs.

* Are you going to output the dependencies of each cell? It seems like it's something you're going to have to figure out anyway, and knowing where information comes from for each cell could help users understand it better. However, I'm not certain how this information would fit nicely into your current format for output.

* I feel like determining the syntax for your language could be a challenge for your DSL. You may need to keep it very structured to make sure you can get the information you need from it without having to do too much sheer programming in your parser.

* Error-handling could also be difficult, though this may be easier if your language is very structured. It would at least limit the amount of complicated syntax errors you'd have to deal with, though run-time errors could still be tricky. Make sure the program gives helpful errors when the user passes in a recursive function that can't create a DP table.

* I like that your plan starts with the abstract syntax. That should make it easier to design the rest of your language.

* Once you have planned out more of the specifics of your language, be sure to break the task down of building a prototype into smaller sub-tasks. It seems like it would be tempting to implement something that can take in an entire program, but make sure you find a way to do it in pieces and build them up. Other than that, your plan looks fairly good to me.

* Unforutnately, I don't know of any parsers or libraries that could help you. :( But it looks like you're pretty settled on Scala, and it seems like you should be fine with that.

* You seem like you're on a good path for your project so far! I look forward to seeing what you actually decide on for your final syntax.
