package de.thbingen.movs.lukas.a4gewinntspezial.game;

import io.realm.RealmObject;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 01.08.2017
 *          <p>
 *          Die KLasse Playerresult speichert alle Informationen zu den Siegen und Niederlagen eines
 *          Spielers ab. Neben den Siegen und Niederlagen wird die Anzahl der gespielten Spiele, die
 *          Summe aller Runden, die Gesamtspielzeit und die bevorzugte Lieblingsspielsteinfarbe ge-
 *          speichert. Für jedes Datenelemnt muss ein Setter un ein Getter zur Verfügung gestellt werden
 */
public class Playerresult extends RealmObject {

    private String name = "";
    private String alias = "";
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getVictories() {
        return victories;
    }

    private void setVictories(int victories) {
        this.victories = victories;
    }

    public int getLosses() {
        return losses;
    }

    private void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGames() {
        return games;
    }

    private void setGames(int games) {
        this.games = games;
    }

    public int getRounds() {
        return rounds;
    }

    private void setRounds(int rounds) {
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