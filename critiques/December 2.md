# Critique - 12/2
## By Emily Blatter

* It seems like much of your error checking that you've done so far should work for 2D cases as well.
* Will the user always want to have the same letters in all base cases (e.g. each case has i and j instead of i and j in one and just i in another)? Make sure all possible combinations are dealt with in a way that makes sense and works.
* Similarly, make sure the recursive cases include the same number of arguments that you started with in the function declaration. Or perhaps, if it's possible, you could have the default for that to be whatever argument was left out to just be passed along without modification (e.g. function(i - 1) => function(i - 1, j)), though you would likely want to throw a warning when you do that.
* I think that in general that as you're implementing 2D stuff, pay careful attention to where users have to list arguments and think about how they might do it wrong.
* Overall, it looks like you're making excellent progress! I'm excited to see your 2D implementation.