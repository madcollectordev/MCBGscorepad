package fr.free.madcollector.mcbgscorepad.game;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;

public class PlayerCardAdapter extends ArrayAdapter<Player> {

    private ArrayList<Player> data;
    private FragmentManagePlayers parentFragment;

    public PlayerCardAdapter(Context context, int layoutId, int viewResourceId, ArrayList data, FragmentManagePlayers parent){
        super(context,layoutId,viewResourceId,data);
        parentFragment = parent;
        this.data = data;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        TextView playerName = (TextView) row.findViewById(R.id.playerlist_name);
        playerName.setText(data.get(position).getName());
        ImageView deleteButton = (ImageView) row.findViewById(R.id.playerDelete);
        deleteButton.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view) {
                ((GameActivity)parentFragment.getActivity()).removePlayer(data.get(position));
                parentFragment.refreshPlayerList();
            }
        });
        return row;
    }
}