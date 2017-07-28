package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.Connections;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Game;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Player;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresults;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse OnlineGameActivity ist zur Anzeige des Spielfeldes und für das eigentliche Spiel-
 *          feld vorbehalten. Zu Beginn wird das Spielfeld über den Java-Code generiert und je
 *          7 Spalten mit 6 ImageViews befüllt. Diese bilden die einzelnen Felder des 4-Gewinnt
 *          Spiels. Neben dem Einsortieren der Spielsteine in die jeweiligen Spalten ist die Game-
 *          Activity auch für die Kommunikation zu dem Partnergerät verantwortlich.
 */
public class OnlineGameActivity extends FullscreenActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient googleApiClient;
    private Playerresults myPlayerresults;
    private LinearLayout[] linearLayouts_Columns = new LinearLayout[7];
    private ImageView[][] imageViews_fields = new ImageView[7][6];
    private KonfettiView konfettiView_online;
    private TextView textView_onlinePlayer;
    private TextView textView_onlineRound;
    private TextView textView_onlineScore1;
    private TextView textView_onlineScore2;
    private long startTime;
    private Game game;
    private Player winner = null;
    private boolean allowedToClick = false;
    private String endpoint;

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse OnlineGameActivity zu leisten hat. Hier wird
     * beispielsweise das Spielfeld generiert, aber auch die Kommunikation zu dem anderen Gerät
     * gehandelt.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Nearby.CONNECTIONS_API)
                .addOnConnectionFailedListener(this)
                .build();

        konfettiView_online = (KonfettiView) findViewById(R.id.konfettiView_online);
        textView_onlinePlayer = (TextView) findViewById(R.id.textView_onlinePlayer);
        textView_onlineRound = (TextView) findViewById(R.id.textView_onlineRound);
        textView_onlineScore1 = (TextView) findViewById(R.id.textView_onlineScore1);
        textView_onlineScore2 = (TextView) findViewById(R.id.textView_onlineScore2);
        textView_onlineScore1.setText("0");
        textView_onlineScore2.setText("0");

        // Initialisiert die LinearLayouts für die Spalten
        linearLayouts_Columns[0] = (LinearLayout) findViewById(R.id.linearlayoutOnline1);
        linearLayouts_Columns[1] = (LinearLayout) findViewById(R.id.linearlayoutOnline2);
        linearLayouts_Columns[2] = (LinearLayout) findViewById(R.id.linearlayoutOnline3);
        linearLayouts_Columns[3] = (LinearLayout) findViewById(R.id.linearlayoutOnline4);
        linearLayouts_Columns[4] = (LinearLayout) findViewById(R.id.linearlayoutOnline5);
        linearLayouts_Columns[5] = (LinearLayout) findViewById(R.id.linearlayoutOnline6);
        linearLayouts_Columns[6] = (LinearLayout) findViewById(R.id.linearlayoutOnline7);

        // Initialisiert jedes einelne Feld
        setupField();
    }

    /**
     * Baut die Verbindung zum GoogleApiClient auf.
     */
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    /**
     * Baut die Verbindung zum GoogleApiClient wieder ab beim Schließen dieser Activity.
     */
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Liefert den Spielernamen, den der Nutzer dieses Geräts für das aktuelle Online-Spiel
     * angegeben hat.
     *
     * @return Der Name als String.
     */
    private String getUserNickname() {
        return getIntent().getExtras().getString("playerName");
    }

    /**
     * Sobald man mit dem ApiClient verbunden ist können die Geräte beginnen ihren Dienst anzubieten
     * oder nach einem Anbieter zu suchen.
     *
     * @param bundle Nicht notwendig.
     */
    public void onConnected(Bundle bundle) {
        if (getIntent().getExtras().getBoolean("host")) {
            startAdvertising();
        } else {
            startDiscovering();
        }
    }

    /**
     * Startet das Anbieten eines neuen Spiels und wartet auf die Anfrage eines anderen Geräts.
     */
    private void startAdvertising() {
        Nearby.Connections.startAdvertising(
                googleApiClient,
                getUserNickname(),
                "de.thbingen.movs.lukas.a4gewinntspezial",
                connectionLifecycleCallback,
                new AdvertisingOptions(Strategy.P2P_STAR))
                .setResultCallback(
                        new ResultCallback<Connections.StartAdvertisingResult>() {
                            public void onResult(Connections.StartAdvertisingResult result) {
                                if (result.getStatus().isSuccess()) {
                                    Toast.makeText(OnlineGameActivity.this, "Suche nach Gegner!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OnlineGameActivity.this, "Es kann nach keinem Gegner gesucht werden!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
    }

    /**
     * Beginnt mit der Suche nach einem Anbieter für ein neues Spiel.
     */
    private void startDiscovering() {
        Nearby.Connections.startDiscovery(
                googleApiClient,
                "de.thbingen.movs.lukas.a4gewinntspezial",
                new EndpointDiscoveryCallback() {
                    public void onEndpointFound(String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo) {
                        String name = getUserNickname();
                        Nearby.Connections.requestConnection(
                                googleApiClient,
                                name,
                                endpointId,
                                connectionLifecycleCallback);
                    }

                    public void onEndpointLost(String s) {
                    }
                },
                new DiscoveryOptions(Strategy.P2P_STAR))
                .setResultCallback(
                        new ResultCallback<Status>() {
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    Toast.makeText(OnlineGameActivity.this, "Suche nach Gegner!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OnlineGameActivity.this, "Es kann nach keinem Gegner gesucht werden!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
    }

    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {

        public void onConnectionInitiated(final String endpointId, final ConnectionInfo connectionInfo) {
            new AlertDialog.Builder(OnlineGameActivity.this)
                    .setTitle("Wollen Sie die Verbindung zu " + connectionInfo.getEndpointName() + " akzeptieren?")
                    .setMessage("Bestätigen Sie, wenn der Code: " + connectionInfo.getAuthenticationToken() + " auch auf dem anderen Gerät angezeigt wird.")
                    .setPositiveButton("Akzeptieren", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            endpoint = endpointId;
                            if (getIntent().getExtras().getBoolean("host")) {
                                game = new Game(getIntent().getExtras().getString("playerName"), connectionInfo.getEndpointName());
                            } else {
                                game = new Game(connectionInfo.getEndpointName(), getIntent().getExtras().getString("playerName"));
                            }
                            Nearby.Connections.acceptConnection(googleApiClient, endpointId, new PayloadCallback() {
                                public void onPayloadReceived(String s, Payload payload) {
                                    String data = new String(payload.asBytes());
                                    if (data.equals("Ende")) {
                                        resetGame();
                                    } else {
                                        insertStone(Integer.parseInt(new String(payload.asBytes())));
                                    }
                                }

                                public void onPayloadTransferUpdate(String s, PayloadTransferUpdate payloadTransferUpdate) {
                                }
                            });
                        }
                    })
                    .setNegativeButton("Ablehnen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(OnlineGameActivity.this, "Die Verbindung wird abgelehnt!", Toast.LENGTH_SHORT).show();
                            Nearby.Connections.rejectConnection(googleApiClient, endpointId);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
                allowedToClick = getIntent().getExtras().getBoolean("host");
                textView_onlinePlayer.setText(game.getPlayerName());
                textView_onlinePlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
                startTime = System.currentTimeMillis();
            }
        }

        public void onDisconnected(String endpointId) {
            Toast.makeText(OnlineGameActivity.this, "Sie wurden von Ihrem Endpunkt leider getrennt!", Toast.LENGTH_SHORT).show();
            finish();
        }

    };

    /**
     * Lässt gelbes und rotes Konfetti über das Spielfeld regnen.
     */
    private void makeKonfetti(Point point, int color) {
        konfettiView_online.build()
                .addColors(getResources().getColor(color), getResources().getColor(R.color.colorAccent))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.CIRCLE, Shape.RECT)
                .addSizes(new Size(12, 5f))
                .setPosition(linearLayouts_Columns[point.x].getX() + linearLayouts_Columns[point.x].getWidth() / 2, imageViews_fields[point.x][point.y].getY() + imageViews_fields[point.x][point.y].getHeight() / 2)
                .burst(100);

    }

    // Nicht benötigt
    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void onClick(View view) {
        if (winner == null) {
            if (allowedToClick) {
                LinearLayout linearLayout = (LinearLayout) view;

                int column = (int) linearLayout.getTag();
                insertStone(column);
            }
        } else {
            resetGame();
        }
    }

    private void insertStone(int column) {
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
                    textView_onlineScore1.setText(String.valueOf(getIntent().getExtras().getInt("score1") + 1));
                } else {
                    textView_onlineScore2.setText(String.valueOf(getIntent().getExtras().getInt("score2") + 1));
                }
            } else {
                game.nextPlayer();
                textView_onlinePlayer.setText(game.getPlayerName());
                textView_onlinePlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
                textView_onlineRound.setText(String.valueOf(game.getCurrentRound()));
            }
            if (allowedToClick) {
                Nearby.Connections.sendPayload(googleApiClient, endpoint, Payload.fromBytes(String.valueOf(column).getBytes()));
            }
            allowedToClick = !allowedToClick;
        }
    }

    private void resetGame() {
        Log.d("OnlineGame", "resetGame: not working");
        game.reset();
        allowedToClick = getIntent().getExtras().getBoolean("host");
        textView_onlinePlayer.setText(game.getPlayerName());
        textView_onlinePlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        Nearby.Connections.sendPayload(googleApiClient, endpoint, Payload.fromBytes("Ende".getBytes()));
        winner = null;
        setupField();
    }

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

}
