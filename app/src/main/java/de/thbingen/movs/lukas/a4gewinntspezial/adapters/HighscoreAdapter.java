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
import de.thbingen.movs.lukas.a4gewinntspezial.game.Playerresults;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class HighscoreAdapter extends RealmRecyclerViewAdapter<Playerresults, HighscoreAdapter.ViewHolder>{

    private Context context;
    private String myId = "";

    public HighscoreAdapter(OrderedRealmCollection<Playerresults> data, Context context) {
        super(data, true);
        this.context = context;
        this.myId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setInformation(getItem(position), position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

        ViewHolder(View view) {
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

        void setInformation(Playerresults playerresults, int position) {
            imageView_medal.setImageDrawable(getMedal(position));
            textView_playerName.setText(playerresults.getAlias());
            textView_victories.setText(String.valueOf(playerresults.getVictories()));
            textView_draws.setText(String.valueOf(playerresults.getDraws()));
            textView_losses.setText(String.valueOf(playerresults.getLosses()));
            textView_rounds.setText(String.valueOf(playerresults.getRounds()));
            textView_time.setText(context.getString(R.string.textView_minutes,playerresults.getTime() / 60, playerresults.getTime() % 60));
            textView_games.setText(String.valueOf(playerresults.getGames()));
            imageView_colorOfPreference.setImageDrawable(getColorOfPreference(playerresults.getColorOfPreference()));
            if (playerresults.getName().equals(myId)) {
                if (playerresults.getColorOfPreference()>= 0) {
                    cardView_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorCardHighlightRed));
                } else {
                    cardView_background.setCardBackgroundColor(context.getResources().getColor(R.color.colorCardHighlightYellow));
                }
            } else {
                cardView_background.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            }
        }

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

        private Drawable getColorOfPreference(int color) {
            if (color == 0) {
                return null;
            } else if (color > 0) {
                return context.getResources().getDrawable(R.drawable.red_player);
            } else {
                return context.getResources().getDrawable(R.drawable.yellow_player);
            }
        }

    }

}
