package edu.augustana.Bots;

import edu.augustana.MorsePlayer;
import edu.augustana.ScenarioCollection;
import edu.augustana.SimScenario;
import edu.augustana.UI.SandboxController;

public class ResponsivePlaying implements PlayingBehavior {

    private final ResponsiveBot bot;

    private int stageNumber = 0; //This variable shows what the bot should be playing. So 1 means that the bot will play its call sign, 2 means it should play its message, and higher means that it is done playing.

    public ResponsivePlaying(ResponsiveBot bot) {
        this.bot = bot;
    }


    @Override
    public void startBehavior() {

        new Thread(() -> { //Need to continuously check "playSound" throughout this loop, and then break out of it if it is false

            SandboxController currentController = null;

            for (SimScenario scenario : ScenarioCollection.getCollection()) {
                if (scenario.isPlaying) {
                    currentController = scenario.getParentController();
                }
            }

            while (stageNumber == 1) {

                //testing
                //System.out.println(bot.getTextCallSign());

                currentController.addMessageToScenarioUI(bot.getName() + ": " + "CQ CQ " +bot.getTextCallSign(),bot.getName() + ": " + "-.-. --.- " + bot.getMorseCallSign());


                try {
                    MorsePlayer.playBotMorseString("-.-. --.- " + bot.getMorseCallSign(), bot.getOutputFrequency(), bot.getFrequencyRange());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (stageNumber != 1) {break;} //This is so that we can stop the sound sooner, rather than waiting till the end

                try { // Use WPM to calculate how long each thing takes
                    Thread.sleep(MorsePlayer.getMessagePlayDuration(bot.getMorseCallSign()) + 8000); //wait for longer to wait for user message
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

            while (stageNumber == 2) {

                //testing
                //System.out.println(bot.getTextBotPhrase());
                currentController.addMessageToScenarioUI(bot.getName() + ": " + bot.getTextBotPhrase(), bot.getName() + ": " + bot.getMorseBotPhrase());

                try {
                    MorsePlayer.playBotMorseString(bot.getMorseBotPhrase(), bot.getOutputFrequency(), bot.getFrequencyRange());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (stageNumber != 2) {break;} //This is so that we can stop the sound sooner, rather than waiting till the end

                try { // Use WPM to calculate how long each thing takes
                    Thread.sleep(MorsePlayer.getMessagePlayDuration(bot.getMorseBotPhrase()) + 8000); //wait for longer to wait for user message
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

            //note: basically just do the continuous playing but seperate the message and the call sign. And then wait for much longer in between the repeated messages to make time for the users message.
            //Make a button to send the users message, and then make a method in the bot to "check message" and just do some string manipulation to compare the users message and the bots actual thing.

        }).start();
    }

    public void increaseStage() {
        stageNumber++;
    }

    public void resetStageNumber() {
        stageNumber = 0;
    }
    public int getStageNumber() {
        return stageNumber;
    }
}
