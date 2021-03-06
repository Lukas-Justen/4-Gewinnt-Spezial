package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 27.04.2017
 *          <p>
 *          Die abstrakte Klasse FullscreenActivity kann für Activities als Grundlage dienen, um zu
 *          verhindern, dass das Betriebssystem seine UI-Elemente anzeigt. Man kann somit den
 *          gesamten Bildschirm für seine App und deren Inhalte verwenden.
 */
public abstract class FullscreenActivity extends AppCompatActivity {

    private View decorView;

    /**
     * Wenn sich der Fokus auf die Activity ändert müssen die UI-Elemente wieder versteckt werden.
     *
     * @param hasFocus Der neue Fokus.
     */
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
    }

    /**
     * Die onCreate() wird von der Superclass aufgerufen und versteckt direkt zu Beginn alle UI-
     * Elemente des Systems. Die abgeleitete Klasse muss lediglich noch setContentView() aufrufen,
     * um das zugrunde liegende Layout festzulegen.
     *
     * @param savedInstanceState Kann benutzt werden, um Inhalte der App wieder herzustellen.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> hideSystemUI());
        hideSystemUI();
    }

    /**
     * Sorgt dafür, dass alle UI-Elemente des Betriebssystems versteckt und ausgeblendet werden.
     * Das hat den Effekt, das man den gesamten Bildschirm im FullScreen-Mode nutzen kann.
     */
    private void hideSystemUI() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * Überschreibt die Methode sodass beim Schließen einer Activity ebenfalls nur eine schlichte
     * Animation erscheint.
     */
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
