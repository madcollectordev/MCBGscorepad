package fr.free.madcollector.mcbgscorepad.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;

public class NewPlayerDialog extends DialogFragment {

    private FragmentManagePlayers parentFragment;

    public NewPlayerDialog(FragmentManagePlayers parent){
        parentFragment = parent;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.players_newplayer_dialog, null))
                .setTitle(R.string.new_player_name)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewPlayerDialog.this.dismiss();
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
                    boolean wantToCloseDialog = true;
                    EditText newPlayerName = (EditText) myDialog.findViewById(R.id.newPlayerInputText);
                    String newPlayerNameString = newPlayerName.getText().toString();
                    if(newPlayerNameString.equals("")) {
                        ((TextView) myDialog.findViewById(R.id.newPlayerErrors)).setText(R.string.error_new_player_name_cannot_be_empty);
                        wantToCloseDialog = false;
                        return;
                    }
                    ((GameActivity)getActivity()).addPlayer(new Player(newPlayerNameString));
                    parentFragment.refreshPlayerList();
                    if(wantToCloseDialog)
                        dismiss();
                }
            });
        }
    }
}