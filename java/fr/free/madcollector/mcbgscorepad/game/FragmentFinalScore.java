package fr.free.madcollector.mcbgscorepad.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.databinding.ScoreFragmentFinalscoreBinding;

public class FragmentFinalScore extends Fragment implements PlayersScoreFragment{

    private ScoreFragmentFinalscoreBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ScoreFragmentFinalscoreBinding.inflate(inflater, container, false);
        setHasOptionsMenu(false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView finalScoresListView = binding.finalscoreListView;
        ArrayList<Player> playerList = ((GameActivity)getActivity()).getPlayers();
        ArrayList<Integer> finalScores = ((GameActivity)getActivity()).getFinalScores();
        FinalScoreAdapter myFinalScoreAdapter = new FinalScoreAdapter(getActivity(),R.layout.players_score_expandable, R.id.player_name, playerList, finalScores, this);
        finalScoresListView.setAdapter(myFinalScoreAdapter);
        myFinalScoreAdapter.notifyDataSetChanged();
    }

    public Activity getScoreActivity(){
        return getActivity();
    }
    public void saveScore(int roundline, Player player, Integer score) {

    }
}
