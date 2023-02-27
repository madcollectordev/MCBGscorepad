package fr.free.madcollector.mcbgscorepad.templates;

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
import fr.free.madcollector.mcbgscorepad.MainActivity;
import fr.free.madcollector.mcbgscorepad.R;

public class NewTemplateDialog extends DialogFragment {

    private MainActivity parentActivity;
    private FragmentTemplateList parentFragment;

    public NewTemplateDialog(FragmentTemplateList parent){
        parentFragment = parent;
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
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.template_newtemplate_dialog, null))
                .setTitle(R.string.new_template_name)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewTemplateDialog.this.dismiss();
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
                    ((TextView) myDialog.findViewById(R.id.newTemplateErrors)).setText("");
                    EditText newTemplateName = (EditText) myDialog.findViewById(R.id.newTemplateInputText);
                    String newTemplateNameString = newTemplateName.getText().toString();
                    if(newTemplateNameString.equals("")) {
                        ((TextView) myDialog.findViewById(R.id.newTemplateErrors)).setText(R.string.error_new_template_name_cannot_be_empty);
                        wantToCloseDialog = false;
                        return;
                    }
                    int templateIndex = parentActivity.getBgTemplateIO().templateIndex(newTemplateNameString);
                    if(templateIndex > -1){
                        ((TextView) myDialog.findViewById(R.id.newTemplateErrors)).setText(R.string.error_new_template_already_exists);
                        wantToCloseDialog = false;
                        return;
                    }
                    boolean creationTemplateOK = parentActivity.getBgTemplateIO().createBGTemplate(newTemplateNameString);
                    if(!creationTemplateOK){
                        ((TextView) myDialog.findViewById(R.id.newTemplateErrors)).setText(R.string.error_new_template_saving);
                        wantToCloseDialog = false;
                        return;
                    }
                    parentFragment.createTemplateList();
                    wantToCloseDialog = true;
                    if(wantToCloseDialog)
                        dismiss();
                }
            });
        }
    }

}