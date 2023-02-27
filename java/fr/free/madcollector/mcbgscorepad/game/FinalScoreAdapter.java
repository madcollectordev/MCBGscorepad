package fr.free.madcollector.mcbgscorepad.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;

public class FinalScoreAdapter extends ArrayAdapter<Player> {

    private ArrayList<Player> data;
    private ArrayList<Integer> scoreLines;
    private PlayersScoreFragment parentFragment;
    private int maxScore = 0;

    public FinalScoreAdapter(Context context, int layoutId, int viewResourceId, ArrayList data,ArrayList scores, PlayersScoreFragment parent){
        super(context,layoutId,viewResourceId,data);
        parentFragment = parent;
        this.data = data;
        scoreLines = scores;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        TextView playerName = (TextView) row.findViewById(R.id.player_name);
        playerName.setText(data.get(position).getName());
        TextView score = (TextView) row.findViewById(R.id.playerScore);
        score.setText(scoreLines.get(position).toString());
        if(scoreLines.get(position)>maxScore){
            maxScore = scoreLines.get(position);
        }
        if(scoreLines.get(position).intValue()==maxScore)
        {
            ((CardView)row.findViewById(R.id.gameScorelineCardView)).setCardBackgroundColor(ContextCompat.getColor(parentFragment.getScoreActivity(), R.color.deep_red));
        }
        else
            ((CardView)row.findViewById(R.id.gameScorelineCardView)).setCardBackgroundColor(ContextCompat.getColor(parentFragment.getScoreActivity(), com.google.android.material.R.color.design_default_color_secondary_variant));
        return row;
    }
}