package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse OnlineActivity ist zum Starten eines neuen OnlineSpiels verantwortlich. Der
 *          Spieler dieses Geräts muss seinen Namen angeben und aus einer Liste einen potentiellen
 *          Gegner wählen. Die Actvity etabliert daraughin eine Datenverbindung zwischen den beiden
 *          Geräten, die zur Kommunikation benötigt wird.
 */
public class OnlineActivity extends AppCompatActivity {

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse OnlineActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
    }

}
