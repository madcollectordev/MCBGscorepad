package fr.free.madcollector.mcbgscorepad.scoresheetgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import fr.free.madcollector.mcbgscorepad.Player;
import fr.free.madcollector.mcbgscorepad.R;
import fr.free.madcollector.mcbgscorepad.databinding.ScoresheetActivityMainBinding;
import fr.free.madcollector.mcbgscorepad.game.GameActivity;
import fr.free.madcollector.mcbgscorepad.templates.BGTemplateIO;
import fr.free.madcollector.mcbgscorepad.templates.BGTemplateScoringLine;
import fr.free.madcollector.mcbgscorepad.templates.BoardgameTemplate;

public class ScoresheetGameActivity extends AppCompatActivity implements GameActivity {

    private AppBarConfiguration appBarConfiguration;
    private BoardgameTemplate myTemplate;
    private ArrayList<Player> players;
    private ArrayList<Integer> finalScore = new ArrayList<Integer>();

    public ScoresheetGameActivity(){
        super();
        myTemplate = new BoardgameTemplate(null);
        players = new ArrayList<Player>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            myTemplate = (new BGTemplateIO(this)).getBGTemplate(extras.getString("newgame_template_name"));
            if(extras.getString("players")!= null) {
                try {
                    JSONObject playersJson = new JSONObject(extras.getString("players"));
                    JSONArray playersJsonArray = playersJson.getJSONArray("players");
                    for (int i=0;i<playersJsonArray.length();i++)
                    {
                        Player newPlayer = new Player(playersJsonArray.getJSONObject(i).getString("name"));
                        ScoresheetGameActivity.this.addPlayer(newPlayer);
                    }

                } catch (JSONException e) {
                }
            }
        }
        super.onCreate(savedInstanceState);
        ScoresheetActivityMainBinding binding = ScoresheetActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.gametoolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_scoresheet_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        getSupportActionBar().setTitle(myTemplate.getName());
    }
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_scoresheet_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public BoardgameTemplate getTemplate(){
        return myTemplate;
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return players;
    }
    @Override
    public void addPlayer(Player newPlayer){
        players.add(newPlayer);
        finalScore.add(null);
        for(int indexTemplate = 0; indexTemplate<myTemplate.getEndGameScoreLines().size();indexTemplate++) {
            myTemplate.getEndGameScoreLines().get(indexTemplate).newPlayer();
        }
    }

    @Override
    public void removePlayer(Player player){
        for(int i = 0; i<players.size();i++)
        {
            if(players.get(i).getName().equals(player.getName())) {
                players.remove(i);
                finalScore.remove(i);
                for(int indexTemplate = 0; indexTemplate<myTemplate.getEndGameScoreLines().size();indexTemplate++) {
                    myTemplate.getEndGameScoreLines().get(indexTemplate).popPlayer(i);
                }
            }
        }
    }

    public int getPlayerIndex(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(player.getName())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void restart(){
        JSONObject playersJson = new JSONObject();
        JSONArray tempPlayerJsonArray = new JSONArray();
        try {
        playersJson.put("players",tempPlayerJsonArray);
        for(int i = 0; i<players.size();i++) {
            JSONObject tempPlayerJson = new JSONObject();
            tempPlayerJson.put("name",players.get(i).getName());
            tempPlayerJsonArray.put(tempPlayerJson);
            }
        }
        catch (JSONException e) {
        }
        Intent intentMain = new Intent(this , ScoresheetGameActivity.class);
        intentMain.putExtra("newgame_template_name", myTemplate.getName());
        intentMain.putExtra("players", playersJson.toString());
        startActivity(intentMain);
        this.finish();
    }

    @Override
    public ArrayList<Integer> getFinalScores(){
        for(int i = 0; i<players.size();i++)
        {
            int playerTotal = 0;
            for(int j = 0;j<myTemplate.getEndGameScoreLines().size();j++)
            {
                BGTemplateScoringLine bgsline = myTemplate.getEndGameScoreLines().get(j);
                if(bgsline.getScores().get(i)!=null)
                    playerTotal += bgsline.getScores().get(i);
            }
            if(finalScore.get(i)!=null)
                finalScore.remove(i);
            finalScore.add(i,new Integer(playerTotal));
        }
        return finalScore;
    }
}