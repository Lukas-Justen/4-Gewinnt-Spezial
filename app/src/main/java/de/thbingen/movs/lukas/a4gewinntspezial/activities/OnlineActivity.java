package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.TextWatcherAdapter;

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
public class OnlineActivity extends FullscreenActivity implements View.OnClickListener {

    private View button_startOnline;
    private EditText editText_playerThis;
    private SegmentedButtonGroup segmentedControl_hostOrClient;
    private String playername = "";

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

        // Initialisiert die benötigten Views
        segmentedControl_hostOrClient = (SegmentedButtonGroup) findViewById(R.id.segmentedControl_hostOrClient);
        button_startOnline = findViewById(R.id.button_startOnline);
        button_startOnline.setOnClickListener(this);
        editText_playerThis = (EditText) findViewById(R.id.editText_thisPlayer);
        editText_playerThis.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean player1 = s.length() > 0;
                if (player1) {
                    button_startOnline.setVisibility(View.VISIBLE);
                } else {
                    button_startOnline.setVisibility(View.INVISIBLE);
                }
            }
        });
        playername = PreferenceManager.getDefaultSharedPreferences(this).getString("onlinePlayer", "");
        editText_playerThis.setText(playername);
    }

    /**
     * Wird automatisch aufgerufen, wenn der Button zum Starten des neuen online Spiels geclickt
     * wurde.
     *
     * @param v Der View, der angeclickt wurde.
     */
    public void onClick(View v) {
        Intent startOnlineGame = new Intent(this, OnlineGameActivity.class);
        playername = editText_playerThis.getText().toString();
        startOnlineGame.putExtra("playerName", playername);
        startOnlineGame.putExtra("host", segmentedControl_hostOrClient.getPosition() == 0);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("onlinePlayer", playername).apply();
        startActivity(startOnlineGame);
    }

}
