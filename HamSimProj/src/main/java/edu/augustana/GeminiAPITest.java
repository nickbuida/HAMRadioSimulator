package edu.augustana;

import swiss.ameri.gemini.api.*;
import swiss.ameri.gemini.gson.GsonJsonParser;
import swiss.ameri.gemini.spi.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class GeminiAPITest {


    // replace the following with a real API key from https://aistudio.google.com
    private static final String API_KEY = "AIzaSyBdtDb5rL_wP8aXegcVpRo-bZIWjalQNQw";

    public static String getGeminiApiKey() {
        if (API_KEY == null || API_KEY.equals("")) {
            throw new IllegalStateException("API_KEY is null/empty. Please set it to a valid API key from https://aistudio.google.com");
        }
        return API_KEY;
    }

    private GeminiAPITest() {
        throw new AssertionError("Not instantiable");
    }
    public static void main(String[] args) throws Exception {
        JsonParser parser = new GsonJsonParser();

        try (var genAi = new GenAi(getGeminiApiKey(), parser)) {
            // each method represents an example usage
            listModels(genAi);
            getModel(genAi);
            countTokens(genAi);
            generateContent(genAi);
            generateContentStream(genAi);
            multiChatTurn(genAi);
//            textAndImage(genAi);
//            embedContents(genAi);
        }


    }

    private static void listModels(GenAi genAi) {
        System.out.println("----- List models");
        genAi.listModels()
                .forEach(System.out::println);
    }
    private static void getModel(GenAi genAi) {
        System.out.println("----- Get Model");
        System.out.println(
                genAi.getModel(ModelVariant.GEMINI_1_5_FLASH)
        );
    }

    private static GenerativeModel createStoryModel() {
        return GenerativeModel.builder()
                .modelName(ModelVariant.GEMINI_1_5_FLASH)
                .addContent(Content.textContent(
                        Content.Role.USER,
                        "You are the chat bot. Act like a person talking through a HAM radio. Respond to this message.."
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

    private static void countTokens(GenAi genAi) {
        System.out.println("----- count tokens");
        var model = createStoryModel();
        Long result = genAi.countTokens(model)
                .join();
        System.out.println("Tokens: " + result);
    }
    private static void generateContent(GenAi genAi) throws InterruptedException, ExecutionException, TimeoutException {
        var model = createStoryModel();
        System.out.println("----- Generate content (blocking)");
        genAi.generateContent(model)
                .thenAccept(gcr -> {
                    System.out.println(gcr);
                    System.out.println("----- Generate content (blocking) usage meta data & safety ratings");
                    System.out.println(genAi.usageMetadata(gcr.id()));
//                    List<GenAi.SafetyRating> safetyRatings = genAi.safetyRatings(gcr.id());
//                    if (safetyRatings != null) {
//                        System.out.println(safetyRatings.stream().map(GenAi.SafetyRating::toTypedSafetyRating).toList());
//                    } else {
//                        System.out.println("Safety ratings are null");
//                    }
                })
                .get(20, TimeUnit.SECONDS);
    }
    private static void generateContentStream(GenAi genAi) {
        System.out.println("----- Generate content (streaming) -- with usage meta data");
        var model = createStoryModel();
        genAi.generateContentStream(model)
                .forEach(x -> {
                    System.out.println(x);
                    // note that the usage metadata is updated as it arrives
                    System.out.println(genAi.usageMetadata(x.id()));
//                    System.out.println(genAi.safetyRatings(x.id()));
                });
    }
    private static void multiChatTurn(GenAi genAi) {
        System.out.println("----- multi turn chat");
        GenerativeModel chatModel = GenerativeModel.builder()
                .modelName(ModelVariant.GEMINI_1_5_FLASH)
                .addContent(new Content.TextContent(
                        Content.Role.USER.roleName(),
                        "Write the first line of a story about a magic backpack."
                ))
                .addContent(new Content.TextContent(
                        Content.Role.MODEL.roleName(),
                        "In the bustling city of Meadow brook, lived a young girl named Sophie. She was a bright and curious soul with an imaginative mind."
                ))
                .addContent(new Content.TextContent(
                        Content.Role.USER.roleName(),
                        "Can you set it in a quiet village in 1600s France? Max 30 words"
                ))
                .build();
        genAi.generateContentStream(chatModel)
                .forEach(System.out::println);
    }


}
