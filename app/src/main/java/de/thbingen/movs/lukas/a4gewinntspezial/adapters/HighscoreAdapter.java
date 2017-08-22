package de.thbingen.movs.lukas.a4gewinntspezial.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.thbingen.movs.lukas.a4gewinntspezial.R;
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresult;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * @author Lukas Justen lukas.justen@th-bingen.de
 * @version 1.0
 *          <p>
 *          Created on 06.06.2017
 *          <p>
 *          Die Klasse HighscoreAdapter befüllt einen RecyclerView mit ViewHoldern, die den Eintrag
 *          einer Person in der Rangliste darstellen. Dieser Adapter kann sowohl für online als auch
 *          lokale Spielstände verwendet werden.
 */
public class HighscoreAdapter extends RealmRecyclerViewAdapter<Playerresult, HighscoreAdapter.HighscoreHolder> {

    private Context context;
    private String myId = "";

    @BindDrawable(R.drawable.medal1)
    Drawable drawable_medal1;
    @BindDrawable(R.drawable.medal2)
    Drawable drawable_medal2;
    @BindDrawable(R.drawable.medal3)
    Drawable drawable_medal3;
    @BindColor(R.color.colorCardHighlightRed)
    int color_highlightRed;
    @BindColor(R.color.colorCardHighlightYellow)
    int color_highlightYellow;
    @BindColor(android.R.color.white)
    int color_white;
    @BindDrawable(R.drawable.player)
    Drawable drawable_player;
    @BindDrawable(R.drawable.red_player)
    Drawable drawable_playerRed;
    @BindDrawable(R.drawable.yellow_player)
    Drawable drawable_playerYellow;

    /**
     * Initialisiert den Adapter mit allen notwendigen Daten vorab.
     *
     * @param data    Die Liste der Ergebnisse, die dargestellt werden soll.
     * @param activity Der Kontext, in dem der Adapter arbeiten soll.
     */
    public HighscoreAdapter(OrderedRealmCollection<Playerresult> data, Activity activity) {
        super(data, true);
        ButterKnife.bind(this, activity);
        this.context = activity.getApplicationContext();
        this.myId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Erstellt einen neuen ViewHolder, der in den RecyclerView eingefügt werden soll.
     *
     * @param parent   Der Parent.
     * @param viewType Die Art des ViewHolders, ist jedoch irrelevant.
     * @return Ein neuer ViewHolder, der in den RecyclerView eingefügt werden kann.
     */
    public HighscoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new HighscoreHolder(itemView);
    }

    /**
     * Bindet einen gewissen Datensatz an einen ViewHolder.
     *
     * @param holder   Der Holder, an den die Daten gebunden werden sollen.
     * @param position Die Position des Datensatz, der dargestellt werden soll.
     */
    public void onBindViewHolder(HighscoreHolder holder, int position) {
        holder.setInformation(getItem(position), position);
    }

    /**
     * Die Klasse HighscoreHolder enthält alle Referenzen auf die Views, die die Informationen zu
     * einem Eintarg in der Rangliste darstellen sollen. Zusätzlich gibt es Methoden, die die Views
     * mit Daten befüllen.
     */
    class HighscoreHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView_highscorePlayerName)
        TextView textView_playerName;
        @BindView(R.id.textView_victories)
        TextView textView_victories;
        @BindView(R.id.textView_draws)
        TextView textView_draws;
        @BindView(R.id.textView_losses)
        TextView textView_losses;
        @BindView(R.id.textView_rounds)
        TextView textView_rounds;
        @BindView(R.id.textView_time)
        TextView textView_time;
        @BindView(R.id.textView_games)
        TextView textView_games;
        @BindView(R.id.imageView_medal)
        ImageView imageView_medal;
        @BindView(R.id.imageView_colorOfPreference)
        ImageView imageView_colorOfPreference;
        @BindView(R.id.cardView_background)
        CardView cardView_background;

        /**
         * Erzeugt einen neuen ViewHolder.
         *
         * @param view Der Parent-View, der die anderen Views enthält.
         */
        HighscoreHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        /**
         * Bindet die Informationen des Spielers an die Views innerhalb des ViewHolders.
         *
         * @param playerresults Die Ergebnisse des Spielers.
         * @param position      Die Position des Spielers.
         */
        void setInformation(Playerresult playerresults, int position) {
            imageView_medal.setImageDrawable(getMedal(position));
            textView_playerName.setText(playerresults.getAlias());
            textView_victories.setText(String.valueOf(playerresults.getVictories()));
            textView_draws.setText(String.valueOf(playerresults.getDraws()));
            textView_losses.setText(String.valueOf(playerresults.getLosses()));
            textView_rounds.setText(String.valueOf(playerresults.getRounds()));
            textView_time.setText(context.getString(R.string.textView_minutes, playerresults.getTime() / 60, playerresults.getTime() % 60));
            textView_games.setText(String.valueOf(playerresults.getGames()));
            imageView_colorOfPreference.setImageDrawable(getColorOfPreference(playerresults.getColorOfPreference()));
            if (playerresults.getName().equals(myId)) {
                if (playerresults.getColorOfPreference() >= 0) {
                    cardView_background.setCardBackgroundColor(color_highlightRed);
                } else {
                    cardView_background.setCardBackgroundColor(color_highlightYellow);
                }
            } else {
                cardView_background.setCardBackgroundColor(color_white);
            }
        }

        /**
         * Liefert eine Medaille falls der Spieler an der Position sich eine verdient hat, ansonsten
         * null also keine Medaille.
         *
         * @param position Die Position des Spielers.
         * @return Die Medaille wenn vorhanden.
         */
        private Drawable getMedal(int position) {
            switch (position) {
                case 0:
                    return drawable_medal1;
                case 1:
                    return drawable_medal2;
                case 2:
                    return drawable_medal3;
            }
            return null;
        }

        /**
         * Liefert anhand der präferierten Farbe des Spielers einen roten oder gelben Spieler. Wenn
         * der Spieler keine Farbe präferiert erhält er ein neutrales Spielersymbol.
         *
         * @param color Die Beforzugte Farbe. Dabei bedeutet ein Wert > 0 Rot und ein Wert < 0 Gelb.
         * @return Das Spielersymbol in der bevorzugten Farbe.
         */
        private Drawable getColorOfPreference(int color) {
            if (color == 0) {
                return drawable_player;
            } else if (color > 0) {
                return drawable_playerRed;
            } else {
                return drawable_playerYellow;
            }
        }

    }

}
