package de.thbingen.movs.lukas.a4gewinntspezial.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresult;
import de.thbingen.movs.lukas.a4gewinntspezial.game.RealmHandler;
import io.realm.Realm;

public class HighscoreDetailActivity extends FullscreenActivity {

    private Playerresult result;
    PieChart pieChart_allResults;
    @BindDrawable(R.drawable.logo)
    Drawable drawable_loss;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String name = bundle.getString("name");
            Realm realm = Realm.getInstance(RealmHandler.getLocalRealmConfig());
            result = realm.where(Playerresult.class).equalTo("name", name).findFirst();
            pieChart_allResults = findViewById(R.id.pieChart_allResults);
            setupPieChart();
        }
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(result.getVictories(), "Siege"));
        entries.add(new PieEntry(result.getDraws(), "Unentschieden"));
        entries.add(new PieEntry(result.getLosses(), "Niederlagen", drawable_loss));

        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(new int[]{R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimaryTransparent}, this);
        PieData data = new PieData(set);
        pieChart_allResults.setData(data);
        pieChart_allResults.invalidate();
    }

}
