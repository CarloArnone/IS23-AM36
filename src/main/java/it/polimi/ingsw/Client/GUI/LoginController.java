package it.polimi.ingsw.Client.GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import static it.polimi.ingsw.Client.GUI.AlertHelper.showAlert;

public class LoginController {

    GUI guiRef = GUI.getInstance();


    @FXML
    private TextField username;

    public void login(){
        Platform.runLater(() -> {
            guiRef.setName(username.getText());
            Window parent = username.getScene().getWindow();
            if(guiRef.getName().isEmpty()){
                showAlert("Please state your Name", parent, "Missing Inputs");
            }
            else {
                guiRef.setStage((Stage) username.getScene().getWindow());
                guiRef.getVirtualViewClient().logInTryEvent(guiRef.getName(), guiRef.getVirtualViewClient());
            }
        });

    }

}

