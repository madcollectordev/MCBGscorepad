package fr.free.madcollector.mcbgscorepad.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.databinding.PlayersFragmentManageBinding;

public class FragmentManagePlayers extends Fragment {

    private PlayersFragmentManageBinding binding;
    private PlayerCardAdapter  myPlayerCardAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setHasOptionsMenu(false);
        binding = PlayersFragmentManageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((FloatingActionButton) getActivity().findViewById(R.id.addPlayerButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPlayerDialog dialog = new NewPlayerDialog(FragmentManagePlayers.this);
                dialog.show(getActivity().getSupportFragmentManager(), "NewPlayerDialog");
            }
        });
        ArrayList<Player> playerList = ((GameActivity)getActivity()).getPlayers();
        ListView playersListview = (ListView) getActivity().findViewById(R.id.managePlayersListView);
        myPlayerCardAdapter = new PlayerCardAdapter(getActivity(),R.layout.players_card, R.id.playerlist_name, playerList, this);
        playersListview.setAdapter(myPlayerCardAdapter);
        myPlayerCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void refreshPlayerList()
    {
        myPlayerCardAdapter.notifyDataSetChanged();
    }

}
