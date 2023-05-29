package it.polimi.ingsw.Client.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class JoinGameController implements Initializable {

    GUI guiRef = GUI.getInstance();

    @FXML
    private ChoiceBox<String> livingRoomsList;


    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList list = FXCollections.observableList(guiRef.getLivingRoomsList());
        livingRoomsList.setItems(list);
    }

    @FXML
    public void joinGame(){
        guiRef.setStage((Stage)livingRoomsList.getScene().getWindow());
        String livId = livingRoomsList.getValue();
        guiRef.getVirtualViewClient().joinGameEvent(livId, guiRef.getName());
    }
}
