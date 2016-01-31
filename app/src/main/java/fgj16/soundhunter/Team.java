package fgj16.soundhunter;

/**
 * Created by tronkko on 30.1.2016.
 */
public class Team {

    /* Team's name */
    private String mName;

    /* Current score */
    private int mScore;

    /* Create team */
    public Team (String name) {
        mName = name;
        mScore = 0;
    }

    /* Add score */
    public void addScore () {
        mScore++;
    }

    /* Get team name */
    public String getName () {
        return mName;
    }

    /* Get current score */
    public int getScore () {
        return mScore;
    }
}
