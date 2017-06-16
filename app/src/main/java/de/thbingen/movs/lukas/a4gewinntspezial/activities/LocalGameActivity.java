package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse LocalGameActivity ist für das lokale Spiel verantwortlich. Nachdem einer
 *          der beiden Spieler gewonnen oder es zu einem Unentschieden gekommen ist wird das
 *          Ergebnis in der Datenbank festgeschrieben. Bei einem solchen Spiel kann es während des
 *          Spiels eher weniger zu Störungen kommen als bei einem Online-Game.
 */
public class LocalGameActivity extends AppCompatActivity {

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse LocalGameActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);
    }

}
