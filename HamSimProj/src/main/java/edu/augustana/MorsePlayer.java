package edu.augustana;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static edu.augustana.CWHandler.startTimer;
import static edu.augustana.CWHandler.stopTimer;
import static edu.augustana.Radio.*;
import static edu.augustana.Radio.stopTone;

public class MorsePlayer {

    private static Random randGen2;
    private static final long beatLength = TimeUnit.NANOSECONDS.toMillis((1200000000L / 20));
    private static int maxToneHz = 3000;
    private static int wordsPerMinute;
    private static double multiplier;
    private static double filterVal;
    private static double sideTone = 400;
    //private static int logFrequencyHelper





    public static void playMorseString(String morseString) throws InterruptedException {

        new Thread(() -> {

        double variation = 0.3;
        char[] morse = morseString.toCharArray();

        randGen2 = new Random();

        for(int i = 0; i < morse.length ; i++){
            if(morse[i] == '.'){
                playTone(Radio.getCwToneFreq());
                startTimer();
                try {
                    Thread.sleep(beatLength + randGen2.nextInt( (int) (beatLength * variation)));
                } catch (InterruptedException e) {
                }
                stopTone();
                stopTimer();
            }else if(morse[i] == '-'){
                playTone(Radio.getCwToneFreq()); startTimer();
                try {
                    Thread.sleep((beatLength * 3) + randGen2.nextInt( (int) (beatLength * variation)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                stopTone(); stopTimer();
            }else{
                try {
                    Thread.sleep((beatLength * 7) + randGen2.nextInt( (int) (beatLength * variation)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(beatLength + randGen2.nextInt( (int) (beatLength * variation)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        }).start();

    }
    
    //Temporary method to get the bots to play their sound
    //Not DRY code at all, but I didn't want to break anything
    public static void playBotMorseString(String morseString, double botFrequency, double frequencyRange) throws InterruptedException {

        //Change the 400 variable to our selected side tone

        new Thread(() -> {

            double variation = 0.3;
            char[] morse = morseString.toCharArray();

            randGen2 = new Random();


            for(int i = 0; i < morse.length ; i++){

                double freq = Math.abs(botFrequency - getSelectedTuneFreq()) * 1000000 + sideTone;

                freq = logFrequency(freq);

                if (freq < 50) {
                    freq = 50;
                }

                if (freq > maxToneHz) {
                    freq = maxToneHz;
                }


                // this is for the tuning slider
                double freqDiff = Math.abs(getSelectedTuneFreq() -  botFrequency);
                if (freqDiff < .0028) {
                    freq = sideTone;
                }
                System.out.println("Final freq: " + freq);
                double filterRange = (frequencyRange * filterVal) / 2;
                if (freqDiff > filterRange) {
                    freq = 0;
                }

                if(morse[i] == '.'){

                    playTone(freq);
                    //startTimer();
                    try {
                        Thread.sleep(beatLength + randGen2.nextInt( (int) (beatLength * variation)));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    stopTone();
                    //stopTimer();
                }else if(morse[i] == '-'){

                    playTone(freq);
                    //startTimer();
                    try {
                        Thread.sleep((beatLength * 3) + randGen2.nextInt( (int) (beatLength * variation)));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    stopTone();
                    //stopTimer();
                }else{
                    try {
                        Thread.sleep((long) ((beatLength * 7) * (multiplier) + randGen2.nextInt( (int) (beatLength * (multiplier) * variation))));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(((beatLength) + randGen2.nextInt( (int) (beatLength * multiplier * variation))));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();




    }

    //returns the amount of time that this message will take to play
    public static long getMessagePlayDuration(String morseMessage) {
        double variation = 0.3;
        char[] morse = morseMessage.toCharArray();

        randGen2 = new Random();

        long millisTimeWaited = 0;

        for(int i = 0; i < morse.length ; i++){
            if(morse[i] == '.'){
                millisTimeWaited = millisTimeWaited + beatLength + randGen2.nextInt( (int) (beatLength * variation));
            }else if(morse[i] == '-'){
                millisTimeWaited = millisTimeWaited + (beatLength * 3) + randGen2.nextInt( (int) (beatLength * variation));
            }else{
                millisTimeWaited = millisTimeWaited + (long) ((beatLength * 7) * (multiplier) + randGen2.nextInt( (int) (beatLength * (multiplier) * variation)));

            }

            millisTimeWaited = millisTimeWaited + (beatLength + randGen2.nextInt( (int) (beatLength * variation)));

        }

        return millisTimeWaited;
    }


    public static void setWordsPerMinuteMultiplier(int wpm) {
        wordsPerMinute = wpm;
        multiplier = (double) 20 / wpm;
    }

    public static void setFrequencyFilter(double num) {
        filterVal = num;
    }

    public static double logFrequency(double tone) {
            tone = 1000 * Math.log10(tone); //if you are about on, it puts out freq of 2500
            double numFix = sideTone - 2900;
            return tone + numFix;
    }


    public static void setSideTone() {
        sideTone = Radio.getCwToneFreq();
    }
}
