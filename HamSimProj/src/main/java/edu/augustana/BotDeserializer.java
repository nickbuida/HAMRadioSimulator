package edu.augustana;

import com.google.gson.*;
import edu.augustana.Bots.Bot;
import edu.augustana.Bots.ResponsiveBot;

import java.lang.reflect.Type;

public class BotDeserializer implements JsonDeserializer<Bot> {
    public ResponsiveBot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Get the "type" field to determine which subclass to instantiate
        String type = jsonObject.get("type").getAsString();

        if ("ResponsiveBot".equals(type)) {
            // Deserialize as a ResponsiveBot
            String name = jsonObject.get("name").getAsString();
            String callSign = jsonObject.get("callSign").getAsString();
            String botPhrase = jsonObject.get("botPhrase").getAsString();
            double answerFreq = jsonObject.get("answerFreq").getAsDouble();
            String expectedAnswer = jsonObject.get("expectedAnswer").getAsString();

            // Assuming constructor matches the fields
            return new ResponsiveBot(1, name, callSign, botPhrase, answerFreq, expectedAnswer);
        }

        // Handle other subclasses of Bot here (e.g., AI bots)
        return null; // Default fallback (or throw an exception)
    }
}

