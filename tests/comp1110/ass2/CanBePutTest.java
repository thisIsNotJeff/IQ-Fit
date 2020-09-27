package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;


import java.util.Arrays;

import static comp1110.ass2.Games.SOLUTIONS;
import static comp1110.ass2.TestUtility.BAD_PIECES;
import static org.junit.Assert.assertTrue;

public class CanBePutTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(500);

    private void test(PuzzlePieces p, int[][] pos, boolean expected) {
        boolean out = GameBoard.canBePut(p, pos);
        assertTrue("Input was " + p + " and " + Arrays.deepToString(pos) + ", expected " + expected + " but got " + out, out == expected);
    }

    @Test
    public void invalidPuzzlePiece() {
        int[][] pos = new int[10][5];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                pos[i][j] = 0;
            }
        }
        PuzzlePieces p1 = new PuzzlePieces(Direction.EAST, Color.blue, 5, 0);
        test(p1, pos, false);
        PuzzlePieces p2 = new PuzzlePieces(Direction.WEST, Color.green, 0, 10);
        test(p2, pos, false);
        PuzzlePieces p3 = new PuzzlePieces(Direction.SOUTH, Color.ORANGE, -1, 0);
        test(p3, pos, false);
        PuzzlePieces p4 = new PuzzlePieces(Direction.WEST, Color.SKYBLUE, 0, -2);
        test(p4, pos, false);
    }

    @Test
    public void invalidPieces() {
        int[][] pos = new int[10][5];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                pos[i][j] = 0;
            }
        }
        PuzzlePieces p1 = new PuzzlePieces(Direction.EAST, Color.blue, 5, 0);
        test(p1, pos, false);
        PuzzlePieces p2 = new PuzzlePieces(Direction.EAST, Color.blue, 0, 10);
        test(p2, pos, false);
        PuzzlePieces p3 = new PuzzlePieces(Direction.EAST, Color.blue, -1, 0);
        test(p3, pos, false);
        PuzzlePieces p4 = new PuzzlePieces(Direction.EAST, Color.blue, 0, -2);
        test(p4, pos, false);
    }

    @Test
    public void offBoard() {
        int[][] pos = new int[10][5];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                pos[i][j] = 0;
            }
        }
        PuzzlePieces p1 = new PuzzlePieces(Direction.EAST, Color.blue, 2, 7);
        test(p1, pos, false);
        PuzzlePieces p2 = new PuzzlePieces(Direction.WEST, Color.RED, 0, 9);
        test(p2, pos, false);
        PuzzlePieces p3 = new PuzzlePieces(Direction.SOUTH, Color.limegreen, 4, 2);
        test(p3, pos, false);
        PuzzlePieces p4 = new PuzzlePieces(Direction.EAST, Color.blue, 4, 0);
        test(p4, pos, false);
    }
    @Test
    public void overlap() {
        int[][] pos = new int[10][5];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                pos[i][j] = 0;
            }
        }
        pos[0][0] = pos[1][0] = pos[2][0] = pos[3][0] = pos[0][1] = 1;
        PuzzlePieces p1 = new PuzzlePieces(Direction.NORTH, Color.orange, 0, 3);
        test(p1, pos, false);
        pos[1][1] = pos[2][1] = pos[3][1] = pos[4][1] = pos[3][2] = pos[4][2] = 1;
        PuzzlePieces p2 = new PuzzlePieces(Direction.SOUTH, Color.skyblue, 1, 3);
        test(p2, pos, false);
        PuzzlePieces p3 = new PuzzlePieces(Direction.EAST, Color.LIMEGREEN, 2, 2);
        test(p3, pos, false);
        pos[5][2] = pos[6][2] = pos[7][2] = pos[8][2] = pos[5][3] = 1;
        PuzzlePieces p4 = new PuzzlePieces(Direction.SOUTH, Color.INDIGO, 4, 3);
        test(p4, pos, false);
    }

    @Test
    public void validPosition() {
        int[][] pos = new int[10][5];
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                pos[i][j] = 0;
            }
        }
        pos[0][0] = pos[1][0] = pos[2][0] = pos[3][0] = pos[0][1] = 1;
        pos[5][2] = pos[6][2] = pos[7][2] = pos[8][2] = pos[5][3] = 1;
        PuzzlePieces p1 = new PuzzlePieces(Direction.NORTH, Color.YELLOW, 1, 1);
        test(p1, pos, true);
        pos[8][0] = pos[9][0] = pos[6][1] = pos[7][1] = pos[8][1] = pos[9][1] = 1;
        PuzzlePieces p2 = new PuzzlePieces(Direction.NORTH, Color.orange, 0, 4);
        test(p2, pos, true);
        pos[2][2] = pos[1][3] = pos[2][3] = pos[3][3] = 1;
        PuzzlePieces p3 = new PuzzlePieces(Direction.WEST, Color.LIMEGREEN, 2, 0);
        test(p3, pos, true);
        PuzzlePieces p4 = new PuzzlePieces(Direction.EAST, Color.navyblue, 2, 8);
        test(p4, pos, true);
    }
}
