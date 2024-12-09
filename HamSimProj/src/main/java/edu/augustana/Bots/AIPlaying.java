package edu.augustana.Bots;

import edu.augustana.MorsePlayer;
import edu.augustana.TextToMorseConverter;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import swiss.ameri.gemini.api.*;

public class AIPlaying implements PlayingBehavior{

    private final AIBot bot;

    public AIPlaying(AIBot bot) {
        this.bot = bot;
    }

    @Override
    public void startBehavior() {
        new Thread(() -> {
            try {
                MorsePlayer.playBotMorseString(TextToMorseConverter.textToMorse("Talk to me at " + bot.getOutputFrequency()), bot.getOutputFrequency(), bot.getFrequencyRange());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }

    public void playResponse(String userMessage) {
        new Thread(() -> {

            String fullPrompt = bot.getSystemPromptText() + "\n" +
                    "Your name is: " + bot.getName() + "\n" +
                    "Respond to the following message in 10 words or less by using the description of your character above:\n"
                    + userMessage;

            requestMessage(fullPrompt);

        }).start();
    }

    public void playRandom() {
        new Thread(() -> {

            //add an if statement here to see if they had been talked to yet. If not, then make them say cq then call sign. Just use a boolean

            String fullPrompt = bot.getSystemPromptText() + "\n" +
                    "Your name is: " + bot.getName() + "\n" +
                    "Generate a random message in 10 words or less by using the description of your character above.";

            requestMessage(fullPrompt);

        }).start();
    }

    public void requestMessage(String fullPrompt){

        var model = createBotModel(fullPrompt);

        bot.getGenAi().generateContent(model)
                .thenAccept(gcr -> {
                    String geminiResponse = gcr.text();
                    System.out.println("Debug: AIBot received response: " + geminiResponse);
                    //add message to chatlog and play the message in morse
                });

    }
    private GenerativeModel createBotModel(String fullPrompt) {
        return GenerativeModel.builder()
                .modelName(ModelVariant.GEMINI_1_5_FLASH)
                .addContent(Content.textContent(
                        Content.Role.USER,
                        fullPrompt
                ))
                .addSafetySetting(SafetySetting.of(
                        SafetySetting.HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT,
                        SafetySetting.HarmBlockThreshold.BLOCK_ONLY_HIGH
                ))
                .generationConfig(new GenerationConfig(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ))
                .build();
    }

}
