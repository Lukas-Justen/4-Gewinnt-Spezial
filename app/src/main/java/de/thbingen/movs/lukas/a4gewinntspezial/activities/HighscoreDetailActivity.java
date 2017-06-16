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
 *          Die Klasse HighscoreDetailActivity zeigt einen einzelnen Eintrag aus der Liste alles
 *          Highscores detailiert an. Hierzu werden Graphen und andere Textfelder verwendet. Neben
 *          denb Rohdaten kann es auch eine Platzierung anhand des Servers geben.
 */
public class HighscoreDetailActivity extends AppCompatActivity {

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse HighscoreDetailActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_detail);
    }

}
