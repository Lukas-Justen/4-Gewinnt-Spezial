package de.thbingen.movs.lukas.a4gewinntspezial.game;

import java.io.Serializable;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 20.06.2017
 *          <p>
 *          Enum für Spieler 1 und Spieler 2. Zusätzlich wird die Farbe und der Spielstein mit
 *          abgespeichert.
 */
public enum Player implements Serializable {

    P1(R.color.colorRed, R.drawable.stone_red), P2(R.color.colorYellow, R.drawable.stone_yellow);

    private final int color;
    private final int image;

    /**
     * Legt einen neuen Spieler an.
     *
     * @param color Farbe des Spielers.
     * @param image Sein Spielstein.
     */
    Player(int color, int image) {
        this.color = color;
        this.image = image;
    }

    /**
     * Liefert die Farbe des Spielers.
     *
     * @return Die Farbe als int-Ressource
     */
    public int getColor() {
        return color;
    }

    /**
     * Liefert den Spielstein des Spielers.
     *
     * @return Der Spielstein als int-Ressource
     */
    public int getImage() {
        return image;
    }

}