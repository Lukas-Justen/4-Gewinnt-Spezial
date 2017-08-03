package de.thbingen.movs.lukas.a4gewinntspezial.application;

import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 12.04.2017
 *          <p>
 *          Die Klasse BaseApplication wird zum Start der App aufgerufen. Sie initialisiert alle
 *          notwendigen Datenelemente zur Kommunikation mit Realm und stellt diese im weiteren
 *          Appverlauf zur Verfügung.
 */
public class BaseApplication extends MultiDexApplication {

    /**
     * Wird beim Start der App aufgerufen, bevor auch nur eine Activity gestartet wurde. Hier wird
     * die Realmdatenbank mit ihrer Configuration initialisiert und notwendige Migrations
     * vorgenommen.
     */
    public void onCreate() {
        // VectorDrawables erlauben
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        super.onCreate();
    }

}