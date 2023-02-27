package fr.free.madcollector.mcbgscorepad.newgame;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.scoresheetgame.ScoresheetGameActivity;
import fr.free.madcollector.mcbgscorepad.templates.BoardgameTemplate;

public class NewGameBGTAdapter extends ArrayAdapter<BoardgameTemplate> {

    private FragmentNewGameTemplateList myFragment;

    public NewGameBGTAdapter(Context context, int layoutId, int viewResourceId, ArrayList data, FragmentNewGameTemplateList frag){
        super(context,layoutId,viewResourceId,data);
        myFragment = frag;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        TextView label=(TextView) row.findViewById(R.id.selectedNewGameBGTemplatedrawer_item1);
        row.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view) {
                Intent intentMain = new Intent(myFragment.getActivity() , ScoresheetGameActivity.class);
                intentMain.putExtra("newgame_template_name", label.getText().toString());
                myFragment.getActivity().startActivity(intentMain);
            }
        });
        return row;
    }

}
