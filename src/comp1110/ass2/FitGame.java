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


    public static class gameTree {
        String gameState;
        Set<Pair<Integer, Integer>> emptyLocations;
        List<gameTree> nodes;

        public gameTree(String gameState, Set<Pair<Integer, Integer>> emptyLocations, List<gameTree> nodes) {
            this.gameState = gameState;
            this.emptyLocations = emptyLocations;
            this.nodes = nodes;
        }

        @Override
        public boolean equals(Object tree) {
            if (this == tree) return true;
            else if (!(tree instanceof gameTree)) return false;
            else return (this.gameState.equals(((gameTree) tree).gameState));
        }

        public static void growTree(gameTree startRoot, int level) {
            // check if the level satisfy the base case to stop.
            if (level != 0) {

                var validityOccupation = validityOccupation(startRoot.gameState);

                var emptyLocations = occupationAsSet(validityOccupation.getValue());

                // iterate over all the empty locations
                for (Pair<Integer, Integer> location : emptyLocations) {
                    Set<String> viablePieces = getViablePiecePlacements(startRoot.gameState, location.getKey(), location.getValue());

                    // for each emptyLocation, iterate over each viable piece to cover that location.
                    if (viablePieces != null && !viablePieces.isEmpty()) {

                        // determine if any of them can be put in the game.
                        for (String piece : viablePieces) {
                            if (canPut(startRoot.gameState, piece)) {
                                var newGameState = sortStringPlacement(startRoot.gameState + piece);
                                var newEmptyLocations = occupationAsSet(validityOccupation(newGameState).getValue());
                                List<gameTree> newGameTree = new ArrayList<>();

                                gameTree nextNodes = new gameTree(newGameState, newEmptyLocations, newGameTree);

                                boolean containsDuplicates = false;
                                // do not adding gameTrees with the same gameState.
                                for (gameTree tree : startRoot.nodes) {
                                    if (nextNodes.equals(tree)) {
                                        containsDuplicates = true;
                                        break;
                                    }
                                }

                                // add new node to the parent node.
                                if (!containsDuplicates)
                                    startRoot.nodes.add(nextNodes);

                            }
                        }
                    } else break;

                }

                // continuing generating the children nodes of children nodes of the startNode.
                for (gameTree childNode : startRoot.nodes) {
                    growTree(childNode, level-1);
//                // at this stage of the game, solution found, stop growing the tree.
//                if (occupationAsSet(validityOccupation(childNode.gameState).getValue()).size() != 0)
                }


            }


        }

    }








    /**
     * Return the solution to a particular challenge.
     **
     * @param challenge A challenge string.
     * @return A placement string describing the encoding of the solution to
     * the challenge.
     */

    public static String getSolution(String challenge) {

        Set<Pair<Integer,Integer>> emptyLocation = occupationAsSet(validityOccupation(challenge).getValue());

        if (emptyLocation.size()==0) return challenge;
        else {
            List<String> results = new ArrayList<>();

            for (Pair<Integer,Integer> coordinates : emptyLocation) {
                //if (getViablePiecePlacements(challenge, coordinates.getKey(), coordinates.getValue()).s)

                // start searching
                findSolution(challenge,coordinates,results);

                // If found one solution, and it's not null(with length greater than 0) return it, and stop.
                for (String result : results) {
                    if (result.length() != 0) return result;
                    break;
                }
            }
        }

        return null;
    }

    public static void findSolution(String challenge, Pair<Integer,Integer> currentLocation, List<String> result) {
        // base case: all the locations on the board are taken, solution found.
        if (occupationAsSet(validityOccupation(challenge).getValue()).size() == 0)
            result.add(challenge);
        else {
            Set<String> viablePieces = getViablePiecePlacements(challenge, currentLocation.getKey(), currentLocation.getValue());
            if (viablePieces != null && viablePieces.size() != 0) {
                for (String piece : viablePieces) {

                    // added chosen piece into the string.
                    if (canPut(challenge,piece))
                        challenge = sortStringPlacement(challenge + piece);
                    else continue;


                    // update the empty locations.
                    Set<Pair<Integer,Integer>> emptyLocation = occupationAsSet(validityOccupation(challenge).getValue());

                    if (emptyLocation.size() == 0) {
                        result.add(challenge);
                        break;
                    }

                    for (Pair<Integer,Integer> newLocation : emptyLocation) {
                        findSolution(challenge,newLocation,result);
                    }

                    // remove the chosen piece.
                    challenge = removePiece(challenge,piece);
                }
            }

        }

    }


    public static Set<Pair<Integer,Integer>> occupationAsSet(int[][] occupationArray) {
        Set<Pair<Integer,Integer>> rtn = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                if (occupationArray[i][j] == 0) rtn.add(new Pair<>(i,j));
            }
        }
        return rtn;
    }

    public static String removePiece(String challenge, String pieceToRemove) {
        String rtn = "";
        for (int i = 0; i < challenge.length(); i += 4) {
            String piece = challenge.substring(i,i+4);
            if (!piece.equals(pieceToRemove)) rtn += piece;
        }
        return rtn;
    }


    public static boolean canPut(String challenge, String piece) {
        List<String> colourAndDirList = new ArrayList<>();
        for (int i = 0; i < challenge.length(); i+=4) {
            colourAndDirList.add(challenge.charAt(i)+""+challenge.charAt(i+3));
        }

        String direction = piece.charAt(3)+"";
        String color = piece.charAt(0) +"";

        switch (color) {
            case "b":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("rN");
                    case "E":
                        return !colourAndDirList.contains("rE");
                    case "S":
                        return !colourAndDirList.contains("rS");
                    case "W":
                        return !colourAndDirList.contains("rW");
                }
                break;
            case "r":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("bN");
                    case "E":
                        return !colourAndDirList.contains("bE");
                    case "S":
                        return !colourAndDirList.contains("bS");
                    case "W":
                        return !colourAndDirList.contains("bW");
                }
                break;
            case "o":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("sN");
                    case "E":
                        return !colourAndDirList.contains("sE");
                    case "S":
                        return !colourAndDirList.contains("sS");
                    case "W":
                        return !colourAndDirList.contains("sW");
                }
                break;
            case "s":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("oN");
                    case "E":
                        return !colourAndDirList.contains("oE");
                    case "S":
                        return !colourAndDirList.contains("oS");
                    case "W":
                        return !colourAndDirList.contains("oW");
                }
                break;
            case "L":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("NN");
                    case "E":
                        return !colourAndDirList.contains("NE");
                    case "S":
                        return !colourAndDirList.contains("NS");
                    case "W":
                        return !colourAndDirList.contains("NW");
                }
                break;
            case "N":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("LN");
                    case "E":
                        return !colourAndDirList.contains("LE");
                    case "S":
                        return !colourAndDirList.contains("LS");
                    case "W":
                        return !colourAndDirList.contains("LW");
                }
                break;
            case "g":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("nN");
                    case "E":
                        return !colourAndDirList.contains("nE");
                    case "S":
                        return !colourAndDirList.contains("nS");
                    case "W":
                        return !colourAndDirList.contains("nW");
                }
                break;
            case "n":
                switch (direction) {
                    case "N":
                        return !colourAndDirList.contains("gN");
                    case "E":
                        return !colourAndDirList.contains("gE");
                    case "S":
                        return !colourAndDirList.contains("gS");
                    case "W":
                        return !colourAndDirList.contains("gW");
                }
                break;
        }

        return true;
    }




}


