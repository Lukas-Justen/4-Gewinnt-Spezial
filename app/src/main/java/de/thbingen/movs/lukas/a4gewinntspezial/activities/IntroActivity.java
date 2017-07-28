package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.Manifest;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import de.thbingen.movs.lukas.a4gewinntspezial.R;

public class IntroActivity extends AppIntro2 {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Mongo",
                "vjehkv erze zgrf kusd r zs gduzf gusd fs fsd duiz gfusz",
                R.drawable.red_player,
                getResources().getColor(R.color.colorPrimary)));

        addSlide(AppIntroFragment.newInstance("Ksdhvfivns",
                "BLIldsui ildls lds uifh siudl fushif uisd",
                R.drawable.red_player,
                getResources().getColor(R.color.colorPrimaryDark)));

        addSlide(AppIntroFragment.newInstance("Cohdfeiv",
                "sd fh idshi fsdiuh ui sddfuihsfduh sfhul ufdhsifhish du fuisduhdfiu hsdihf hsd fiud",
                R.drawable.red_player,
                getResources().getColor(R.color.colorAccent)));

        addSlide(AppIntroFragment.newInstance("Jsdfhcvbd",
                "sdfh sdiufh idsu hfdsuhiufhsidhf usdhfi sidufhiushdfjsdijf js iojdoifjosj doisjdf",
                R.drawable.red_player,
                getResources().getColor(R.color.colorPrimaryDark)));

        askForPermissions(new String[] {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(true);
    }

    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("firstStart", false).apply();
        finish();
    }

    public void onSlideChanged(Fragment oldFragment, Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

}