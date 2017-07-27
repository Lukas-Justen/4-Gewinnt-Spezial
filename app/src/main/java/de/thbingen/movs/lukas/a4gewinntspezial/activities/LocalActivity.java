package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.TextWatcherAdapter;
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


    // Der Button zum Starten des Spiels
    private View button_startLocal;

    // Die Textfelder und Variablen zur Überwachung der Eingabe der Spielernamen bzw. der Ergebnisse
    private EditText editText_player1;
    private boolean player1 = false;
    private TextView textView_localPlayer1;
    private int scorePlayer1 = 0;
    private EditText editText_player2;
    private boolean player2 = false;
    private TextView textView_localPlayer2;
    private int scorePlayer2 = 0;
    private Playerresults playerresults1;
    private Playerresults playerresults2;
    private int roundCount = 0;
    private Realm realm;

    // Request_Code um das Ergebnis des Spiels zu erhalten
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
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    /**
     * Wird automatisch aufgerufen, wenn der Button zum Starten des neuen lokalen Spiels geclickt
     * wurde.
     *
     * @param v Der View, der angeclickt wurde
     */
    public void onClick(View v) {
        Intent startLocalGame = new Intent(this, LocalGameActivity.class);
        startLocalGame.putExtra("player1", editText_player1.getText().toString());
        startLocalGame.putExtra("player2", editText_player2.getText().toString());
        startLocalGame.putExtra("score1", scorePlayer1);
        startLocalGame.putExtra("score2", scorePlayer2);
        startActivityForResult(startLocalGame, START_GAME_CODE);
        roundCount++;
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
                playerresults1 = findPlayer(editText_player1.getText().toString());
                playerresults2 = findPlayer(editText_player2.getText().toString());

                scorePlayer1 += data.getExtras().getInt("player1");
                scorePlayer2 += data.getExtras().getInt("player2");
                textView_localPlayer1.setText(String.valueOf(scorePlayer1));
                textView_localPlayer2.setText(String.valueOf(scorePlayer2));

                realm.beginTransaction();
                playerresults1.addVictories(data.getExtras().getInt("player1"));
                playerresults2.addVictories(data.getExtras().getInt("player2"));
                if (!data.getExtras().getBoolean("draw")) {
                    playerresults1.addLosses(data.getExtras().getInt("player1"));
                    playerresults2.addLosses(data.getExtras().getInt("player2"));
                }
                playerresults1.addTime(data.getExtras().getLong("playtime"));
                playerresults2.addTime(data.getExtras().getLong("playtime"));
                playerresults1.addColorOfPreference(1);
                playerresults2.addColorOfPreference(-1);
                playerresults1.nextGame();
                playerresults2.nextGame();
                playerresults1.addRounds(data.getExtras().getInt("round"));
                playerresults2.addRounds(data.getExtras().getInt("round"));
                realm.insertOrUpdate(playerresults1);
                realm.insertOrUpdate(playerresults2);
                realm.commitTransaction();
            }
        }
    }

    /**
     * Sucht die entsprechenden Spielergebnisse zu dem gegebenen Namen aus der Datenbank.
     *
     * @param playerName Der Spielername dessen Ergebnisse man sucht.
     *
     * @return Die Ergebnisse des Spielers.
     */
    private Playerresults findPlayer(String playerName) {
        RealmQuery<Playerresults> entriesForPlayer = realm.where(Playerresults.class).equalTo("name", playerName);
        if (entriesForPlayer.count() > 0) {
            return entriesForPlayer.findFirst();
        }
        Playerresults playerresults = new Playerresults();
        playerresults.setName(playerName);
        return playerresults;
    }

}
