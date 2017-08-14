package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Game;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Player;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresult;
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
public abstract class GameActivity extends FullscreenActivity implements View.OnClickListener {

    // Das Spielfeld
    @BindViews({R.id.linearlayout1, R.id.linearlayout2, R.id.linearlayout3, R.id.linearlayout4, R.id.linearlayout5, R.id.linearlayout6, R.id.linearlayout7})
    protected LinearLayout[] linearLayouts_Columns;
    @BindView(R.id.linearLayout_dialog)
    protected LinearLayout linearLayout_dialog;
    @BindView(R.id.konfettiView)
    protected KonfettiView konfettiView;
    @BindView(R.id.textView_player)
    protected TextView textView_player;
    @BindView(R.id.textView_round)
    protected TextView textView_round;
    @BindView(R.id.textView_score1)
    protected TextView textView_score1;
    @BindView(R.id.textView_score2)
    protected TextView textView_score2;
    @BindView(R.id.button_newGame)
    protected Button button_newGame;
    @BindView(R.id.textView_resultTime)
    protected TextView textView_resultTime;
    @BindView(R.id.textView_resultRound)
    protected TextView textView_resultRound;
    @BindView(R.id.textView_resultWinner)
    protected TextView textView_resultWinner;
    @BindView(R.id.textView_resultScore1)
    protected TextView textView_resultScore1;
    @BindView(R.id.textView_resultScore2)
    protected TextView textView_resultScore2;
    protected ImageView[][] imageViews_fields = new ImageView[7][6];

    // Die Spielvariable
    protected int scorePlayer1 = 0;
    protected int scorePlayer2 = 0;
    protected Game game;
    protected Realm realm;

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse LocalGameActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        setupField();
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
        if (game != null && game.getWinner() == null && game.getFieldsLeft() > 0) {
            LinearLayout linearLayout = (LinearLayout) view;

            int column = (int) linearLayout.getTag();
            insertStone(column);
        }
    }

    /**
     * Befüllt die Layouts des Spielfelds mit leeren Feldern für ein neues Spiel.
     */
    protected void setupField() {
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
        textView_player.setText(game.getPlayerName());
        textView_player.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        textView_round.setText(String.valueOf(game.getCurrentRound()));
    }

    /**
     * Wirft einen Stein in die angegebene Spalte. Daraufhin wird überprüft, ob es einen Gewinner
     * gibt.
     *
     * @param column Die Spalte, in die der Stein eingeworfen werden soll.
     */
    protected int insertStone(int column) {
        int row = game.insert(column);

        if (row >= 0) {
            imageViews_fields[column][row].setImageDrawable(getResources().getDrawable(game.getPlayerTurn().getImage()));

            if (game.checkWin()) {
                hasWinner();
            } else {
                if (game.getFieldsLeft() > 0) {
                    nextPlayer();
                } else {
                    showNewGameButton();
                }
            }
        }
        return row;
    }

    /**
     * Lässt Konfetti an der Position der Siegerkombination explodieren und aktualisiert die
     * Anzeigen nach dem Sieg.
     */
    protected void hasWinner() {
        for (Point p : game.getWinPositions()) {
            imageViews_fields[p.x][p.y].setImageDrawable(getResources().getDrawable(R.drawable.stone_green));
            makeKonfetti(p, game.getPlayerTurn().getColor());
        }
        if (game.getWinner() == Player.P1) {
            textView_score1.setText(String.valueOf(++scorePlayer1));
            saveResults(1, 0);
        } else {
            textView_score2.setText(String.valueOf(++scorePlayer2));
            saveResults(0, 1);
        }
        showNewGameButton();
    }

    /**
     * Zeigt den Button zum Starten einer neuen Partie an.
     */
    protected void showNewGameButton() {
        if (game.getWinner() == null) {
            saveResults(0, 0);
        }
        showResults();
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
        linearLayout_dialog.startAnimation(fadeIn);
        linearLayout_dialog.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                button_newGame.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        resetGame();
                    }
                });
            }
        }, 2000);
    }

    /**
     * Zeigt beim Ende des Spiels die aktuellen Spielergebnisse, Rundenzeiten und sonstige
     * Informationen an.
     */
    protected void showResults() {
        textView_resultScore1.setText(String.valueOf(scorePlayer1));
        textView_resultScore2.setText(String.valueOf(scorePlayer2));
        textView_resultRound.setText(getString(R.string.textView_resultRound, game.getCurrentRound()));
        textView_resultTime.setText(getString(R.string.textView_resultTime, game.getPlayTime() / 60, game.getPlayTime() % 60));
        if (game.getWinner() == null) {
            textView_resultWinner.setText(getString(R.string.textView_resultWinnerDraw));
            textView_resultWinner.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
        } else {
            getWinnerText();
        }
    }

    /**
     * Liefert die Konfiguration für den Sieger-textView beim Ergebnisbildschirm
     */
    protected abstract void getWinnerText();

    /**
     * Lässt gelbes und rotes Konfetti an der Position der Siegerkombination explodieren.
     */
    protected void makeKonfetti(Point point, int color) {
        konfettiView.build()
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
     * Sucht die entsprechenden Spielergebnisse zu dem gegebenen Namen aus der Datenbank. Falls noch
     * nicht ein solcher Eintrag existiert wird einfach ein neuer Eintrag erzeugt mit passendem
     * Alias.
     *
     * @param name  Die ID des Spielers dessen Ergebnisse man sucht.
     * @param alias Der Alias der für einen neuen Eintrag verwendet werden soll.
     * @return Die Ergebnisse des Spielers.
     */
    protected Playerresult findPlayer(String name, String alias) {
        RealmQuery<Playerresult> entriesForPlayer = realm.where(Playerresult.class).equalTo("name", name);
        if (entriesForPlayer.count() > 0) {
            return entriesForPlayer.findFirst();
        }
        Playerresult playerresult = new Playerresult();
        playerresult.setName(name);
        playerresult.setAlias(alias);
        return playerresult;
    }

    /**
     * Setzt das Spielfeld und die TextViews zur Anzeige der Runde für eine neue Spielrunde auf.
     */
    protected void resetGame() {
        game.reset();
        textView_player.setText(game.getPlayerName());
        textView_player.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        textView_round.setText(String.valueOf(game.getCurrentRound()));
        setupField();
        linearLayout_dialog.setVisibility(View.GONE);
        button_newGame.setOnClickListener(null);
    }


    /**
     * Speichert die Ergebnisse der letzten Partie in der Datenbanbk.
     */
    protected abstract void saveResults(int player1, int player2);

}