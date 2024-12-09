package edu.augustana;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextToMorseTest {

    @Test
    public void testMorseToEnglish() {
        //Simple english test cases
        assertEquals("HELLO", TextToMorseConverter.morseToText("...././.-../.-../---"));
        assertEquals("WORLD", TextToMorseConverter.morseToText(".--/---/.-./.-../-.."));
        //Numbers
        assertEquals("123", TextToMorseConverter.morseToText(".----/..---/...--"));
        //Numbers and letters
        assertEquals("123HELLO", TextToMorseConverter.morseToText(".----/..---/...--/...././.-../.-../---"));
        // Space between words
        assertEquals("HELLO WORLD", TextToMorseConverter.morseToText("...././.-../.-../---/*/.--/---/.-./.-../-.."));
    }

    @Test
    public void testEnglishToMorse() {
        // Simple cases
        assertEquals(".... . .-.. .-.. ---", TextToMorseConverter.textToMorse("HELLO"));
        assertEquals(".-- --- .-. .-.. -..", TextToMorseConverter.textToMorse("WORLD"));
        // Numbers
        assertEquals(".---- ..--- ...--", TextToMorseConverter.textToMorse("123"));
        // Numbers and Letters
        assertEquals(".---- ..--- ...-- .... . .-.. .-.. --- .-- --- .-. .-.. -..", TextToMorseConverter.textToMorse("123HELLOWORLD"));
        // Space between words
        assertEquals(".... . .-.. .-.. --- * .-- --- .-. .-.. -..", TextToMorseConverter.textToMorse("HELLO WORLD"));
    }

    @Test
    public void testEdgeCases() {
        // Empty input
        assertEquals("", TextToMorseConverter.textToMorse(""));
        assertEquals("", TextToMorseConverter.morseToText(""));

        // Unsupported characters
        //Potentially depending on the need for this more supported characters can be added
        assertEquals("? ? ? ?", TextToMorseConverter.textToMorse("@#$%"));

        // Mixed case
        assertEquals(".... . .-.. .-.. ---", TextToMorseConverter.textToMorse("Hello"));
        assertEquals("HELLO", TextToMorseConverter.morseToText("...././.-../.-../---"));

        // Extra spaces in Morse code input
        assertEquals("HELLO", TextToMorseConverter.morseToText(" ...././.-../.-../---"));

        // Invalid Morse code
        assertEquals("?", TextToMorseConverter.morseToText("......"));
    }


}
