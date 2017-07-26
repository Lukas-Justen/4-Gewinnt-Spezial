package de.thbingen.movs.lukas.a4gewinntspezial.game;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Playerresults extends RealmObject {

    private String name = "";
    private RealmList<Enemy> enemy = new RealmList<>();
    private int victories = 0;
    private int losses = 0;
    private int games = 0;
    private int rounds = 0;
    private int time = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Enemy> getEnemy() {
        return enemy;
    }

    public void setEnemy(RealmList<Enemy> enemy) {
        this.enemy = enemy;
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

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addVictories(int victory) {
        setVictories(getVictories() + victory);
    }

    public void nextGame() {
        setGames(getGames() + 1);
    }

    public void addRounds(int rounds) {
        setRounds(getRounds()+rounds);
    }

    public void addLosses(int victories) {
        setLosses(getLosses() + 1 - victories);
    }

}