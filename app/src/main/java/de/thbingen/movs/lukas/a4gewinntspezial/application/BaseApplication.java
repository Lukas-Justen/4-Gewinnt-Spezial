package de.thbingen.movs.lukas.a4gewinntspezial.application;

import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatDelegate;

import io.realm.Realm;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 12.04.2017
 *          <p>
 *          Die Klasse BaseApplication wird zum Start der App aufgerufen. Sie stellt die App für
 *          Kompatibilität mit VektorDrawables ein.
 */
public class BaseApplication extends MultiDexApplication {

    /**
     * Wird beim Start der App aufgerufen, bevor auch nur eine Activity gestartet wurde. Hier wird
     * die Kompatibilität der App mit Vektorgrafiken ermöglicht.
     */
    public void onCreate() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate();
    }

}