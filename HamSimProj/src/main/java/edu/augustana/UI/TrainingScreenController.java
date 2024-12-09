package edu.augustana.UI;

import edu.augustana.CWFlashcards;
import edu.augustana.CWHandler;
import edu.augustana.HandleListeningSim;
import edu.augustana.PaddleHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;


public class TrainingScreenController {

    //All FXML stuff for the listening training tab. Will have to
    //delete a lot of this because it is repeated, and also we are
    //going to get rid of a lot of the radio in this tab.

    @FXML
    private TextField botCallSignTextField;

    @FXML
    private TextField botMessageTextField;

    @FXML
    private ScrollPane enteredGuessesScrollPane;

    @FXML
    private VBox guessedMessagesVBox;


    @FXML
    private Button startSimButton;

    @FXML
    private Button stopSimButton;

    @FXML
    private Button submitGuessButton;

    @FXML
    private Label correctIncorrectLabel;

    @FXML
    private Button playLetterButton;

    @FXML
    private TextField guessTextField;

    @FXML
    private Button submitButton;

    @FXML
    private Label trainingTypeLabel;

    @FXML
    private ChoiceBox<String> trainingTypeChoiceBox;

    @FXML
    private Label difficultyLabel;

    @FXML
    private CheckBox lettersCheckBox;

    @FXML
    private CheckBox numbersCheckBox;

    @FXML
    private CheckBox abbrevCheckBox;

    @FXML
    private Button startTrainingButton;

    @FXML
    private Label lettersLabel;

    @FXML
    private Label guessLabel;

    @FXML
    private Button endButton;

    private String[] trainingTypeArray = {"Listening", "Typing"};

    private ImageView cheatSheetImage;




    @FXML
    void initialize() {

        //Initializing the listening training tab. Need to add code here. Need to at least initialize the tune in slider
        startSimButton.setOnAction(evt -> {
            try {
                HandleListeningSim.openBotView(guessedMessagesVBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        assert cheatSheetImage != null : "fx:id=\"cheatSheetImage\" was not injected: check your FXML file 'TrainingScreen.fxml'.";

        submitGuessButton.setOnAction(evt -> {HandleListeningSim.checkGuess(botCallSignTextField.getText(), botMessageTextField.getText(), guessedMessagesVBox);
            botCallSignTextField.clear();
            botMessageTextField.clear();});


        stopSimButton.setOnAction(evt -> HandleListeningSim.stopSim(guessedMessagesVBox));

        //CW Flashcards Section
        setDefaultMenu();
        trainingTypeChoiceBox.setItems(FXCollections.observableArrayList(trainingTypeArray));
        submitButton.setOnAction(evt-> {
            if (playLetterButton.getText().equals("Play Letter")) {
                if (CWFlashcards.handleGuess(guessTextField.getText().toUpperCase(), true)) {
                    correctIncorrectLabel.setText("Correct!");
                    guessTextField.setText("");
                } else {
                    correctIncorrectLabel.setText("Incorrect, try again!");
                }
                correctIncorrectLabel.setVisible(true);
            } else if (playLetterButton.getText().equals("Next Letter")) {
                try {
                    System.out.println("CW STRING: " + CWHandler.getCwString());
                    String morse = CWHandler.getCwString();
                    morse = morse.substring(3);
                    if (CWFlashcards.handleGuess(morse, false)) {
                        correctIncorrectLabel.setText("Correct!");
                    } else {
                        correctIncorrectLabel.setText("Incorrect, try again!");
                    }
                    correctIncorrectLabel.setVisible(true);
                    CWHandler.resetCwString();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must type something in.");
                    alert.showAndWait();
                }

            }
        });
        correctIncorrectLabel.setVisible(false);
        playLetterButton.setOnAction(evt -> {
            if (playLetterButton.getText().equals("Play Letter")) {
                correctIncorrectLabel.setVisible(false);
                try {
                    CWFlashcards.playLetter(true);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (playLetterButton.getText().equals("Next Letter")) {
                correctIncorrectLabel.setVisible(false);
                try {
                    lettersLabel.setText("Letter(s): " + CWFlashcards.playLetter(false));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        startTrainingButton.setOnAction(evt -> {
            if (!lettersCheckBox.isSelected() && !numbersCheckBox.isSelected() && !abbrevCheckBox.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a difficulty type.");
                alert.showAndWait();
                return;
            } else {
                CWFlashcards.generateAlphabetList(lettersCheckBox.isSelected(), numbersCheckBox.isSelected(), abbrevCheckBox.isSelected());
            }
            try {
                if (trainingTypeChoiceBox.getValue().equals("Listening")) {
                    setListeningMenu();
                } else if (trainingTypeChoiceBox.getValue().equals("Typing")) {
                    setTypingMenu();
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "You must select a training type.");
                alert.showAndWait();
            }
        });

        endButton.setOnAction(evt -> {
            setDefaultMenu();
            CWFlashcards.resetCurrent();
        });

        //End of CW Flashcards Section


        }

        private void setDefaultMenu() {
            trainingTypeLabel.setVisible(true);
            trainingTypeChoiceBox.setVisible(true);
            difficultyLabel.setVisible(true);
            lettersCheckBox.setVisible(true);
            numbersCheckBox.setVisible(true);
            abbrevCheckBox.setVisible(true);
            startTrainingButton.setVisible(true);
            correctIncorrectLabel.setVisible(false);
            lettersLabel.setVisible(false);
            playLetterButton.setVisible(false);
            guessLabel.setVisible(false);
            guessTextField.setVisible(false);
            submitButton.setVisible(false);
            endButton.setVisible(false);
        }

        private void setListeningMenu() {
            playLetterButton.setVisible(true);
            playLetterButton.setText("Play Letter");
            guessLabel.setVisible(true);
            guessTextField.setVisible(true);
            submitButton.setVisible(true);
            endButton.setVisible(true);
            trainingTypeLabel.setVisible(false);
            trainingTypeChoiceBox.setVisible(false);
            difficultyLabel.setVisible(false);
            lettersCheckBox.setVisible(false);
            numbersCheckBox.setVisible(false);
            abbrevCheckBox.setVisible(false);
            startTrainingButton.setVisible(false);
            lettersLabel.setVisible(false);
        }

        private void setTypingMenu() {
            lettersLabel.setVisible(true);
            playLetterButton.setVisible(true);
            playLetterButton.setText("Next Letter");
            guessLabel.setVisible(false);
            guessTextField.setVisible(false);
            submitButton.setVisible(true);
            endButton.setVisible(true);
            trainingTypeLabel.setVisible(false);
            trainingTypeChoiceBox.setVisible(false);
            difficultyLabel.setVisible(false);
            lettersCheckBox.setVisible(false);
            numbersCheckBox.setVisible(false);
            abbrevCheckBox.setVisible(false);
            startTrainingButton.setVisible(false);
        }

}
