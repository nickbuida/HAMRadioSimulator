package edu.augustana.UI;

import java.io.IOException;

import edu.augustana.*;
import edu.augustana.Bots.Bot;
import edu.augustana.Bots.ContinuousMessageBot;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.sound.sampled.LineUnavailableException;

public class MainUiController {
    private boolean isMuted = false;
    private double savedVolume = 0.0;
    private boolean isPressed = false;

    private SandboxController sandboxController;

    @FXML
    private Label displayLabel;

    @FXML
    private Tab enviromentTab;

    @FXML
    private Button filterBtn;

    @FXML
    private Slider freqSlider;

    @FXML
    private Button joinBtn;

    @FXML
    private VBox knobBox00;

    @FXML
    private VBox knobBox01;

    @FXML
    private VBox knobBox10;

    @FXML
    private VBox knobBox11;

    @FXML
    private GridPane knobGridPane;

    @FXML
    private HBox mainHbox;

    @FXML
    private RadioButton muteBtn;

    @FXML
    private RadioButton powerBtn;

    @FXML
    private ImageView radioImage;

    @FXML
    private ChoiceBox<?> scenarioChoiceBox;

    @FXML
    private Button servInfoBtn;

    @FXML
    private HBox serverhbox;

    @FXML
    private Button closeButton;

    @FXML
    private Button fullScreenButton;

    @FXML
    private Button minimizeButton;

    @FXML
    private Button configButton;

    @FXML
    private Button sandboxButton;

    @FXML
    private Button trainingButton;


    @FXML
    private Slider frequencyFilterSlider;

    @FXML
    private ToolBar myToolBar;

    @FXML
    private HBox toolBarHbox;

    @FXML
    private HBox leftSpacingHbox;

    @FXML
    private HBox midSpacingHbox;

    @FXML
    private HBox rightSpacingHbox;

    private static InstructionsController instructionsController;

    private TrainingScreenController trainingScreenController;



    KnobControl volumeKnob;
    KnobControl filterKnob;
    KnobControl bandKnob;
    KnobControl toneKnob;
    private Boolean firstLoad = true;


