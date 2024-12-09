package edu.augustana;

import java.util.*;


public class TextToMorseConverter {
    // Map to hold the Morse code mappings
    private static final Map<String, String> morseCodeMap = new HashMap<>();

    static {
        morseCodeMap.put("A", ".-");
        morseCodeMap.put("B", "-...");
        morseCodeMap.put("C", "-.-.");
        morseCodeMap.put("D", "-..");
        morseCodeMap.put("E", ".");
        morseCodeMap.put("F", "..-.");
        morseCodeMap.put("G", "--.");
        morseCodeMap.put("H", "....");
        morseCodeMap.put("I", "..");
        morseCodeMap.put("J", ".---");
        morseCodeMap.put("K", "-.-");
        morseCodeMap.put("L", ".-..");
        morseCodeMap.put("M", "--");
        morseCodeMap.put("N", "-.");
        morseCodeMap.put("O", "---");
        morseCodeMap.put("P", ".--.");
        morseCodeMap.put("Q", "--.-");
        morseCodeMap.put("R", ".-.");
        morseCodeMap.put("S", "...");
        morseCodeMap.put("T", "-");
        morseCodeMap.put("U", "..-");
        morseCodeMap.put("V", "...-");
        morseCodeMap.put("W", ".--");
        morseCodeMap.put("X", "-..-");
        morseCodeMap.put("Y", "-.--");
        morseCodeMap.put("Z", "--..");
        morseCodeMap.put("1", ".----");
        morseCodeMap.put("2", "..---");
        morseCodeMap.put("3", "...--");
        morseCodeMap.put("4", "....-");
        morseCodeMap.put("5", ".....");
        morseCodeMap.put("6", "-....");
        morseCodeMap.put("7", "--...");
        morseCodeMap.put("8", "---..");
        morseCodeMap.put("9", "----.");
        morseCodeMap.put("0", "-----");
        morseCodeMap.put(".", ".-.-.-");
        morseCodeMap.put("-", "-....-");
        morseCodeMap.put(" ", "*"); // Space separator in Morse code
    }


    private static final HashMap<String, String> morseToTextMap = new HashMap<>();

    // Static block to initialize the Morse Code mappings
    static {
        morseToTextMap.put(".-", "A");
        morseToTextMap.put("-...", "B");
        morseToTextMap.put("-.-.", "C");
        morseToTextMap.put("-..", "D");
        morseToTextMap.put(".", "E");
        morseToTextMap.put("..-.", "F");
        morseToTextMap.put("--.", "G");
        morseToTextMap.put("....", "H");
        morseToTextMap.put("..", "I");
        morseToTextMap.put(".---", "J");
        morseToTextMap.put("-.-", "K");
        morseToTextMap.put(".-..", "L");
        morseToTextMap.put("--", "M");
        morseToTextMap.put("-.", "N");
        morseToTextMap.put("---", "O");
        morseToTextMap.put(".--.", "P");
        morseToTextMap.put("--.-", "Q");
        morseToTextMap.put(".-.", "R");
        morseToTextMap.put("...", "S");
        morseToTextMap.put("-", "T");
        morseToTextMap.put("..-", "U");
        morseToTextMap.put("...-", "V");
        morseToTextMap.put(".--", "W");
        morseToTextMap.put("-..-", "X");
        morseToTextMap.put("-.--", "Y");
        morseToTextMap.put("--..", "Z");
        morseToTextMap.put(".----", "1");
        morseToTextMap.put("..---", "2");
        morseToTextMap.put("...--", "3");
        morseToTextMap.put("....-", "4");
        morseToTextMap.put(".....", "5");
        morseToTextMap.put("-....", "6");
        morseToTextMap.put("--...", "7");
        morseToTextMap.put("---..", "8");
        morseToTextMap.put("----.", "9");
        morseToTextMap.put("-----", "0");
        morseToTextMap.put("/", " ");// Space between words
        morseToTextMap.put(" ", " ");// Space between words
        morseToTextMap.put("*", " ");
    }


    // Method to convert text to Morse code
    public static String textToMorse(String text) {
        StringBuilder morseCode = new StringBuilder();

        for (char c : text.toUpperCase().toCharArray()) {
            System.out.println(c);
            String morseChar = morseCodeMap.get(Character.toString(c));
            if (morseChar != null) {
                morseCode.append(morseChar).append(" ");
            } else {
                // Handle characters not in Morse code map
                morseCode.append("");
            }
        }

        return morseCode.toString().trim();
    }

    public static String morseToText(String morseString) {
        if(Objects.equals(morseString, "")){
            return morseString;
        }
        String[] morseCodeArray = morseString.split("/");
        StringBuilder result = new StringBuilder();
        for (String morseCode : morseCodeArray) {
            morseCode = morseCode.trim();

            // Get the corresponding letter from the map
            String letter = morseToTextMap.get(morseCode);

            // If the Morse code is valid, append the letter
            if (letter != null) {
                result.append(letter);
            } else {
                result.append("");  // For invalid Morse code
            }
        }

        return result.toString();
    }

    public static String spacedMorseToText(String morseString) {
        if(Objects.equals(morseString, "")){
            return morseString;
        }
        String[] morseCodeArray = morseString.split(" ");
        StringBuilder result = new StringBuilder();
        for (String morseCode : morseCodeArray) {
            morseCode = morseCode.trim();

            // Get the corresponding letter from the map
            String letter = morseToTextMap.get(morseCode);

            // If the Morse code is valid, append the letter
            if (letter != null) {
                result.append(letter);
            } else {
                result.append("");  // For invalid Morse code
            }
        }

        return result.toString();
    }


    public static HashMap<String, String> getMorseToTextMap() {
        return morseToTextMap;
    }

    public static Map<String, String> getTextToMorseMap() {
        return morseCodeMap;
    }

    public static boolean containsAlphabetLetter(String string) {
        char[] characters = string.toCharArray();
        for (char c : characters) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }
}
