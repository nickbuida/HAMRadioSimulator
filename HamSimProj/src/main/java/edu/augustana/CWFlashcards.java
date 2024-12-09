package edu.augustana;

import java.util.*;

public class CWFlashcards {

    private static HashMap<String, String> morseToTextMap;
    private static Map<String, String> textToMorseMap;
    private static List<String> alphabetList = new ArrayList<>();
    private static Random randGen = new Random();
    private static String currentLetter = "";
    private static String currentMorse;
    private static Boolean lettersSetting;
    private static Boolean numbersSetting;
    private static Boolean abbrevSetting;

    public static Boolean handleGuess(String guess, Boolean isListening) {
        if (isListening) {
            if (Objects.equals(textToMorseMap.get(guess), currentMorse)) {
                currentLetter = "";
                currentMorse = "";
                return true;
            } else {
                return false;
            }
        } else {
            if (Objects.equals(morseToTextMap.get(guess), currentLetter)) {
                currentLetter = "";
                currentMorse = "";
                return true;
            } else {
                return false;
            }
        }
    }

    public static String playLetter(Boolean isListening) throws InterruptedException {
        String letter;
        if (currentLetter.isEmpty()) {
            letter = selectRandomLetter();
            currentLetter = letter;
        } else {
            letter = currentLetter;
        }
        currentMorse = TextToMorseConverter.textToMorse(letter);
        currentMorse = currentMorse.replace(' ', '/');
        System.out.println("Current Letter: " + currentLetter);
        System.out.println("Current morse string: " + currentMorse);
        if (isListening) {
            MorsePlayer.playMorseString(currentMorse);
        }
        return currentLetter;
    }

    public static void generateAlphabetList(Boolean letters, Boolean numbers, Boolean abbrev) {
        lettersSetting = letters;
        numbersSetting = numbers;
        abbrevSetting = abbrev;
        textToMorseMap = new HashMap<>(TextToMorseConverter.getTextToMorseMap());
        morseToTextMap = new HashMap<>(TextToMorseConverter.getMorseToTextMap());
        if (!letters) {
            removeLetters();
        }
        if (!numbers) {
            removeNumbers();
        }
        if (abbrev) {
            addAbbrevToMap();
        } else {
            removeAbbrev();
        }
        alphabetList.clear();
        alphabetList.addAll(textToMorseMap.keySet());
        alphabetList.remove(" ");
    }

    private static String selectRandomLetter() {
        if (alphabetList.isEmpty()) {
            generateAlphabetList(lettersSetting, numbersSetting, abbrevSetting);
        }
        String letter = alphabetList.get(randGen.nextInt(alphabetList.size()));
        for (int i = 0; i < alphabetList.size(); i ++) {
            if (alphabetList.get(i).equals(letter)) {
                alphabetList.remove(i);
            }
        }
        return letter;
    }

