package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.Manifest;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

public class IntroActivity extends AppIntro2 {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(getString(R.string.title_welcome)
                , getString(R.string.message_welcome)
                , R.drawable.logo
                , getResources().getColor(R.color.colorPrimaryTransparent)
                , getResources().getColor(R.color.colorPrimaryDark)
                , getResources().getColor(R.color.colorPrimaryDark)));

        addSlide(AppIntroFragment.newInstance(getString(R.string.title_local)
                , getString(R.string.message_local)
                , R.drawable.local_screen
                , getResources().getColor(R.color.colorPrimary)
                , getResources().getColor(android.R.color.white)
                , getResources().getColor(android.R.color.white)));

        addSlide(AppIntroFragment.newInstance(getString(R.string.title_online)
                , getString(R.string.message_online)
                , R.drawable.online_screen
                , getResources().getColor(R.color.colorPrimaryDark)
                , getResources().getColor(R.color.colorPrimaryTransparent)
                , getResources().getColor(R.color.colorPrimaryTransparent)));

        addSlide(AppIntroFragment.newInstance(getString(R.string.title_highscore)
                , getString(R.string.message_highscore)
                , R.drawable.highscore_screen
                , getResources().getColor(R.color.colorDivider)
                , getResources().getColor(R.color.colorPrimaryTransparent)
                , getResources().getColor(R.color.colorPrimaryTransparent)));

        askForPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION},3);
        showSkipButton(false);
        setProgressButtonEnabled(true);
    }

    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("firstStart", false).apply();
        super.finish();
    }

    public void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    public void finish() {

    }

}