    @FXML
    void initialize() throws IOException {
        displayLabel.setText("");

        toolBarHbox.setPrefWidth(myToolBar.getWidth());

        HBox.setHgrow(toolBarHbox, Priority.ALWAYS);
        toolBarHbox.setAlignment(Pos.CENTER_RIGHT);

        HBox.setHgrow(midSpacingHbox, Priority.ALWAYS);
        midSpacingHbox.setAlignment(Pos.CENTER_LEFT);

        midSpacingHbox.setPrefWidth(toolBarHbox.getWidth()/4);

        assert mainHbox != null : "fx:id=\"mainHbox\" was not injected: check your FXML file 'MainUI.fxml'.";
        assert radioImage != null : "fx:id=\"radioImage\" was not injected: check your FXML file 'MainUI.fxml'.";

        //morseText = new Label();
        volumeKnob = new KnobControl();
        knobBox00.getChildren().add(volumeKnob);
        filterKnob = new KnobControl();
        knobBox10.getChildren().add(filterKnob);
        bandKnob = new KnobControl();
        knobBox01.getChildren().add(bandKnob);
        toneKnob = new KnobControl();
        knobBox11.getChildren().add(toneKnob);

        closeButton.setOnAction(evt -> Platform.exit());
        minimizeButton.setOnAction(evt -> App.windowStage.setIconified(true));
        fullScreenButton.setOnAction(evt -> handleFullScreenButtonPress(App.windowStage));
        configButton.setOnAction(evt -> {
            try {
                setConfigPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        sandboxButton.setOnAction(evt -> {
            try {
                setServerPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        trainingButton.setOnAction(evt -> {
            try {
                setTrainingPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        muteBtn.setOnAction(evt -> {
            isMuted = !isMuted;
            if (isMuted) {
                try {
                    mute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    unmute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        freqSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
           int band = Radio.getBand();
           updateRadioFrequency(Radio.getBand(), newValue.doubleValue());
           updateDisplayText( Radio.getSelectedTuneFreq(), Radio.getCwToneFreq(), band);

        });

        volumeKnob.valueProperty().addListener((observable, oldValue, newValue) -> {

            double scaledValue = (newValue.doubleValue() / 100);
            Radio.updateGain(scaledValue);
        });


        filterKnob.valueProperty().addListener((observable, oldValue, newValue) -> {
            int val = (int)((newValue.doubleValue()/100)*6379);
            Radio.changeFilterValue(val + 10);

        });

        bandKnob.valueProperty().addListener((observable, oldValue, newValue) -> {
            double angle = (newValue.doubleValue() / 100)*360;
            Radio.setBand(chooseBand(angle));
            updateRadioFrequency(Radio.getBand(), freqSlider.getValue());
            updateDisplayText( Radio.getSelectedTuneFreq(), Radio.getCwToneFreq(), chooseBand(angle));
        });


        toneKnob.valueProperty().addListener((observable, oldValue, newValue) -> {

            double newFreq = (((newValue.doubleValue() / 100)*400) + 400);

            if(newFreq < 400)newFreq = 400;

            Radio.setCwToneFreq(newFreq);
            MorsePlayer.setSideTone();

            updateDisplayText( Radio.getSelectedTuneFreq(), Radio.getCwToneFreq(), Radio.getBand());

        });

        powerBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    try {
                        System.out.println("Radio Initialized");
                        Radio.initializeRadio();
                        updateKnobValues();
                    } catch (LineUnavailableException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("RadioButton is deselected (toggled off)");
                }
            }
        });


        //Loading the other fxml in the HBOX. Starting with the trainingscreen for now. Maybe make this a method so that we can have DRY coding. Just pass in the string for the fxml name

        setTrainingPane();
        firstLoad = false;

        frequencyFilterSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            MorsePlayer.setFrequencyFilter((double) newValue);
        });

    }

    private void updateRadioFrequency (int band, double frequencySliderValue){
        switch (band){
            case 10:
                Radio.setTunningRF(((frequencySliderValue/100)*1.7) + 28);
                break;

            case 17:
                Radio.setTunningRF(((frequencySliderValue/100)*(18.168 - 18.068) + 18.068));
                break;

            case 20:
                Radio.setTunningRF(((frequencySliderValue)/100)*(14.350 - 14.000) + 14.000);
                break;

            case 30:
                Radio.setTunningRF(((frequencySliderValue/100)*(10.15 - 10.1) + 10.1));
                break;

            case 40:
                Radio.setTunningRF(((frequencySliderValue/100)*(7.300 - 7.000) + 7.000));
                break;

            case 80:
                Radio.setTunningRF(((frequencySliderValue/100)*(4.0 - 3.5) + 3.5));
                break;
        }
    }

    private void setTrainingPane() throws IOException {
        if (!firstLoad) {
            mainHbox.getChildren().remove(mainHbox.getChildren().size() - 1);
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/augustana/TrainingScreen.fxml"));
        System.out.println(getClass().getResource("/edu/augustana/TrainingScreen.fxml"));

        VBox trainingVbox = loader.load();
        mainHbox.getChildren().add(trainingVbox);
        trainingScreenController = loader.getController();
        trainingScreenController.setParentController(this);
    }

    private void mute() throws IOException{
        savedVolume = volumeKnob.getValue();
        volumeKnob.setValue(0.0);
    }

    private void unmute() throws IOException{
        volumeKnob.setValue(savedVolume);
    }


    private void setServerPane() throws IOException {
        mainHbox.getChildren().remove(mainHbox.getChildren().size() - 1);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/augustana/Sandbox.fxml"));
        VBox trainingVbox = loader.load();
        SandboxController controller = loader.getController();
        sandboxController = controller;
        controller.setMainUIControllerController(this);
        mainHbox.getChildren().add(trainingVbox);
    }

    private void setConfigPane() throws IOException {
        mainHbox.getChildren().remove(mainHbox.getChildren().size() - 1);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/augustana/Config.fxml"));
        VBox trainingVbox = loader.load();
        mainHbox.getChildren().add(trainingVbox);
    }


    private void updateKnobValues() throws InterruptedException {
        toneKnob.setValue(((Radio.getCwToneFreq()-400)/400)*100);
        toneKnob.skin.rotateToPosition((int)((Radio.getCwToneFreq()-400)/400)*100);
        bandKnob.skin.rotateToPosition(100);
        bandKnob.setValue(100);
        filterKnob.setValue(((Radio.getFilterValue()/6379)*100));
        filterKnob.skin.rotateToPosition((int)  ((Radio.getFilterValue()/6379)*100));
        volumeKnob.setValue((Radio.getSoundAmplitud()/4)*100);
    }

    void updateDisplayText(double rFrequency, double tFrequency, int band) {
        DecimalFormat dfRFrequency = new DecimalFormat("#.####"); // Up to four decimal places for rFrequency
        DecimalFormat dfTFrequency = new DecimalFormat("#"); // No decimal places for tFrequency

        String formattedTFrequency = dfTFrequency.format(tFrequency);
        String formattedRFrequency = dfRFrequency.format(rFrequency);

        displayLabel.setText(formattedTFrequency + "Hz  " + formattedRFrequency + "Mhz  " + band + "m ");
    }

    int chooseBand(double angle){
        if(angle >= 270){
            return 10;
        } else if (angle > 225) {
            return 17;
        } else if (angle > 180) {
            return 20;
        } else if(angle > 90){
            return 30;
        } else if(angle > 45){
            return 40;
        } else if(angle >= 0){
            return 80;
        } else{
            return 0;
        }

    }

    public void handleKeyPress(KeyEvent keyEvent) throws InterruptedException {
        if (!isPressed) {
            if(sandboxController == null){
                handleKeyPressHelper(keyEvent);
            }else if(!sandboxController.isTextFieldActive()){
                handleKeyPressHelper(keyEvent);
            }

        }
    }

    private void handleKeyPressHelper(KeyEvent keyEvent){
        isPressed = true;
          System.out.println(System.nanoTime());
        if (keyEvent.getCode() == KeyCode.J) {
            new Thread(() -> {
                try {
                    PaddleHandler.playContinuousDot();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();

        } else if (keyEvent.getCode() == KeyCode.K) {
            new Thread(() ->{
                try {
                    PaddleHandler.playContinuousDash();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else if (keyEvent.getCode() == KeyCode.L) {
            CWHandler.startTimer();
        } else if (keyEvent.getCode() == KeyCode.N) {
            Radio.toggleNoise();
        }
    }

    public void handleKeyRelease(KeyEvent keyEvent) throws Exception {
            if(sandboxController == null){
                handleKeyReleaseHelper(keyEvent);
            }else if(!sandboxController.isTextFieldActive()){
                handleKeyReleaseHelper(keyEvent);
            }
    }

    private void handleKeyReleaseHelper(KeyEvent keyEvent) throws Exception {

        if (keyEvent.getCode() == KeyCode.J || keyEvent.getCode() == KeyCode.K) {
            CWHandler.sendMessageTimer();
            PaddleHandler.stopPaddlePress();
//            addToMorseBox(PaddleHandler.getCwString()); // stops first paddle press on keyRelease of second paddle if both are held simultaneously
//            addToEnglishBox(PaddleHandler.getCwString());
            System.out.println(CWHandler.getCwString());
        } else if (keyEvent.getCode() == KeyCode.L) {
            CWHandler.stopTimer();
            CWHandler.sendMessageTimer();
//            addToMorseBox(CWHandler.getCwString());
//            addToEnglishBox(CWHandler.getCwString());
        }
        if ((keyEvent.getCode() == KeyCode.J || keyEvent.getCode() == KeyCode.K || keyEvent.getCode() == KeyCode.L) && TrainingScreenController.inTraining) {
            trainingScreenController.updateCWLabel();
            trainingScreenController.setCWVisible();
        }
        isPressed = false;
    }

    private void handleFullScreenButtonPress(Stage stage) {
        stage.setFullScreen(!stage.isFullScreen());
    }

//    private void addToMorseBox(String morse) {
//        morseText.setText(morse);
//       // System.out.println("Label text: " + morseText.getText());
//    }


    public void showMessageInTextBox(ContinuousMessageBot selectedBot) {
        String fullMessage = selectedBot.getMorseCallSign() + "/*//*/" + selectedBot.getMorseBotPhrase();
        //addToEnglishBox(fullMessage.replace(' ', '/'));
        //addToMorseBox(fullMessage.replace(' ', '/'));
    }

    public void showInstructions() throws IOException {
        if (!InstructionsController.isShowing) {
            Stage mainUIStage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("Instructions.fxml"));

            mainUIStage.setTitle("Instructions");
            mainUIStage.setScene(new Scene(loader.load()));
            mainUIStage.initStyle(StageStyle.UNDECORATED);
            mainUIStage.show();

            instructionsController = loader.getController();
            instructionsController.setParentController(this, mainUIStage);
            InstructionsController.isShowing = true;
        }
    }
}