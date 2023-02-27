package fr.free.madcollector.mcbgscorepad.scoresheetgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.game.PlayersScoreFragment;
import fr.free.madcollector.mcbgscorepad.game.ScoreInputDialog;
import fr.free.madcollector.mcbgscorepad.templates.BGTemplateScoringLine;

public class ScorelineExpandableAdapter extends BaseExpandableListAdapter {

    private ArrayList<Player> playersData;
    private ArrayList<BGTemplateScoringLine> scoreLines;
    private PlayersScoreFragment parentFragment;

    // constructor
    public ScorelineExpandableAdapter(FragmentScoresheetGameMain fragment, ArrayList playersData, ArrayList scores) {
        parentFragment = fragment;
        this.playersData = playersData;
        this.scoreLines = scores;

    }

    @Override
    // Gets the data associated with the given child within the given group.
    public Object getChild(int lstPosn, int expanded_ListPosition) {
        return null;
    }

    @Override
    // Gets the ID for the given child within the given group.
    // This ID must be unique across all children within the group. Hence we can pick the child uniquely
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    @Override
    // Gets a View that displays the data for the given child within the given group.
    public View getChildView(int lstPosn, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ((Fragment)parentFragment).getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.players_score_expandable, null);
        }
        TextView playerName = (TextView) convertView.findViewById(R.id.player_name);
        playerName.setText(playersData.get(expanded_ListPosition).getName());
        TextView score = (TextView) convertView.findViewById(R.id.playerScore);
        if(scoreLines.get(lstPosn).getScores().get(expanded_ListPosition)!= null) {
            score.setText(scoreLines.get(lstPosn).getScores().get(expanded_ListPosition).toString());
            score.setVisibility(View.VISIBLE);
        }
        else score.setVisibility(View.INVISIBLE);
        convertView.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view) {
                ScoreInputDialog dialog = new ScoreInputDialog(parentFragment, lstPosn, playersData.get(expanded_ListPosition),scoreLines.get(lstPosn).getScores().get(expanded_ListPosition));
                dialog.show(((Fragment) parentFragment).getActivity().getSupportFragmentManager(), "InputScoreDialog");
            }
        });
        return convertView;
    }

    @Override
    // Gets the number of children in a specified group.
    public int getChildrenCount(int listPosition) {
        return playersData.size();
    }

    @Override
    // Gets the data associated with the given group.
    public Object getGroup(int listPosition) {
        return scoreLines.get(listPosition);
    }

    @Override
    // Gets the number of groups.
    public int getGroupCount() {
        return scoreLines.size();
    }

    @Override
    // Gets the ID for the group at the given position. This group ID must be unique across groups.
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    // Gets a View that displays the given group.
    // This View is only for the group--the Views for the group's children
    // will be fetched using getChildView()
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) ((Fragment)parentFragment).getActivity().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.scoresheet_bgscoreline_card_expandable, null);
        }
        BGTemplateScoringLine bgsl= (BGTemplateScoringLine) getGroup(listPosition);
        if(bgsl.getIconString()!=null)
        {
            byte[] decodedString = Base64.decode(bgsl.getIconString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ((ImageView)convertView.findViewById(R.id.gameScoreLineIcon)).setImageBitmap(decodedByte);
        }
        else  ((ImageView)convertView.findViewById(R.id.gameScoreLineIcon)).setImageBitmap(null);
        if(bgsl.getColor()!=-1)
        {
            ((ImageView)convertView.findViewById(R.id.gameScoreLineIcon)).getBackground().setTint(bgsl.getColor());
        }
        else ((ImageView)convertView.findViewById(R.id.gameScoreLineIcon)).getBackground().setTint(ContextCompat.getColor(((Fragment)parentFragment).getActivity(), com.google.android.material.R.color.design_default_color_secondary_variant));
        TextView scoreNameTV = (TextView) convertView.findViewById(R.id.gameScorelinelist_name);
        scoreNameTV.setText(bgsl.getName());
        return convertView;
    }

    @Override
    // Indicates whether the child and group IDs are stable across changes to the underlying data.
    public boolean hasStableIds() {
        return false;
    }

    @Override
    // Whether the child at the specified position is selectable.
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}