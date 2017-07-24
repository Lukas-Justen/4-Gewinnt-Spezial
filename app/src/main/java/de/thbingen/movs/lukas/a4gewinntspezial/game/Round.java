package de.thbingen.movs.lukas.a4gewinntspezial.game;

import io.realm.RealmObject;

public class Round extends RealmObject {

    private int numberOfRound;
    private int redScore;
    private int yellowScore;
    private int roundsNeeded;
    private boolean draw;

    public int getNumberOfRound() {
        return numberOfRound;
    }

    public void setNumberOfRound(int numberOfRound) {
        this.numberOfRound = numberOfRound;
    }

    public int getRedScore() {
        return redScore;
    }

    public void setRedScore(int redScore) {
        this.redScore = redScore;
    }

    public int getYellowScore() {
        return yellowScore;
    }

    public void setYellowScore(int yellowScore) {
        this.yellowScore = yellowScore;
    }

    public int getRoundsNeeded() {
        return roundsNeeded;
    }

    public void setRoundsNeeded(int roundsNeeded) {
        this.roundsNeeded = roundsNeeded;
    }

    public boolean getDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

}
