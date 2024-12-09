package edu.augustana.UI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import edu.augustana.App;
import javafx.stage.Stage;

public class InstructionsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button okButton;

    private MainUiController controller;

    private Stage stage;

    public static boolean isShowing;

    @FXML
    void initialize() {
        //assert okButton != null : "fx:id=\"okButton\" was not injected: check your FXML file 'Instructions.fxml'.";
        okButton.setOnAction(evt -> {
            isShowing = false;
            stage.close();
        });
    }

    public void setParentController(MainUiController parentController, Stage stage) {
        this.stage = stage;
        controller = parentController;
    }


}
