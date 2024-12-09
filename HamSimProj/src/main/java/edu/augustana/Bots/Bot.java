package edu.augustana.Bots;

import java.util.Random;

public abstract class Bot {

    private String morseBotPhrase;
    private String morseCallSign;

    private String textBotPhrase;
    private String textCallSign;


    private transient PlayingBehavior behaviorType;

    private double outputFrequency;
    private double frequencyRange;

    private final int band;

    private String name;

    static final Random randomGen = new Random();



    public Bot(int band) {
        this.band = band;

        //assigns the random output frequency depending on the band
        generateOutputFreqAndRange(band);

    }

    //Helper method for the constructors to get the random output frequency and frequency range
    private void generateOutputFreqAndRange(int band) {

        switch (band) {
            case 10:
                double frequency10 = 28000 + randomGen.nextInt(1701); //This is so that you get a random number in this range
                this.outputFrequency = frequency10 / 1000;
                this.frequencyRange = 1.7;
                break;

            case 17:
                double frequency17 = 18068 + randomGen.nextInt(101);
                this.outputFrequency = frequency17 / 1000;
                this.frequencyRange = .1;
                break;

            case 20:
                double frequency20 = 14000 + randomGen.nextInt(351);
                this.outputFrequency = frequency20 / 1000;
                this.frequencyRange = .35;
                break;

            case 30:
                double frequency30 = 10100 + randomGen.nextInt(51);
                this.outputFrequency = frequency30 / 1000;
                this.frequencyRange = .05;
                break;

            case 40:
                double frequency40 = 7000 + randomGen.nextInt(301);
                this.outputFrequency = frequency40 / 1000;
                this.frequencyRange = .3;
                break;

            case 80:
                double frequency80 = 3500 + randomGen.nextInt(501);
                this.outputFrequency = frequency80 / 1000;
                this.frequencyRange = .5;
                break;
        }
    }

    //Sets the behavior for the bot using the interface
    public void setBehaviorType(PlayingBehavior type) {
        this.behaviorType = type;
    }

    public void playBehavior() {
        behaviorType.startBehavior();
    }

    public PlayingBehavior getBehaviorType() {
        return behaviorType;
    }


    /**
     * Accessor method for outputFrequency
     * @return outputFrequency
     */
    public double getOutputFrequency() {
        return outputFrequency;

        //this method can be used if we want to add a hint button
    }

    /**
     * Accessor method for frequencyRange
     * @return frequencyRange
     */
    public double getFrequencyRange() {
        return frequencyRange;
    }

    /**
     * Accessor method for name
     * @return name
     */
    public String getName() {
        return name;
    }

    //setter for name
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Accessor method for botPhrase
     * @return botPhrase
     */
    public String getTextBotPhrase() {
        return textBotPhrase;
    }

    public void setTextBotPhrase(String text) {
        this.textBotPhrase = text;
    }

    /**
     * Accessor method for callSign
     * @return callSign
     */
    public String getTextCallSign() {return textCallSign;}

    public void setTextCallSign(String text) {
        this.textCallSign = text;
    }

    /**
     * Accessor method for morseBotPhrase
     * @return morseBotPhrase
     */
    public String getMorseBotPhrase() {return morseBotPhrase;}

    public void setMorseBotPhrase(String text) {
        this.morseBotPhrase = text;
    }

    /**
     * Accessor method for morseCallSign
     * @return morseCallSign
     */
    public String getMorseCallSign() {
        return morseCallSign;
    }

    public void setMorseCallSign(String text) {
        this.morseCallSign = text;
    }


    /**
     * makes the bots play their sound. Could make the continuous bot start their playing loop, or it could make the responsive/AI bots respond
     */
    public abstract void playSound();

    /**
     * Makes the bots stop playing their sound or message. Mostly used for continuous bot
     */
    public abstract void stopSound();



    /**
     * returns a string representation of the bot
     * @return String
     */
    public String toString() {
        return getName() + ", " + getTextCallSign() + ", " + getOutputFrequency();
    }



}
