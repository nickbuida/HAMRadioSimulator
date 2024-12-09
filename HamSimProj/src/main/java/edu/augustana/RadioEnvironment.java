package edu.augustana;

import java.util.Random;

public class RadioEnvironment {

    // Environmental factors
    public double temperature = 20;   // Degrees Celsius
    public double humidity = 0.75;    // Relative humidity (0 to 1)
    public double windSpeed = 10;     // Wind speed in km/h
    public double solarActivity = 100; // Solar activity index (0 to 300)

    String nameScenario = "";

    // Weights for each factor
    private final double weightTemperature = 0.2;
    private final double weightHumidity = 0.3;
    private final double weightWindSpeed = 0.1;
    private final double weightSolarActivity = 0.4;

    // Max values for normalization
    private final double maxTemperature = 50;      // Max temperature
    private final  double maxHumidity = 1;          // Max humidity
    private final double maxWindSpeed = 150;       // Max wind speed
    private final  double maxSolarActivity = 300;   // Max solar activity

    // Normalizing the environmental factors
    private double normTemperature = temperature / maxTemperature;
    private double normHumidity = humidity / maxHumidity;
    private double normWindSpeed = windSpeed / maxWindSpeed;
    private double normSolarActivity = solarActivity / maxSolarActivity;

    transient Random  random = new Random();


    public RadioEnvironment(String name, double solarActivity, double windSpeed, double humidity, double temperature) {
        this.nameScenario = name;
        this.solarActivity = solarActivity;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.temperature = temperature;
    }

    double getNoiseAmplitude(){
        double noiseAmplitude = (normTemperature * weightTemperature +
                normHumidity * weightHumidity +
                normWindSpeed * weightWindSpeed +
                normSolarActivity * weightSolarActivity);
        noiseAmplitude += random.nextDouble() * 0.1 - 0.05;
        noiseAmplitude = Math.max(0.0, Math.min(noiseAmplitude, 4.0));

        System.out.printf("Simulated Noise Amplitude: %.4f%n",noiseAmplitude);

        return noiseAmplitude;
    }







}
