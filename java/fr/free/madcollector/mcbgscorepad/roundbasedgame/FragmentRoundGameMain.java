package fr.free.madcollector.mcbgscorepad.roundbasedgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.databinding.RoundbasedFragmentMainBinding;
import fr.free.madcollector.mcbgscorepad.game.PlayersScoreFragment;
import fr.free.madcollector.mcbgscorepad.game.QuitGameDialog;

public class FragmentRoundGameMain extends Fragment implements PlayersScoreFragment {

    private RoundbasedFragmentMainBinding binding;
    private RoundExpandableAdapter expandableListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                QuitGameDialog dialog = new QuitGameDialog(true);
                dialog.show(getActivity().getSupportFragmentManager(), "QuitGameDialog");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_game, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.players_toolbar:
                NavHostFragment.findNavController(this).navigate(R.id.action_FragmentRoundGameMain_to_FragmentManagePlayers);
                return true;
            case R.id.action_newgame:
                QuitGameDialog newGameDialog = new QuitGameDialog(false);
                newGameDialog.show(getActivity().getSupportFragmentManager(), "QuitGameDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setHasOptionsMenu(true);
        binding = RoundbasedFragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ExpandableListView scorelinesExpandableListView= binding.expandableScoresListView;
        expandableListAdapter = new RoundExpandableAdapter(this, ((RoundGameActivity)getActivity()).getPlayers(), ((RoundGameActivity)getActivity()).getRounds());
        scorelinesExpandableListView.setAdapter(expandableListAdapter);

        // This method is called when the group is expanded
        scorelinesExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // This method is called when the group is collapsed
        scorelinesExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // This method is called when the child in any group is clicked
        // via a toast method, it is shown to display the selected child item as a sample
        // we may need to add further steps according to the requirements
        scorelinesExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        binding.finalScoresbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FragmentRoundGameMain.this)
                        .navigate(R.id.action_FragmentRoundGameMain_to_FragmentFinalScore);
            }
        });
        binding.newRoundbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RoundGameActivity)FragmentRoundGameMain.this.getActivity()).newRound();
                expandableListAdapter.notifyDataSetChanged();
            }
        });
        binding.buttonStartingPoints.setText(""+((RoundGameActivity)getActivity()).getStartingPoints());
        binding.buttonStartingPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartingPointsDialog dialog = new StartingPointsDialog(FragmentRoundGameMain.this);
                dialog.show(getActivity().getSupportFragmentManager(), "StartingPointsDialog");
            }
        });
    }

    @Override
    public Activity getScoreActivity(){
        return getActivity();
    }

    @Override
    public void saveScore(int roundline, Player player, Integer score){
        Round bgsline = ((RoundGameActivity)getActivity()).getRounds().get(roundline);
        bgsline.setScore(((RoundGameActivity)getActivity()).getPlayerIndex(player),score);
        expandableListAdapter.notifyDataSetChanged();
    }


    public void refreshStartingPoints() {
        binding.buttonStartingPoints.setText(""+((RoundGameActivity)getActivity()).getStartingPoints());
    }
}