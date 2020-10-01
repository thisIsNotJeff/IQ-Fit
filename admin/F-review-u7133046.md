## Code Review

Reviewed by: <Qinrui Cheng>, <u7133046>

Reviewing code written by: <Yuxuan Hu> <u7167529>

Component: 
- [Viewer.java](https://gitlab.cecs.anu.edu.au/u7133046/comp1110-ass2-tue15g/-/blob/master/src/comp1110/ass2/gui/Viewer.java#L30-117) the simple viewer from the task #4.

### Comments 

1. With this simple viewer, we can visualize the string placement easily. 
This is really helpful when I write task6, it helped me debug without wasting time one drawing the game on paper.

2. However, I found there aren't many comments in the code, it's easy to lose their orientations when someone is reading a bunch of code without documentations.

3. The structure in the Viewer is pretty clear, he used a For Loop to construct the main body of the code.
He handled different directions in the placement string by rotating the picture. I noticed the code in line 97 - 106 is very similar to what he done in the line 78 - 87.
I believe he can tighten the code by optimizing the structure of the if-else statement. Overall, I think the structure of the code in the Viewer.java is good.

4. In the Viewer.java, some of the variable names are too short, for example, the "char t" represents the 10 pieces in the game, one character is too short to understand what it stands for,
although it's not a big deal, but I think it would be better if those variable names could be more detailed. The style in Viewer is pretty coherent.


