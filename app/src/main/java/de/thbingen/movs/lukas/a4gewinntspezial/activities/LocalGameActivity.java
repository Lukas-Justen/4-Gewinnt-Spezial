package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Game;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Player;
import icepick.Icepick;
import icepick.State;
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

    // Das Spielffeld
    private LinearLayout[] linearLayouts_Columns = new LinearLayout[7];
    private ImageView[][] imageViews_fields = new ImageView[7][6];
    private KonfettiView konfettiView_local;
    private TextView textView_localPlayer;
    private TextView textView_localRound;
    private TextView textView_localScore1;
    private TextView textView_localScore2;


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
        textView_localScore1.setText(String.valueOf(getIntent().getExtras().getInt("score1")));
        textView_localScore2.setText(String.valueOf(getIntent().getExtras().getInt("score2")));


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

        // Initialisiert jedes einelne Feld
        for (int j = 0; j < 7; j++) {
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

        textView_localPlayer.setText(game.getPlayerName());
        textView_localPlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
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
        if (winner == null) {
            LinearLayout linearLayout = (LinearLayout) view;

            int column = (int) linearLayout.getTag();
            int row = game.insert(column);

            if (row >= 0) {
                imageViews_fields[column][row].setImageDrawable(getResources().getDrawable(game.getPlayerTurn().getImage()));

                if (game.checkWin()) {
                    for (Point p : game.getWinPositions()) {
                        imageViews_fields[p.x][p.y].setImageDrawable(getResources().getDrawable(R.drawable.stone_green));
                        makeKonfetti(p, game.getPlayerTurn().getColor());
                    }
                    winner = game.getPlayerTurn();
                    if (winner == Player.P1) {
                        textView_localScore1.setText(String.valueOf(getIntent().getExtras().getInt("score1") +1));
                    } else {
                        textView_localScore2.setText(String.valueOf(getIntent().getExtras().getInt("score2") +1));
                    }
                } else {
                    game.nextPlayer();
                    textView_localPlayer.setText(game.getPlayerName());
                    textView_localPlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
                    textView_localRound.setText(String.valueOf(game.getCurrentRound()));
                }

            }
        } else {
            finish();
        }
    }

    /**
     * Lässt gelbes und rotes Konfetti über das Spielfeld regnen.
     */
    private void makeKonfetti(Point point, int color) {
        konfettiView_local.build()
                .addColors(getResources().getColor(color), getResources().getColor(R.color.colorAccent))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.CIRCLE, Shape.RECT)
                .addSizes(new Size(12, 5f))
                .setPosition(linearLayouts_Columns[point.x].getX() + linearLayouts_Columns[point.x].getWidth()/2, imageViews_fields[point.x][point.y].getY() + imageViews_fields[point.x][point.y].getHeight()/2)
                .burst(100);

    }

    /**
     * Schließt die Activity, Liefert das Ergebnis der Partie an die aufrufende Activity zurück und
     * speichert das Ergebnis der Partie in der Realm-Datenbank.
     */
    public void finish() {
        if (winner == null && !(game.getFieldsLeft() == 0)) {
            setResult(RESULT_CANCELED);
        } else {
            Intent data = new Intent();
            data.putExtra("player1", (winner == Player.P1) ? 1 : 0);
            data.putExtra("player2", (winner == Player.P2) ? 1 : 0);
            data.putExtra("round", Integer.parseInt(textView_localRound.getText().toString()));
            setResult(RESULT_OK, data);
        }
        super.finish();
    }

}
