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
public enum Player implements Serializable{

    P1(R.color.colorRed, R.drawable.stone_red), P2(R.color.colorYellow, R.drawable.stone_yellow);

    private int color;
    private int image;

    Player(int color, int image) {
        this.color = color;
        this.image = image;
    }

    public int getColor() {
        return color;
    }

    public int getImage() {
        return image;
    }

}