package fr.free.madcollector.mcbgscorepad.roundbasedgame;

import java.util.ArrayList;

public class Round {
        private String name;
        private ArrayList<Integer> scores;

        public Round(String name){

            this.name = name;
            scores = new ArrayList<Integer>();
        }

    public String getName(){
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String toString() {
        return name;
    }

    public void newPlayer(){
        scores.add(null);
    }
    public void popPlayer(int position)
    {
        scores.remove(position);
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }

    public void setScore(int index, Integer score) {
        scores.remove(index);
        scores.add(index,score);
    }

}
