package edu.augustana;

public class BiquadLowPassFilter8Bit {

    // Biquad filter coefficients
    private double a0, a1, a2, b1, b2;
    private double cutoffFreqFilter;
    private double sampleRate;

    // Filter state variables (previous inputs and outputs)
    private double prevInput1 = 0.0, prevInput2 = 0.0;
    private double prevOutput1 = 0.0, prevOutput2 = 0.0;

    // Constructor to initialize the filter with sample rate and cutoff frequency
    public BiquadLowPassFilter8Bit(double newSampleRate, double cutoffFreq) {
        cutoffFreqFilter = cutoffFreq;
        sampleRate = newSampleRate;
        calculateCoefficients();
    }

    // Method to calculate the filter coefficients based on sample rate and cutoff frequency
    private void calculateCoefficients() {
        double omega = 2.0 * Math.PI * cutoffFreqFilter / sampleRate;
        double alpha = Math.sin(omega) / (2.0 * 0.707);  // Q factor of 0.707 for Butterworth filter

        double cosOmega = Math.cos(omega);
        double a0Inv = 1.0 / (1.0 + alpha);

        // Biquad filter coefficients for a low-pass filter
        a0 = (1.0 - cosOmega) / 2.0 * a0Inv;
        a1 = (1.0 - cosOmega) * a0Inv;
        a2 = a0;
        b1 = -2.0 * cosOmega * a0Inv;
        b2 = (1.0 - alpha) * a0Inv;
    }

    // Method to reset the filter state (useful when stopping or restarting the filter)
    public void reset() {
        prevInput1 = prevInput2 = 0.0;
        prevOutput1 = prevOutput2 = 0.0;
    }

    // Apply the filter to a single sample
    public double processSample(double inputSample) {
        // Calculate the filtered output sample using the biquad filter equation
        calculateCoefficients();
        double outputSample = a0 * inputSample + a1 * prevInput1 + a2 * prevInput2 - b1 * prevOutput1 - b2 * prevOutput2;

        // Update previous inputs and outputs for the next sample
        prevInput2 = prevInput1;
        prevInput1 = inputSample;
        prevOutput2 = prevOutput1;
        prevOutput1 = outputSample;

        return outputSample;
    }

    public void setFilterValue(double new_cutoffFreqFilter){
        cutoffFreqFilter = new_cutoffFreqFilter;
    }

}

