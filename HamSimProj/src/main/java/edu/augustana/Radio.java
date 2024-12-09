package edu.augustana;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.util.Random;

import static java.lang.Double.valueOf;

public class Radio {

    private static SourceDataLine line1;
    private static SourceDataLine line2;
    private static AudioFormat format;

    private static double selectedRF;
    private static double tunningRF;
    private static double noiseAmplitud;
    private static Random randGen;
    private static double soundAmplitud;
    public static final int MAX_CWTONE_FREQ = 800;
    public static final int MIN_CWTONE_FREQ = 400;
    private static double oldNoiseAmplitude;



    private static final int SAMPLE_RATE = 44100;
    private static final int BUFFER_SIZE = 256;
    private static final int BUFFER_SIZE2 = 4096;
    private static SourceDataLine line;
    private static boolean isPlaying = false;
    private static byte[] buffer = new byte[BUFFER_SIZE2];
    private static byte[] buffer2 = new byte[BUFFER_SIZE2];
    private static boolean isRadioOn;
    private static int filterVal;
    private static BiquadLowPassFilter8Bit biquadFilter;
    private static BiquadLowPassFilter8Bit biquadFilter2;
    private static int lowerNoiseFreqBoundary = 250;
    private static int upperNoiseFreqBoundary = 550;

    private static int simTime;
    private static double cwToneFreq = 400;
    private static int band;





    public static void initializeRadio() throws LineUnavailableException{


        isRadioOn = true;
        simTime = 1200;
        cwToneFreq = 400;
        band = 10;

//        updateNoiseGain(0);
        randGen = new Random();
        soundAmplitud = 0;
        filterVal = 10;
        biquadFilter = new BiquadLowPassFilter8Bit(SAMPLE_RATE, filterVal);
        biquadFilter2 = new BiquadLowPassFilter8Bit(SAMPLE_RATE, filterVal);


        format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false);
        line = AudioSystem.getSourceDataLine(format);
        line.open(format, BUFFER_SIZE2);
        line.start();

        line2 = AudioSystem.getSourceDataLine(format);
        line2.open(format, BUFFER_SIZE2);
        line2.start();

        playNoise();

    }


    // Method to generate a tone and play it on the given line
    static void playTone(double frequency) {
        if (isPlaying) {
            return;  // Prevent starting multiple threads for the same tone
        }

        isPlaying = true;
        new Thread(() -> {


            double[] angles = new double[10];
            angles[0] = 0;

            while (isPlaying) {// Only play while isPlaying is true

                playSound(angles, 0, BUFFER_SIZE2, biquadFilter, buffer, line, frequency);


            }
        }).start();
    }

    // Method to stop the tone
    static void stopTone() {
        isPlaying = false;

        line.flush();  // Clear the buffer to stop sound
    }


    private static double generateGaussianNoise(double stdDev, Random random) {
        double u1 = random.nextDouble();
        double u2 = random.nextDouble();

        return (Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2)) * stdDev;
    }

    public static void  setSelectedRF(double newSelectedRF){

        selectedRF = newSelectedRF;

    }

    public static void  setTunningRF(double newTunningRF){

        tunningRF = newTunningRF;

    }

    public static void updateGain(double newAmplitud){

        soundAmplitud = newAmplitud;

    }

    public static double getSoundAmplitud(){
        return soundAmplitud;
    }

    public static double getFilterValue(){
        return filterVal;
    }

    public static boolean isRadioOn(){
        return isRadioOn;
    }


    public static void updateNoiseGain(double newAmplitud){

        noiseAmplitud = newAmplitud;

    }

    public static double getSelectedOutFrequency(){
        return selectedRF;
    }


    //NOTE DELETE THIS AFTER REFACTORING VAIRABLE NAMES
    public static double getSelectedTuneFreq(){
        return tunningRF;
    }

    public static double getCwToneFreq(){
        return cwToneFreq;
    }

    public static void setCwToneFreq(double newCwToneFreq){
        cwToneFreq =  newCwToneFreq;
    }

    public static int getBand(){
        return band;
    }

    public static void setBand(int newBand){
        band = newBand;
    }


    public static int getTime(){
        return simTime;
    }

    public static void setTime(int newTime){
        simTime = newTime;
    }





    private static void playNoise(){

        new Thread(() -> {


            double[] angles = new double[10];
            angles[0] = 0;


            while (isRadioOn) {

                int frequency = randGen.nextInt(upperNoiseFreqBoundary - lowerNoiseFreqBoundary) + lowerNoiseFreqBoundary;

                playSound(angles, 0, BUFFER_SIZE2, biquadFilter2, buffer2, line2, 0);


            }
        }).start();
    }

    private void stopNoise(){
        line2.flush();
    }


    public static void changeFilterValue(int newFilterVal){

        biquadFilter2.setFilterValue(newFilterVal);
        biquadFilter.setFilterValue(newFilterVal);
    }

    static void playSound(double[] angles, int angleIndex, int bufferSize, BiquadLowPassFilter8Bit filter, byte[] buffer,  SourceDataLine line, double frequency){
        double angleIncrement  = 2.0 * Math.PI * frequency / format.getSampleRate();

        // Only play while isPlaying is true
        for (int i = 0; i < bufferSize; i++) {
            double sineSample = ( soundAmplitud * ((Math.sin(angles[angleIndex])) + generateGaussianNoise(noiseAmplitud, randGen))); // 0 for no noise
            //double sineSample = ( soundAmplitud * ((Math.sin(angles[angleIndex]))));

            // Clip and normalize the sample to fit into 8-bit range [-128, 127]
            sineSample = Math.max(-1.0, Math.min(1.0, sineSample));  // Clip sample to [-1.0, 1.0]


            double filteredSample = filter.processSample(sineSample);

            // Scale sample to 8-bit range [-128 to 127]
            byte sample = (byte) (filteredSample * 127);
            buffer[i] = sample;  // Write sample to buffer

            angles[angleIndex] += angleIncrement;
            if (angles[angleIndex] > 2.0 * Math.PI) {
                angles[angleIndex] -= 2.0 * Math.PI;
            }
        }

        line.write(buffer, 0, buffer.length);


    }

    public static void toggleNoise() {
        if (noiseAmplitud != 0) {
            oldNoiseAmplitude = noiseAmplitud;
            noiseAmplitud = 0;
        } else {
            noiseAmplitud = oldNoiseAmplitude;
        }
    }

    public static void setNoiseAmplitude(double newAmplitude){
        noiseAmplitud = newAmplitude;
    }

    public static double generateFrequencyRange(int band) {

        double output = 0;

        switch (band){
            case 10:
                output = 1.7;
                break;

            case 17:

                output = .1;
                break;

            case 20:
                output = .35;
                break;

            case 30:

                output = .05;
                break;

            case 40:
                output = .3;
                break;

            case 80:
                output = .5;
                break;
        }
        return output;
    }

    public static double getMinFreqInBand() {

        double minFreq = 0;

        switch (band){
            case 10:
                minFreq = 28.000;
                break;

            case 17:

                minFreq = 18.068;
                break;

            case 20:
                minFreq = 14.000;
                break;

            case 30:

                minFreq = 10.100;
                break;

            case 40:
                minFreq = 7.000;
                break;

            case 80:
                minFreq = 3.500;
                break;
        }
        return minFreq;
    }






}
