package edu.augustana;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FrequencySliderTest {


    @Test
    public void testLogFrequency() {
    //    assertEquals(304.46, MorsePlayer.logFrequency(400, 59898, 586), .1);
    }

    @Test
    public void testCheckIfHigherThanBot() {
        assertEquals(32.2, MorsePlayer.checkIfHigherThanBot(400, 500, 667.8), .1);
        assertEquals(600, MorsePlayer.checkIfHigherThanBot(400, 400, 600), .1);
    }

}
