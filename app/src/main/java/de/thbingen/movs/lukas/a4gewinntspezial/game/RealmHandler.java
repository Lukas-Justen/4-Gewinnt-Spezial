package de.thbingen.movs.lukas.a4gewinntspezial.game;

import io.realm.ObjectServerError;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 27.07.2017
 *          <p>
 *          Der RealmHandler verwaltet die verschiedenen Konfigurationen, auf die dieser Nutzer seine
 *          Daten schreiben kann. Zum einen verwaltet er die lokale Datenbank, welche nur lokal auf
 *          dem Gerät gespeichert ist und nicht synchronisiert wird. Auf der anderen Seite wird die
 *          online-Datenbank auf dem RealmObjectServer gespeichert und immer aktualisiert.
 */
public class RealmHandler {

    private static RealmConfiguration localConfig = null;
    private static RealmConfiguration onlineConfig = null;

    /**
     * Liefert die lokale Konfiguration für den Zugang zur Datenbank. Wenn bereits ein Zugang
     * existiert wird dieser zurückgegeben. Ansonsten wird ein neuer angelegt.
     *
     * @return Die lokale realm-Konfiguration.
     */
    public static RealmConfiguration getLocalRealmConfig() {
        if (localConfig != null) {
            return localConfig;
        }
        localConfig = new RealmConfiguration.Builder()
                .name("local.realm")
                .schemaVersion(0)
                .build();
        return localConfig;
    }

    /**
     * Der online-Zugang zur Realmdatenbank. Falls noch keine Konfiguration angelegt wurde wird eine
     * neue erstellt, ansonsten wird die bereits vorhandene Konfiguration an den Aufrufer zurück-
     * geliefert.
     *
     * @return Der Online-Zugang.
     */
    public static RealmConfiguration getOnlineRealmConfig() throws ObjectServerError {
        if (onlineConfig != null) {
            return onlineConfig;
        }
        SyncCredentials syncCredentials = SyncCredentials.usernamePassword("Test", "Test", false);
        SyncUser syncUser = SyncUser.login(syncCredentials, "http://143.93.91.3:80/auth");
        onlineConfig = new SyncConfiguration.Builder(syncUser, "realm://143.93.91.3:80/~/default")
                .schemaVersion(0)
                .name("online.realm")
                .build();
        return onlineConfig;
    }

}
