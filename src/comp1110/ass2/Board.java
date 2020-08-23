package comp1110.ass2;

public class Board {
    Games game;
    public int[][] position = new int[10][5];

    /**
     * start a new game with the given difficulty
     * @param difficulty difficulty of the game
     */
    public Board(int difficulty) {

    }

    /**
     * given a puzzle and a position represented by an array,
     * judge if the puzzle can be put in the position
     * @param puzzle given a puzzle
     * @param position the upper left of the puzzle will be put on the position
     * @return return true if there is not overlap or the puzzle will not be outside
     * of the board. Otherwise return false.
     */
    public boolean canBePut(Puzzle puzzle, int[] position) {
        return false;
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
    public boolean ifFinished() { return false; }

}
