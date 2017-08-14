package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.HighscoreAdapter;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresult;
import de.thbingen.movs.lukas.a4gewinntspezial.game.RealmHandler;
import io.realm.Realm;
import io.realm.Sort;
import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 06.06.2017
 *          <p>
 *          Die Klasse HighscoreActivity stellt alle jemals erfassten Spielstände in einer Liste dar
 *          wobei die Spielstände und Highscores der jeweiligen Personen grafisch ansprechend darge-
 *          stellt werden.
 */
public class HighscoreActivity extends FullscreenActivity implements SegmentedButtonGroup.OnClickedButtonPosition {

    @BindView(R.id.recyclerView_highscores)
    RecyclerView recyclerView_highscores;
    private Realm realm;
    private DynamicBox box;

    /**
     * Die Methode wird automatisch umgehend nach dem Starten der Activity aufgerufen und dient als
     * Ausgangspunkt für das Anlegen und Finden benötigter Views. Außerdem werden die verschiedenen
     * Button mit den Funktionen belegt, die die Klasse HighscoreActivity zu leisten hat.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        ButterKnife.bind(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Realm.init(this);
        realm = Realm.getInstance(RealmHandler.getLocalRealmConfig());

        recyclerView_highscores.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_highscores.setAdapter(new HighscoreAdapter(realm.where(Playerresult.class).findAllSorted("victories", Sort.DESCENDING), this));
        ((SegmentedButtonGroup) findViewById(R.id.segmentedControl_highscore)).setOnClickedButtonPosition(this);
        box = new DynamicBox(this, recyclerView_highscores);
    }

    /**
     * Zeigt die lokalen Spielergebnisse im Recyclerview an.
     */
    private void localHighscoreSelected() {
        realm = Realm.getInstance(RealmHandler.getLocalRealmConfig());
        recyclerView_highscores.setAdapter(new HighscoreAdapter(realm.where(Playerresult.class).findAllSorted("victories", Sort.DESCENDING), this));
    }

    /**
     * Zeigt die online Spielergebnisse im Recylerview bei vorhandener Internetverbindung an.
     */
    private void onlineHighscoreSelected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            realm = Realm.getInstance(RealmHandler.getOnlineRealmConfig());
            recyclerView_highscores.setAdapter(new HighscoreAdapter(realm.where(Playerresult.class).findAllSorted("victories", Sort.DESCENDING), this));
        } else {
            recyclerView_highscores.setAdapter(null);
            box.showInternetOffLayout();
            box.setInternetOffTitle("Fehler");
            box.setInternetOffMessage("Sie haben keine Internetverbindung, bitte aktivieren sie die Verbindung");
            box.setClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    onlineHighscoreSelected();
                }
            });
        }
    }

    /**
     * Je nach der Auswahl der SegmentedControl werden entweder die online oder die lokalen Spiel-
     * stände angezeigt.
     *
     * @param position Die Position der aktuell ausgewählten Schaltfläche innerhalb der Segmented-
     *                 Control. Dabei bedeutet 0 = lokal und 1 = online.
     */
    public void onClickedButtonPosition(int position) {
        box.hideAll();
        switch (position) {
            case 0:
                localHighscoreSelected();
                break;
            case 1:
                onlineHighscoreSelected();
                break;
        }
    }

}
