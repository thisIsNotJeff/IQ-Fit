package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import java.util.HashSet;
import static org.junit.Assert.assertTrue;

public class newGamesTest {

    @Rule
    public Timeout globalTimeout = Timeout.millis(2000);
    private int getDifficulty(Games objective) {
        for (int i = 0; i < Games.SOLUTIONS.length; i++) {
            if (objective == Games.SOLUTIONS[i])
                return i / (Games.SOLUTIONS.length/5);
        }
        return -1;
    }

    private int countObjectives(Games[] objectives) {
        HashSet<Games> set = new HashSet<>();
        for (Games o : objectives)
            set.add(o);
        return set.size();
    }

    private void doTest(int difficulty) {
        Games[] out = new Games[24];
        for (int i = 0; i < out.length; i++) {
            out[i] = Games.newGames(difficulty);
            int diff = getDifficulty(out[i]);
            assertTrue("Expected difficulty " + difficulty + ", but " + (diff == -1 ? "did not get one from the prepared objectives" : "got one of difficulty " + diff) + ": problem number " + out[i].getNumber() + ".", diff == difficulty);
        }
        int unique = countObjectives(out);
        assertTrue("Expected at least 3 different objectives after calling newObjective() 12 times, but only got " + unique + ".", unique >= 3);
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
