package it.polimi.ingsw.Client.GUI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateGameController implements Initializable {

    GUI guiRef = GUI.getInstance();

    @FXML
    private ChoiceBox<Integer> playersNum;
    @FXML
    private TextField GameID;

    @FXML
    public void createGame(){
        Platform.runLater(() -> {
            String gameID = GameID.getText();
            Integer pn = playersNum.getValue();
            guiRef.getVirtualViewClient().createGameEvent(gameID, guiRef.getMySelf(), pn);
        });
    }


    private void initializePlayersNumChoiceBox() {
        ObservableList<Integer> list = FXCollections.observableArrayList();
        list.addAll(2, 3, 4);
        playersNum.setItems(list);
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializePlayersNumChoiceBox();
    }
}
