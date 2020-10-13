package comp1110.ass2;

import javafx.util.Pair;

import java.util.Arrays;

/**
 * @author Yuxuan Hu
 */

public class GameBoard {
    public int[][] occupationArray = new int[10][5];

    /**
     * start a new game with the given difficulty
     * @param difficulty difficulty of the game
     */
    public GameBoard(int difficulty) {
    }

    /**
     * given a puzzle and a position represented by an array,
     * judge if the puzzle can be put in the position
     * @param puzzle given a puzzle
     * @return return true if there is not overlap or the puzzle will not be outside
     * of the board. Otherwise return false.
     */
    /*public static boolean canBePut(PuzzlePieces puzzle, int[][] occupationArray) {
        String pos = puzzle.toString();
        if(!FitGame.validityOccupation(pos).getKey()) return false;
        int[][] previous_occupation;
        int[][] puzzle_occupation;
        previous_occupation = occupationArray;
        puzzle_occupation = FitGame.validityOccupation(puzzle.toString()).getValue();
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                previous_occupation[i][j] += puzzle_occupation[i][j];
            }
        }
        for(int m = 0; m < 10; m++) {
            for(int n = 0; n < 5; n++) {
                if(occupationArray[m][n] == 2) {
                    for(int i = 0; i < 10; i++) {
                        for(int j = 0; j < 5; j++) {
                            previous_occupation[i][j] -= puzzle_occupation[i][j];
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }*/
    public static boolean canBePut(PuzzlePieces puzzle, int[][] occupationArray) {
        String pos = puzzle.toString();
        if(!FitGame.validityOccupation(pos).getKey()) return false;
        int[][] puzzle_occupation;
        puzzle_occupation = FitGame.validityOccupation(puzzle.toString()).getValue();
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                occupationArray[i][j] += puzzle_occupation[i][j];
            }
        }
        for(int m = 0; m < 10; m++) {
            for(int n = 0; n < 5; n++) {
                if(occupationArray[m][n] == 2) {
                    for(int i = 0; i < 10; i++) {
                        for(int j = 0; j < 5; j++) {
                            occupationArray[i][j] -= puzzle_occupation[i][j];
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    public static void removePiece(PuzzlePieces puzzle, int[][] occupationArray) {
        int[][] puzzle_occupation;
        puzzle_occupation = FitGame.validityOccupation(puzzle.toString()).getValue();
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                occupationArray[i][j] -= puzzle_occupation[i][j];
            }
        }
    }

    public int[][] getPos() {
        return occupationArray;
    }

    /**
     * judge if the position is empty
     * @param position given a position
     * @return return true if it's empty. Otherwise return false.
     */
    public boolean ifEmpty(int[] position) {
        return false;
    }

    /**
     * judge if all the holes on the board is filled by puzzles
     * @return return true if the board is filled.  Otherwise return false.
     */
    public boolean ifFinished() { return false;  }

}
