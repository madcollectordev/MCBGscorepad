package fr.free.madcollector.mcbgscorepad.newgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.roundbasedgame.RoundGameActivity;

public class FragmentNewGameType extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.newgame_fragment_select_type,
                container, false);
        setHasOptionsMenu(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button launchRoundbasedGameButton = getActivity().findViewById(R.id.roundbase_newgame_button);
        launchRoundbasedGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getActivity() , RoundGameActivity.class);
                getActivity().startActivity(intentMain);
            }
        });
        Button launchGameButton = getActivity().findViewById(R.id.templated_newgame_button);
        launchGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentNewGameType.this)
                        .navigate(R.id.action_FragmenNewGameType_to_FragmentNewGameTemplateList);
            }
        });

    }

 }