package fr.free.madcollector.mcbgscorepad.roundbasedgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import fr.free.madcollector.mcbgscorepad.R;

public class StartingPointsDialog extends DialogFragment {

    private RoundGameActivity parentActivity;
    private FragmentRoundGameMain myFragment;

    public StartingPointsDialog(FragmentRoundGameMain fragment)
    {
        myFragment = fragment;
        parentActivity = (RoundGameActivity) myFragment.getActivity();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            parentActivity = (RoundGameActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.roundbased_startingpoints_input_dialog, null))
                .setTitle(R.string.starting_points)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StartingPointsDialog.this.dismiss();
                    }
                })
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
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
            Button positiveButton = (Button) myDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    EditText newStartingPointsEdit = (EditText) myDialog.findViewById(R.id.startingScoreInputText);
                    String startingValue = newStartingPointsEdit.getText().toString();
                    Integer intScoreValue;
                    try{
                        intScoreValue = new Integer(startingValue);
                    }
                    catch (NumberFormatException  e){
                        return;
                    }
                    parentActivity.setStartingPoints(intScoreValue.intValue());
                    myFragment.refreshStartingPoints();
                    StartingPointsDialog.this.dismiss();
                }
            });
        }
    }

}