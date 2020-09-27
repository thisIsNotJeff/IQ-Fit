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
        test(p1, pos, false); // invalid row
        PuzzlePieces p2 = new PuzzlePieces(Direction.EAST, Color.blue, 0, 10);
        test(p2, pos, false); // invalid column
        PuzzlePieces p3 = new PuzzlePieces(Direction.EAST, Color.blue, 0, 0);
        test(p3, pos, true); // invalid column
    }
/*
    @Test
    public void piecesWellFormed() {
        for (int i = 0; i < SOLUTIONS.length; i++)
            for (int j = 4; j < SOLUTIONS[i].placement.length(); j += 4) {
                test(SOLUTIONS[i].placement.substring(0, j), true);
                test(SOLUTIONS[i].placement.substring(0, j - 4) + BAD_PIECES[i % BAD_PIECES.length], false);
            }
    }

    @Test
    public void correctOrder() {
        for (int i = 0; i < SOLUTIONS.length; i++) {
            test(SOLUTIONS[i].placement, true);
            String wrong = SOLUTIONS[i].placement.substring(SOLUTIONS[i].placement.length() / 2);
            wrong += SOLUTIONS[i].placement.substring(0, SOLUTIONS[i].placement.length() / 2);
            test(wrong, false);
        }
    }

    @Test
    public void duplicatesA() {
        for (int i = 0; i < 40; i += 4) {
            if (i == 16)
                continue;
            String bad = SOLUTIONS[100].placement.substring(0, i) +
                    SOLUTIONS[100].placement.substring(16, 20) +
                    SOLUTIONS[100].placement.substring(i + 4, 40);
            test(SOLUTIONS[i + 32].placement, true);
            test(bad, false);
        }
    }

    @Test
    public void duplicatesB() {
        for (int i = 0; i < 40; i += 4) {
            if (i == 16)
                continue;
            String bad = SOLUTIONS[100].placement.substring(0, i) +
                    SOLUTIONS[100].placement.substring(16, 20).charAt(0) +
                    "00W" +
                    SOLUTIONS[100].placement.substring(i + 4, 40);
            test(SOLUTIONS[i + 32].placement, true);
            test(bad, false);
        }
    }*/
}
