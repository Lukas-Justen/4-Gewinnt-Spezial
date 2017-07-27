package de.thbingen.movs.lukas.a4gewinntspezial.game;

import io.realm.RealmObject;

public class Playerresults extends RealmObject {

    private String name = "";
    private int victories = 0;
    private int losses = 0;
    private int games = 0;
    private int rounds = 0;
    private long time = 0;
    private int colorOfPreference = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getColorOfPreference() {
        return colorOfPreference;
    }

    public void setColorOfPreference(int colorOfPreference) {
        this.colorOfPreference = colorOfPreference;
    }

    public void addVictories(int victory) {
        setVictories(getVictories() + victory);
    }

    public void nextGame() {
        setGames(getGames() + 1);
    }

    public void addRounds(int rounds) {
        setRounds(getRounds() + rounds);
    }

    public void addLosses(int victories) {
        setLosses(getLosses() + 1 - victories);
    }

    public void addTime(long playtime) {
        setTime(getTime() + playtime);
    }

    public int getDraws() {
        return getGames() - getVictories() - getLosses();
    }

    public void addColorOfPreference(int color) {
        setColorOfPreference(getColorOfPreference()+color);
    }

}