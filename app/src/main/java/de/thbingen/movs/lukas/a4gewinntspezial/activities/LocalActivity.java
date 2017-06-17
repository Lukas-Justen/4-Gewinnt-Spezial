package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.TextWatcherAdapter;

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

    // Der Button zum Starten des Spiels
    private View button_startLocal;

    // Die beiden Textfelder und Variablen zur Überwachung der Eingabe der Spielernamen
    private EditText editText_player1;
    private boolean player1 = false;
    private EditText editText_player2;
    private boolean player2 = false;

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
    }

    /**
     * Wird automatisch aufgerufen, wenn der Button zum Starten des neuen lokalen Spiels geclickt
     * wurde.
     *
     * @param v Der View, der angeclickt wurde
     */
    public void onClick(View v) {
        Intent startLocalGame = new Intent(this,LocalGameActivity.class);
        startLocalGame.putExtra("player1", editText_player1.getText().toString());
        startLocalGame.putExtra("player2", editText_player2.getText().toString());
        startActivity(startLocalGame);
    }

}
