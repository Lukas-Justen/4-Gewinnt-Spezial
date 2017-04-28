package de.thbingen.movs.lukas.a4gewinntspezial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        // Der Button zum Starten eines lokalen Spiels
        Button buttonLocal = (Button) findViewById(R.id.button_local);
        buttonLocal.setTag(ButtonStates.Local);
        buttonLocal.setOnClickListener(this);
    }

    /**
     * Wird automatisch aufgerufen, wenn ein Button innerhalb des Layouts angeclickt. Der Button
     * local Startet die Activity zum Starten eines neuen lokalen Spiels.
     *
     * @param v
     */
    public void onClick(View v) {
        switch ((ButtonStates) v.getTag()) {
            case Local:
                Intent local = new Intent(this, LocalActivity.class);
                startActivity(local);
                break;
            case Online:
                break;
            case Highscores:
                break;
            default:
                break;
        }
    }

}

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 28.04.2017
 *
 *          Merkt sich die drei unterschiedlichen Buttons
 */
enum ButtonStates {

    Local, Online, Highscores;

}
