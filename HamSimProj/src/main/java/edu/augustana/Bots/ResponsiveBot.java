package edu.augustana.Bots;

import edu.augustana.TextToMorseConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class ResponsiveBot extends Bot{

    private final String expectedAnswer;//need to create this
    private final double answerFreq;


    public ResponsiveBot(int band, String name, String callSign, String botPhrase, double answerFreq, String expectedAnswer) {
        super(band);

        setBehaviorType(new ResponsivePlaying(this));

        setName(name);

        setTextBotPhrase(botPhrase);
        setTextCallSign(callSign);

        setMorseBotPhrase(TextToMorseConverter.textToMorse(botPhrase));
        setMorseCallSign(TextToMorseConverter.textToMorse(callSign));

        this.expectedAnswer = expectedAnswer;
        this.answerFreq = answerFreq;



    }

    @Override
    public void playSound() {
        updateStage();
        playBehavior();
    }

    @Override
    public void stopSound() {
        resetStage();
    }

    private void updateStage() {
        ResponsivePlaying playingBehavior = (ResponsivePlaying) getBehaviorType();
        playingBehavior.increaseStage();
    }

    private void resetStage() {
        ResponsivePlaying playingBehavior = (ResponsivePlaying) getBehaviorType();
        playingBehavior.resetStageNumber();
    }

    public int getStage() {
        ResponsivePlaying playingBehavior = (ResponsivePlaying) getBehaviorType();
        return playingBehavior.getStageNumber();
    }

    public double getAnswerFreq() {
        return answerFreq;
    }

    /**
     * this method checks the message that the user sends to see if it is the correct response
     * @param userMorseString
     * @return
     */
    public boolean checkMessage(String userMorseString) {


        int stageNumber = getStage();

        String answerToCompare;

        if (stageNumber == 1) {
            answerToCompare = makeStringComparable("RO" + getTextCallSign()).toString();
        } else if (stageNumber == 2) {
            answerToCompare = makeStringComparable(expectedAnswer).toString();
        } else {
            return false;  //just doing this so that if the bot is done, then the check message does nothing.
        }

        String userText = TextToMorseConverter.morseToText(userMorseString.replace(' ', '/'));
        String userTextToCompare = makeStringComparable(userText).toString();

        if (userTextToCompare.equals(answerToCompare)) {
            updateStage();
            return true;
        } else {
            return false;
        }

    }

    //Takes a string and gets rid of all white spaces so that we can easily compare the strings.
    //We have to do this because in the user morse string, there might be additional white spaces that we don't want
    private StringBuilder makeStringComparable(String string) {

        String[] textArray = string.toUpperCase().trim().split(" ");
        StringBuilder userTextToCompare = new StringBuilder();
        for (int i = 0; i < textArray.length; i++) {
            userTextToCompare.append(textArray[i]);
        }

        return userTextToCompare;
    }


    public String getExpectedAnswer() {
        return expectedAnswer;
    }
}
