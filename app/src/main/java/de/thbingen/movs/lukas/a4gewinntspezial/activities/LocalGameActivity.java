package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Game;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Player;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresult;
import de.thbingen.movs.lukas.a4gewinntspezial.game.RealmHandler;
import io.realm.Realm;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse LocalGameActivity ist für das lokale Spiel verantwortlich. Nachdem einer
 *          der beiden Spieler gewonnen oder es zu einem Unentschieden gekommen ist wird das
 *          Ergebnis in der Datenbank festgeschrieben. Bei einem solchen Spiel kann es während des
 *          Spiels eher weniger zu Störungen kommen als bei einem Online-Game.
 */
public class LocalGameActivity extends GameActivity implements View.OnClickListener {

    private String name1;
    private String name2;

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse LocalGameActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle receivedData = getIntent().getExtras();
        if (receivedData != null) {
            name1 = receivedData.getString("player1");
            name2 = receivedData.getString("player2");
        }
        game = new Game(name1, name2);
        textView_player.setText(game.getPlayerName());
        textView_player.setTextColor(ContextCompat.getColor(this,game.getPlayerTurn().getColor()));

        Realm.init(this);
        realm = Realm.getInstance(RealmHandler.getLocalRealmConfig());
    }

    /**
     * Liefert die Konfiguration für den Sieger-textView beim Ergebnisbildschirm
     */
    protected void getWinnerText() {

        if (game.getWinner() == Player.P1) {
            textView_resultWinner.setTextColor(color_red);
            textView_resultWinner.setText(getString(R.string.textView_resultWinnerVictory, name1));
        } else {
            textView_resultWinner.setTextColor(color_yellow);
            textView_resultWinner.setText(getString(R.string.textView_resultWinnerVictory, name2));
        }
    }

    /**
     * Schließt die Activity, Liefert das Ergebnis der Partie an die aufrufende Activity zurück und
     * speichert das Ergebnis der Partie in der Realm-Datenbank.
     */
    public void finish() {
        Intent data = new Intent();
        data.putExtra("player1", scorePlayer1);
        data.putExtra("player2", scorePlayer2);
        setResult(RESULT_OK, data);
        super.finish();
    }

    /**
     * Speichert die Ergebnisse der letzten Partie in der Datenbanbk.
     */
    protected void saveResults(int player1, int player2) {
        Playerresult playerresult1 = findPlayer(name1, name1);
        Playerresult playerresult2 = findPlayer(name2, name2);
        realm.beginTransaction();
        playerresult1.addVictories(player1);
        playerresult2.addVictories(player2);
        if (player1 != player2) {
            playerresult1.addLosses(player1);
            playerresult2.addLosses(player2);
        }
        long time = game.getPlayTime();
        playerresult1.addTime(time);
        playerresult2.addTime(time);
        playerresult1.addColorOfPreference(1);
        playerresult2.addColorOfPreference(-1);
        playerresult1.nextGame();
        playerresult2.nextGame();
        playerresult1.addRounds(game.getCurrentRound());
        playerresult2.addRounds(game.getCurrentRound());
        realm.insertOrUpdate(playerresult1);
        realm.insertOrUpdate(playerresult2);
        realm.commitTransaction();
    }

}
