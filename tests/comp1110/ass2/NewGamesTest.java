package comp1110.ass2;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class NewGamesTest {

    private void testDifficulty(int difficulty) {
        Games[] out = new Games[24];
        for (int i = 0; i < out.length; i++) {
            out[i] = Games.newGames(difficulty);
            int diff = Games.getDifficulty(out[i]);
            assertTrue("Expected difficulty " + difficulty + ", but " + (diff == -1 ? "did not get one from the pre-defined games" : "got one of difficulty " + diff) + ": problem number " + out[i].getNumber() + ".", diff == difficulty);
        }
        int unique = Games.countGames(out);
        assertTrue("Expected at least 3 different objectives after calling newGames() 24 times, but only got " + unique + ".", unique >= 3);
    }

    @Test
    public void testStarter() {
        testDifficulty(0);
    }

    @Test
    public void testJunior() {
        testDifficulty(1);
    }

    @Test
    public void testExpert() {
        testDifficulty(2);
    }

    @Test
    public void testMaster() { testDifficulty(3); }

    @Test
    public void testWizard() { testDifficulty(4); }
}
