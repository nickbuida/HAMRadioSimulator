package edu.augustana;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import edu.augustana.Bots.AIBot;
import edu.augustana.Bots.Bot;
import edu.augustana.Bots.ResponsiveBot;
import edu.augustana.UI.SandboxController;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimScenario {

    @Expose
    private String description ;

    @Expose
    private int numBots;

    @Expose
    private String scenarioName;

    @Expose
    private RadioEnvironment environment;

    @Expose
    private BotCollection botCollection;

    @Expose
    private String scenarioType; //Either a Responsive scenario or an AI scenario

    public boolean isPlaying;

    private transient SandboxController parentController;


    public SimScenario(String name, String description, RadioEnvironment environment, BotCollection botCollection, String type){
        this.scenarioName = name;
        this.description = description;
        this.environment = environment;
        this.botCollection = botCollection;
        scenarioType = type;
        isPlaying = false;
    }

    public static SimScenario getDefaultScenario(){

        String defDescription = "";


        RadioEnvironment defRadioEnvironment = new RadioEnvironment("DEFAULT",0.1,0.1,0.1,0.1);
        ArrayList<Bot> defBotList = new ArrayList<>();
        BotCollection defBotCollection = new BotCollection(defBotList);
        SimScenario defaultScenario = new SimScenario("DEFAULT",defDescription, defRadioEnvironment, defBotCollection, "Responsive");

        return defaultScenario;

    }



    public void startScenario() throws InterruptedException {
        Radio.setNoiseAmplitude(environment.getNoiseAmplitude());
        isPlaying = true;
        if(!botCollection.getBots().isEmpty()){
            for(Bot bot: botCollection.getBots()){
                bot.playSound();
            }
        }

        handleAIScenario();



    }

    public void stopScenario(){
        Radio.setNoiseAmplitude(0);
        isPlaying = false;
        if(!botCollection.getBots().isEmpty()){
            for(Bot bot: botCollection.getBots()){
                bot.stopSound();
            }
        }
    }

    private void handleAIScenario() {
        if (this.scenarioType.equals("AI")) {
            new Thread(() -> {
                Random randGen = new Random();
                while (this.isPlaying) {

                    try {
                        Thread.sleep(20000); //Generates a random message every 20 seconds
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if (!botCollection.getBots().isEmpty()) {

                        int index = randGen.nextInt(botCollection.getBots().size());
                        AIBot bot = (AIBot) botCollection.getBots().get(index);
                        bot.randomMessage();
                    }
                }

            }).start();

        }

    }

    public void setParentController(SandboxController controller) {
        this.parentController = controller;
    }

    public SandboxController getParentController() {
        return parentController;
    }

    public boolean checkUserInput(String userInput){
        return false;
    }

    public String getDescription(){
        return description;
    }

    public String getName(){
        return scenarioName;
    }

    public RadioEnvironment getEnvironment(){
        return environment;
    }

    public String getType(){
        return scenarioType;
    }

    public BotCollection getBotCollection(){
        return botCollection;
    }

    @Override
    public String toString() {
        return this.scenarioName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        // Check if the object is an instance of Agent
        if (obj instanceof SimScenario) {
            SimScenario other = (SimScenario) obj;
            // Define equality based on name and id
            return this.scenarioName == other.scenarioName;
        }

        return false;
    }

    public void setEnvironment(RadioEnvironment environment) {
        this.environment = environment;
    }

    public void setDescription(String newDescription){
        this.description = newDescription;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public void saveToFile() {
        // Create a Gson instance with the custom serializer for Bot
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Bot.class, new BotSerializer())  // Register the custom serializer for Bot
                .setPrettyPrinting()  // Optional: Makes JSON output more readable
                .create();

        // Initialize the JavaFX FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON File");

        // Set a filter to only allow saving as JSON files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        // Show the Save dialog
        File fileToSave = fileChooser.showSaveDialog(new Stage());

        if (fileToSave != null) {
            // Ensure the file has a .json extension
            if (!fileToSave.getName().endsWith(".json")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".json");
            }

            // Write JSON to the selected file
            try (FileWriter writer = new FileWriter(fileToSave)) {
                gson.toJson(this, writer);  // Serialize the current SimScenario object to JSON
                System.out.println("File saved to: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void checkMessage(String userMorseMessage) {

        //Add message to the chat log
        parentController.addMessageToScenarioUI(TextToMorseConverter.morseToText(userMorseMessage.replace(' ', '/')), userMorseMessage.replace(' ', '/'));

        if (getType().equals("Responsive")) {
            boolean answerCorrect = false;

            double userFreq = Radio.getSelectedTuneFreq();

            double lowestFreqDistance = (double) Integer.MAX_VALUE;
            ResponsiveBot closestBot = null;

            for (Bot bot : botCollection.getBots()) {
                ResponsiveBot responsiveBot = (ResponsiveBot) bot;

                if (responsiveBot.getStage() == 1) {
                    if (Math.abs(bot.getOutputFrequency() - userFreq) < lowestFreqDistance) {
                        closestBot = (ResponsiveBot) bot;
                        lowestFreqDistance = Math.abs(bot.getOutputFrequency() - userFreq);
                    }
                } else if (responsiveBot.getStage() == 2) {
                    if (userFreq >= responsiveBot.getAnswerFreq() - 0.05 && userFreq <= responsiveBot.getAnswerFreq() + 0.05) {
                        if (responsiveBot.checkMessage(userMorseMessage)) {
                            answerCorrect = true;
                        }

                    }
                }
            }
            if (closestBot != null) {
                if (closestBot.checkMessage(userMorseMessage)) {
                    answerCorrect = true;
                }
            }


            if (answerCorrect) {
                parentController.addMessageToScenarioUI("**Congrats! You answered correctly. Move onto the next part of the scenario.**","");
            } else {
                parentController.addMessageToScenarioUI("**Uh oh. You answered incorrectly. You either messed up your message, are at the wrong frequency, or you waited too long to finish your message. Try again.**", "");
            }

        } else { //Code if it is an AI Scenario

            double userFreq = Radio.getSelectedTuneFreq();

            double lowestFreqDistance = (double) Integer.MAX_VALUE;
            AIBot closestBot = null;

            for (Bot bot : botCollection.getBots()) {
                AIBot aiBot = (AIBot) bot;

                if (Math.abs(bot.getOutputFrequency() - userFreq) < lowestFreqDistance) {
                    closestBot = (AIBot) bot;
                    lowestFreqDistance = Math.abs(bot.getOutputFrequency() - userFreq);
                }
            }

            if (closestBot != null) {
                closestBot.talkTo(userMorseMessage);
            }

        }



    }



}
