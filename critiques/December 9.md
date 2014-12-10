# Critique - 12/2
## By Emily Blatter

* When I first tried to edit the sample program you had to make it 2D, I forgot to change the arguments at the top, and it gave me a really unhelpful error. However, after I fixed it and then went back to try and recreate the error, it started to give reasonable errors. It's probably just some weird quirk of eclipse and probably not worth dealing with, but you can maybe look into it if you have time later.
* I ran a bunch of tests that I didn't do last time to try and break it, but it seemed like you handled errors very well. I like that some of the errors now tell you about what dependencies it has trouble with, which should make it easier for the user to conceptualize what they might be doing wrong.
* I tried running it on the following program:
	```def function(i, j): if (i == 0 && j == 0): return else consider: function(i-1, j-1),  function(i-1, j), function(i, j-1)```
	but it returns the error:
	```The DP table cannot be filled out: cell(0, 1) depends on cell(-1, 0), which is outside the bounds of the DP table.```
	However, I think this is something that should not throw an error? Might be worth looking into.
* It seems like your way of calculating runtime is solid, though as I've said before, DP is not a strength of mine so you may want to check with someone else.