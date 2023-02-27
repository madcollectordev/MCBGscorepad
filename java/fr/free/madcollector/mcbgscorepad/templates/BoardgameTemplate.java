package fr.free.madcollector.mcbgscorepad.templates;

import java.util.ArrayList;

public class BoardgameTemplate {
    private String name;
    private ArrayList<BGTemplateScoringLine> endGameLines;


    public BoardgameTemplate(String bgname)
    {

        name = bgname;
        endGameLines = new ArrayList<BGTemplateScoringLine>();
    }

    public String toString()
    {
        return name;
    }

    public String getName() {
        return name;
    }

    public void addEndScoreLine(BGTemplateScoringLine sl)
    {
        endGameLines.add(sl);
    }
    public ArrayList<BGTemplateScoringLine> getEndGameScoreLines() {
        return endGameLines;
    }

    public BGTemplateScoringLine getScoreLineByName(String name)
    {
        for(int i = 0; i<endGameLines.size();i++)
            if(endGameLines.get(i).getName().equalsIgnoreCase(name))
                return  endGameLines.get(i);
        return null;
    }
}
