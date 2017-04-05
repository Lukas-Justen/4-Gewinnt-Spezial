package de.thbingen.movs.lukas.a4gewinntspezial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse GameActivity ist zur Anzeige des Spielfeldes und für das eigentliche Spiel-
 *          feld vorbehalten. Zu Beginn wird das Spielfeld über den Java-Code generiert und je
 *          7 Spalten mit 6 ImageViews befüllt. Diese bilden die einzelnen Felder des 4-Gewinnt
 *          Spiels. Neben dem Einsortieren der Spielsteine in die jeweiligen Spalten ist die Game-
 *          Activity auch für die Kommunikation zu dem Partnergerät verantwortlich.
 */
public class GameActivity extends AppCompatActivity {

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse GameActivity zu leisten hat. Hier wird
     * beispielsweise das Spielfeld generiert, aber auch die Kommunikation zu dem anderen Gerät
     * gehandelt.
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

}