    private static void addAbbrevToMap() {
        textToMorseMap.put("AGN", ".-/--./-.");// A .-
        textToMorseMap.put("ANT", ".-/-./-");// B -...
        textToMorseMap.put("AS", ".-/...");// C -.-.
        textToMorseMap.put("BK", "-.../-.-");//D -..
        textToMorseMap.put("BTU", "-.../-/..-");//E .
        textToMorseMap.put("B4", "-.../....-");// "F", "..-."
        textToMorseMap.put("CFM", "-.-./..-./--");// "G", "--."
        textToMorseMap.put("CK", "-.-./-.-");// "H", "...."
        textToMorseMap.put("CL", "-.-./.-..");// "I", ".."
        textToMorseMap.put("CLG", "-.-./.-../--.");// "J", ".---"
        textToMorseMap.put("CX", "-.-./-..-");// "K", "-.-"
        textToMorseMap.put("COS", "-.-./---/..."); // "L", ".-.."
        textToMorseMap.put("CQ", "-.-./--.-"); // "M", "--"
        textToMorseMap.put("CS", "-.-./..."); // "N", "-."
        textToMorseMap.put("CUL", "-.-./..-/.-..");// "O", "---"
        textToMorseMap.put("CW", "-.-./.--"); // "P", ".--."
        textToMorseMap.put("DE", "-../."); //  "Q", "--.-"
        textToMorseMap.put("DX", "-../-..-");// "R", ".-."
        textToMorseMap.put("EMRG", "./--/.-./--."); // "S", "..."
        textToMorseMap.put("ES", "./..."); // "T", "-"
        textToMorseMap.put("FB", "..-./-..."); // "U", "..-"
        textToMorseMap.put("FREQ", "..-./.-././--.-"); // "V", "...-"
        textToMorseMap.put("GA", "--./.-");// "W", ".--"
        textToMorseMap.put("GG", "--./--.");// "X", "-..-"
        textToMorseMap.put("GM", "--./--"); // "Y", "-.--"
        textToMorseMap.put("GN", "--./-."); // "Z", "--.."
        textToMorseMap.put("GUD", "--./..-/-..");
        textToMorseMap.put("HEE", "...././.");
        textToMorseMap.put("II", "../..");
        textToMorseMap.put("KN", "-.-/-.");
        textToMorseMap.put("NIL", "-./../.-..");
        textToMorseMap.put("OK", "---/-.-");
        textToMorseMap.put("PLS", ".--./.-../...");
        textToMorseMap.put("PWR", ".--./.--/.-.");
        textToMorseMap.put("RFI", ".-./..-./..");
        textToMorseMap.put("RIG", ".-./../--.");
        textToMorseMap.put("RPT", ".-./.--./-");
        textToMorseMap.put("SIG", ".../../--.");
        textToMorseMap.put("SRI", ".../.-./..");
        textToMorseMap.put("TFC", "-/..-./-.-.");
        textToMorseMap.put("TKS", "-/-.-/...");
        textToMorseMap.put("SOS", ".../---/...");

        morseToTextMap.put(".-/--./-.", "AGN");// A .-
        morseToTextMap.put(".-/-./-", "ANT");// B -...
        morseToTextMap.put(".-/...", "AS");// C -.-.
        morseToTextMap.put("-.../-.-", "BK");//D -..
        morseToTextMap.put("-.../-/..-", "BTU");//E .
        morseToTextMap.put("-.../....-", "B4");// "F", "..-."
        morseToTextMap.put("-.-./..-./--", "CFM");// "G", "--."
        morseToTextMap.put("-.-./-.-", "CK");// "H", "...."
        morseToTextMap.put("-.-./.-..", "CL");// "I", ".."
        morseToTextMap.put("-.-./.-../--.", "CLG");// "J", ".---"
        morseToTextMap.put("-.-./-..-", "CX");// "K", "-.-"
        morseToTextMap.put("-.-./---/...", "COS"); // "L", ".-.."
        morseToTextMap.put("-.-./--.-", "CQ"); // "M", "--"
        morseToTextMap.put("-.-./...", "CS"); // "N", "-."
        morseToTextMap.put("-.-./..-/.-..", "CUL");// "O", "---"
        morseToTextMap.put("-.-./.--", "CW"); // "P", ".--."
        morseToTextMap.put("-../.", "DE"); //  "Q", "--.-"
        morseToTextMap.put("-../-..-", "DX");// "R", ".-."
        morseToTextMap.put("./--/.-./--.", "EMRG"); // "S", "..."
        morseToTextMap.put("./...", "ES"); // "T", "-"
        morseToTextMap.put("..-./-...", "FB"); // "U", "..-"
        morseToTextMap.put("..-./.-././--.-", "FREQ"); // "V", "...-"
        morseToTextMap.put("--./.-", "GA");// "W", ".--"
        morseToTextMap.put("--./--.", "GG");// "X", "-..-"
        morseToTextMap.put("--./--", "GM"); // "Y", "-.--"
        morseToTextMap.put("--./-.", "GN"); // "Z", "--.."
        morseToTextMap.put("--./..-/-..", "GUD");
        morseToTextMap.put("...././.", "HEE");
        morseToTextMap.put("../..", "II");
        morseToTextMap.put("-.-/-.", "KN");
        morseToTextMap.put("-./../.-..", "NIL");
        morseToTextMap.put("---/-.-", "OK");
        morseToTextMap.put(".--./.-../...", "PLS");
        morseToTextMap.put(".--./.--/.-.", "PWR");
        morseToTextMap.put(".-./..-./..", "RFI");
        morseToTextMap.put(".-./../--.", "RIG");
        morseToTextMap.put(".-./.--./-", "RPT");
        morseToTextMap.put(".../../--.", "SIG");
        morseToTextMap.put(".../.-./..", "SRI");
        morseToTextMap.put("-/..-./-.-.", "TFC");
        morseToTextMap.put("-/-.-/...", "TKS");
        morseToTextMap.put(".../---/...", "SOS");
    }

    private static void removeAbbrev() {
        textToMorseMap.entrySet().removeIf(stringStringEntry -> stringStringEntry.getKey().length() > 1);
        morseToTextMap.entrySet().removeIf(stringStringEntry -> stringStringEntry.getValue().length() > 1);
    }

    private static void removeNumbers() {
        String letters = "1234567890";
        textToMorseMap.entrySet().removeIf(stringStringEntry -> letters.contains(stringStringEntry.getKey()));
        morseToTextMap.entrySet().removeIf(stringStringEntry -> letters.contains(stringStringEntry.getValue()));
    }

    private static void removeLetters() {
        String letters = "1234567890";
        textToMorseMap.entrySet().removeIf(stringStringEntry -> stringStringEntry.getKey().length() == 1 && !letters.contains(stringStringEntry.getKey()));
        morseToTextMap.entrySet().removeIf(stringStringEntry -> stringStringEntry.getValue().length() == 1 && !letters.contains(stringStringEntry.getValue()));
    }

    //This resets currentLetter and currentMorse. this is used after the end training button is hit so that if you go back in, the last letter not completed is not still up in the queue.
    public static void resetCurrent() {
        currentLetter = "";
        currentMorse = "";
    }

}
