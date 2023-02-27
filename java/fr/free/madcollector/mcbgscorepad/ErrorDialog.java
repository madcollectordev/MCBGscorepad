package fr.free.madcollector.mcbgscorepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ErrorDialog extends DialogFragment {

    private MainActivity parentActivity;
    private String myMessage;

    public ErrorDialog(String message) {
        myMessage = message;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            parentActivity = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error)
                .setMessage(myMessage)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

}