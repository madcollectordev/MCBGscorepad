package fr.free.madcollector.mcbgscorepad.templates;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.navigation.fragment.NavHostFragment;
import fr.free.madcollector.mcbgscorepad.MainActivity;
import fr.free.madcollector.mcbgscorepad.R;

public class BgTemplateSelectionToggleAdapter  extends ArrayAdapter<BoardgameTemplate> {

    private ArrayList<String> checkedBG;
    private FragmentTemplateList myFragment;

    public BgTemplateSelectionToggleAdapter(Context context, int layoutId, int viewResourceId, ArrayList data, FragmentTemplateList frag){
        super(context,layoutId,viewResourceId,data);
        checkedBG = new ArrayList<String>();
        myFragment = frag;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View row = super.getView(position, convertView, parent);
        CheckBox cBox=(CheckBox)row.findViewById(R.id.checkBoxBGTemplate);
        TextView label=(TextView) row.findViewById(R.id.selectedBGTemplatedrawer_item1);
        cBox.setChecked(false);
        for(int i=0;i<checkedBG.size();i++)
        {
            if(checkedBG.get(i).equalsIgnoreCase(label.getText().toString()))
                cBox.setChecked(true);
        }
        if(!myFragment.getSelectionMode()){
            cBox.setVisibility(View.GONE);
            row.setOnClickListener(new AdapterView.OnClickListener(){
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("editTemplate_name", label.getText().toString());
                    NavHostFragment.findNavController(myFragment).navigate(R.id.action_FragmentTemplateList_to_FragmentTemplateEdit, bundle);
                }
            });
            row.setOnLongClickListener(new AdapterView.OnLongClickListener(){
                public boolean onLongClick(View view) {
                    if(!myFragment.getSelectionMode()) {
                        myFragment.displayTemplateSelectionList(position);
                    }
                    return true;
                }
            });
        }
        else {
            cBox.setVisibility(View.VISIBLE);
            cBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (cBox.isChecked()) {
                        boolean found = false;
                        for (int i = 0; i < checkedBG.size(); i++) {
                            if (checkedBG.get(i).equalsIgnoreCase(label.getText().toString()))
                                found = true;
                        }
                        if (!found) {
                            checkedBG.add(label.getText().toString());
                        }
                    } else {
                        for (int i = 0; i < checkedBG.size(); i++) {
                            if (checkedBG.get(i).equalsIgnoreCase(label.getText().toString())) {
                                checkedBG.remove(i);
                            }
                        }
                    }
                    setAppTitle();
                }
            });
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cBox.setChecked(!cBox.isChecked());
                    cBox.callOnClick();
                }
            });

        }
        return row;
    }

    public ArrayList<String> getCheckedBG() {
        return checkedBG;
    }

    public int getSelectionSize() {
        return checkedBG.size();
    }


    public void addCheckedBG(String s)
    {
        checkedBG.add(s);
    }

    public void setAppTitle()
    {
        switch(getSelectionSize())
        {
            case 0:
                ((MainActivity)myFragment.getActivity()).getSupportActionBar().setTitle(R.string.no_selection);
                break;
            case 1:
                ((MainActivity)myFragment.getActivity()).getSupportActionBar().setTitle("1 "+((MainActivity)myFragment.getActivity()).getString(R.string.one_selected));
                break;
            default:
                ((MainActivity)myFragment.getActivity()).getSupportActionBar().setTitle(getSelectionSize()+" "+((MainActivity)myFragment.getActivity()).getString(R.string.n_selected));
                break;
        }
        switch (getSelectionSize())
        {
            case 0:
                myFragment.setEnableDelete(false);
                break;
            default:
                myFragment.setEnableDelete(true);
                break;
        }

    }

    public void resetheckedBG() {
        checkedBG.clear();
    }

}
