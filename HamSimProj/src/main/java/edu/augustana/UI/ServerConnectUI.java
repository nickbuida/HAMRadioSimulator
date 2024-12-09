package edu.augustana.UI;
import edu.augustana.HamRadioServerClient;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerConnectUI {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button connectBtn;

    @FXML
    private TextField userCallSign;

    public SandboxController parentController;

    private String serverID;

    @FXML
    void initialize(){

        connectBtn.setOnAction(event -> {
            try {
                HamRadioServerClient.connectToServer(serverID,userCallSign.getText());
                parentController.setConnected();
                parentController.setUserName(userCallSign.getText());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        cancelBtn.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

    }

    public void setServerID(String ID){
        serverID = ID;
    }



}




