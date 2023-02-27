package fr.free.madcollector.mcbgscorepad.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.DialogFragment;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;

public class ScoreInputDialog extends DialogFragment {

    private GameActivity parentActivity;
    private PlayersScoreFragment myFragment;
    private Player myPlayer;
    private Integer roundLine;
    private Integer currentScore;

    public ScoreInputDialog(PlayersScoreFragment fragment, Integer roundLineIndex, Player player, Integer currentScore)
    {
        myFragment = fragment;
        myPlayer = player;
        this.roundLine = roundLineIndex;
        this.currentScore = currentScore;
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
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.score_input_dialog, null))
                .setTitle(R.string.input_score)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ScoreInputDialog.this.dismiss();
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
            EditText newScoreEdit = (EditText) myDialog.findViewById(R.id.scoreInputText);
            if(currentScore != null)
                newScoreEdit.setText(""+currentScore);
            ImageView calculatorButton = (ImageView) myDialog.findViewById(R.id.calculatorIcon);
            ArrayList<HashMap<String,Object>> items =new ArrayList<HashMap<String,Object>>();
            final PackageManager pm = myFragment.getScoreActivity().getPackageManager();
            List<PackageInfo> packs = pm.getInstalledPackages(0);
            for (PackageInfo pi : packs) {
                if( pi.packageName.toString().toLowerCase().contains("calcul")){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("appName", pi.applicationInfo.loadLabel(pm));
                    map.put("packageName", pi.packageName);
                    items.add(map);
                }
            }
            if(items.size()<1){
                calculatorButton.setVisibility(View.GONE);
            }
            calculatorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(items.size()>=1){
                        String packageName = (String) items.get(0).get("packageName");
                        Intent i = pm.getLaunchIntentForPackage(packageName);
                        if (i != null)
                            startActivity(i);
                    }
                }
            });
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    boolean wantToCloseDialog = true;
                    EditText newScoreEdit = (EditText) myDialog.findViewById(R.id.scoreInputText);
                    String scoreValue = newScoreEdit.getText().toString();
                    if(scoreValue.equals("")) {
                        ((TextView) myDialog.findViewById(R.id.scoreErrors)).setText(R.string.error_value_cannot_be_empty);
                        wantToCloseDialog = false;
                        return;
                    }
                    Integer intScoreValue;
                    try{
                        intScoreValue = new Integer(scoreValue);
                    }
                    catch (NumberFormatException  e){
                        ((TextView) myDialog.findViewById(R.id.scoreErrors)).setText(R.string.error_invalid_score_format);
                        wantToCloseDialog = false;
                        return;
                    }
                    myFragment.saveScore(roundLine,myPlayer,intScoreValue);
                    if(wantToCloseDialog)
                        dismiss();
                }
            });
        }
    }

}