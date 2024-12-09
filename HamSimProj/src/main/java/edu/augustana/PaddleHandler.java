package edu.augustana;

import javafx.scene.input.KeyCode;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


import static edu.augustana.CWHandler.stopTimer;
import static edu.augustana.Radio.*;
import static edu.augustana.Radio.stopTone;

public class PaddleHandler {
    private static int wordsPerMinute = 18;
    private static long dotDurationPaddle = 1200000000L / 20;
    private static long dashDurationPaddle = dotDurationPaddle * 3;
    private static boolean dotPaddlePressed;
    private static boolean dashPaddlePressed;
    private static StringBuilder cwString = CWHandler.getCwStringBuilder();
    private static long paddleReleaseTime;
   // private static Boolean alreadyPressed = true;





    public static void playContinuousDot() throws Exception {
        if (!dotPaddlePressed && !dashPaddlePressed) {
            // System.out.println("tone and dot loop start");
            stopSpaceTimer();
            dotPaddlePressed = true;
            while (dotPaddlePressed) {
                playTone(Radio.getCwToneFreq());
                try {
                    //CWHandler.addToCwString(".");
                    cwString.append(".");
                    Thread.sleep(TimeUnit.NANOSECONDS.toMillis(dotDurationPaddle));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                stopTone();
                try {
                    Thread.sleep(TimeUnit.NANOSECONDS.toMillis(dotDurationPaddle));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //  System.out.println("tone and dot loop end");
            }
        }
    }

    public static void playContinuousDash() throws Exception {
        if (!dashPaddlePressed && !dotPaddlePressed) {
            stopSpaceTimer();
            dashPaddlePressed = true;
            while (dashPaddlePressed) {
                playTone(Radio.getCwToneFreq());
                try {
                   // CWHandler.addToCwString("-");
                    cwString.append("-");
                    Thread.sleep(TimeUnit.NANOSECONDS.toMillis(dashDurationPaddle));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                stopTone();
                try {
                    Thread.sleep(TimeUnit.NANOSECONDS.toMillis(dotDurationPaddle));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void stopPaddlePress() throws Exception {
        dotPaddlePressed = false;
        dashPaddlePressed = false;
        startSpaceTimer();
    }

    public static void startSpaceTimer() throws Exception {
        paddleReleaseTime = System.nanoTime();

    }

    public static void stopSpaceTimer() throws Exception {
        int multiplier = 20 / wordsPerMinute;
        long timeSinceReleased = System.nanoTime() - paddleReleaseTime;
        if (timeSinceReleased > (dotDurationPaddle * 7 - 1) * multiplier) {

            cwString.append("/*/");
            System.out.println(cwString.toString());
        } else if (timeSinceReleased > ((dotDurationPaddle * 3) + ((dotDurationPaddle * 3) * 0.2)) * multiplier) {
            cwString.append("/");
        }
    }

    public static void setWordsPerMinute(Integer wpm) {
        wordsPerMinute = wpm;
    }

    public static int getWordsPerMinute() {
        return wordsPerMinute;
    }

    public static String getCwString() {
        return cwString.toString();
    }

}
