package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.TextWatcherAdapter;
import icepick.Icepick;
import icepick.State;

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

    // Die Views für die Eingabe
    @BindView(R.id.button_startLocal)
    View button_startLocal;
    @BindView(R.id.edittext_player1)
    EditText editText_player1;
    @BindView(R.id.edittext_player2)
    EditText editText_player2;
    @BindView(R.id.textview_localPlayer1)
    TextView textView_localPlayer1;
    @BindView(R.id.textview_localPlayer2)
    TextView textView_localPlayer2;

    private String player1Name = "";
    private String player2Name = "";
    private boolean player1HasName = false;
    private boolean player2HasName = false;
    @State
    int scorePlayer1 = 0;
    @State
    int scorePlayer2 = 0;
    private final int START_GAME_CODE = 1234;

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
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);

        // Initialisiert die Eingabefelder für die Spielernamen
        player1Name = PreferenceManager.getDefaultSharedPreferences(this).getString("localPlayer1", "");
        editText_player1.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player1HasName = s.length() > 0 && !s.toString().equals(player2Name);
                if (player1HasName && player2HasName) {
                    button_startLocal.setVisibility(View.VISIBLE);
                } else {
                    button_startLocal.setVisibility(View.INVISIBLE);
                }
            }
        });
        editText_player1.setText(player1Name);

        player2Name = PreferenceManager.getDefaultSharedPreferences(this).getString("localPlayer2", "");
        editText_player2.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                player2HasName = s.length() > 0 && !s.toString().equals(player1Name);
                if (player1HasName && player2HasName) {
                    button_startLocal.setVisibility(View.VISIBLE);
                } else {
                    button_startLocal.setVisibility(View.INVISIBLE);
                }
            }
        });
        editText_player2.setText(player2Name);

        updateScore();
    }

    /**
     * Wird automatisch aufgerufen, wenn der Button zum Starten des neuen lokalen Spiels geclickt
     * wurde.
     *
     * @param v Der View, der angeclickt wurde.
     */
    @OnClick(R.id.button_startLocal)
    public void onClick(View v) {
        Intent startLocalGame = new Intent(this, LocalGameActivity.class);
        player1Name = editText_player1.getText().toString();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("localPlayer1", player1Name).apply();
        startLocalGame.putExtra("player1", player1Name);
        player2Name = editText_player2.getText().toString();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("localPlayer2", player2Name).apply();
        startLocalGame.putExtra("player2", player2Name);
        startActivityForResult(startLocalGame, START_GAME_CODE);
    }

    /**
     * Wird aufgerufen, wenn ein Ergebnis zurückgeliefert wird nach dem Starten eines neuen Spiels.
     *
     * @param requestCode Der Code um identifizieren zu können von welchem Aufruf Ergebnis stammt.
     * @param resultCode  Ergebnis Ok oder Cancled?
     * @param data        Die Übertragenen Daten.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_GAME_CODE) {
            if (resultCode == RESULT_OK) {
                scorePlayer1 = data.getExtras().getInt("player1");
                scorePlayer2 = data.getExtras().getInt("player2");
                updateScore();
            }
        }
    }

    /**
     * Aktualisiert den Inhalt der Anzeige für den aktuellen Spielstand zwischen Spieler 1 und 2.
     */
    private void updateScore() {
        textView_localPlayer1.setText(String.valueOf(scorePlayer1));
        textView_localPlayer2.setText(String.valueOf(scorePlayer2));
    }

    /**
     * Speichert den aktuellen Spielstand in den savedInstanceState.
     *
     * @param outState Der Zustand, der gespeichert wird.
     */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

}
