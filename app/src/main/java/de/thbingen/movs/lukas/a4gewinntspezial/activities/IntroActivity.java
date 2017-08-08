package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 01.08.2017
 *          <p>
 *          Die Klasse IntroActivity zeigt die Intrografiken beim aller ersten Start der App an.
 *          Zusätzlich werden die notwendigen Permissions für den Online-Spiel-Betrieb eingeholt.
 *          Nearby Connections verlangt hierbei neben den Bluetooth- und Wifi-Rechten auch die
 *          Erlaubnis für die Standortfreigabe.
 */
public class IntroActivity extends AppIntro2 {

    /**
     * Zeigt die vier Slides zum Spiel an und holt die Permissions ein..
     *
     * @param savedInstanceState Wird benötigt um den Zustand des Screens zu sichern.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("firstStart", true)) {
            showIntro();
        } else {
            startApp();
        }
    }

    /**
     * Reagiert, wenn der Done-Button zum Abschließen des Intros gedrückt wird. Wenn der User an dieser
     * Stelle angekommen ist wird die Main-Activity gestartet.
     *
     * @param currentFragment Der aktuelle Slide.
     */
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("firstStart", false).apply();
        startApp();
    }

    /**
     * Startet die richtige App nach dem Intro.
     */
    private void startApp() {
        Intent i = new Intent(this, StartActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Zeigt das Intro mit den 4 Slides an.
     */
    private void showIntro() {
        addSlide(getSlide(R.string.title_welcome, R.string.message_welcome, R.drawable.logo, R.color.colorPrimaryTransparent, R.color.colorPrimaryDark));
        addSlide(getSlide(R.string.title_local, R.string.message_local, R.drawable.local_screen, R.color.colorPrimary, android.R.color.white));
        addSlide(getSlide(R.string.title_online, R.string.message_online, R.drawable.online_screen, R.color.colorPrimaryDark, R.color.colorPrimaryTransparent));
        addSlide(getSlide(R.string.title_highscore, R.string.message_highscore, R.drawable.highscore_screen, R.color.colorDivider, R.color.colorPrimaryTransparent));

        askForPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
        showSkipButton(false);
        setProgressButtonEnabled(true);
    }

    /**
     * Liefert einen Slide für das Intro mit den angegebenen Parametern.
     *
     * @param title      Der Titel des Slides.
     * @param message    Die Nachricht des Slides.
     * @param image      Das Bild auf dem Slide.
     * @param background Die Hintergrundfarbe des Slides.
     * @param textColor  Die Textfarbe des Slides.
     * @return Der fertige Slide.
     */
    private AppIntroFragment getSlide(int title, int message, int image, int background, int textColor) {
        return AppIntroFragment.newInstance(getString(title)
                , getString(message)
                , image
                , getResources().getColor(background)
                , getResources().getColor(textColor)
                , getResources().getColor(textColor));
    }

}