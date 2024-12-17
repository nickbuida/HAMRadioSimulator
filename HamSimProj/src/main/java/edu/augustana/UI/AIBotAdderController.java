package edu.augustana.UI;

import edu.augustana.BotCollection;
import edu.augustana.Bots.AIBot;
import edu.augustana.Bots.ResponsiveBot;
import edu.augustana.Radio;
import edu.augustana.SimScenario;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AIBotAdderController {

    @FXML
    private Button addBtn;

    @FXML
    private TextField botNameField;

    @FXML
    private TextField callSignField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField aiBotPromptTextField;

    private SimScenario scenario;

    private BotCollection botCollection;

    private ScenarioBuildController parentController;

    @FXML
    void initialize(){

        addBtn.setOnAction( event -> {
            try {
                addBot();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                parentController.updateBotListView();

                stage.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            clearBotAdderTextBoxes();

        });


        cancelBtn.setOnAction(evt -> {
            Stage stage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
            stage.close();
        });


    }

    public void setParentController(ScenarioBuildController parentController) {
        this.parentController = parentController;
    }

    public void setBotCollection(BotCollection collection){
        botCollection = collection;
    }

    private void addBot() throws InterruptedException {


        AIBot bot = new AIBot(Radio.getBand(), botNameField.getText(), callSignField.getText(), aiBotPromptTextField.getText());
        botCollection.addBot(bot);

    }

    //Clears the text boxes so that when you add it, it doesn't keep the same info
    private void clearBotAdderTextBoxes() {
        botNameField.clear();
        callSignField.clear();
        aiBotPromptTextField.clear();
    }


}
