package comp1110.ass2;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class NewGamesTest {

    private void testDifficulty(int difficulty) {

        // create 1000 games with newGames method, and test if all these games are having right difficulty level
        ArrayList<Games> out = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            out.add(i,Games.newGames(difficulty));
            int diff = Games.getDifficulty(out.get(i));
            assertTrue("Expected difficulty " + difficulty + ", but " + (diff == -1 ? "did not get one from the pre-defined games" : "got one of difficulty " + diff) + ".", diff == difficulty);
        }


        ArrayList<Games> distinct = new ArrayList<>(out.size());
        for(int i =0; i < out.size();i++){
            if(!distinct.contains(out.get(i))){
                distinct.add(out.get(i));
            }
        }
        assertTrue("Expected all 24 distinct games from difficulty " + difficulty + ", but only got " + distinct.size() + ".", distinct.size() == 24);
    }

    @Test
    public void testStarter() { testDifficulty(0);}

    @Test
    public void testJunior() { testDifficulty(1);}

    @Test
    public void testExpert() { testDifficulty(2);}

    @Test
    public void testMaster() { testDifficulty(3);}

    @Test
    public void testWizard() { testDifficulty(4);}
}
