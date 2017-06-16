package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
public class StartActivity extends FullscreenActivity implements View.OnClickListener {

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

        // Initialisiert die Buttons auf dem Start-Screen
        View button_local = findViewById(R.id.button_local);
        button_local.setOnClickListener(this);
        View button_online = findViewById(R.id.button_online);
        button_online.setOnClickListener(this);
        View button_highscore = findViewById(R.id.button_highscores);
        button_highscore.setOnClickListener(this);
    }

    /**
     * Wird automatisch aufgerufen, wenn ein Button innerhalb des Layouts angeclickt wird. Der
     * Button local Startet die Activity zum Starten eines neuen lokalen Spiels.
     *
     * @param view Die View, die angeclickt wurde.
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_local:
                Intent intentLocal = new Intent(this,LocalActivity.class);
                startActivity(intentLocal);
                break;
            case R.id.button_online:
                Intent intentOnline = new Intent(this,OnlineActivity.class);
                startActivity(intentOnline);
                break;
            case R.id.button_highscores:
                Intent intentHighscores = new Intent(this,HighscoreActivity.class);
                startActivity(intentHighscores);
                break;
        }
    }

}

