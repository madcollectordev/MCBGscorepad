package fr.free.madcollector.mcbgscorepad.roundbasedgame;

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
import fr.free.madcollector.mcbgscorepad.databinding.RoundbasedActivityMainBinding;
import fr.free.madcollector.mcbgscorepad.game.GameActivity;

public class RoundGameActivity extends AppCompatActivity implements GameActivity {

    private AppBarConfiguration appBarConfiguration;
    private int startingPoints = 0;
    private ArrayList<Round> rounds;
    private ArrayList<Player> players;
    private ArrayList<Integer> finalScore = new ArrayList<Integer>();

    public RoundGameActivity(){
        super();
        rounds = new ArrayList<Round>();
        players = new ArrayList<Player>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            Bundle extras = this.getIntent().getExtras();
            if (extras != null) {
                try{
                    startingPoints = new Integer(extras.getString("starting_points")).intValue();
                }
                catch (NumberFormatException nfe){}
                if(extras.getString("players")!= null) {
                    try {
                        JSONObject playersJson = new JSONObject(extras.getString("players"));
                        JSONArray playersJsonArray = playersJson.getJSONArray("players");
                        for (int i=0;i<playersJsonArray.length();i++)
                        {
                            Player newPlayer = new Player(playersJsonArray.getJSONObject(i).getString("name"));
                            RoundGameActivity.this.addPlayer(newPlayer);
                        }

                    } catch (JSONException e) {
                    }
                }
            }
        super.onCreate(savedInstanceState);
        RoundbasedActivityMainBinding binding = RoundbasedActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.roundgametoolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_roundbased_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_roundbased_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setStartingPoints(int points)
    {
        startingPoints = points;
    }

    public int getStartingPoints() {
        return startingPoints;
    }

    public ArrayList<Round> getRounds(){
        return rounds;
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return players;
    }
    @Override
    public void addPlayer(Player newPlayer){
        players.add(newPlayer);
        finalScore.add(null);
        for(int indexTemplate = 0; indexTemplate<rounds.size();indexTemplate++) {
            rounds.get(indexTemplate).newPlayer();
        }
    }

    @Override
    public void removePlayer(Player player){
        for(int i = 0; i<players.size();i++)
        {
            if(players.get(i).getName().equals(player.getName())) {
                players.remove(i);
                finalScore.remove(i);
                for(int indexTemplate = 0; indexTemplate < rounds.size();indexTemplate++) {
                    rounds.get(indexTemplate).popPlayer(i);
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
        Intent intentMain = new Intent(this , RoundGameActivity.class);
        intentMain.putExtra("starting_points", startingPoints);
        intentMain.putExtra("players", playersJson.toString());
        startActivity(intentMain);
        this.finish();
    }

    @Override
    public ArrayList<Integer> getFinalScores(){
        for(int i = 0; i<players.size();i++)
        {
            int playerTotal = startingPoints;
            for(int j = 0;j<rounds.size();j++)
            {
                Round myRound = rounds.get(j);
                if(myRound.getScores().get(i)!=null)
                    playerTotal += myRound.getScores().get(i);
            }
            if(finalScore.get(i)!=null)
                finalScore.remove(i);
            finalScore.add(i,new Integer(playerTotal));
        }
        return finalScore;
    }

    public void newRound() {
        int index = rounds.size();
        rounds.add(index, new Round(""+(index+1)));
        for(int i = 0; i<players.size();i++){
            rounds.get(index).newPlayer();
        }
    }
}