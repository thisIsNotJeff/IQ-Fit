package comp1110.ass2;

import java.util.*;

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

        /** test if the String has four characters. */
        if(piecePlacement.length()!=4){return false;}

        /** test if the first character is valid descriptor character. */
        switch (String.valueOf(piecePlacement.charAt(0)).toUpperCase()){
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
                    shapes.append(s.toLowerCase().charAt(0));
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

        return flag;
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
        if (!isPlacementWellFormed(placement)) return false;
        else {
            int[][] pos = new int[10][5];
            int i = 0;
            int j = 0;
            int col;
            int row;
            while(i < placement.length()) {
                col = placement.charAt(i + 1) - '0';
                row = placement.charAt(i + 2) - '0';
                if((col < 0)||(col > 9)||(row < 0)||(row > 4)) return false;
                if((placement.charAt(i + 3) != 'N')&&(placement.charAt(i + 3) != 'E')&&
                        (placement.charAt(i + 3) != 'S')&&(placement.charAt(i + 3) != 'W')) return false;
                else if(placement.charAt(i) == 'b') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'B') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'g') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row + 1] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'G') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'i') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'I') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'l') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'L') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'n') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row + 1] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'N') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 7)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 2)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'o') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'O') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'p') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 1] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'P') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'r') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'R') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 's') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'S') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'y') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                        }
                    }
                }
                else if(placement.charAt(i) == 'Y') {
                    if(placement.charAt(i + 3) == 'N') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 2][row] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'E') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 1][row + 2] += 1;
                            pos[col + 1][row + 3] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'S') {
                        if((col > 6)||(row > 3)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                            pos[col + 2][row + 1] += 1;
                            pos[col + 3][row + 1] += 1;
                        }
                    }
                    else if(placement.charAt(i + 3) == 'W') {
                        if((col > 8)||(row > 1)) return false;
                        else {
                            pos[col][row] += 1;
                            pos[col][row + 1] += 1;
                            pos[col][row + 2] += 1;
                            pos[col][row + 3] += 1;
                            pos[col + 1][row] += 1;
                            pos[col + 1][row + 1] += 1;
                        }
                    }
                } else if (placement.charAt(i) == '*') {
                    pos[col][row] += 1;
                }
                else return false;
                i += 4;
            }

            for(i = 0; i < 10; i++) {
                for(j = 0; j < 5; j++) {
                    if(pos[i][j] > 1) return false;
                }
            }
        }


        return true;
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

        Set<String> result = new HashSet<>();

        Integer columns = col;
        Integer rows = row;
        if (isPlacementValid(sortStringPlacement(placement+'*' + columns.toString() + rows.toString() + 'W'))) {
            for (Color c : Color.values()) {
                for (Direction d : Direction.values()) {
                    for (Integer y = 0; y < 5; y++) {
                        for (Integer x = 0; x < 10; x++) {
                            String newPiece = c.value + x.toString() + y.toString() + d.value;
                            if (isPlacementValid(sortStringPlacement(placement + newPiece))) {
                                String newPlacement =sortStringPlacement(placement + newPiece + '*' + columns.toString() + rows.toString() + 'W');
                                if (!isPlacementValid(newPlacement)) {
                                    if (c != Color.TEST) result.add(newPiece);
                                }
                            }
                        }
                    }
                }
            }
        } else return null;



        if (result.size() == 0) return null;
        else return result;

    }

    /**
     * Return the sorted StringPlacement.
     *
     * @param placement A String placement.
     * @return A sorted String placement according to the alphabetical order of the color of pieces.
     */

    public static String sortStringPlacement(String placement) {
        List<String> grouped = new ArrayList<>();
        for (int i = 0; i < placement.length(); i+=4) {
            grouped.add(placement.substring(i,i+4));
        }
        grouped.sort(Comparator.comparing((String s) -> s.substring(0, 1).toLowerCase()));
        String sorted = "";
        for (String s : grouped) sorted += s;
        return sorted;
    }



    /**
     * Return the solution to a particular challenge.
     **
     * @param challenge A challenge string.
     * @return A placement string describing the encoding of the solution to
     * the challenge.
     */
    public static String getSolution(String challenge) {
        for (int i = 0; i < Games.SOLUTIONS.length; i++) {
            if (challenge == Games.SOLUTIONS[i].objective) {
                return Games.SOLUTIONS[i].placement;
            }
        }
        return null;  // FIXME Task 9: determine the solution to the game, given a particular challenge
    }
}


