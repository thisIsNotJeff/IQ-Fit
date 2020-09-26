package comp1110.ass2;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class NewGamesTest {

    /**
     * @param game the game
     * @return the difficulty level of the game (0 - starter, 1 - junior, 2 - expert, 3 - master, 4 - wizard)
     */
    public int getDifficulty(Games game) {
        for (int i = 0; i < Games.SOLUTIONS.length; i++) {
            if (game == Games.SOLUTIONS[i])
                return i / (Games.SOLUTIONS.length/5);
        }
        return -1;
    }

    /**
     * @param games the array contained numbers of games objective
     * @return the number of games contained in the array
     */
    private int countGames(Games[] games) {
        return games.length;
    }

    private void doTest(int difficulty) {
        Games[] out = new Games[24];
        for (int i = 0; i < out.length; i++) {
            out[i] = Games.newGames(difficulty);
            int diff = getDifficulty(out[i]);
            assertTrue("Expected difficulty " + difficulty + ", but " + (diff == -1 ? "did not get one from the prepared games" : "got one of difficulty " + diff) + ": problem number " + out[i].getNumber() + ".", diff == difficulty);
        }
        int unique = countGames(out);
        assertTrue("Expected at least 3 different objectives after calling newGames() 24 times, but only got " + unique + ".", unique >= 3);
    }

    @Test
    public void testStarter() {
        doTest(0);
    }

    @Test
    public void testJunior() {
        doTest(1);
    }

    @Test
    public void testExpert() {
        doTest(2);
    }

    @Test
    public void testMaster() { doTest(3); }

    @Test
    public void testWizard() { doTest(4); }
}
