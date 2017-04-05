package de.thbingen.movs.lukas.a4gewinntspezial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse StartActivity stellt den StartScreen der App zur Verfügung. Hier werden die
 *          Spielernamen angegeben oder der aktuelle Spielstand angezeigt. Zusätzlich bietet die
 *          Oberfläche Möglichkeiten zur Konfiguration eines neuen Multiplayer Spiels über  die
 *          Schaltfläche "Start Game". Neben dem aktuellen Spielstand wird aber auch eine Liste
 *          aller Highscores angeboten, die man über den Button "Highscores" einsehen kann. Der
 *          Button "Visit Game" ist zum joinen eines Multiplayerspiels gedacht.
 */
public class StartActivity extends AppCompatActivity {

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse StartActivity zu leisten hat.
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

}
