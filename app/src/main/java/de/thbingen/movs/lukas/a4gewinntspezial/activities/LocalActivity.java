package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.TextWatcherAdapter;
import de.thbingen.movs.lukas.a4gewinntspezial.application.RealmHandler;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresults;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse LocalActivity ist für das Starten eines neuen lokalen 4-Gewinnt Spiels
 *          verantwortlich. Hierfür müssen die beiden Spieler ihren Namen angeben und anschließend
 *          über einen Button-Click das Spiel starten.
 */
public class LocalActivity extends FullscreenActivity implements View.OnClickListener {

    private View button_startLocal;
    private boolean player1 = false;
    private boolean player2 = false;
    private EditText editText_player1;
    private EditText editText_player2;
    private TextView textView_localPlayer1;
    private TextView textView_localPlayer2;
    private final int START_GAME_CODE = 1234;
    private String playerName1 = "";
    private String playerName2 = "";


    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse LocalActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        // Initialisiert die benötigten Views
        button_startLocal = findViewById(R.id.button_startLocal);
        button_startLocal.setOnClickListener(this);
        textView_localPlayer1 = (TextView) findViewById(R.id.textview_localPlayer1);
        editText_player1 = (EditText) findViewById(R.id.edittext_player1);
        editText_player1.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player1 = s.length() > 0;
                if (player1 && player2) {
                    button_startLocal.setVisibility(View.VISIBLE);
                } else {
                    button_startLocal.setVisibility(View.INVISIBLE);
                }
            }
        });
        textView_localPlayer2 = (TextView) findViewById(R.id.textview_localPlayer2);
        editText_player2 = (EditText) findViewById(R.id.edittext_player2);
        editText_player2.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player2 = s.length() > 0;
                if (player1 && player2) {
                    button_startLocal.setVisibility(View.VISIBLE);
                } else {
                    button_startLocal.setVisibility(View.INVISIBLE);
                }
            }
        });
        playerName1 = PreferenceManager.getDefaultSharedPreferences(this).getString("localPlayer1","");
        editText_player1.setText(playerName1);
        playerName2 = PreferenceManager.getDefaultSharedPreferences(this).getString("localPlayer2","");
        editText_player2.setText(playerName2);
    }

    /**
     * Wird automatisch aufgerufen, wenn der Button zum Starten des neuen lokalen Spiels geclickt
     * wurde.
     *
     * @param v Der View, der angeclickt wurde
     */
    public void onClick(View v) {
        Intent startLocalGame = new Intent(this, LocalGameActivity.class);
        playerName1 = editText_player1.getText().toString();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("localPlayer1", playerName1).apply();
        playerName2 = editText_player2.getText().toString();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("localPlayer2", playerName2).apply();
        startLocalGame.putExtra("player1", playerName1);
        startLocalGame.putExtra("player2", playerName2);
        startActivityForResult(startLocalGame, START_GAME_CODE);
    }

    /**
     * Wird aufgerufen, wenn ein Ergebnis zurückgeliefert wird nach dem Starten eines neuen Spiels.
     *
     * @param requestCode Der Code um identifizieren zu können von welchem Aufruf Ergebnis stammt
     * @param resultCode  Ergebnis Ok oder Cancled?
     * @param data        Die Übertragenen Daten.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_GAME_CODE) {
            if (resultCode == RESULT_OK) {
                textView_localPlayer1.setText(String.valueOf(data.getExtras().getInt("player1")));
                textView_localPlayer2.setText(String.valueOf(data.getExtras().getInt("player2")));
            }
        }
    }

}
