package edu.augustana.UI;

import edu.augustana.*;
import edu.augustana.Bots.Bot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class SandboxController {


    @FXML
    private VBox mainVbox;

    @FXML
    private ListView<Bot> agentList;

    @FXML
    private Button editScenarioBtn;

    @FXML
    private ChoiceBox<SimScenario> scenarioChoiceBox;

    @FXML
    private Label scenarioDescription;


    @FXML
    private Button startStopScenarioBtn;

    @FXML
    private Button newScenarioBtn;

    private ScenarioBuildController buildController;

    @FXML
    private Slider wpmSlider;


    private MainUiController mainUIController;

    @FXML
    private Button serverJoinLeaveBtn;

    @FXML
    private ListView<TextFlow> serverListView;

    @FXML
    private Button createServerBtn;

    @FXML
    private Tab serverInfoTab;


    @FXML
    private Button stopScenario;

    @FXML
    private Button updateServersBtn;

    @FXML
    private ListView<String> userList;

    @FXML
    private VBox chatLogVbox;

    @FXML
    private Button scenarioSendMessageButton;

    @FXML
    private TextField scenarioSendMessageField;

    @FXML
    private VBox scenarioChatLog;


    @FXML
    private Button sendMessageSeverButton;

    @FXML
    private TextField serverMessageField;

    @FXML
    private CheckBox showMorseServer;

    private Tab WPMTab;

    @FXML
    private Slider morsePlayerSlider;

    @FXML
    private CheckBox showMorseScenario;

    @FXML
    void initialize() throws Exception {

        ScenarioCollection.addScenario(SimScenario.getDefaultScenario());
        scenarioChoiceBox.getItems().addAll(ScenarioCollection.getCollection());
        scenarioChoiceBox.setValue(ScenarioCollection.getCollection().get(0));
        scenarioDescription.setText(scenarioChoiceBox.getValue().getDescription());
        agentList.getItems().addAll(scenarioChoiceBox.getValue().getBotCollection().getBots());

        updateListOfServer();

        morsePlayerSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int wpm = newValue.intValue();
            MorsePlayer.setWordsPerMinuteMultiplier(wpm);
        });


        sendMessageSeverButton.setStyle(
                "-fx-border-color: white; " +
                        "-fx-border-width: 1px; " +
                        "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 15px; " +
                        "-fx-border-radius: 15px;"
        );
        sendMessageSeverButton.setOnAction(event -> {

            try {

                HamRadioServerClient.sendMessage(TextToMorseConverter.textToMorse(serverMessageField.getText()));
                serverMessageField.setText("");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

        startStopScenarioBtn.setOnAction(evt -> {
            if(!scenarioChoiceBox.getValue().isPlaying){
                try {
                    scenarioChatLog.getChildren().clear();
                    scenarioChoiceBox.getValue().startScenario();
                    startStopScenarioBtn.textProperty().set("Stop");
                    scenarioChoiceBox.getValue().setParentController(this);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else if (Radio.isRadioOn()){
                scenarioChoiceBox.getValue().stopScenario();
                startStopScenarioBtn.textProperty().set("Start");
            }

        });

        createServerBtn.setOnAction(event -> {
            Stage serverBuildStage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("serverBuildUI.fxml"));
            serverBuildStage.setTitle("Server Builder");
            try {
                serverBuildStage.setScene(new Scene(loader.load()));
                HamRadioServerClient.setUIController(this);
                ServerBuildController controller = loader.getController();
                controller.parentController = this;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serverBuildStage.show();
            createServerBtn.setVisible(false);

        });

        newScenarioBtn.setOnAction(evt -> {
            try {
                Stage scenarioBuildStage = new Stage();
                FXMLLoader loader = new FXMLLoader(App.class.getResource("ScenarioBuildUI.fxml"));
                scenarioBuildStage.setTitle("Scenario Builder");
                scenarioBuildStage.setScene(new Scene(loader.load()));
                scenarioBuildStage.show();
                Thread.sleep(500);
                buildController = loader.getController();
                buildController.newScenario();
                buildController.setParentController(this);

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        editScenarioBtn.setOnAction(evt -> {
            try {
                Stage scenarioBuildStage = new Stage();
                FXMLLoader loader = new FXMLLoader(App.class.getResource("ScenarioBuildUI.fxml"));

                scenarioBuildStage.setTitle("Scenario Builder");
                scenarioBuildStage.setScene(new Scene(loader.load()));
                scenarioBuildStage.show();
                Thread.sleep(500);
                buildController = loader.getController();
                buildController.setParentController(this);
                buildController.editScenario(scenarioChoiceBox.getValue());

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        scenarioChoiceBox.setOnAction(event -> {
            scenarioChoiceBox.setValue(ScenarioCollection.getCollection().get(ScenarioCollection.getCollection().size() - 1));
            displayCurrentScenario();
        });

        wpmSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            MorsePlayer.setWordsPerMinuteMultiplier((int) wpmSlider.getValue());
        });

        MorsePlayer.setWordsPerMinuteMultiplier((int) wpmSlider.getValue());



        serverJoinLeaveBtn.setOnAction(event -> {
            if(HamRadioServerClient.isConnected == true){
                try {

                    HamRadioServerClient.disconnectServer();
                    serverJoinLeaveBtn.setText("Connect");
                    updateListOfServer();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else if(serverListView.getSelectionModel().getSelectedItem() !=  null && Radio.isRadioOn()){
                HamRadioServerClient.setUIController(this);
                Stage serverConnectStage = new Stage();
                FXMLLoader loader = new FXMLLoader(App.class.getResource("serverConnect.fxml"));
                serverConnectStage.setTitle("Connect To Server");

                try {
                    serverConnectStage.setScene(new Scene(loader.load()));
                    ServerConnectUI controller = loader.getController();
                    controller.parentController = this;

                    // Get the selected TextFlow from the ListView
                    TextFlow selectedTextFlow = (TextFlow) serverListView.getSelectionModel().getSelectedItem();

                    if (selectedTextFlow != null) {
                        // Extract the server ID from the TextFlow (assumes it's in the first Text node)
                        Text serverIdText = (Text) selectedTextFlow.getChildren().get(1); // Assuming the second child contains the server ID
                        String serverID = serverIdText.getText().trim();
                        controller.setServerID(serverID);
                    } else {
                        throw new IllegalStateException("No server selected!");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    System.err.println("Error retrieving server ID: " + e.getMessage());
                    e.printStackTrace();
                }

                serverConnectStage.show();

                try {
                    updateListOfServer();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        updateServersBtn.setOnAction(event -> {
            try {
                updateListOfServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //This checks if the message in the box is either in english or morse, and then adds the morse variation
        //of the message to the chat log
        scenarioSendMessageButton.setOnAction(evt -> {
            if (scenarioChoiceBox.getValue().isPlaying) {
                if (scenarioSendMessageField.getText() != null) {
                    String message = scenarioSendMessageField.getText();

                    scenarioSendMessageField.clear();

                    //actually checks if the message is correct and adds to chat log
                    scenarioChoiceBox.getValue().checkMessage(TextToMorseConverter.textToMorse(message));

                }
            }





        });

    }



    public void setMainUIControllerController(MainUiController controller) {
        mainUIController = controller;
    }

    public void updateScenarioChoice(){
        scenarioChoiceBox.getItems().clear();
        scenarioChoiceBox.getItems().addAll(ScenarioCollection.getCollection());

    }

    public void setCurrentScenario(SimScenario scenario){
        scenarioChoiceBox.setValue(scenario);
    }

    public void displayCurrentScenario(){
        scenarioDescription.setText(scenarioChoiceBox.getValue().getDescription());

    }

    public void displayBots(){
        agentList.getItems().clear();
        agentList.getItems().addAll(scenarioChoiceBox.getValue().getBotCollection().getBots());
    }


    private void openScenarioBuilder() throws IOException {
        //load fxml
        Stage scenarioBuildStage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("ScenarioBuildUI.fxml"));
        buildController = loader.getController();
        scenarioBuildStage.setTitle("Scenario Builder");
        scenarioBuildStage.setScene(new Scene(loader.load()));
        scenarioBuildStage.show();
    }


    public void setConnected() {
        serverJoinLeaveBtn.setText("Disconnect");
    }

    public void updateListOfServer() throws Exception {
        serverListView.setStyle("-fx-text-fill: black; -fx-control-inner-background: #cccccc;");
        serverListView.getItems().clear();

        Set<String> serverIdSet = HamRadioServerClient.getAvailableServers().keySet();

        if(!serverIdSet.isEmpty()){
            for (String servId : serverIdSet) {
                // Create Text nodes for different parts of the string
                Text boldServerId = new Text("Server ID: ");
                boldServerId.setStyle("-fx-font-weight: bold;");

                Text serverIdText = new Text(servId + " ");

                Text boldNoiseLevel = new Text("Background Noise Level: ");
                boldNoiseLevel.setStyle("-fx-font-weight: bold;");

                Text noiseLevelText = new Text(HamRadioServerClient.getServerCondition(servId) + " ");


                Text boldUserCount = new Text("User Count: ");
                boldUserCount.setStyle("-fx-font-weight: bold;");

                Text userCountText = new Text(HamRadioServerClient.getAvailableServers().get(servId).size() + "");

                // Combine all Text nodes into a TextFlow
                TextFlow textFlow = new TextFlow(boldServerId, serverIdText, boldNoiseLevel, noiseLevelText, boldUserCount, userCountText);

                // Add TextFlow to the ListView
                serverListView.getItems().add(textFlow);
            }
        }else{
            serverListView.getItems().add(new TextFlow(new Text("No servers Available Yet")));
        }



    }

    public void setCreateServerVisible(boolean bool){
        createServerBtn.setVisible(bool);
    }

    public void addMessageToServerUI(String message, String morseMessage){

        Platform.runLater(() -> {
            Label labelMessage = new Label(message);
            labelMessage.setWrapText(true);
            labelMessage.setPrefWidth(275);
            labelMessage.setPrefHeight(Region.USE_COMPUTED_SIZE);
            chatLogVbox.getChildren().add(labelMessage);

            if(showMorseServer.isSelected() && !morseMessage.equals("")){
                Label labelMessage2 = new Label("Morse: " + morseMessage);
                labelMessage2.setWrapText(true);
                labelMessage2.setPrefWidth(275);
                labelMessage2.setPrefHeight(Region.USE_COMPUTED_SIZE);
                chatLogVbox.getChildren().add(labelMessage2);
            }

        });
    }

    public void addMessageToScenarioUI(String message, String morseMessage){

        Platform.runLater(() -> {
            Label labelMessage = new Label(message);
            labelMessage.setWrapText(true);
            labelMessage.setPrefWidth(380);
            labelMessage.setPrefHeight(Region.USE_COMPUTED_SIZE);
            scenarioChatLog.getChildren().add(labelMessage);

            if(showMorseScenario.isSelected() && !morseMessage.equals("")){
                Label labelMessage2 = new Label("Morse: " + morseMessage);
                labelMessage2.setWrapText(true);
                labelMessage2.setPrefWidth(275);
                labelMessage2.setPrefHeight(Region.USE_COMPUTED_SIZE);
                scenarioChatLog.getChildren().add(labelMessage2);
            }


        });


    }

    public void setUserName(String name){
        HamRadioServerClient.setUserName(name);
    }

    public void updateUserList(String serverId){

        List<String> users = HamRadioServerClient.getAvailableServers().get(serverId);
        userList.getItems().clear();
        userList.getItems().addAll(users);
    }

    public boolean isTextFieldActive() {
        return serverMessageField.isFocused();
    }

    public void clearServerChat(){
        chatLogVbox.getChildren().clear();
    }

}
