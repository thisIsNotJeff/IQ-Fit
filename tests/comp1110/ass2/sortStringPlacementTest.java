package comp1110.ass2;

import org.junit.Test;

import java.util.*;

import static comp1110.ass2.FitGame.sortStringPlacement;
import static org.junit.Assert.assertEquals;

public class sortStringPlacementTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty() {
        sortStringPlacement("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLengthOne() {
        sortStringPlacement(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAllString() {
        sortStringPlacement("ssss");
    }

    @Test
    public void testOnePiece() {
        // iterate through each possible pieces colors.
        for (Color c : Color.values()) {
            if (c == Color.TEST) continue;
            // iterate through each possible directions.
            for (Direction d : Direction.values()) {
                // iterate through each possible columns.
                for (int i = 0; i < 9; i++) {
                    // iterate through each possible rows.
                    for (int j = 0; j < 5; j++) {
                        String input = c.value + Integer.toString(i) + j + d.value;
                        assertEquals(input, sortStringPlacement(input));
                    }
                }
            }
        }
    }

    @Test
    public void testRandom() {
        // use the Games Array from Games class as test inputs.
        for (Games g : Games.SOLUTIONS) {
            List<String> testList = new ArrayList<>();

            //pieces length is the number of pieces in a placement String.
            int piecesLength = 0;

            // transfer a String of placement as a list of pieces.
            for (int i = 0; i < g.objective.length(); i += 4) {
                piecesLength ++;
                testList.add(g.objective.substring(i,i+4));
            }

            Random random = new Random();
            Set<String> testSet = new HashSet<>();
            // generate random number of pieces to test for each objective in the Games.(mess up the order of each pieces)
            int numberOfPieces = (int) (Math.random() * piecesLength + 1);

            // keep adding random pieces until it meets the number requirement.
            while (testSet.size() < numberOfPieces) {
                String randomPiece = testList.get(random.nextInt(piecesLength));
                testSet.add(randomPiece);
            }


            ArrayList<Integer> usedIndex = new ArrayList<>();

            String testPlacement = "";
            // collect the indices of used pieces from the list. And generate testPlacement.
            for (String s : testSet) {
                testPlacement += s;
                usedIndex.add(testList.indexOf(s));
            }

            // sort the usedIndices so that we can reassemble the testPlacement in the correct order.
            Collections.sort(usedIndex);
            String expectedResult = "";
            for (Integer i : usedIndex) expectedResult += testList.get(i);

            // check if the reassembled placement math the sorted one.
            assertEquals(expectedResult, sortStringPlacement(testPlacement));
        }
    }







}
