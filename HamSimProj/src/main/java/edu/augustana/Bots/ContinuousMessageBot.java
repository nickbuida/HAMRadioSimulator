package edu.augustana.Bots;


import edu.augustana.TextToMorseConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class ContinuousMessageBot extends Bot{

    private boolean playSound;


    public static ArrayList<String> botPhraseArray = new ArrayList<>(Arrays.asList("Hello", "SOS", "Pizza is good", "You are cool", "Good Morning", "Good Afternoon", "Good night", "Weather is good", "My CW is bad", "Help me", "The wilderness needs to be explored")); //Add the phrases to this list
    public static ArrayList<String> botCallSignArray = new ArrayList<>(Arrays.asList("K8X", "K5AA", "B2AA", "N2ASD", "K8ABC", "KB9VBR", "A22A", "K7LQ", "N6Y", "W3TRO", "W8IA", "N9NA")); //Add list of call signs
    public static ArrayList<String> usedBotPhrases = new ArrayList<>();
    public static ArrayList<String> usedCallSigns = new ArrayList<>();


    //this count is just for the names
    private static int count = 1;




    //bot constructor for training listening sim
    public ContinuousMessageBot(int band) {
        super(band);

        setBehaviorType(new ContinuousPlaying(this));

        setName("bot" + count);

        count++;

        String selection = botPhraseArray.get(randomGen.nextInt(botPhraseArray.size()));
        botPhraseArray.remove(selection);
        usedBotPhrases.add(selection); //This is so that we can add them back into the array once we stop the sim
        setTextBotPhrase(selection);
        setMorseBotPhrase(TextToMorseConverter.textToMorse(selection)); //Morse string of their phrase


        selection = botCallSignArray.get(randomGen.nextInt(botCallSignArray.size()));
        botCallSignArray.remove(selection);
        usedCallSigns.add(selection); //This is so that we can add them back into the array once we stop the sim
        setTextCallSign(selection);
        setMorseCallSign(TextToMorseConverter.textToMorse(selection)); //Morse string of their callSign
        this.playSound = false;



        //testing
        System.out.println(getTextCallSign() + " " + getTextBotPhrase() + " " + getOutputFrequency());

    }

    @Override
    public void playSound() {

        playSound = true;
        playBehavior(); //calls the super class method which calls the playingBehavior object
    }

    @Override
    public void stopSound() {
        playSound = false;
    }

    //accessor method for playSound
    public boolean getPlaySound() {
        return playSound;
    }

}
