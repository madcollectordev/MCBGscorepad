package fr.free.madcollector.mcbgscorepad.templates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.free.madcollector.mcbgscorepad.ErrorDialog;
import fr.free.madcollector.mcbgscorepad.MainActivity;
import fr.free.madcollector.mcbgscorepad.R;

public class FragmentTemplateEdit extends Fragment {
    private BoardgameTemplate myTemplate;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
       View view = inflater.inflate(R.layout.template_fragment_template_edit,
                container, false);
        setHasOptionsMenu(true);
        return view;

        }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if(((MainActivity)getActivity()).getBgTemplateIO().deleteTemplate(myTemplate)) {
                    NavHostFragment.findNavController(this).navigate(R.id.action_FragmentTemplateEdit_to_FragmentTemplateList);
                }
                else {
                    ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_saving));
                    dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(this.getArguments().getString("editTemplate_name"));
        myTemplate = ((MainActivity)getActivity()).getBgTemplateIO().getBGTemplate(this.getArguments().getString("editTemplate_name"));
        if(myTemplate!=null)
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(myTemplate.getName());

        //End of Game
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.endGameEditListView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        BGTemplateScoringLineCardAdapter adapter = new BGTemplateScoringLineCardAdapter(this);
        adapter.setDataList(myTemplate.getEndGameScoreLines());
        ItemTouchHelper.Callback callback = new BGTemplateScoringLineMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);

        //Save button
        /*
        Button mySaveButton = (Button) getActivity().findViewById(R.id.savetemplate_button);
        mySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).getBgTemplateIO().saveTemplate(myTemplate);
            }
        });
         */
        // floating action button
        ((FloatingActionButton) getActivity().findViewById(R.id.newScorelineButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("editScorelineTemplate_name", myTemplate.getName());
                NavHostFragment.findNavController(FragmentTemplateEdit.this).navigate(R.id.action_FragmentTemplateEdit_to_FragmentScorelineEdit, bundle);
            }
        });

    }

    public BoardgameTemplate getTemplate(){
        return myTemplate;
    }
}
