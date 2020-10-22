package comp1110.ass2;

import javafx.util.Pair;

import java.util.*;

/**
 * This class provides the text interface for the IQ Fit Game
 * <p>
 * The game is based directly on Smart Games' IQ-Fit game
 * (https://www.smartgames.eu/uk/one-player-games/iq-fit)
 * @author Boyang Gao, Yuxuan Hu, Qinrui Cheng
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
     * @author Boyang Gao
     */

    static boolean isPiecePlacementWellFormed(String piecePlacement) {

        /** test if the String has four characters. */
        if(piecePlacement.length()!=4){return false;}

        /** test if the first character is valid descriptor character. */
        String firstCharacter = String.valueOf(piecePlacement.charAt(0)).toUpperCase();
        switch (firstCharacter){
            case "B": case "G": case "I": case "L": case "N": case "O":case "P": case "R": case "S": case "Y": case "*":
                break; default: return false;
        }

        /** test if the second character is between 0 to 9. */
        String secondCharacter = String.valueOf(piecePlacement.charAt(1));
        switch (secondCharacter){case "0": case "1": case "2": case "3": case "4": case "5":case "6": case "7": case "8": case "9":
                break; default: return false;
        }

        /** test if the third character is between 0 to 4. */
        String thirdCharacter = String.valueOf(piecePlacement.charAt(2));
        switch (thirdCharacter){case "0": case "1": case "2": case "3": case "4":
            break; default: return false;
        }

        /** test if the fourth character in valid orientation N, S, E, W */
        String forthCharacter = String.valueOf(piecePlacement.charAt(3));
        switch(forthCharacter){
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
     *
     * Author: Qinrui Cheng u7133046.
     */
    public static boolean isPlacementWellFormed(String placement) {

        // check if input satisfies condition 1.
        if (placement.length() % 4 == 0
                && placement.length() >= 4
                && placement.length() <= 40) {

            //ArrayList<String> grouped = new ArrayList<>();

            StringBuilder shapes = new StringBuilder();

            // group input String and only preserve the shapes in lower cases. And check if the input satisfies condition 2.
            for (int i = 0; i < placement.length(); i+=4) {
                String pieces = placement.substring(i,i+4);
                if (isPiecePlacementWellFormed(pieces))
                    shapes.append(pieces.substring(0,1).toLowerCase());
                else return false;
            }

            // check if the input satisfies condition 3.
            if (shapes.chars().distinct().count() == shapes.length()) {

                char[] sortedShapes = shapes.toString().toCharArray();
                //sort the array of shapes according to the alphabetical order.
                Arrays.sort(sortedShapes);

                //check if the input satisfies the last condition.
                return Arrays.equals(shapes.toString().toCharArray(), sortedShapes);//shapes.toString().equals(sortedShape.toString());

            } else return false;
        } else return false;

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
        return validityOccupation(placement).getKey();
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
     *
     * Author: Qinrui Cheng u7133046.
     */

    static Set<String> getViablePiecePlacements(String placement, int col, int row) {

        Set<String> result = new HashSet<>();

        Integer columns = col;
        Integer rows = row;
        String newPiece = "";
        // get all the possible locations
        var candidate = possibleLocation(col, row);

        // collect the used colors, so we don't need to iterate all the possibilities.
        Character[] usedColor = getUsedColor(placement);

        // first check if the input placement plus a test color(which only occupy that location) is valid
        if (isPlacementValid(sortStringPlacement(placement + '*' + columns.toString() + rows.toString() + 'W'))) {

            // then iterate through all the colors exclude the Test one.
            for (Color c : Color.values()) {
                if (c == Color.TEST) continue;

                // check if the board is empty or the current iterating color haven't been used.
                if (placement.length() == 0 || !Arrays.asList(usedColor).contains(Character.toLowerCase(c.value))) {

                    // iterate through every direction
                    for (Direction d : Direction.values()) {

                        //iterate through every possible location.
                        for (Pair<Integer, Integer> p : candidate) {
                            newPiece = c.value + p.getKey().toString() + p.getValue().toString() + d.value;
                            var pair = validityOccupation(sortStringPlacement(placement + newPiece));
                            var occupation = pair.getValue();
                            var valid = pair.getKey();
                            // if the board state is valid after the putting the new piece
                            if (valid) {
                                //to see if the desired location is covered.
                                if (occupation[col][row] == 1)
                                    result.add(newPiece);
                            }
                        }
                    }
                }
            }
        }

        if (result.size() == 0) return null;
        else return result;
    }

    /**
     * Given a column and a row, return all the possible locations that pieces in the IQ FIT can put.
     *
     * @param col Current column.
     * @param row Current row.
     * @return A set of all the possible coordinates from the given row and column.
     *
     * Author: Qinrui Cheng u7133046.
     */

    public static Set<Pair<Integer, Integer>> possibleLocation(int col, int row) {
        Set<Pair<Integer, Integer>> result = new HashSet<>();

        /* these are all the possible locations for pieces on the direction EAST or WEST can put.
        The possible locations depends on the number of rows */
        if (row == 0)
            getRange(col, row, 1, 0, result);
        else if (row == 1)
            getRange(col, row, 1, 1, result);
        else if (row == 2)
            getRange(col, row, 1, 2, result);
        else if (row == 3) {
            int newRow = row-1;
            getRange(col, newRow, 1, 2, result);
        } else {
            int newRow = row-2;
            getRange(col, newRow, 1, 1, result);
        }

        // add all the possible locations for pieces in the direction of SOUTH or NORTH can put.
        getRange(col, row, 3, 1, result);

        return result;
    }

    /**
     * Given current column, current row, the number of column and row you want to reduce, return all the coordinates from the reduced
     * column and row to the current ones to the given set.
     *
     * @param col Current column.
     * @param row Current row.
     * @param colChange Number of columns to reduce.
     * @param rowChange Number of rows to change.
     * @param toAdd The set to add all coordinates from the calculated range.
     *
     * Author: Qinrui Cheng u7133046.
     */

    public static void getRange(int col, int row, int colChange, int rowChange, Set<Pair<Integer,Integer>> toAdd) {
        int rowStart = 0;
        int colStart = 0;

        //figure out the start location of row.
        for (int i = row; i > row - (rowChange+1); i--) {
            if (i < 0) break;
            else rowStart = i;
        }

        //figure out the start location of column.
        for (int i = col; i > col - (colChange+1); i--) {
            if (i < 0) break;
            else colStart = i;
        }

        //add all the coordinates form colStart to col and from rowStart to row.
        for (int i = rowStart; i < row + 1; i++) {
            for (int j = colStart; j < col + 1; j++) {
                toAdd.add(new Pair<>(j,i));
            }
        }
    }


    /**
     * Given a placement string, return all the used color in the placement.
     *
     * @param placement A string of placement.
     * @return An array of used color.
     *
     * Author: Qinrui Cheng u7133046.
     */

    public static Character[] getUsedColor(String placement) {
        ArrayList<Character> used = new ArrayList<>();
        for (int i = 0; i < placement.length(); i = i+4) {
            used.add(Character.toLowerCase(placement.charAt(i)));
        }
        return used.toArray(new Character[0]);
    }

    /**
     * Return the sorted StringPlacement, the input string must be composed by well formed String pieces.
     *
     * @param placement A String placement.
     * @return A sorted String placement according to the alphabetical order of the color of pieces.
     *
     * Author: Qinrui Cheng u7133046.
     */

    public static String sortStringPlacement(String placement) {
        List<String> grouped = new ArrayList<>();

        // if the input is in the incorrect length, throw exception.
        if (placement.length() == 0 || placement.length() % 4 != 0)
            throw new IllegalArgumentException("Incorrect placement String");
            // if any of the pieces of the placement String is not well formed, throw exception.
        else {
            for (int i = 0; i < placement.length(); i+=4) {
                String piece = placement.substring(i,i+4);
                if (isPiecePlacementWellFormed(piece))
                    grouped.add(piece);
                else throw new IllegalArgumentException("Incorrect placement String");
            }
        }
        // sort the ArrayList of String pieces according to their color.
        grouped.sort(Comparator.comparing((String s) -> s.substring(0, 1).toLowerCase()));
        StringBuilder sorted = new StringBuilder();
        // recompose the placement String.
        for (String s : grouped) sorted.append(s);
        return sorted.toString();
    }


    /**
     * Given a placement string, return a pair contains the validity of the placement according to the rule of the game and
     * an int array of array illustrate the real occupation of each pieces in the placement string.
     * into a List of Pairs.
     *
     * @param placement A string of placement.
     * @return A Pair of contains the validity of placement and the occupation int array of array.
     *
     * Author: Yuxuan Hu  u7167529 / Qinrui Cheng u7133046.
     */
    public static Pair<Boolean, int[][]> validityOccupation(String placement) {

        //create a container for pieces parsed from string
        ArrayList<String> pieces = new ArrayList<>();

        for (int i = 0; i < placement.length() - 3; i = i + 4) {
            pieces.add(placement.substring(i, i + 4));
        }


        //create a int array of array to illustrate the real occupation of pieces.
        int[][] occupationArray = new int[10][5];

        Pair<Boolean, int[][]> b = new Pair<>(false, null);

        if (placement.length() == 0) return new Pair<>(true,occupationArray);
        else if (!isPlacementWellFormed(placement)) return b;
        else {
            for (String piece : pieces) {

                int row = Character.getNumericValue(piece.charAt(2));
                int column = Character.getNumericValue(piece.charAt(1));
                char charColour = piece.charAt(0);
                char charDirection = piece.charAt(3);


                if ((column < 0) || (column > 9) || (row < 0) || (row > 4)) return b;

                if ((charDirection != 'N') && (charDirection != 'E') &&
                        (charDirection != 'S') && (charDirection != 'W')) return b;


                if (charColour == 'b') {
                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    }
                } else if (charColour == 'B') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'g') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                        }
                    }
                } else if (charColour == 'G') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'i') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                        }
                    }
                } else if (charColour == 'I') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                        }
                    }
                } else if (charColour == 'l') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'L') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'n') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                        }
                    }
                } else if (charColour == 'N') {

                    if (charDirection == 'N') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 7) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 2)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'o') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'O') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    }
                } else if (charColour == 'p') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                        }
                    }
                } else if (charColour == 'P') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    }
                } else if (charColour == 'r') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    }
                } else if (charColour == 'R') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    }
                } else if (charColour == 's') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'S') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                        }
                    }
                } else if (charColour == 'y') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                        }
                    }
                } else if (charColour == 'Y') {

                    if (charDirection == 'N') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 2][row] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else if (charDirection == 'E') {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 1][row + 2] += 1;
                            occupationArray[column + 1][row + 3] += 1;
                        }
                    } else if (charDirection == 'S') {
                        if ((column > 6) || (row > 3)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                            occupationArray[column + 2][row + 1] += 1;
                            occupationArray[column + 3][row + 1] += 1;
                        }
                    } else {
                        if ((column > 8) || (row > 1)) return b;
                        else {
                            occupationArray[column][row] += 1;
                            occupationArray[column][row + 1] += 1;
                            occupationArray[column][row + 2] += 1;
                            occupationArray[column][row + 3] += 1;
                            occupationArray[column + 1][row] += 1;
                            occupationArray[column + 1][row + 1] += 1;
                        }
                    }
                } else if (charColour == '*') {

                    occupationArray[column][row] += 1;
                } else return b;
            }

            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 5; j++) {
                    if(occupationArray[i][j] > 1) return b;
                }
            }
        }

        return new Pair<>(true,occupationArray);
    }



    /**
     * A class used to found the solution of a challenge,
     *
     * Author: Qinrui Cheng  u7133046.
     */
    public static class gameState {
        String gameState;
        String currentSolution;

        public gameState(String gameState, String currentSolution) {
            this.gameState = gameState;
            this.currentSolution = currentSolution;
        }

        @Override
        public String toString() {
            return "Tree "+this.gameState + " CurrentSolution "+this.currentSolution;
        }

    }



    /**
     * Given a gameState, a List indicating the pieces used so far, a HashMap of String and a List of String to store the invalid gameStates and the piece chosen for that state,
     * a List for storing the result and a possible combination to search, findSolution will call itself recursively until the solution is found.
     *
     * @param startRoot a starting root for search.
     * @param pieceTrack a List to store the chosen pieces.
     * @param invalidStates a HashMap to store the invalid States and pieces.
     * @param result a List to store the result.
     * @param combination a possible combination to search.
     *
     * Author: Qinrui Cheng  u7133046.
     */
    public static void findSolution(gameState startRoot, List<String> pieceTrack, HashMap<String, ArrayList<String>> invalidStates, List<String> result, Combinations combination) {
        var validityOccupation = validityOccupation(startRoot.gameState);
        var emptyLocations = occupationAsSet(validityOccupation.getValue());


        // check if the level satisfy the base case to stop.
        if (emptyLocations.size() == 0) {
            result.add(startRoot.gameState);
        } else  {
            for (Pair<Integer, Integer> emptyLocation : emptyLocations) {
                if (!result.isEmpty()) break;
                var viablePieces = getViablePiecePlacements(startRoot.gameState, emptyLocation.getKey(), emptyLocation.getValue());
                if (viablePieces != null && !viablePieces.isEmpty()) {
                    for (String piece : viablePieces) {
                        if (!result.isEmpty()) break;

                        // only put pieces satisfies these conditions
                        if ((!invalidStates.containsKey(piece) || !invalidStates.get(piece).contains(startRoot.gameState)) && canPut(piece) && correctCombination(combination, startRoot.currentSolution) && canPut(startRoot.currentSolution,piece)) {

                            var newGameState = sortStringPlacement(startRoot.gameState + piece);

                            pieceTrack.add(piece);

                            startRoot.gameState = newGameState;

                            findSolution(startRoot, pieceTrack, invalidStates, result, combination);

                        }


                    }



                }

                // if found any point can't be cover by a pieces than the whole gameState is not valid, so break.
                break;

            }

            // remove the last piece that cause the game to be invalid.
            var latestPiece = pieceTrack.remove(pieceTrack.size() - 1);

            var originalState = removePiece(startRoot.gameState, latestPiece);


            // update the invalidState
            if (invalidStates.containsKey(latestPiece)) {
                var listToBeUpdate = invalidStates.get(latestPiece);
                listToBeUpdate.add(originalState);
                invalidStates.compute(latestPiece, (k, v) -> v = listToBeUpdate);
            } else {
                ArrayList<String> newList = new ArrayList<>();
                newList.add(originalState);
                invalidStates.put(latestPiece, newList);
            }

            startRoot.gameState = originalState;

        }


    }



    /**
     * Given a Combination, and a String challenge, return if the current String challenge satisfies the
     * condition of the Combination.
     *
     * @param combinations A combination representing the possible pieces would solve the game.
     * @param challenge A string challenge.
     * @return if the conditions are satisfied.
     * Author: Qinrui Cheng  u7133046.
     */

    public static boolean correctCombination(Combinations combinations, String challenge) {
        ArrayList<String> pieceAsList = new ArrayList<>();
        int small = combinations.small;
        int medium = combinations.medium;
        int large = combinations.large;

        for (int i = 0; i < challenge.length(); i+=4) {
            char color = challenge.charAt(i);

            switch (color) {
                case 'g':
                case 'i':
                case 'l':
                case 'n':
                    if (small > 0)
                        small = small-1;
                    else return false;

                case 'B':
                case 'O':
                case 'P':
                case 'R':
                case 'S':
                case 'Y':
                    if (large > 0)
                        large = large-1;
                    else return false;

                default:
                    if (medium > 0)
                        medium = medium-1;
                    else return false;
            }
        }

        return small>=0 && medium >=0 && large >= 0;
    }




    /**
     * Combinations represent the possible pieces to found the solution.
     *
     * Author: Qinrui Cheng  u7133046.
     */

    public static class Combinations {

        int small;
        int medium;
        int large;



        /**
         * @param small small are the pieces that cover 4 coordinates.
         * @param medium medium are the pieces that cover 5 coordinates.
         * @param large large are the pieces that cover 6 coordinates.
         */
        public Combinations(int small, int medium, int large) {
            this.small = small;
            this.medium = medium;
            this.large = large;
        }

        @Override
        public String toString() {
            return "small "+this.small+" medium "+this.medium+" large "+this.large;
        }

    }


    /**
     * SortByAverage will sort the Combinations according to how the different size of pieces are distributed,
     * the more balanced one will be in the former places.
     *
     * Author: Qinrui Cheng  u7133046.
     */
    public static class SortByAverage implements Comparator<Combinations> {

        @Override
        public int compare(Combinations o1, Combinations o2) {

            int a = Math.max(Math.max(o1.small, o1.medium),o1.large);
            int b = Math.min(Math.min(o1.small, o1.medium),o1.large);


            int c = Math.max(Math.max(o2.small, o2.medium),o2.large);
            int d = Math.min(Math.min(o2.small, o2.medium),o2.large);


            return (a-b) - (c-d);
        }
    }



    /**
     * pieceCombination will found all the combinations that might solve a challenge by giving it
     * the number of empty coordinates in the board.
     * @param emptyLocations the number of empty locations on the board.
     *
     * @return a List of Combinations.
     *
     * Author: Qinrui Cheng  u7133046.
     */

    public static ArrayList<Combinations> pieceCombinations(int emptyLocations) {
        ArrayList<Combinations> combinations = new ArrayList<>();

        // found how many small pieces need.
        for (int i = 0; i*4 <= emptyLocations && i <= 4; i++) {
            // found how many medium pieces need.
            for (int j = 0; j*5 <= emptyLocations && j <= 10; j++) {
                // found how many large pieces need.
                for (int k = 0; k*6 <= emptyLocations && k <= 6; k++) {
                    if (i*4 + j*5 + k*6 == emptyLocations)
                        combinations.add(new Combinations(i,j,k));
                }
            }
        }

        return combinations;
    }



    /**
     * Given a String representing a piece, canPut will determine if the piece can be put on the board.
     * This method will test pieces on the trivial situations.
     *
     * @param piece piece to be tested.
     *
     * @return if this piece can be put on the board.
     *
     * Author: Qinrui Cheng  u7133046.
     */
    public static boolean canPut(String piece) {
        char color = piece.charAt(0);
        char col = piece.charAt(1);
        char row = piece.charAt(2);
        char dir = piece.charAt(3);

        switch (color) {
            case 'b':
            case 'B':
            case 'L':
            case 'N':
            case 'O':
            case 'r':
            case 'R':
            case 'y':
                if (row == 0 && dir == 'S') return false;
                else if (row == 3 && dir == 'N') return false;
                else if (col == 0 && dir == 'E') return false;
                else if (col == 8 && dir == 'W') return false;

            default:
                return true;

        }

    }

    /**
     * Return the solution to a particular challenge.
     * *
     *
     * @param challenge A challenge string.
     * @return A placement string describing the encoding of the solution to
     * the challenge.
     *
     * Author: Qinrui Cheng  u7133046.
     */

    public static String getSolution(String challenge) {
        gameState test = new gameState(challenge,"");

        List<String> result = new ArrayList<>();

        HashMap<String,ArrayList<String>> invalidStates = new HashMap<>();
        List<String> pieceTrack = new ArrayList<>();

        var combinations = pieceCombinations(occupationAsSet(validityOccupation(challenge).getValue()).size());


        // sort the Combinations to improve the possibility of finding the solution.
        combinations.sort(new SortByAverage());

        int numberOfPieceToPut = 10 - challenge.length()/4;

        for (Combinations combination : combinations) {
            // test if the total number of pieces are not the same as the pieces need to solve the game.
            if (combination.small+combination.medium+ combination.large != numberOfPieceToPut)
                continue;
            findSolution(test,pieceTrack,invalidStates,result,combination);

            if (!result.isEmpty())
                break;

        }

        return result.get(0);
    }



    /**
     * occupationAsSet will convert a int Array of empty locations to a set of
     * Pair of empty locations, with key indicating the column, value indicating the row.
     *
     * @param occupationArray an Array of Array representing the board occupations.
     *
     * @return a Set of Pairs representing the coordinates.
     * Author: Qinrui Cheng  u7133046.
     */
    public static Set<Pair<Integer,Integer>> occupationAsSet(int[][] occupationArray) {
        Set<Pair<Integer,Integer>> rtn = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (occupationArray[i][j] == 0) rtn.add(new Pair<>(i,j));
            }
        }
        return rtn;
    }

    /**
     * removePiece will remove a specific piece from the given challenge.
     *
     * @param challenge a String challenge.
     * @param pieceToRemove the piece to be removed form the String challenge
     *
     * @return the String challenge after removing the specified piece.
     *  Author: Qinrui Cheng  u7133046.
     */
    public static String removePiece(String challenge, String pieceToRemove) {
        StringBuilder rtn = new StringBuilder();
        for (int i = 0; i < challenge.length(); i += 4) {
            String piece = challenge.substring(i,i+4);
            if (!piece.equals(pieceToRemove)) rtn.append(piece);
        }
        return sortStringPlacement(rtn.toString());
    }






    /**
     * canPut will take a challenge and a piece, check if the piece can be add in the challenge
     * according to whether the challenge contains pieces with the same shape as the piece, or can be rotated to get the
     * same shape. Since we know the the solution of a game must be unique, this can't be happened.
     *
     * @param challenge a String challenge.
     * @param piece a piece to be tested.
     *
     * @return if the piece won't violate the uniqueness of the solution or not.
     *  Author: Qinrui Cheng  u7133046.
     */
    public static boolean canPut(String challenge, String piece) {
        List<Character> colourList = new ArrayList<>();
        for (int i = 0; i < challenge.length(); i+=4) {
            colourList.add(challenge.charAt(i));
        }

        char color = piece.charAt(0);

        switch (color) {
            case 'b':
                return !colourList.contains('r');
            case 'r':
                return !colourList.contains('b');
            case 'o':
                return !colourList.contains('s');
            case 's':
                return !colourList.contains('o');
            case 'L':
                return !colourList.contains('N');
            case 'N':
                return !colourList.contains('L');
            case 'g':
                return !colourList.contains('n');
            case 'n':
                return !colourList.contains('g');
        }

        return true;
    }




}


