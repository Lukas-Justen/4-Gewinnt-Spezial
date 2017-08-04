package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
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
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.EndpointDiscoveryAdapter;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.PayloadCallbackAdapter;
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
 *          Die Klasse OnlineGameActivity ist zur Anzeige des Spielfeldes und für das eigentliche Spiel-
 *          feld vorbehalten. Zu Beginn wird das Spielfeld über den Java-Code generiert und je
 *          7 Spalten mit 6 ImageViews befüllt. Diese bilden die einzelnen Felder des 4-Gewinnt
 *          Spiels. Neben dem Einsortieren der Spielsteine in die jeweiligen Spalten ist die Game-
 *          Activity auch für die Kommunikation zu dem Partnergerät verantwortlich.
 */
public class OnlineGameActivity extends FullscreenActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient googleApiClient;
    private final ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {
        public void onConnectionInitiated(final String endpointId, final ConnectionInfo connectionInfo) {
            new AlertDialog.Builder(OnlineGameActivity.this)
                    .setTitle("Wollen Sie die Verbindung zu " + connectionInfo.getEndpointName() + " akzeptieren?")
                    .setMessage("Bestätigen Sie, wenn der Code: " + connectionInfo.getAuthenticationToken() + " auch auf dem anderen Gerät angezeigt wird.")
                    .setPositiveButton("Akzeptieren", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            acceptConnection(endpointId, connectionInfo);
                        }
                    })
                    .setNegativeButton("Ablehnen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(OnlineGameActivity.this, "Die Verbindung wird abgelehnt!", Toast.LENGTH_SHORT).show();
                            Nearby.Connections.rejectConnection(googleApiClient, endpointId);
                        }
                    })
                    .show();
        }

        public void onConnectionResult(String endpointId, ConnectionResolution result) {
            if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
                allowedToClick = getIntent().getExtras().getBoolean("host");
                textView_onlinePlayer.setText(game.getPlayerName());
                textView_onlinePlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
                startTime = System.currentTimeMillis();
                connected = true;
            }
        }

        public void onDisconnected(String endpointId) {
            Toast.makeText(OnlineGameActivity.this, "Sie wurden von Ihrem Endpunkt leider getrennt!", Toast.LENGTH_SHORT).show();
            finish();
        }
    };
    private LinearLayout[] linearLayouts_Columns = new LinearLayout[7];
    private ImageView[][] imageViews_fields = new ImageView[7][6];
    private KonfettiView konfettiView_online;
    private TextView textView_onlinePlayer;
    private TextView textView_onlineRound;
    private TextView textView_onlineScore1;
    private TextView textView_onlineScore2;
    private Game game;
    private String endpoint;
    private Player winner = null;
    private long startTime;
    private boolean connected = false;
    private boolean allowedToClick = false;
    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private Realm realm;
    private Playerresults playerresults;

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

        googleApiClient = buildGoogleApiClient();

        konfettiView_online = (KonfettiView) findViewById(R.id.konfettiView_online);
        textView_onlinePlayer = (TextView) findViewById(R.id.textView_onlinePlayer);
        textView_onlineRound = (TextView) findViewById(R.id.textView_onlineRound);
        textView_onlineScore1 = (TextView) findViewById(R.id.textView_onlineScore1);
        textView_onlineScore2 = (TextView) findViewById(R.id.textView_onlineScore2);

        linearLayouts_Columns[0] = (LinearLayout) findViewById(R.id.linearlayoutOnline1);
        linearLayouts_Columns[1] = (LinearLayout) findViewById(R.id.linearlayoutOnline2);
        linearLayouts_Columns[2] = (LinearLayout) findViewById(R.id.linearlayoutOnline3);
        linearLayouts_Columns[3] = (LinearLayout) findViewById(R.id.linearlayoutOnline4);
        linearLayouts_Columns[4] = (LinearLayout) findViewById(R.id.linearlayoutOnline5);
        linearLayouts_Columns[5] = (LinearLayout) findViewById(R.id.linearlayoutOnline6);
        linearLayouts_Columns[6] = (LinearLayout) findViewById(R.id.linearlayoutOnline7);

        setupField();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Realm.init(this);
        realm = Realm.getInstance(RealmHandler.getOnlineRealmConfig());
    }

    /**
     * Erstellt den GoogleApiClient für die Verwendung von Nearby Connections.
     *
     * @return Der ApiClient für die Kommunikation
     */
    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Nearby.CONNECTIONS_API)
                .addOnConnectionFailedListener(this)
                .build();
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
     * Handelt einen Click auf das Spielfeld. Wenn das Spiel im gange ist wird falls möglich und
     * erlaubt ein Stein in die jeweilige Spalte eingeworfen. Wenn das Spiel bereits rum ist wird
     * üebr einen zusätzlichen Click ein neues Spiel gestartet.
     *
     * @param view Der View, der angeclickt wurde.
     */
    public void onClick(View view) {
        if (connected) {
            if (winner == null && game.getFieldsLeft() > 0) {
                if (allowedToClick) {
                    LinearLayout linearLayout = (LinearLayout) view;

                    int column = (int) linearLayout.getTag();
                    insertStone(column);
                }
            } else {
                Nearby.Connections.sendPayload(googleApiClient, endpoint, Payload.fromBytes("Ende".getBytes()));
                resetGame();
            }
        }
    }

    /**
     * Bereitet die Anzeigen und die Rundentafel für den nächsten Spieler vor.
     */
    private void nextPlayer() {
        game.nextPlayer();
        textView_onlinePlayer.setText(game.getPlayerName());
        textView_onlinePlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        textView_onlineRound.setText(String.valueOf(game.getCurrentRound()));
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
                                handleResultCallback(result.getStatus());
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
                new EndpointDiscoveryAdapter() {
                    public void onEndpointFound(String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo) {
                        requestConnection(endpointId);
                    }
                },
                new DiscoveryOptions(Strategy.P2P_STAR))
                .setResultCallback(
                        new ResultCallback<Status>() {
                            public void onResult(Status status) {
                                handleResultCallback(status);
                            }
                        });
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
            if (allowedToClick) {
                Nearby.Connections.sendPayload(googleApiClient, endpoint, Payload.fromBytes(String.valueOf(column).getBytes()));
            }
            allowedToClick = !allowedToClick;
        }
    }

    /**
     * Fragt nach eine neue Verbindung bei dem gefundenen Enpunkt an.
     *
     * @param endpointId Der Enpunkt bei dem angefragt wird.
     */
    private void requestConnection(String endpointId) {
        String name = getUserNickname();
        Nearby.Connections.requestConnection(
                googleApiClient,
                name,
                endpointId,
                connectionLifecycleCallback);
    }

    /**
     * Handelt das Ergebnis der Discovery bzw. des Advertisen.
     *
     * @param status Der Status des Ergebnisses.
     */
    private void handleResultCallback(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(OnlineGameActivity.this, "Suche nach Gegner!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(OnlineGameActivity.this, "Es kann nach keinem Gegner gesucht werden!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Lässt gelbes und rotes Konfetti über das Spielfeld regnen.
     */
    private void makeKonfetti(Point point, int color) {
        konfettiView_online.build()
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
     * Macht Konfetti und aktualisiert die Anzeigen nach dem Sieg.
     */
    private void hasWinner() {
        for (Point p : game.getWinPositions()) {
            imageViews_fields[p.x][p.y].setImageDrawable(getResources().getDrawable(R.drawable.stone_green));
            makeKonfetti(p, game.getPlayerTurn().getColor());
        }
        winner = game.getPlayerTurn();
        if (winner == Player.P1) {
            textView_onlineScore1.setText(String.valueOf(++scorePlayer1));
            saveMyResults(1, 0);
        } else {
            textView_onlineScore2.setText(String.valueOf(++scorePlayer2));
            saveMyResults(0, 1);
        }
    }


    /**
     * Setzt das Spielfeld und die TextViews zur Anzeige der Runde für eine neue Spielrunde auf.
     */
    private void resetGame() {
        if (winner == null) {
            saveMyResults(0, 0);
        }
        game.reset();
        allowedToClick = getIntent().getExtras().getBoolean("host");
        textView_onlinePlayer.setText(game.getPlayerName());
        textView_onlinePlayer.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
        textView_onlineRound.setText(String.valueOf(game.getCurrentRound()));
        winner = null;
        setupField();
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
     * Reagiert auf die Antwort vom Partnergerät.
     *
     * @param payload Die Payload, die vom Partner bei diesem Gerät angekommen ist.
     */
    private void handlePayload(Payload payload) {
        String data = new String(payload.asBytes());
        if (data.equals("Ende")) {
            resetGame();
        } else {
            insertStone(Integer.parseInt(new String(payload.asBytes())));
        }
    }

    /**
     * Akzeptiert die angefragte Verbindung und bereitet alles für die spätere Kommunikation mit dem
     * Partnergerät vor.
     *
     * @param endpointId     Die Id des anderen Geräts
     * @param connectionInfo Die zusätzlichen Informationen
     */
    private void acceptConnection(String endpointId, ConnectionInfo connectionInfo) {
        endpoint = endpointId;
        if (getIntent().getExtras().getBoolean("host")) {
            game = new Game(getIntent().getExtras().getString("playerName"), connectionInfo.getEndpointName());
        } else {
            game = new Game(connectionInfo.getEndpointName(), getIntent().getExtras().getString("playerName"));
        }
        Nearby.Connections.acceptConnection(googleApiClient, endpointId, new PayloadCallbackAdapter() {
            public void onPayloadReceived(String s, Payload payload) {
                handlePayload(payload);
            }
        });
    }

    /**
     * Speichert die Ergebnisse der letzten Partie in der Datenbanbk.
     *
     * @param host   Ergebnis des hosts.
     * @param client Ergebnis des clients
     */
    private void saveMyResults(int host, int client) {
        playerresults = findPlayer(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        realm.beginTransaction();
        playerresults.setAlias(getIntent().getExtras().getString("playerName"));
        if (host != client) {
            if (getIntent().getExtras().getBoolean("host")) {
                playerresults.addVictories(host);
                playerresults.addLosses(host);
            } else {
                playerresults.addVictories(client);
                playerresults.addLosses(client);
            }
        }
        playerresults.addTime((System.currentTimeMillis() - startTime) / 1000);
        playerresults.addColorOfPreference(getIntent().getExtras().getBoolean("host") ? 1 : -1);
        playerresults.nextGame();
        playerresults.addRounds(game.getCurrentRound());
        realm.insertOrUpdate(playerresults);
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
        playerresults.setType("online");
        return playerresults;
    }

    // Nicht benötigt
    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

}