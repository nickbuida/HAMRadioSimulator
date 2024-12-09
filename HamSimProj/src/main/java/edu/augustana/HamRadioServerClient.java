package edu.augustana;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import edu.augustana.UI.SandboxController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class HamRadioServerClient {

    private static SandboxController uiController;
    private static String userName;

    private static final String API_URL = "http://34.27.101.208:8000"; // Replace with FastAPI server's URL
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static HamRadioWebSocketClient socketClient = new HamRadioWebSocketClient();
    public static boolean isConnected = false;
    private static String currentServerID = "";

    // Method to create a server by sending a POST request
    public static void createServer(String serverId, double noiseLevel) throws Exception {
        String url = String.format("%s/server/%s?noise_level=%.2f", API_URL, serverId, noiseLevel);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody()) // POST with no body
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to create server: " + response.body());
        }
    }

    // Method to retrieve the list of available servers and their connected clients
    public static Map<String, List<String>> getAvailableServers() {
        Map<String, List<String>> serverClientsMap = new HashMap<>();
        Gson gson = new Gson();

        try {
            HttpRequest serverRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/servers"))
                    .GET()
                    .build();
            HttpResponse<String> serverResponse = httpClient.send(serverRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonResponse = gson.fromJson(serverResponse.body(), JsonObject.class);
            JsonArray activeServersArray = jsonResponse.getAsJsonArray("active_servers");

            if (activeServersArray == null || activeServersArray.size() == 0) {
                System.out.println("No servers available");
                return serverClientsMap;
            }

            for (int i = 0; i < activeServersArray.size(); i++) {
                String serverId = activeServersArray.get(i).getAsString();

                HttpRequest clientRequest = HttpRequest.newBuilder()
                        .uri(URI.create(API_URL + "/server/" + serverId + "/clients"))
                        .GET()
                        .build();
                HttpResponse<String> clientResponse = httpClient.send(clientRequest, HttpResponse.BodyHandlers.ofString());

                JsonObject clientResponseJson = gson.fromJson(clientResponse.body(), JsonObject.class);
                JsonArray clientsArray = clientResponseJson.getAsJsonArray("clients");

                List<String> clients = gson.fromJson(clientsArray, new TypeToken<List<String>>() {}.getType());
                serverClientsMap.put(serverId, clients);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return serverClientsMap;
    }

    // Method to connect to a server
    public static void connectToServer(String serverId, String userID) throws Exception {
        currentServerID = serverId;
        isConnected = true;
        socketClient.connect(serverId,userID);
        uiController.addMessageToServerUI("Connected to server: " + serverId,"");
        uiController.updateUserList(serverId);
        Radio.setNoiseAmplitude(getServerCondition(serverId));
        uiController.updateListOfServer();
    }

    // Method to send a message via WebSocket
    public static void sendMessage(String message) throws Exception {

        if (isConnected) {
            String formattedMessage = String.valueOf(Radio.getSelectedTuneFreq()) + "," + String.valueOf(Radio.generateFrequencyRange(Radio.getBand())) + "," + message + "," + getUserName();
            socketClient.sendMessage(formattedMessage);
        }
    }

    // Method to disconnect from the server
    public static void disconnectServer() throws Exception {
        currentServerID = "";
        isConnected = false;
        socketClient.disconnectWebSocket();
        uiController.clearServerChat();
    }

    // Method to handle incoming messages
    public static void handleReceivedMessage(String message) throws InterruptedException {

        uiController.updateUserList(currentServerID);


        String[] messageParts = message.split(",", 4);
        double frequency = Double.valueOf(messageParts[0]);
        double range = Double.valueOf(messageParts[1]);
        String user = messageParts[3];
        String morseMessage = messageParts[2];

        String formattedMessage = user + ": " + TextToMorseConverter.spacedMorseToText(morseMessage);
        System.out.println(message);
        MorsePlayer.playBotMorseString(morseMessage, frequency, range);
        uiController.addMessageToServerUI(formattedMessage,morseMessage);
    }

    public static String getServerURL() {
        return API_URL;
    }

    public static double getServerCondition(String serverId) throws Exception {
        String url = API_URL + "/server/" + serverId + "/conditions";
        Gson gson = new Gson();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            return jsonResponse.get("noise_level").getAsDouble(); // Extract and return only the noise level
        } else {
            throw new RuntimeException("Failed to retrieve server condition: " + response.body());
        }
    }

    public static void setUIController(SandboxController controller) {
        uiController = controller;
    }

    public static void setUserName(String name){
        userName = name;
    }

    public static String getUserName(){return userName;}
}
