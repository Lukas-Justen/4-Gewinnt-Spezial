package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.EndpointDiscoveryAdapter;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.PayloadCallbackAdapter;
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
 *          Die Klasse OnlineGameActivity ist zur Anzeige des Spielfeldes und für das eigentliche Spiel-
 *          feld vorbehalten. Zu Beginn wird das Spielfeld über den Java-Code generiert und je
 *          7 Spalten mit 6 ImageViews befüllt. Diese bilden die einzelnen Felder des 4-Gewinnt
 *          Spiels. Neben dem Einsortieren der Spielsteine in die jeweiligen Spalten ist die Game-
 *          Activity auch für die Kommunikation zu dem Partnergerät verantwortlich.
 */
public class OnlineGameActivity extends GameActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient googleApiClient;
    private String endpoint;
    private boolean allowedToClick = false;

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

        googleApiClient = buildGoogleApiClient();

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
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("host")) {
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
        if (allowedToClick) {
            super.onClick(view);
        }
    }

    /**
     * Setzt das Spielfeld und die TextViews zur Anzeige der Runde für eine neue Spielrunde auf.
     */
    protected void resetGame() {
        if (game.getFieldsLeft() <= 0 || game.getWinner() != null) {
            allowedToClick = getIntent().getExtras() != null && getIntent().getExtras().getBoolean("host");
            super.resetGame();
            Nearby.Connections.sendPayload(googleApiClient, endpoint, Payload.fromBytes("Ende".getBytes()));
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
                getConnectionLifecycleCallback(),
                new AdvertisingOptions(Strategy.P2P_STAR))
                .setResultCallback(result -> handleResultCallback(result.getStatus()));
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
                .setResultCallback(this::handleResultCallback);
    }

    /**
     * Liefert den Spielernamen, den der Nutzer dieses Geräts für das aktuelle Online-Spiel
     * angegeben hat.
     *
     * @return Der Name als String.
     */
    private String getUserNickname() {
        if (getIntent().getExtras() != null) {
            return getIntent().getExtras().getString("playerName");
        }
        return "";
    }

    /**
     * Liefert den LifecyclerCallback für die Kommunikation mit dem anderen Gerät.
     *
     * @return Liefert den Lifecycle für die NearbyConnection.
     */
    private ConnectionLifecycleCallback getConnectionLifecycleCallback() {
        return new ConnectionLifecycleCallback() {
            public void onConnectionInitiated(final String endpointId, final ConnectionInfo connectionInfo) {
                new AlertDialog.Builder(OnlineGameActivity.this)
                        .setTitle("Wollen Sie die Verbindung zu " + connectionInfo.getEndpointName() + " akzeptieren?")
                        .setMessage("Bestätigen Sie, wenn der Code: " + connectionInfo.getAuthenticationToken() + " auch auf dem anderen Gerät angezeigt wird.")
                        .setPositiveButton("Akzeptieren", (dialog, which) -> acceptConnection(endpointId, connectionInfo))
                        .setNegativeButton("Ablehnen", (dialog, which) -> refuseConnection(endpointId))
                        .show();
            }

            public void onConnectionResult(String endpointId, ConnectionResolution result) {
                if (result.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
                    allowedToClick = getIntent().getExtras() != null && getIntent().getExtras().getBoolean("host");
                    textView_player.setText(game.getPlayerName());
                    textView_player.setTextColor(getResources().getColor(game.getPlayerTurn().getColor()));
                }
            }

            public void onDisconnected(String endpointId) {
                Toast.makeText(OnlineGameActivity.this, "Sie wurden von Ihrem Endpunkt leider getrennt!", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
    }

    /**
     * Wirft einen Stein in die angegebene Spalte. Daraufhin wird überprüft, ob es einen Gewinner
     * gibt.
     *
     * @param column Die Spalte, in die der Stein eingeworfen werden soll.
     */
    protected int insertStone(int column) {
        int row = super.insertStone(column);
        if (row >= 0) {
            if (allowedToClick) {
                Nearby.Connections.sendPayload(googleApiClient, endpoint, Payload.fromBytes(String.valueOf(column).getBytes()));
            }
            allowedToClick = !allowedToClick;
        }
        return row;
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
                getConnectionLifecycleCallback());
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
     * Speichert die Ergebnisse der letzten Partie in der Datenbanbk.
     *
     * @param player1 Ergebnis des hosts.
     * @param player2 Ergebnis des clients
     */
    protected void saveResults(int player1, int player2) {
        Playerresult playerresult = findPlayer(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID), "");
        realm.beginTransaction();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            playerresult.setAlias(bundle.getString("playerName"));
            if (player1 != player2) {
                if (bundle.getBoolean("host")) {
                    playerresult.addVictories(player1);
                    playerresult.addLosses(player1);
                } else {
                    playerresult.addVictories(player2);
                    playerresult.addLosses(player2);
                }
            }
        }
        playerresult.addTime(game.getPlayTime());
        playerresult.addColorOfPreference(getIntent().getExtras().getBoolean("host") ? 1 : -1);
        playerresult.nextGame();
        playerresult.addRounds(game.getCurrentRound());
        realm.insertOrUpdate(playerresult);
        realm.commitTransaction();
    }

    /**
     * Reagiert auf die Antwort vom Partnergerät.
     *
     * @param payload Die Payload, die vom Partner bei diesem Gerät angekommen ist.
     */
    private void handlePayload(Payload payload) {

        if (payload != null && payload.asBytes() != null) {
            byte[] bytes = payload.asBytes();
            if (bytes != null) {
                String data = new String(bytes);
                if (data.equals("Ende")) {
                    resetGame();
                } else {
                    insertStone(Integer.parseInt(new String(bytes)));
                }
            }
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
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("host")) {
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
     * Lehnt die Verbindung zu dem angegebenen Enpunkt ab.
     *
     * @param endpointId Die ID des Endpunkts der abgelehnt werden soll.
     */
    private void refuseConnection(String endpointId) {
        Toast.makeText(OnlineGameActivity.this, "Die Verbindung wird abgelehnt!", Toast.LENGTH_SHORT).show();
        Nearby.Connections.rejectConnection(googleApiClient, endpointId);
    }

    /**
     * Liefert die Konfiguration für den Sieger-textView beim Ergebnisbildschirm
     */
    protected void getWinnerText() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("host")) {
            if (game.getWinner() == Player.P1) {
                textView_resultWinner.setText(getString(R.string.textView_resultWinnerVictory));
            } else {
                textView_resultWinner.setText(getString(R.string.textView_resultWinnerDefeat));
            }
            textView_resultWinner.setTextColor(getResources().getColor(R.color.colorRed));
        } else {
            if (game.getWinner() == Player.P2) {
                textView_resultWinner.setText(getString(R.string.textView_resultWinnerVictory));
            } else {
                textView_resultWinner.setText(getString(R.string.textView_resultWinnerDefeat));
            }
            textView_resultWinner.setTextColor(getResources().getColor(R.color.colorYellow));
        }
    }

    // Nicht benötigt
    public void onConnectionSuspended(int i) {
    }

    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

}