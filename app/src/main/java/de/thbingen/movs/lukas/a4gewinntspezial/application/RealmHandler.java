package de.thbingen.movs.lukas.a4gewinntspezial.application;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class RealmHandler {

    public static Realm getLocalRealm(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("ConnectFour")
                .schemaVersion(0)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(config);
        return Realm.getDefaultInstance();
    }

    public static Realm getOnlineRealm(Context context) {
        Realm.init(context);
        SyncCredentials syncCredentials = SyncCredentials.usernamePassword("Test", "Test", false);
        SyncUser syncUser = SyncUser.login(syncCredentials, "http://143.93.91.3:80/auth");
        SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(syncUser, "realm://143.93.91.3:80/~/default").build();
        Realm.setDefaultConfiguration(syncConfiguration);
        return Realm.getDefaultInstance();
    }

}
