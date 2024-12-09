package edu.augustana.UI;

import edu.augustana.PaddleHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

public class ConfigController {
    @FXML
    private Slider paddleWPMSlider;


    @FXML
    void initialize() {
        paddleWPMSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
             PaddleHandler.setWordsPerMinute(newValue.intValue());
             System.out.println(PaddleHandler.getWordsPerMinute());
        });
    }


}
