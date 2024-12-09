package edu.augustana;

import java.util.ArrayList;

public class ScenarioCollection {

    private static ArrayList<SimScenario> scenarioCollection = new ArrayList<>();

    public static ArrayList<SimScenario> getCollection(){
        return scenarioCollection;
    }

    public static void addScenario(SimScenario scenario){
        scenarioCollection.add(scenario);
    }

    public static  void removeScenario(SimScenario scenario){
        scenarioCollection.remove(scenario);
    }



}
