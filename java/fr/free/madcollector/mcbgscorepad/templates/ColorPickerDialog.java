package fr.free.madcollector.mcbgscorepad.templates;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import fr.free.madcollector.mcbgscorepad.R;

public class ColorPickerDialog extends DialogFragment {

    private int[] colors;
    private FragmentScorelineEdit myFragment;

    public ColorPickerDialog(FragmentScorelineEdit  fragment){
        myFragment = fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.template_colorpicker_dialog, null))
                .setTitle(R.string.choose_color)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ColorPickerDialog.this.dismiss();
                    }
                });
        return builder.create();

    }

    @Override
    public void onStart()
    {
        super.onStart();
        AlertDialog myDialog = (AlertDialog)getDialog();
        if(myDialog != null)
        {
            colors = getContext().getResources().getIntArray(R.array.scorelinescolors);
            ListView myColorListView = (ListView) myDialog.findViewById(R.id.colorPickerColorsView);
            ArrayAdapter myAdapter = new ArrayAdapter(getActivity(),R.layout.template_colorpicker_line,R.id.color_name){
                @Override
                public View getView(final int position, final View convertView, ViewGroup parent) {
                    View row = super.getView(position, convertView, parent);
                    TextView colorButton = (TextView) row.findViewById(R.id.color_name);
                    colorButton.setText("");
                    colorButton.setBackgroundColor(colors[position]);
                    colorButton.setOnClickListener(new AdapterView.OnClickListener(){
                        public void onClick(View view) {
                            myFragment.changeColorPickerButtonColor(colors[position]);
                            ColorPickerDialog.this.dismiss();
                        }
                    });
                    return row;
                }
            };
            myColorListView.setAdapter(myAdapter);
            int[] colors = getContext().getResources().getIntArray(R.array.scorelinescolors);
            for(int i =0;i<colors.length;i++)
            {
                myAdapter.add(colors[i]);
            }
            myAdapter.notifyDataSetChanged();
        }
    }
}
