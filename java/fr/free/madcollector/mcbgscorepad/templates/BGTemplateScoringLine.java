package fr.free.madcollector.mcbgscorepad.templates;

import java.util.ArrayList;

public class BGTemplateScoringLine {
    private String name;
    private int color;
    private String iconString;
    private ArrayList<Integer> scores;

    public BGTemplateScoringLine(){
        name = null;
        color = -1;
    }
    public BGTemplateScoringLine(String n){
        name = n;
        color = -1;
        scores  = new ArrayList<Integer>();
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

    public int getColor(){
        return color;
    }

    public void setColor(int color)
    {this.color=color;}

    public String getIconString() {
        return iconString;
    }

    public void setIconString(String iconString) {
        this.iconString = iconString;
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
