package de.thbingen.movs.lukas.a4gewinntspezial.application;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class RealmHandler {

    private static RealmConfiguration localConfig = null;
    private static RealmConfiguration onlineConfig = null;

    public static RealmConfiguration getLocalRealmConfig() {
        if (localConfig != null) {
            return localConfig;
        }
        localConfig = new RealmConfiguration.Builder()
                .name("local.realm")
                .schemaVersion(0)
                .migration(new Migration())
                .build();
        return localConfig;
    }

    public static RealmConfiguration getOnlineRealmConfig() {
        if (onlineConfig != null) {
            return onlineConfig;
        }
        SyncCredentials syncCredentials = SyncCredentials.usernamePassword("Test", "Test", false);
        SyncUser syncUser = SyncUser.login(syncCredentials, "http://143.93.91.3:80/auth");
        onlineConfig= new SyncConfiguration.Builder(syncUser, "realm://143.93.91.3:80/~/default")
                .schemaVersion(0)
                .name("online.realm")
                .build();
        return onlineConfig;
    }

}
