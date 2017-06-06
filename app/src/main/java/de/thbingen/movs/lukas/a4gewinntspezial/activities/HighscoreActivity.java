package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 06.06.2017
 *          <p>
 *          Die Klasse HighscoreActivity stellt alle jemals erfassten Spielstände in einer Liste dar
 *          wobei es zusätzlich die Möglichkeit gibt sich die Spielstände graphisch darstellen zu
 *          lassen.
 */
public class HighscoreActivity extends AppCompatActivity {

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse HighscoreActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
    }

}
