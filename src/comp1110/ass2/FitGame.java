package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * This class provides the text interface for the IQ Fit Game
 * <p>
 * The game is based directly on Smart Games' IQ-Fit game
 * (https://www.smartgames.eu/uk/one-player-games/iq-fit)
 */
public class FitGame {

    /**
     * Determine whether a piece placement is well-formed according to the
     * following criteria:
     * - it consists of exactly four characters
     * - the first character is a valid piece descriptor character (b, B, g, G, ... y, Y)
     * - the second character is in the range 0 .. 9 (column)
     * - the third character is in the range 0 .. 4 (row)
     * - the fourth character is in valid orientation N, S, E, W
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    static boolean isPiecePlacementWellFormed(String piecePlacement) {

        // test if the String has four characters.
        if(piecePlacement.length()!=4){return false;}

        // test if the first character is valid descriptor character.
        switch (String.valueOf(piecePlacement.charAt(0)).toUpperCase()){
            case "B": case "G": case "I": case "L": case "N": case "O":case "P": case "R": case "S": case "Y":
                break; default: return false;
        }

        // test if the second character is between 0 to 9.
        String secondCharacter = String.valueOf(piecePlacement.charAt(1));
        switch (secondCharacter){case "0": case "1": case "2": case "3": case "4": case "5":case "6": case "7": case "8": case "9":
                break; default: return false;
        }

        // test if the third character is between 0 to 4.
        String thirdCharacter = String.valueOf(piecePlacement.charAt(2));
        switch (thirdCharacter){case "0": case "1": case "2": case "3": case "4":
            break; default: return false;
        }

        // test if the fourth character in valid orientation N, S, E, W
        switch(String.valueOf(piecePlacement.charAt(3)).toUpperCase()){
            case "N": case "E": case "S": case "W":
                break; default: return false;
        }


        return true; // FIXME Task 2: determine whether a piece placement is well-formed
    }

    /**
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N four-character piece placements (where N = 1 .. 10);
     * - each piece placement is well-formed
     * - no shape appears more than once in the placement
     * - the pieces are ordered correctly within the string
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementWellFormed(String placement) {
        boolean flag = true;

        // check if input satisfies condition 1.
        if (placement.length() % 4 == 0
                && placement.length() >= 4
                && placement.length() <= 40) {

            ArrayList<String> grouped = new ArrayList<>();

            // group input String as an ArrayList of 4 chars as it's element.
            for (int i = 0; i < placement.length(); i++) {
                grouped.add(placement.substring(i,i+4));
                i = i + 3;
            }
            // check if the input satisfies condition 2.
            for (String s : grouped) {
                if (!isPiecePlacementWellFormed(s)) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                StringBuilder shapes = new StringBuilder();
                // flatten the ArrayList into a String which only preserves shapes from the input.
                for (String s : grouped) {
                    shapes.append(s.charAt(0));
                }

                // check if the input satisfies condition 3.
                if (shapes.chars().distinct().count() == shapes.length()) {

                    char[] unsortedShapes = shapes.toString().toLowerCase().toCharArray();
                    //sort the array of shapes according to the alphabetical order.
                    Arrays.sort(unsortedShapes);

                    StringBuilder sortedShape = new StringBuilder();

                    for (char c : unsortedShapes) sortedShape.append(c);
                    //check if the input satisfies the last condition.
                    if (!shapes.toString().toLowerCase().equals(sortedShape.toString())) {
                        flag = false;
                    }

                } else flag = false;
            }
        } else flag = false;

        return flag; // FIXME Task 3: determine whether a placement is well-formed
    }




    /**
     * Determine whether a placement string is valid.
     *
     * To be valid, the placement string must be:
     * - well-formed, and
     * - each piece placement must be a valid placement according to the
     *   rules of the game:
     *   - pieces must be entirely on the board
     *   - pieces must not overlap each other
     *
     * @param placement A placement string
     * @return True if the placement sequence is valid
     */
    public static boolean isPlacementValid(String placement) {
        return false; // FIXME Task 5: determine whether a placement string is valid
    }

    /**
     * Given a string describing a placement of pieces, and a location
     * that must be covered by the next move, return a set of all
     * possible next viable piece placements which cover the location.
     *
     * For a piece placement to be viable it must:
     *  - be a well formed piece placement
     *  - be a piece that is not already placed
     *  - not overlap a piece that is already placed
     *  - cover the location
     *
     * @param placement A starting placement string
     * @param col      The location's column.
     * @param row      The location's row.
     * @return A set of all viable piece placements, or null if there are none.
     */
    static Set<String> getViablePiecePlacements(String placement, int col, int row) {
        return null; // FIXME Task 6: determine the set of all viable piece placements given existing placements
    }

    /**
     * Return the solution to a particular challenge.
     **
     * @param challenge A challenge string.
     * @return A placement string describing the encoding of the solution to
     * the challenge.
     */
    public static String getSolution(String challenge) {
        return null;  // FIXME Task 9: determine the solution to the game, given a particular challenge
    }
}
