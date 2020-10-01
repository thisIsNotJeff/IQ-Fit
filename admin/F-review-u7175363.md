## Code Review

Reviewed by: Boyang Gao, u7175363

Reviewing code written by: Qinrui Cheng, u7133046

Component: 
[isPlacementWellFormed(String placement)](https://gitlab.cecs.anu.edu.au/u7133046/comp1110-ass2-tue15g/-/blob/master/src/comp1110/ass2/FitGame.java#L73-123) and
[sortStringPlacement(String placement)](https://gitlab.cecs.anu.edu.au/u7133046/comp1110-ass2-tue15g/-/blob/master/src/comp1110/ass2/FitGame.java#L270-292) from FitGame class.
### Comments 

1. Use of commenting: Methods are well commented. Relevant information such as brief explanation of function, all parameters, and return value can be easily extracted from the comments, making Qinrui's codes easier for other group members to understand.
2. Naming: name of methods are accord with Camel principal, and most of these name reflect the function of methods. 
3. Redundancy: some of the control statements can be simplified. For example, in isPlacementWellFormed method, L84 - 87, instead of adding statement "i = i + 3" to the for-loop, same result can be achieved by change incremental statement from "i++" to "i += 4", which makes the codes look more precise.
4. Exception handling: throw exception is used in sortStringPlacement method to check the validity of the input String. This helps our group to easily detect and debug if there is something unusual associated with this method.

