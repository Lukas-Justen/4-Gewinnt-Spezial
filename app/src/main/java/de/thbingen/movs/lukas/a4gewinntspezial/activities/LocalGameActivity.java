package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.application.RealmHandler;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Game;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Player;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresults;
import io.realm.Realm;
import io.realm.RealmQuery;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

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
public class LocalGameActivity extends FullscreenActivity implements View.OnClickListener {

    // Das Spielfeld
    private LinearLayout[] linearLayouts_Columns = new LinearLayout[7];
    private ImageView[][] imageViews_fields = new ImageView[7][6];
    private KonfettiView konfettiView_local;
    private TextView textView_localPlayer;
    private TextView textView_localRound;
    private TextView textView_localScore1;
    private TextView textView_localScore2;
    private long startTime;
    private Playerresults playerresults1;
    private Playerresults playerresults2;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private Realm realm;

    // Die Spielvariable
    private Game game;
    private Player winner = null;

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse LocalGameActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_game);

        konfettiView_local = (KonfettiView) findViewById(R.id.konfettiView_local);
        textView_localPlayer = (TextView) findViewById(R.id.textView_localPlayer);
        textView_localRound = (TextView) findViewById(R.id.textView_localRound);
        textView_localScore1 = (TextView) findViewById(R.id.textView_localScore1);
        textView_localScore2 = (TextView) findViewById(R.id.textView_localScore2);

        // Legt ein neues Spiel an und Initialisiert die LinearLayouts für die Spalten
        Bundle receivedData = getIntent().getExtras();
        game = new Game(receivedData.getString("player1"), receivedData.getString("player2"));
        linearLayouts_Columns[0] = (LinearLayout) findViewById(R.id.linearlayoutLocal1);
        linearLayouts_Columns[1] = (LinearLayout) findViewById(R.id.linearlayoutLocal2);
        linearLayouts_Columns[2] = (LinearLayout) findViewById(R.id.linearlayoutLocal3);
        linearLayouts_Columns[3] = (LinearLayout) findViewById(R.id.linearlayoutLocal4);
        linearLayouts_Columns[4] = (LinearLayout) findViewById(R.id.linearlayoutLocal5);
        linearLayouts_Columns[5] = (LinearLayout) findViewById(R.id.linearlayoutLocal6);
        linearLayouts_Columns[6] = (LinearLayout) findViewById(R.id.linearlayoutLocal7);

        setupField();

        textView_localPlayer.setText(game.getPlayerName());
        textView_localPlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        startTime = System.currentTimeMillis();

        Realm.init(this);
        realm = Realm.getInstance(RealmHandler.getLocalRealmConfig());
    }

    /**
     * Reagiert auf den Click auf eine Spalte. Falls es noch keinen Gewinner gibt überprüft die
     * Game-Instanz, ob man in die gewählte Spalte einen Spielstein einwerfen kann. Wenn das der Fall
     * ist erscheint in der Spalte der passende Spielstein. Anschließend wird überprüft, ob es durch
     * diesen Stein einen Sieger gibt. Falls ja regnet Konfetti über das Spielfeld. Bei einem weiteren
     * Druck auf das Spielfeld wird diese Activity wieder geschlossen und man kommt wieder auf den
     * Screen zum konfigurieren eines neuen Lokalen Spiels.
     *
     * @param view Der View, bzw. die Spalte, die angeclickt wurde.
     */
    public void onClick(View view) {
        if (winner == null && game != null && game.getFieldsLeft() > 0) {
            LinearLayout linearLayout = (LinearLayout) view;

            int column = (int) linearLayout.getTag();
            insertStone(column);
        } else {
            resetGame();
        }
    }

    /**
     * Setzt das Spielfeld und die TextViews zur Anzeige der Runde für eine neue Spielrunde auf.
     */
    private void resetGame() {
        if (winner == null) {
            saveResults(0, 0);
        }
        game.reset();
        textView_localPlayer.setText(game.getPlayerName());
        textView_localPlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        textView_localRound.setText(String.valueOf(game.getCurrentRound()));
        winner = null;
        setupField();
        startTime = System.currentTimeMillis();
    }

    /**
     * Befüllt die Layouts des Spielfelds mit leeren Feldern für ein neues Spiel.
     */
    private void setupField() {
        for (int j = 0; j < 7; j++) {
            linearLayouts_Columns[j].removeAllViews();
            for (int i = 0; i < 6; i++) {
                ImageView image = new ImageView(this);
                image.setImageDrawable(getResources().getDrawable(R.drawable.field));
                imageViews_fields[j][i] = image;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
                linearLayouts_Columns[j].addView(image, params);
            }
            linearLayouts_Columns[j].setTag(j);
            linearLayouts_Columns[j].setOnClickListener(this);
        }
    }

    /**
     * Bereitet die Anzeigen und die Rundentafel für den nächsten Spieler vor.
     */
    private void nextPlayer() {
        game.nextPlayer();
        textView_localPlayer.setText(game.getPlayerName());
        textView_localPlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        textView_localRound.setText(String.valueOf(game.getCurrentRound()));
    }

    /**
     * Wirft einen Stein in die angegebene Spalte. Daraufhin wird überprüft, ob es einen Gewinner
     * gibt.
     *
     * @param column Die Spalte, in die der Stein eingeworfen werden soll.
     */
    private void insertStone(int column) {
        int row = game.insert(column);

        if (row >= 0) {
            imageViews_fields[column][row].setImageDrawable(getResources().getDrawable(game.getPlayerTurn().getImage()));

            if (game.checkWin()) {
                hasWinner();
            } else {
                nextPlayer();
            }
        }
    }

    /**
     * Macht Konfetti und aktualisiert die Anzeigen nach dem Sieg.
     */
    private void hasWinner() {
        for (Point p : game.getWinPositions()) {
            imageViews_fields[p.x][p.y].setImageDrawable(getResources().getDrawable(R.drawable.stone_green));
            makeKonfetti(p, game.getPlayerTurn().getColor());
        }
        winner = game.getPlayerTurn();
        if (winner == Player.P1) {
            textView_localScore1.setText(String.valueOf(++scorePlayer1));
            saveResults(1, 0);
        } else {
            textView_localScore2.setText(String.valueOf(++scorePlayer2));
            saveResults(0, 1);
        }
    }

    /**
     * Lässt gelbes und rotes Konfetti über das Spielfeld regnen.
     */
    private void makeKonfetti(Point point, int color) {
        konfettiView_local.build()
                .addColors(getResources().getColor(color), getResources().getColor(R.color.colorWin))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.CIRCLE, Shape.RECT)
                .addSizes(new Size(12, 5f))
                .setPosition(linearLayouts_Columns[point.x].getX() + linearLayouts_Columns[point.x].getWidth() / 2, imageViews_fields[point.x][point.y].getY() + imageViews_fields[point.x][point.y].getHeight() / 2)
                .burst(100);

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
    private void saveResults(int player1, int player2) {
        playerresults1 = findPlayer(getIntent().getExtras().getString("player1"));
        playerresults2 = findPlayer(getIntent().getExtras().getString("player2"));

        realm.beginTransaction();
        playerresults1.addVictories(player1);
        playerresults2.addVictories(player2);
        if (player1 != player2) {
            playerresults1.addLosses(player1);
            playerresults2.addLosses(player2);
        }
        long time = (System.currentTimeMillis() - startTime) / 1000;
        playerresults1.addTime(time);
        playerresults2.addTime(time);
        playerresults1.addColorOfPreference(1);
        playerresults2.addColorOfPreference(-1);
        playerresults1.nextGame();
        playerresults2.nextGame();
        playerresults1.addRounds(game.getCurrentRound());
        playerresults2.addRounds(game.getCurrentRound());
        realm.insertOrUpdate(playerresults1);
        realm.insertOrUpdate(playerresults2);
        realm.commitTransaction();
    }

    /**
     * Sucht die entsprechenden Spielergebnisse zu dem gegebenen Namen aus der Datenbank.
     *
     * @param playerName Der Spielername dessen Ergebnisse man sucht.
     * @return Die Ergebnisse des Spielers.
     */
    private Playerresults findPlayer(String playerName) {
        RealmQuery<Playerresults> entriesForPlayer = realm.where(Playerresults.class).equalTo("name", playerName);
        if (entriesForPlayer.count() > 0) {
            return entriesForPlayer.findFirst();
        }
        Playerresults playerresults = new Playerresults();
        playerresults.setName(playerName);
        playerresults.setAlias(playerName);
        playerresults.setType("local");
        return playerresults;
    }

}
