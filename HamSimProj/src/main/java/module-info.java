module edu.augustana {
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.controls;
    requires javax.websocket.api;
    requires tyrus.client;
    requires com.google.gson;
    requires java.sql;
    requires java.net.http;
    requires swiss.ameri.gemini.api;
    requires swiss.ameri.gemini.gson;

    // Ensure Gson is required
    opens edu.augustana;// Opens the package to Gson

    exports edu.augustana;
    exports edu.augustana.Bots;
    opens edu.augustana.Bots;
    exports edu.augustana.UI;
    opens edu.augustana.UI;
}
