package de.thbingen.movs.lukas.a4gewinntspezial.adapters;

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

    /**
     * Initialisiert den Adapter mit allen notwendigen Daten vorab.
     *
     * @param data    Die Liste der Ergebnisse, die dargestellt werden soll.
     * @param context Der Kontext, in dem der Adapter arbeiten soll.
     */
    public HighscoreAdapter(OrderedRealmCollection<Playerresult> data, Context context) {
        super(data, true);
        this.context = context;
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

        private TextView textView_playerName;
        private TextView textView_victories;
        private TextView textView_draws;
        private TextView textView_losses;
        private TextView textView_rounds;
        private TextView textView_time;
        private TextView textView_games;
        private ImageView imageView_medal;
        private ImageView imageView_colorOfPreference;
        private CardView cardView_background;

        /**
         * Erzeugt einen neuen ViewHolder und hinterlegt alle Views, die später zur Darstellung der
         * Informationen benötigt werden in entsprechenden Variablen.
         *
         * @param view Der Parent-View, der die anderen Views enthält.
         */
        HighscoreHolder(View view) {
            super(view);

            textView_playerName = (TextView) view.findViewById(R.id.textView_highscorePlayerName);
            textView_victories = (TextView) view.findViewById(R.id.textView_victories);
            textView_draws = (TextView) view.findViewById(R.id.textView_draws);
            textView_losses = (TextView) view.findViewById(R.id.textView_losses);
            textView_rounds = (TextView) view.findViewById(R.id.textView_rounds);
            textView_time = (TextView) view.findViewById(R.id.textView_time);
            textView_games = (TextView) view.findViewById(R.id.textView_games);
            imageView_medal = (ImageView) view.findViewById(R.id.imageView_medal);
            imageView_colorOfPreference = (ImageView) view.findViewById(R.id.imageView_colorOfPreference);
            cardView_background = (CardView) view.findViewById(R.id.cardView_background);
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
                    cardView_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorCardHighlightRed));
                } else {
                    cardView_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorCardHighlightYellow));
                }
            } else {
                cardView_background.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
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
                    return context.getResources().getDrawable(R.drawable.medal1);
                case 1:
                    return context.getResources().getDrawable(R.drawable.medal2);
                case 2:
                    return context.getResources().getDrawable(R.drawable.medal3);
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
                return context.getResources().getDrawable(R.drawable.player);
            } else if (color > 0) {
                return context.getResources().getDrawable(R.drawable.red_player);
            } else {
                return context.getResources().getDrawable(R.drawable.yellow_player);
            }
        }

    }

}
