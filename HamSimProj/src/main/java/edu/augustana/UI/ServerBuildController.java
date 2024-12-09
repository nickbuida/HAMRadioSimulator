package edu.augustana.UI;

import edu.augustana.HamRadioServerClient;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ServerBuildController {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button createServerBtn;

    @FXML
    private TextField scenarioNameField;

    public SandboxController parentController;

    @FXML
    private Slider noiseSlider;

    @FXML
    private TextField userIDField;


    @FXML
    void initialize(){


        cancelBtn.setOnAction(evt -> {
            parentController.setCreateServerVisible(true);
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.close();
        });


        createServerBtn.setOnAction(event -> {

            try {
                HamRadioServerClient.createServer(scenarioNameField.getText(),noiseSlider.getValue());
                HamRadioServerClient.connectToServer(scenarioNameField.getText(),userIDField.getText());
                HamRadioServerClient.setUserName(userIDField.getText());
                parentController.setConnected();
                parentController.updateListOfServer();
                parentController.setCreateServerVisible(true);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });

    }



}
