package edu.augustana.UI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.augustana.*;
import edu.augustana.Bots.Bot;
import edu.augustana.Bots.ContinuousMessageBot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ScenarioBuildController {

    @FXML
    private Button addBotBtn;

    @FXML
    private ListView<Bot> botListTable;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button deletebotBtn;

    @FXML
    private TextArea descripTionField;

    @FXML
    private TextField humidityField;

    @FXML
    private Button saveScenarioBtn;

    @FXML
    private TextField scenarioNameField;

    @FXML
    private ChoiceBox<String> scenarioTypeChoice;

    @FXML
    private TextField solarIndex;

    @FXML
    private TextField tempField;

    @FXML
    private TextField windSpeedField;

    @FXML
    private Button loadBtn;

    @FXML
    private Button saveFileBtn;

    private SimScenario scenario;

    private BotAdderController adderController;

    private BotCollection botCollection;

    private RadioEnvironment environment;

    private SandboxController parentController;

    private boolean isNewScenario = true;




    @FXML
    void initialize(){

        scenarioTypeChoice.setValue("Responsive");
        scenarioTypeChoice.getItems().add("Responsive");
        scenarioTypeChoice.getItems().add("AI");

        addBotBtn.setOnAction(event -> {
            try {
                openBotAdder();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        cancelBtn.setOnAction(evt -> {
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.close();
        });

        saveScenarioBtn.setOnAction(event -> {

            createScenario();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        });

        saveFileBtn.setOnAction(event -> {
            if(scenario == null){
                scenario = createScenario();
                scenario.saveToFile();
            }else{
                scenario.saveToFile();
            }

        });

        loadBtn.setOnAction(event -> {
            openFile();
        });

        deletebotBtn.setOnAction(event -> {
            deleteBot();
        });

    }

    public void loadScenario(){
        environment  = scenario.getEnvironment();
        botCollection  = scenario.getBotCollection();
        descripTionField.setText(scenario.getDescription());
        scenarioNameField.setText(scenario.getName());
        tempField.setText(String.valueOf(environment.temperature));
        humidityField.setText(String.valueOf(environment.humidity));
        windSpeedField.setText(String.valueOf(environment.windSpeed));
        solarIndex.setText(String.valueOf(environment.solarActivity));
        scenarioTypeChoice.setValue(scenario.getType());
        updateBotListView();
    }

    public void setScenario(SimScenario scenario){
        this.scenario = scenario;
    }

    public void updateBotListView(){
        botListTable.getItems().clear();
        botListTable.getItems().addAll(botCollection.getBots());
    }

    public void deleteBot(){
        if(botListTable.getSelectionModel().getSelectedItem() !=  null){
            this.botCollection.deleteBot(botListTable.getSelectionModel().getSelectedItem());
            updateBotListView();
        }


    }


    private void openBotAdder() throws IOException {
        Stage scenarioBuildStage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("BotAdder.fxml"));

        scenarioBuildStage.setTitle("Scenario Builder");
        scenarioBuildStage.setScene(new Scene(loader.load()));
        scenarioBuildStage.show();

        adderController = loader.getController();
        adderController.setBotCollection(botCollection);
        adderController.setParentController(this);
    }

    public void editScenario(SimScenario scenario){

        isNewScenario = false;

        this.scenario = scenario;

        loadScenario();

    }

    public void loadFromFile(){
        isNewScenario =  false;
    }
    public void openFile() {
        Stage fileChooserStage = new Stage();

        // Set up the FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        // Open the FileChooser dialog
        File selectedFile = fileChooser.showOpenDialog(fileChooserStage);

        // If a file is selected, deserialize it
        if (selectedFile != null) {
            deserializeJson(selectedFile);
        }
    }

    public void deserializeJson(File file) {
        // Create a Gson instance with the custom deserializer for Bot
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Bot.class, new BotDeserializer())  // Register the custom deserializer for Bot
                .setPrettyPrinting()  // Optional: Makes JSON output more readable
                .create();

        try (FileReader reader = new FileReader(file)) {
            // Deserialize the SimScenario object (which includes BotCollection)
            this.scenario = gson.fromJson(reader, SimScenario.class);
            loadScenario();  // Load the deserialized scenario
            System.out.println("Deserialization successful.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void newScenario(){
        botCollection = new BotCollection(new ArrayList<Bot>());
    }

    private SimScenario createScenario(){

        if(isNewScenario){

            environment = new RadioEnvironment("scenarioNameField.getText()",
                    Double.parseDouble(solarIndex.getText()),
                    Double.parseDouble(windSpeedField.getText()),
                    Double.parseDouble(humidityField.getText()),
                    Double.parseDouble(tempField.getText()));
                    SimScenario newScenario = new SimScenario(scenarioNameField.getText(),descripTionField.getText(),
                    environment, botCollection, scenarioTypeChoice.getValue());

            ScenarioCollection.addScenario(newScenario);
            parentController.updateScenarioChoice();
            parentController.displayBots();
            return newScenario;
        }else{

            environment = new RadioEnvironment("scenarioNameField.getText()",
                    Double.parseDouble(solarIndex.getText()),
                    Double.parseDouble(windSpeedField.getText()),
                    Double.parseDouble(humidityField.getText()),
                    Double.parseDouble(tempField.getText()));

            scenario.setScenarioName(scenarioNameField.getText());
            scenario.setDescription(descripTionField.getText());
            scenario.setEnvironment(environment);
            parentController.updateScenarioChoice();
            parentController.displayBots();
            return scenario;
        }
    }


    public void setParentController(SandboxController controller){
        parentController = controller;
    }


}
