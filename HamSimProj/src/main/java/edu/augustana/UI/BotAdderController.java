package edu.augustana.UI;

import edu.augustana.BotCollection;
import edu.augustana.Bots.ContinuousMessageBot;
import edu.augustana.Bots.ResponsiveBot;
import edu.augustana.Radio;
import edu.augustana.SimScenario;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BotAdderController {

    @FXML
    private Button addBtn;

    @FXML
    private TextField botNameField;

    @FXML
    private TextField callSignField;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField messageField;

    private SimScenario scenario;

    private BotCollection botCollection;

    private ScenarioBuildController parentController;


    @FXML
    private TextField answerFreqField;

    @FXML
    private TextField expectedAnswerField;

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

        //this section of code makes sure that the frequency is actually a double
        try {
            Double.parseDouble(answerFreqField.getText());
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Alert");
            alert.setContentText("Only put a double value in the frequency");
            alert.showAndWait();

            return; //makes sure that the rest of this method doesn't complete. This stops some sort of looping thing happening
        }

        //this section of code will check if the frequency is actually in the band that you are in
        //This if statement checks if the freq is in the range of the band
        if (Double.parseDouble(answerFreqField.getText()) < Radio.getMinFreqInBand() || Double.parseDouble(answerFreqField.getText()) > Radio.getMinFreqInBand() + Radio.generateFrequencyRange(Radio.getBand())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Alert");
            double maxFreq = Radio.getMinFreqInBand() + Radio.generateFrequencyRange(Radio.getBand());
            alert.setContentText("Valid frequencies are between " + Radio.getMinFreqInBand() + " and " + maxFreq + ", in the band you are in.");
            alert.showAndWait();

            return; //makes sure that the rest of this method doesn't complete. This stops some sort of looping thing happening
        }


        ResponsiveBot bot = new ResponsiveBot(Radio.getBand(), botNameField.getText(), callSignField.getText(), messageField.getText(), Double.parseDouble(answerFreqField.getText()), expectedAnswerField.getText());
        botCollection.addBot(bot);

    }

    //Clears the text boxes so that when you add it, it doesn't keep the same info
    private void clearBotAdderTextBoxes() {
        botNameField.clear();
        callSignField.clear();
        messageField.clear();
        answerFreqField.clear();
        expectedAnswerField.clear();
    }


}
