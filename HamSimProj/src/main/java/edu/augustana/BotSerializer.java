package edu.augustana;
import com.google.gson.*;
import edu.augustana.Bots.Bot;
import edu.augustana.Bots.ResponsiveBot;

import java.lang.reflect.Type;

public class BotSerializer implements JsonSerializer<Bot> {
    @Override
    public JsonElement serialize(Bot src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();

        // Add the common fields
        json.addProperty("name", src.getName());
        json.addProperty("callSign", src.getTextCallSign());
        json.addProperty("botPhrase", src.getTextBotPhrase());

        // Add the type field to distinguish subclasses
        if (src instanceof ResponsiveBot) {
            json.addProperty("type", "ResponsiveBot");
            ResponsiveBot responsiveBot = (ResponsiveBot) src;
            json.addProperty("expectedAnswer", responsiveBot.getExpectedAnswer());
            json.addProperty("answerFreq", responsiveBot.getAnswerFreq());
        }
        // Add other subclasses of Bot here if necessary (for example, AI bots)

        return json;
    }
}
