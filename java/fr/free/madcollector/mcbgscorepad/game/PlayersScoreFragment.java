package fr.free.madcollector.mcbgscorepad.game;

import android.app.Activity;

import fr.free.madcollector.mcbgscorepad.Player;

public interface PlayersScoreFragment {
         Activity getScoreActivity();
         void saveScore(int roundline, Player player, Integer score);
}
