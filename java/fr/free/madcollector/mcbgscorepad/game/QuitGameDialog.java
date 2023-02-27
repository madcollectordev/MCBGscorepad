package fr.free.madcollector.mcbgscorepad.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import fr.free.madcollector.mcbgscorepad.R;

public class QuitGameDialog extends DialogFragment {

    private GameActivity parentActivity;
    private boolean quit = true;

    public QuitGameDialog(boolean quit)
    {
        this.quit = quit;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            parentActivity = (GameActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.quit_game)
                .setMessage(R.string.quit_game_warning)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        QuitGameDialog.this.dismiss();
                    }
                })
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(quit)
                            parentActivity.finish();
                        else parentActivity.restart();
                    }
                });
        return builder.create();
    }

}