package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Game;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Player;
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
                if (game.getPlayerTurn() == Player.P1) {
                    imageViews_fields[column][row].setImageDrawable(getResources().getDrawable(R.drawable.stone_red));
                } else {
                    imageViews_fields[column][row].setImageDrawable(getResources().getDrawable(R.drawable.stone_yellow));
                }

                if (game.checkWin()) {
                    for (Point p : game.getWinPositions()) {
                        imageViews_fields[p.x][p.y].setImageDrawable(getResources().getDrawable(R.drawable.stone_green));
                    }
                    winner = game.getPlayerTurn();
                    makeKonfetti();
                } else {
                    game.nextPlayer();
                }
            }
        } else {
            finish();
        }
    }

    /**
     * Lässt gelbes und rotes Konfetti über das Spielfeld regnen.
     */
    private void makeKonfetti() {
        KonfettiView konfettiView = (KonfettiView) findViewById(R.id.konfettiView_local);
        konfettiView.build()
                .addColors(getResources().getColor(R.color.colorRed), getResources().getColor(R.color.colorYellow))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.CIRCLE, Shape.RECT)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                .stream(500, 5000L);
    }

    /**
     * Speichert die Daten im Zusammenhang zu der gespielten Runde in der Datenbank ab. Neben den
     * Gewinnern & Verlieren sollen auch Daten zur Anzahl der benötigten Züge & die Namen der Spieler
     * vermerkt werden.
     */
    private void saveGameResult() {
        // TODO speichern aller relevanter Daten in der Datenbank
    }

    /**
     * Schließt die Activity, Liefert das Ergebnis der Partie an die aufrufende Activity zurück und
     * speichert das Ergebnis der Partie in der Realm-Datenbank.
     */
    public void finish() {
        if (winner == null) {
            setResult(RESULT_CANCELED);
        } else {
            Intent data = new Intent();
            data.putExtra("player1", (winner == Player.P1) ? 1 : 0);
            data.putExtra("player2", (winner == Player.P2) ? 1 : 0);
            setResult(RESULT_OK, data);
        }
        saveGameResult();
        super.finish();
    }

}
