package de.thbingen.movs.lukas.a4gewinntspezial.game;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Result extends RealmObject{

    private boolean local;
    private String redName;
    private String yellowName;
    private int redScore;
    private int yellowScore;
    private RealmList<Round> rounds = new RealmList<>();
    private long id;

    public boolean getLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public String getRedName() {
        return redName;
    }

    public void setRedName(String redName) {
        this.redName = redName;
    }

    public String getYellowName() {
        return yellowName;
    }

    public void setYellowName(String yellowName) {
        this.yellowName = yellowName;
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

    public RealmList<Round> getRounds() {
        return rounds;
    }

    public void setRounds(RealmList<Round> rounds) {
        this.rounds = rounds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
