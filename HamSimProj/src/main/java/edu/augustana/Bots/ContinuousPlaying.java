package edu.augustana.Bots;

import edu.augustana.MorsePlayer;

public class ContinuousPlaying implements PlayingBehavior{


    private final ContinuousMessageBot bot;

    public ContinuousPlaying(ContinuousMessageBot bot) {
        this.bot = bot;
    }

    /**
     * Calls the bot to play their call sign and message in a loop while the boolean playSound is true
     */
    public void startBehavior() {

        //Can probably have this just play the whole message, but when I do I need to add more space between the call sign
        //and the message. So just append an * between the two I think.
        System.out.println(bot.getOutputFrequency());


        new Thread(() -> { //Need to continuously check "playSound" throughout this loop, and then break out of it if it is false
            while (bot.getPlaySound()) {
                try {
                    MorsePlayer.playBotMorseString(bot.getMorseCallSign(), bot.getOutputFrequency(), bot.getFrequencyRange());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!bot.getPlaySound()) {break;} //This is so that we can stop the sound sooner, rather than waiting till the end

                try { // Use WPM to calculate how long each thing takes
                    Thread.sleep(MorsePlayer.getMessagePlayDuration(bot.getMorseCallSign()) + 2000); //waiting the duration of the message, plus 2 seconds
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!bot.getPlaySound()) {break;}

                try {
                    MorsePlayer.playBotMorseString(bot.getMorseBotPhrase(), bot.getOutputFrequency(), bot.getFrequencyRange());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (!bot.getPlaySound()) {break;}

                try {
                    Thread.sleep(MorsePlayer.getMessagePlayDuration(bot.getMorseBotPhrase()) + 2000); //Same thing as other sleep
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }

}
