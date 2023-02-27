package fr.free.madcollector.mcbgscorepad.templates;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import fr.free.madcollector.mcbgscorepad.ErrorDialog;
import fr.free.madcollector.mcbgscorepad.MainActivity;
import fr.free.madcollector.mcbgscorepad.R;

public class FragmentScorelineEdit extends Fragment {

    private BoardgameTemplate myTemplate;
    private BGTemplateScoringLine myScoreLine = new BGTemplateScoringLine();
    private boolean isEdit = false;
    private ActivityResultLauncher<Intent> iconFileActivityIntent;

    public FragmentScorelineEdit(){
        iconFileActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Add same code that you want to add in onActivityResult method
                        FragmentScorelineEdit.this.setIcon(result.getData().getData());
                    }
                });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.template_fragment_scoreline_edit,
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
                if(((MainActivity)getActivity()).getBgTemplateIO().deleteScoreline(myTemplate,myScoreLine)) {
                   Bundle bundle = new Bundle();
                    bundle.putString("editTemplate_name", myTemplate.getName());
                    NavHostFragment.findNavController(this).navigate(R.id.action_FragmentScorelineEdit_to_FragmentTemplateEdit, bundle);
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
        String actionBarTitle = "";
        myTemplate = ((MainActivity)getActivity()).getBgTemplateIO().getBGTemplate(this.getArguments().getString("editScorelineTemplate_name"));
        myScoreLine = new BGTemplateScoringLine();
        if(this.getArguments().getString("editScoreline_name") != null) {
            isEdit = true;
            BGTemplateScoringLine tempMyScoreLine = ((MainActivity)getActivity()).getBgTemplateIO().getScoreline(myTemplate.getName(), this.getArguments().getString("editScoreline_name"));
            if(tempMyScoreLine != null)
                myScoreLine = tempMyScoreLine;
            else myScoreLine.setName(this.getArguments().getString("editScoreline_name"));
        }
       if(myTemplate!=null)
           actionBarTitle += this.getArguments().getString("editScorelineTemplate_name");
       if(isEdit)
           actionBarTitle += "-"+myScoreLine.getName();
       else actionBarTitle += "-"+getActivity().getString(R.string.add_scoreline);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(actionBarTitle);

        if(isEdit)
            ((EditText) getActivity().findViewById(R.id.newScorelineInputText)).setText(myScoreLine.getName());

        Button colorButton = (Button) getActivity().findViewById(R.id.newScoreColorButton);
        if(myScoreLine.getColor() != -1)
            colorButton.setBackgroundTintList(ColorStateList.valueOf(myScoreLine.getColor()));
        colorButton.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view) {
                ColorPickerDialog picker = new ColorPickerDialog(FragmentScorelineEdit.this);
                picker.show(getActivity().getSupportFragmentManager().beginTransaction(), "ColorPicker");
            }
        });

        ImageView iconview = (ImageView) getActivity().findViewById(R.id.newScoreIcon);
        if(myScoreLine.getIconString()!=null){
            byte[] decodedString = Base64.decode(myScoreLine.getIconString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            iconview.setImageBitmap(decodedByte);
        }
        iconview.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                iconFileActivityIntent.launch(intent);
            }
        });

        Button saveButton = (Button) getActivity().findViewById(R.id.saveScoreline_button);
        if(isEdit)
            saveButton.setText(getActivity().getString(R.string.save));
        else
            saveButton.setText(getActivity().getString(R.string.add));
        saveButton.setOnClickListener(new AdapterView.OnClickListener(){
            public void onClick(View view) {
                boolean isSaveOK = true;
                //Empty name
                if (((EditText) getActivity().findViewById(R.id.newScorelineInputText)).getText().toString().equals("")) {
                    ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_new_scoreline_name_cannot_be_empty));
                    dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
                    isSaveOK = false;
                }
                if(isEdit && (!(((EditText) getActivity().findViewById(R.id.newScorelineInputText)).getText().toString()).equalsIgnoreCase(myScoreLine.getName())))
                //Already exists
                if (((MainActivity) getActivity()).getBgTemplateIO().endGameLineIndex(myTemplate.getName(), ((EditText) getActivity().findViewById(R.id.newScorelineInputText)).getText().toString()) > -1) {
                    ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_new_scoreline_already_exists));
                    dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
                    isSaveOK = false;
                }
                //OK to save
                if (isSaveOK){
                    if (((MainActivity) getActivity()).getBgTemplateIO().saveScoreLine(myTemplate, myScoreLine, ((EditText) getActivity().findViewById(R.id.newScorelineInputText)).getText().toString())) {
                        Bundle bundle = new Bundle();
                        bundle.putString("editTemplate_name", myTemplate.getName());
                        NavHostFragment.findNavController(FragmentScorelineEdit.this).navigate(R.id.action_FragmentScorelineEdit_to_FragmentTemplateEdit, bundle);

                    } else {
                        ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_saving));
                        dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
                    }
                }
            }
        });
    }

    public void changeColorPickerButtonColor (int color)
    {
        myScoreLine.setColor(color);
        Button colorButton = (Button) getActivity().findViewById(R.id.newScoreColorButton);
        colorButton.setBackgroundTintList(ColorStateList.valueOf(myScoreLine.getColor()));
        colorButton.invalidate();
    }

    public void setIcon(Uri dataURI) {
        ImageView iconview = (ImageView) getActivity().findViewById(R.id.newScoreIcon);
        //Save the Icon as a String
        Bitmap selectedImage = null;
        try {
            selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dataURI);
        }
        catch (FileNotFoundException fnf)
        {
            ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_saving));
            dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
        }
        catch (IOException ioe)
        {
            ErrorDialog dialog = new ErrorDialog(getActivity().getString(R.string.error_wrong_file_format));
            dialog.show(getActivity().getSupportFragmentManager().beginTransaction(), "ErrorSaving");
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String strBase64= Base64.encodeToString(byteArray, 0);
        myScoreLine.setIconString(strBase64);
        //Load the new String
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iconview.setImageBitmap(decodedByte);
    }

}