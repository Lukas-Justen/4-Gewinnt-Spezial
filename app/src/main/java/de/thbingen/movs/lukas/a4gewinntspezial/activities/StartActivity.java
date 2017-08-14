package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse StartActivity stellt den StartScreen der App zur Verfügung. Zusätzlich bietet
 *          die Oberfläche Möglichkeiten zum Starten eines neuen Online-Games bzw. eines neuen
 *          Lokalen-Games. Neben dem Startenneuer Spiele wird aber auch eine Liste aller Highscores
 *          angeboten, die man über den Button "Highscores" einsehen kann.
 */
public class StartActivity extends FullscreenActivity {

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse StartActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    /**
     * Der Button local startet die Activity zum Starten eines neuen lokalen Spiels.
     */
    @OnClick(R.id.button_local)
    public void startLocal() {
        Intent intentLocal = new Intent(this, LocalActivity.class);
        startActivity(intentLocal);
    }

    /**
     * Der Button online zum Starten eines neuen Online-games.
     */
    @OnClick(R.id.button_online)
    public void startOnline() {
        Intent intentOnline = new Intent(this, OnlineActivity.class);
        startActivity(intentOnline);
    }

    /**
     * Der letzten Button mit der Aufschritt Highscores zeigt die Highscores in Tabellenform an.
     */
    @OnClick(R.id.button_highscores)
    public void startHighscore() {
        Intent intentHighscores = new Intent(this, HighscoreActivity.class);
        startActivity(intentHighscores);
    }

}

