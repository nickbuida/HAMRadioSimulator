package edu.augustana.Bots;

import swiss.ameri.gemini.api.*;
import swiss.ameri.gemini.spi.JsonParser;
import swiss.ameri.gemini.gson.GsonJsonParser;
//import java.lang.Record


public class AIBot extends Bot{

    private String systemPromptText;
    private JsonParser parser;
    GenAi genAi;

    public AIBot(int band, String name, String callSign, String systemPromptText) {
        super(band);
        setName(name);
        setTextCallSign(callSign);
        setBehaviorType(new AIPlaying(this));
        this.systemPromptText = systemPromptText;
        this.genAi = new GenAi("AIzaSyBdtDb5rL_wP8aXegcVpRo-bZIWjalQNQw", new GsonJsonParser());
    }


    @Override
    public void playSound() {

    }

    @Override
    public void stopSound() {

    }


    public void talkTo(String userMorseMessage) {

    }

    public void randomMessage() {

    }

    public GenAi getGenAi() {
        return genAi;
    }

    public String getSystemPromptText() {
        return systemPromptText;
    }


//    public static void main(String[] args) {
//        AIBot aiBot = new AIBot(1, "AI Bot", "AI", "System Prompt");
//        aiBot.requestMessage("What's the best Christmas song?");
//        try {
//            // Wait for the asynchronous task to complete
//            Thread.sleep(5000); // Adjust the duration as needed
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

}

