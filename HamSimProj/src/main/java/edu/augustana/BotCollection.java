package edu.augustana;

import edu.augustana.Bots.Bot;

import java.util.ArrayList;

public class BotCollection {

    //Replace this with appropriate classes
    private ArrayList<Bot> botCollection;

    public BotCollection(ArrayList<Bot> botList){
        botCollection = botList;
    }

    public void addBot(Bot bot){
        botCollection.add(bot);
    }

    public ArrayList<Bot> getBots(){
        return botCollection;
    }

    public void deleteBot(Bot bot){
        botCollection.remove(bot);
    }

}
