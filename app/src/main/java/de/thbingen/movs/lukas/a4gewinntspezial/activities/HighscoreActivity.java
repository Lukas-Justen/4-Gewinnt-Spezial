package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.adapters.HighscoreAdapter;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresults;
import io.realm.Realm;
import io.realm.Sort;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 06.06.2017
 *          <p>
 *          Die Klasse HighscoreActivity stellt alle jemals erfassten Spielstände in einer Liste dar
 *          wobei es zusätzlich die Möglichkeit gibt sich die Spielstände graphisch darstellen zu
 *          lassen.
 */
public class HighscoreActivity extends FullscreenActivity implements View.OnTouchListener {


    private RecyclerView recyclerView_highscores;
    private View segmentedControl_highscore;
    private boolean fadeOutAllowed = false;
    private boolean fadeInAllowed = true;
    private Handler fadeHandler = new Handler();

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

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        recyclerView_highscores = (RecyclerView) findViewById(R.id.recyclerView_highscores);
        recyclerView_highscores.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_highscores.setAdapter(new HighscoreAdapter(realm.where(Playerresults.class).findAllSorted("victories", Sort.DESCENDING),this));
        recyclerView_highscores.setOnTouchListener(this);
        segmentedControl_highscore = findViewById(R.id.segmentedControl_highscore);
    }

    public boolean onTouch(View v, MotionEvent event) {
        /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (segmentedControl_highscore.getVisibility() == View.VISIBLE) {
                Animation animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_slow);
                segmentedControl_highscore.startAnimation(animFadeOut);
                segmentedControl_highscore.setVisibility(View.GONE);
            }
            fadeOutAllowed = false;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            final Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_slow);
            fadeOutAllowed = true;
            fadeHandler.removeCallbacksAndMessages(null);
            fadeHandler.postDelayed(new Runnable() {
                public void run() {
                    if (fadeOutAllowed) {
                        segmentedControl_highscore.startAnimation(animFadeIn);
                        segmentedControl_highscore.setVisibility(View.VISIBLE);
                    }
                }
            }, 2000);
        }*/
        return false;
    }

}
