package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.TextWatcherAdapter;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 05.04.2017
 *          <p>
 *          Die Klasse OnlineActivity ist zum Starten eines neuen OnlineSpiels verantwortlich. Der
 *          Spieler dieses Geräts muss seinen Namen angeben. Die Actvity etabliert daraufhin eine
 *          Datenverbindung zwischen den beiden Geräten, die zur Kommunikation benötigt wird.
 */
public class OnlineActivity extends FullscreenActivity implements View.OnClickListener {

    private View button_startOnline;
    private EditText editText_playerThis;
    private SegmentedButtonGroup segmentedControl_hostOrClient;
    private String playerName = "";
    private BluetoothAdapter bluetoothAdapter;

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse OnlineActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        // Initialisiert die benötigten Views
        segmentedControl_hostOrClient = (SegmentedButtonGroup) findViewById(R.id.segmentedControl_hostOrClient);
        button_startOnline = findViewById(R.id.button_startOnline);
        button_startOnline.setOnClickListener(this);
        editText_playerThis = (EditText) findViewById(R.id.editText_thisPlayer);
        editText_playerThis.addTextChangedListener(new TextWatcherAdapter() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean player1 = s.length() > 0;
                if (player1) {
                    button_startOnline.setVisibility(View.VISIBLE);
                } else {
                    button_startOnline.setVisibility(View.INVISIBLE);
                }
            }
        });
        playerName = PreferenceManager.getDefaultSharedPreferences(this).getString("onlinePlayer", "");
        editText_playerThis.setText(playerName);
    }

    /**
     * Deaktiviert beim Schließen dieser Activity wieder den BluetoothAdapter, der ursprünglich für
     * das Online-Spiel benötigt wurde.
     */
    public void finish() {
        super.finish();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.disable();
        }
    }

    /**
     * Wird automatisch aufgerufen, wenn der Button zum Starten des neuen online Spiels geclickt
     * wurde.
     *
     * @param v Der View, der angeclickt wurde.
     */
    public void onClick(View v) {
        if (!checkNetworkForNearby()) {
            buildAlertMessageNoGps();
        } else {
            startGame();
        }
    }

    /**
     * Überprüft, ob sowohl das Bluetooth als auch die GPS-Position durch den Benutzer aktiviert
     * wurden. Wenn das der Fall ist wird das Spiel gestartet.
     *
     * @return True, wenn das Netzwerk bereit ist, sonst false
     */
    public boolean checkNetworkForNearby() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.enable();
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Startet ein neues Online-Spiel anhand des angegebenen Spielernamen und der Wahl ob man Host
     * oder Client sein möchte.
     */
    private void startGame() {
        Intent startOnlineGame = new Intent(this, OnlineGameActivity.class);
        playerName = editText_playerThis.getText().toString();
        startOnlineGame.putExtra("playerName", playerName);
        startOnlineGame.putExtra("host", segmentedControl_hostOrClient.getPosition() == 0);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("onlinePlayer", playerName).apply();
        startActivity(startOnlineGame);
    }

    /**
     * Zeigt einen Alert-Dialog an, zum Aktivieren des GPS.
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Dein GPS scheint deaktiviert zu sein, willst du es für ein Online-Spiel aktivieren?")
                .setCancelable(false)
                .setPositiveButton("Aktivieren", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
