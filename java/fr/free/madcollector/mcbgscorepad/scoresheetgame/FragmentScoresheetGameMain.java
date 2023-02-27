package fr.free.madcollector.mcbgscorepad.scoresheetgame;

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
import fr.free.madcollector.mcbgscorepad.databinding.ScoresheetFragmentMainExpandableBinding;
import fr.free.madcollector.mcbgscorepad.game.PlayersScoreFragment;
import fr.free.madcollector.mcbgscorepad.game.QuitGameDialog;
import fr.free.madcollector.mcbgscorepad.templates.BGTemplateScoringLine;

public class FragmentScoresheetGameMain extends Fragment implements PlayersScoreFragment {

    private ScoresheetFragmentMainExpandableBinding binding;
    private ScorelineExpandableAdapter expandableListAdapter;

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
                NavHostFragment.findNavController(this).navigate(R.id.action_FragmentScoresheetGameMain_to_FragmentManagePlayers);
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
        binding = ScoresheetFragmentMainExpandableBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExpandableListView  scorelinesExpandableListView= binding.expandableScoresListView;
        expandableListAdapter = new ScorelineExpandableAdapter(this, ((ScoresheetGameActivity)getActivity()).getPlayers(), ((ScoresheetGameActivity)getActivity()).getTemplate().getEndGameScoreLines());
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
                NavHostFragment.findNavController(FragmentScoresheetGameMain.this)
                        .navigate(R.id.action_FragmentScoresheetGameMain_to_FragmentFinalScore);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
    super.onResume();
    ((ScoresheetGameActivity) getActivity()).getSupportActionBar().setTitle(((ScoresheetGameActivity) getActivity()).getTemplate().getName());
    }
    @Override
    public Activity getScoreActivity(){
        return getActivity();
    }

    @Override
    public void saveScore(int roundline, Player player, Integer score){
        BGTemplateScoringLine bgsline = ((ScoresheetGameActivity)getActivity()).getTemplate().getEndGameScoreLines().get(roundline);
        bgsline.setScore(((ScoresheetGameActivity)getActivity()).getPlayerIndex(player),score);
        expandableListAdapter.notifyDataSetChanged();
    }
}