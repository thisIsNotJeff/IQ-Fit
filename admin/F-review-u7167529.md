## Code Review

Reviewed by: <Yuxuan Hu>, <u7167529>

Reviewing code written by: <Boyang Gao> <u7175363>

Component: implementChallenge and makePlacement in [Board](https://gitlab.cecs.anu.edu.au/u7133046/comp1110-ass2-tue15g/-/blob/master/src/comp1110/ass2/gui/Board.java#L196-282)

### Comments 

1. This piece of code implements the function to initialize a new game. The code style is very brief and clear, and it doesn't influence the unfinished task7.
2. However, there isn't many comments. So it might be inconvenient when someone want to read. The puzzlepieces have different size so the layout of different pieces is different. The comment should mention this because adjust the layout is the main purpose of this task.
3. This piece of code is divided into two pieces, implementChallenge and makePlacement. In implementChallenge method, a string of placement is produced and in makePlacement, the code deals with the layout of the game. The logic and structure is clear and straightforward.
4. The variance name and method name are clear. the code style is unified with task4 and task7. It makes others to understand its logic easily. And it is also easier when making some further changes.
 


