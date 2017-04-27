package de.thbingen.movs.lukas.a4gewinntspezial;

import android.os.Bundle;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse StartActivity stellt den StartScreen der App zur Verfügung. Hier kann der
 *          eigene Spielernamen angegeben werden. Zusätzlich bietet die Oberfläche Möglichkeiten zum
 *          Starten eines neuen Online-Games bzw. eines neuen lokalen Spiels. Neben dem Starten
 *          neuer Spiele wird aber auch eine Liste aller Highscores angeboten, die man über den
 *          Button "Highscores" einsehen kann.
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
    }

}
