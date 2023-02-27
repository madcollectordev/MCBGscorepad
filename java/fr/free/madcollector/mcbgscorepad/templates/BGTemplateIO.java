package fr.free.madcollector.mcbgscorepad.templates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.content.FileProvider;
import fr.free.madcollector.mcbgscorepad.R;

public class BGTemplateIO {

    private final Activity superActivity;

    public BGTemplateIO(Activity ac)
    {
        superActivity = ac;
    }


    public ArrayList<BoardgameTemplate> getBGTemplateList() {
        ArrayList<BoardgameTemplate> boardgames = new ArrayList<BoardgameTemplate>();
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for (int i = 0; i < boardgameArray.length(); i++) {
                JSONObject boardgame = boardgameArray.getJSONObject(i);
                BoardgameTemplate bgt = new BoardgameTemplate(boardgame.getString("name"));
                boardgames.add(bgt);
            }
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","Error while reading the template list in the database");
        }
        List<BoardgameTemplate> tempList = new ArrayList<>(boardgames);
        Collections.sort(tempList, (s1, s2) ->
        s1.getName().compareToIgnoreCase(s2.getName()));
        boardgames = new ArrayList<>(tempList);
        return boardgames;
    }

    public JSONObject getBGTemplateListJSon()
    {
        String templatesString = superActivity.getSharedPreferences("boardgame_templates",Context.MODE_PRIVATE).getString("boardgame_templates", null);
        //Log.v("MGBGScorepad","Database Content  "+templatesString);
        JSONObject templatesJson;
        try {
            if(templatesString==null) {
                //Log.v("MGBGScorepad","No boardgame yet in the database");
                templatesJson = new JSONObject();
                JSONArray boardgamesTemplates = new JSONArray();
                templatesJson.put("boardgames",boardgamesTemplates);

            }
            else
                templatesJson = new JSONObject(templatesString);
        } catch (JSONException e) {
            Log.v("MGBGScorepad","Error creating JSON Template list: "+e.toString());
            throw new RuntimeException(e);
        }
        return templatesJson;
    }

    public int templateIndex(String templateName)
    {
        int result = -1;
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for (int j = 0; j < boardgameArray.length(); j++) {
                JSONObject boardgame = boardgameArray.getJSONObject(j);
                if (boardgame.getString("name").equalsIgnoreCase(templateName)) {
                    result = j;
                }
            }
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","No boardgame found in the database");
        }
        return result;
    }

    public boolean createBGTemplate(String newTemplateNameString) {
        boolean result = true;
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            JSONObject newBoargame = new JSONObject();
            newBoargame.put("name",newTemplateNameString);
            boardgameArray.put(newBoargame);
            SharedPreferences.Editor e = superActivity.getSharedPreferences("boardgame_templates", Context.MODE_PRIVATE).edit();
            e.putString("boardgame_templates", templatesJson.toString());
            e.commit();
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","Error while inserting the template in the database");
            result = false;
        }
        return result;
    }
    public BoardgameTemplate getBGTemplate(String templateNameString) {
        BoardgameTemplate returnTemplate = null;
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for (int j = 0; j < boardgameArray.length(); j++) {
                JSONObject boardgame = boardgameArray.getJSONObject(j);
                if (boardgame.getString("name").equalsIgnoreCase(templateNameString)) {
                    returnTemplate = new BoardgameTemplate(boardgame.getString("name"));
                    try {
                        JSONArray endScoreLines = boardgame.getJSONArray("endgame_scores");
                        for (int l = 0; l < endScoreLines.length(); l++) {
                            BGTemplateScoringLine sLine = new BGTemplateScoringLine(endScoreLines.getJSONObject(l).getString("name"));
                            try{sLine.setColor(new Integer(endScoreLines.getJSONObject(l).getString("color")));} catch (JSONException e) {}
                            try {sLine.setIconString((endScoreLines.getJSONObject(l).getString("icon"))); } catch (JSONException e) {}
                            returnTemplate.addEndScoreLine(sLine);
                        }
                    }
                    catch (JSONException e) {
                    }
                }
            }
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","Boardgame not found in the database");
        }
        return returnTemplate;
    }


    public boolean deleteTemplates(ArrayList<String> checkedBG) {
        JSONObject bgJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = (JSONArray) bgJson.get("boardgames");
            for (int i = 0; i < checkedBG.size(); i++) {
                for (int j = 0; j < boardgameArray.length(); j++) {
                    JSONObject boardgame = boardgameArray.getJSONObject(j);
                    if (boardgame.getString("name").equalsIgnoreCase(checkedBG.get(i))) {
                        boardgameArray.remove(j);
                    }
                }
                SharedPreferences.Editor e = superActivity.getSharedPreferences("boardgame_templates", Context.MODE_PRIVATE).edit();
                e.putString("boardgame_templates", bgJson.toString());
                e.commit();

            }
        }
        catch (JSONException jse){
            return false;
        }
        return true;
    }

    public int endGameLineIndex(String name, String newScorelineNameString) {
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for (int j = 0; j < boardgameArray.length(); j++) {
                JSONObject boardgame = boardgameArray.getJSONObject(j);
                if (boardgame.getString("name").equalsIgnoreCase(name)) {
                    JSONArray endGameArray = boardgame.getJSONArray("endgame_scores");
                    for(int k=0;k<endGameArray.length();k++){
                        JSONObject scoreline = endGameArray.getJSONObject(k);
                        if(scoreline.getString("name").equalsIgnoreCase(newScorelineNameString))
                            return k;
                    }
                }
            }
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","endGameLineIndex : No boardgame line found in the database");
        }
        return -1;
    }

    public BGTemplateScoringLine getScoreline(String templateName, String scorelineNameString) {
        BGTemplateScoringLine returnScoreLine = null;
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for (int j = 0; j < boardgameArray.length(); j++) {
                JSONObject boardgame = boardgameArray.getJSONObject(j);
                if (boardgame.getString("name").equalsIgnoreCase(templateName)) {
                    JSONArray endGameArray = boardgame.getJSONArray("endgame_scores");
                    for (int k = 0; k < endGameArray.length(); k++) {
                        JSONObject jSonscoreline = endGameArray.getJSONObject(k);
                        if (jSonscoreline.getString("name").equalsIgnoreCase(scorelineNameString)) {
                            returnScoreLine = new BGTemplateScoringLine(jSonscoreline.getString("name"));
                            try {
                                returnScoreLine.setColor(new Integer(jSonscoreline.getString("color")));
                            } catch (JSONException e) {}
                            try {
                                returnScoreLine.setIconString(jSonscoreline.getString("icon"));
                            } catch (JSONException e) {}
                        }
                    }
                }
            }
        }
        catch (JSONException e) {
        }
        return returnScoreLine;
    }

    public boolean saveTemplate(BoardgameTemplate myTemplate) {
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for(int i = 0; i<boardgameArray.length();i++)
            {
                JSONObject tempBoargame = boardgameArray.getJSONObject(i);
                if (tempBoargame.getString("name").equalsIgnoreCase(myTemplate.getName()))
                {

                    try {
                        tempBoargame.remove("endgame_scores");
                        JSONArray endgameScores = new JSONArray();
                        tempBoargame.put("endgame_scores",endgameScores);
                        for (int l = 0; l < myTemplate.getEndGameScoreLines().size(); l++) {
                            JSONObject endGameline = new JSONObject();
                            endGameline.put("name",myTemplate.getEndGameScoreLines().get(l).getName());
                            if(myTemplate.getEndGameScoreLines().get(l).getColor() != -1)
                                endGameline.put("color",myTemplate.getEndGameScoreLines().get(l).getColor());
                            if(myTemplate.getEndGameScoreLines().get(l).getIconString() != null)
                                endGameline.put("icon",myTemplate.getEndGameScoreLines().get(l).getIconString());
                            endgameScores.put(endGameline);
                        }
                    }
                    catch (JSONException e) {
                    }
                    SharedPreferences.Editor e = superActivity.getSharedPreferences("boardgame_templates", Context.MODE_PRIVATE).edit();
                    e.putString("boardgame_templates", templatesJson.toString());
                    e.commit();
                    return true;
                }
            }
            Log.v("MGBGScorepad","Boardgame not found while updating the template in the database");
            return false;
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","Error while updating the template in the database");
            return false;
        }
    }

    public boolean deleteTemplate(BoardgameTemplate myTemplate) {
        JSONObject bgJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = (JSONArray) bgJson.get("boardgames");
            for (int j = 0; j < boardgameArray.length(); j++) {
                JSONObject boardgame = boardgameArray.getJSONObject(j);
                if (boardgame.getString("name").equalsIgnoreCase(myTemplate.getName())) {
                    boardgameArray.remove(j);
                }
            }
            SharedPreferences.Editor e = superActivity.getSharedPreferences("boardgame_templates", Context.MODE_PRIVATE).edit();
            e.putString("boardgame_templates", bgJson.toString());
            e.commit();
        }
        catch (JSONException jse){
            return false;
        }
        return true;
    }

    public boolean saveScoreLine(BoardgameTemplate myTemplate, BGTemplateScoringLine myScoreLine, String newName) {
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for (int j = 0; j < boardgameArray.length(); j++) {
                JSONObject boardgame = boardgameArray.getJSONObject(j);
                if (boardgame.getString("name").equalsIgnoreCase(myTemplate.getName())) {
                    JSONArray endGameArray = new JSONArray();
                    try {
                        endGameArray = boardgame.getJSONArray("endgame_scores");
                    } catch (JSONException e) {
                        boardgame.put("endgame_scores", endGameArray);
                    }
                    boolean isReplace = false;
                    JSONObject endGameline= new JSONObject();
                    for (int k = 0; k < endGameArray.length(); k++) {
                        JSONObject scoreline = endGameArray.getJSONObject(k);
                        if (scoreline.getString("name").equalsIgnoreCase(myScoreLine.getName()))
                        {
                            isReplace = true;
                            endGameline = scoreline;
                        }
                    }
                    if(!isReplace) {
                        endGameArray.put(endGameline);
                    }
                    else{
                        endGameline.remove("name");
                        endGameline.remove("color");
                        endGameline.remove("icon");
                    }
                    endGameline.put("name", newName);
                    if (myScoreLine.getColor() != -1)
                        endGameline.put("color", myScoreLine.getColor());
                    if (myScoreLine.getIconString() != null)
                        endGameline.put("icon", myScoreLine.getIconString());
                    SharedPreferences.Editor e = superActivity.getSharedPreferences("boardgame_templates", Context.MODE_PRIVATE).edit();
                    e.putString("boardgame_templates", templatesJson.toString());
                    e.commit();
                    return true;
                }
            }
            return false;
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","saveScoreLine : No boardgame line found in the database");
            return false;
        }
    }

    public boolean deleteScoreline(BoardgameTemplate myTemplate, BGTemplateScoringLine myScoreLine) {
        JSONObject templatesJson = getBGTemplateListJSon();
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            for (int j = 0; j < boardgameArray.length(); j++) {
                JSONObject boardgame = boardgameArray.getJSONObject(j);
                if (boardgame.getString("name").equalsIgnoreCase(myTemplate.getName())) {
                    JSONArray endGameArray = boardgame.getJSONArray("endgame_scores");
                    for(int k=0;k<endGameArray.length();k++) {
                        JSONObject scoreline = endGameArray.getJSONObject(k);
                        if (scoreline.getString("name").equalsIgnoreCase(myScoreLine.getName()))
                            endGameArray.remove(k);
                    }
                    SharedPreferences.Editor e = superActivity.getSharedPreferences("boardgame_templates", Context.MODE_PRIVATE).edit();
                    e.putString("boardgame_templates", templatesJson.toString());
                    e.commit();
                    return true;
                }
            }
            return false;
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","deleteScoreline : No boardgame line found in the database");
            return false;
        }
    }

    public boolean exportTemplates(ArrayList<String> checkedBG) {
        try {
            JSONObject bgJson2export = new JSONObject();
            JSONArray boardgamesTemplates = new JSONArray();
            bgJson2export.put("boardgames",boardgamesTemplates);

            JSONObject templatesJson = getBGTemplateListJSon();
            JSONArray boardgameArray = (JSONArray) templatesJson.get("boardgames");
            for (int i = 0; i < checkedBG.size(); i++) {
                for (int j = 0; j < boardgameArray.length(); j++) {
                    JSONObject boardgame = boardgameArray.getJSONObject(j);
                    if (boardgame.getString("name").equalsIgnoreCase(checkedBG.get(i))) {
                        boardgamesTemplates.put(boardgame);
                    }
                }
            }
            File path = superActivity.getFilesDir();
            File file = new File(path, "MCBGBoardgames.txt");
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(bgJson2export.toString().getBytes());
            stream.close();

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            Uri fileURI = FileProvider.getUriForFile(superActivity, superActivity.getApplicationContext().getPackageName() + ".provider", file);
            if(file.exists()) {
                intentShareFile.setType("application/json");
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentShareFile.putExtra(Intent.EXTRA_STREAM,  fileURI);
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        "MCBGSC "+superActivity.getResources().getString(R.string.export));
//                intentShareFile.putExtra(Intent.EXTRA_TEXT, superActivity.getResources().getString(R.string.export));
                superActivity.startActivity(Intent.createChooser(intentShareFile, superActivity.getResources().getString(R.string.export)));
            }
        }
        catch (JSONException jse){
            return false;
        } catch (FileNotFoundException e) {
            Log.v("MGBGScorepad","exportTemplates : File not found");
            return false;
        } catch (IOException e) {
            Log.v("MGBGScorepad","exportTemplates : Error reading file");
            return false;
        }
        return true;
    }

    public void importTemplates(Uri returnUri) {
        JSONObject templatesJson = getBGTemplateListJSon();
        String jsonContent = null;
        JSONObject importJson = new JSONObject();
        //Read file to import
        try {
            InputStream in = superActivity.getContentResolver().openInputStream(returnUri);
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            for (String line; (line = r.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            jsonContent = total.toString();
        } catch (FileNotFoundException e) {
            Toast.makeText(superActivity, superActivity.getResources().getString(R.string.fileNotFound), Toast.LENGTH_LONG).show();
            Log.v("MGBGScorepad", "importTemplates : File not found");
            return;
        } catch (IOException e) {
            Toast.makeText(superActivity, superActivity.getResources().getString(R.string.error_reading_file), Toast.LENGTH_LONG).show();
            Log.v("MGBGScorepad", "importTemplates : Error reading file");
            return;
        }
        try {
            importJson = new JSONObject(jsonContent);
        }
        catch (JSONException e) {
            Toast.makeText(superActivity, superActivity.getResources().getString(R.string.invalid_file_format), Toast.LENGTH_LONG).show();
            return;
        }
        //OK import now
        try {
            JSONArray boardgameArray = templatesJson.getJSONArray("boardgames");
            JSONArray importArray = (JSONArray) importJson.get("boardgames");
            for (int i = 0; i < importArray.length(); i++) {
                JSONObject bgJson = (JSONObject) importArray.get(i);
                if(templateIndex(bgJson.getString("name")) == -1)
                    boardgameArray.put(bgJson);
            }
            SharedPreferences.Editor e = superActivity.getSharedPreferences("boardgame_templates", Context.MODE_PRIVATE).edit();
            e.putString("boardgame_templates", templatesJson.toString());
            e.commit();
        }
        catch (JSONException e) {
            Log.v("MGBGScorepad","importTemplates: Error while updating the template in the database");
            return;
        }
    }
}