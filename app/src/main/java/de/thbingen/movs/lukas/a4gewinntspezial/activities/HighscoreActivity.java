package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.HighscoreAdapter;
import de.thbingen.movs.lukas.a4gewinntspezial.game.RealmHandler;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresult;
import io.realm.Realm;
import io.realm.Sort;

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
public class HighscoreActivity extends FullscreenActivity implements SegmentedButtonGroup.OnPositionChanged {

    private RecyclerView recyclerView_highscores;
    private Realm realm;

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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Realm.init(this);
        realm = Realm.getInstance(RealmHandler.getLocalRealmConfig());

        recyclerView_highscores = (RecyclerView) findViewById(R.id.recyclerView_highscores);
        recyclerView_highscores.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_highscores.setAdapter(new HighscoreAdapter(realm.where(Playerresult.class).findAllSorted("victories", Sort.DESCENDING), this));
        ((SegmentedButtonGroup) findViewById(R.id.segmentedControl_highscore)).setOnPositionChanged(this);
    }

    /**
     * Je nach der Auswahl der SegmentedControl werden entweder die online oder die lokalen Spiel-
     * stände angezeigt.
     *
     * @param position Die Position der aktuell ausgewählten Schaltfläche innerhalb der Segmented-
     *                 Control. Dabei bedeutet 0 = lokal und 1 = online.
     */
    public void onPositionChanged(int position) {
        switch (position) {
            case 0:
                realm = Realm.getInstance(RealmHandler.getLocalRealmConfig());
                recyclerView_highscores.setAdapter(new HighscoreAdapter(realm.where(Playerresult.class).findAllSorted("victories", Sort.DESCENDING), this));
                break;
            case 1:
                realm = Realm.getInstance(RealmHandler.getOnlineRealmConfig());
                recyclerView_highscores.setAdapter(new HighscoreAdapter(realm.where(Playerresult.class).findAllSorted("victories", Sort.DESCENDING), this));
                break;
        }
    }

}
