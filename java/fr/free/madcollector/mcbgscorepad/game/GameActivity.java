package fr.free.madcollector.mcbgscorepad.game;

import java.util.ArrayList;

import fr.free.madcollector.mcbgscorepad.Player;

public interface GameActivity {
        ArrayList<Player> getPlayers();
        void addPlayer(Player player);

        void removePlayer(Player player);
        void finish();
        void restart();

        ArrayList<Integer> getFinalScores();
}
