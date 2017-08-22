package de.thbingen.movs.lukas.a4gewinntspezial.game

import io.realm.RealmObject

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
class Playerresult : RealmObject() {

    var name: String = ""
    var alias: String = ""
    var victories: Int = 0
    var losses: Int = 0
    var games: Int = 0
    var rounds: Int = 0
    var time: Long = 0
    var colorOfPreference: Int = 0

    fun addVictories(victory: Int) {
        victories += victory
    }

    fun nextGame() {
        games++
    }

    fun addRounds(rounds: Int) {
        this.rounds += rounds
    }

    fun addLosses(victories: Int) {
        losses += 1 - victories
    }

    fun addTime(playtime: Long) {
        time += playtime
    }

    fun getDraws(): Int {
        return games - victories - losses
    }

    fun addColorOfPreference(color: Int) {
        colorOfPreference += color
    }

}