package fr.free.madcollector.mcbgscorepad.templates;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import fr.free.madcollector.mcbgscorepad.ErrorDialog;
import fr.free.madcollector.mcbgscorepad.MainActivity;
import fr.free.madcollector.mcbgscorepad.R;

public class FragmentTemplateList extends Fragment {
    private ArrayList<BoardgameTemplate> bGTemplatesList;
    private ListView bgTemplatesListview;
    private boolean isSelectionMode = false;
    private Menu optionsMenu;
    private boolean isEnabledBinButton = false;
    private ActivityResultLauncher<Intent> iconFileActivityIntent;

    public FragmentTemplateList(){
        iconFileActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Add same code that you want to add in onActivityResult method
                        Uri returnUri = result.getData().getData();
                        ((MainActivity)getActivity()).getBgTemplateIO().importTemplates(returnUri);
                        createTemplateList();
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isSelectionMode)
                {
                    toggleDisplayList();
                }
                else NavHostFragment.findNavController(FragmentTemplateList.this).navigate(R.id.action_FragmentTemplateList_to_FragmentMain, savedInstanceState);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        optionsMenu = menu;
        inflater.inflate(R.menu.menu_delete_icon, optionsMenu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.bin_toolbar);
        if(item!=null) {
            item.setEnabled(isEnabledBinButton);
            item.setVisible(isEnabledBinButton);
        }
        MenuItem item2=menu.findItem(R.id.bin_export);
        if(item2!=null) {
            item2.setEnabled(isEnabledBinButton);
            item2.setVisible(isEnabledBinButton);
        }
        MenuItem item3=menu.findItem(R.id.bin_import);
        if(item3!=null) {
            item3.setEnabled(!isEnabledBinButton);
            item3.setVisible(!isEnabledBinButton);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.template_fragment_template_list,
                container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createTemplateList();
        ((FloatingActionButton) getActivity().findViewById(R.id.newTemplateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewTemplateDialog dialog = new NewTemplateDialog(FragmentTemplateList.this);
                dialog.show(getActivity().getSupportFragmentManager(), "NewTemplateDialog");

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bgTemplatesListview = null;
        bGTemplatesList = null;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bin_toolbar:
                deleteSelectedTemplate();
                return true;
            case R.id.bin_export:
                exportSelectedTemplate();
                return true;
            case R.id.bin_import:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                iconFileActivityIntent.launch(intent);
                return true;
            case android.R.id.home:
                if(isSelectionMode){
                    toggleDisplayList();
                    return true;
                }
                else {
                    NavHostFragment.findNavController(this).navigate(R.id.action_FragmentTemplateList_to_FragmentMain);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        createTemplateList();
        ((FloatingActionButton) getActivity().findViewById(R.id.newTemplateButton)).show();
    }

    public void deleteSelectedTemplate(){
        BgTemplateSelectionToggleAdapter mybgTemplatesListAdapter = (BgTemplateSelectionToggleAdapter) bgTemplatesListview.getAdapter();
        if(((MainActivity)getActivity()).getBgTemplateIO().deleteTemplates(mybgTemplatesListAdapter.getCheckedBG())) {
            bGTemplatesList = ((MainActivity)getActivity()).getBgTemplateIO().getBGTemplateList();
            createTemplateList();
        }
        else {
            ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_saving));
            dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
        }
    }
    public void exportSelectedTemplate(){
        BgTemplateSelectionToggleAdapter mybgTemplatesListAdapter = (BgTemplateSelectionToggleAdapter) bgTemplatesListview.getAdapter();
        if(((MainActivity)getActivity()).getBgTemplateIO().exportTemplates(mybgTemplatesListAdapter.getCheckedBG())) {
        }
        else {
            ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_saving));
            dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
        }
    }
    public void createTemplateList(){
        ((FloatingActionButton) getActivity().findViewById(R.id.newTemplateButton)).show();
        isSelectionMode= false;
        setEnableDelete(false);
        bGTemplatesList = ((MainActivity)getActivity()).getBgTemplateIO().getBGTemplateList();
        bgTemplatesListview = (ListView) getActivity().findViewById(R.id.templatesListView);
        bgTemplatesListview.setAdapter(null);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.manage_scorepads);
        BgTemplateSelectionToggleAdapter mybgTemplatesListAdapter = new BgTemplateSelectionToggleAdapter(getActivity(),R.layout.template_boardgame_templates_list_selection, R.id.selectedBGTemplatedrawer_item1, bGTemplatesList,this);
        bgTemplatesListview.setAdapter(mybgTemplatesListAdapter);
        mybgTemplatesListAdapter.notifyDataSetChanged();
    }

    public void displayTemplateSelectionList(int position) {
        isSelectionMode = true;
        bgTemplatesListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ((FloatingActionButton) getActivity().findViewById(R.id.newTemplateButton)).hide();
        BgTemplateSelectionToggleAdapter mybgTemplatesListAdapter = (BgTemplateSelectionToggleAdapter) bgTemplatesListview.getAdapter();
        mybgTemplatesListAdapter.resetheckedBG();
        mybgTemplatesListAdapter.addCheckedBG(((BoardgameTemplate)bGTemplatesList.get(position)).getName());
        mybgTemplatesListAdapter.setAppTitle();
        mybgTemplatesListAdapter.notifyDataSetChanged();
    }

    public void toggleDisplayList(){
        isSelectionMode= false;
        setEnableDelete(false);
        ((FloatingActionButton) getActivity().findViewById(R.id.newTemplateButton)).show();
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.manage_scorepads);
        BgTemplateSelectionToggleAdapter mybgTemplatesListAdapter = (BgTemplateSelectionToggleAdapter) bgTemplatesListview.getAdapter();
        mybgTemplatesListAdapter.resetheckedBG();
        mybgTemplatesListAdapter.notifyDataSetChanged();
    }

    public void setEnableDelete(boolean decision)
    {
        isEnabledBinButton = decision;
        ((MainActivity)getActivity()).supportInvalidateOptionsMenu();
    }

    public boolean getSelectionMode()
    {
        return isSelectionMode;
    }
}