package fr.free.madcollector.mcbgscorepad.newgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import fr.free.madcollector.mcbgscorepad.MainActivity;
import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.templates.BoardgameTemplate;

public class FragmentNewGameTemplateList extends Fragment {
    private ArrayList<BoardgameTemplate> bGTemplatesList;
    private ListView bgTemplatesListview;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.newgame_fragment_template_list,
                container, false);
        setHasOptionsMenu(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bGTemplatesList = ((MainActivity)getActivity()).getBgTemplateIO().getBGTemplateList();
        bgTemplatesListview = (ListView) getActivity().findViewById(R.id.newGametemplatesListView);
        NewGameBGTAdapter mybgTemplatesListAdapter = new NewGameBGTAdapter(getActivity(),R.layout.newgame_boardgame_templates_list_selection, R.id.selectedNewGameBGTemplatedrawer_item1, bGTemplatesList,this);
        bgTemplatesListview.setAdapter(mybgTemplatesListAdapter);
        mybgTemplatesListAdapter.notifyDataSetChanged();
    }


}