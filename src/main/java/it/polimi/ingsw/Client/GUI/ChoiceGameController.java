package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChoiceGameController {

    GUI guiRef = GUI.getInstance();
    @FXML
    private Button createGameSceneStarter;


    @FXML
    public void goToCreateGameScene(){
        guiRef.setStage((Stage) createGameSceneStarter.getScene().getWindow());
        guiRef.loadScene("/FXML/createGameScene.fxml", "CreateGame");
    }


    public void goToJoinGameScene() {
        guiRef.setStage((Stage) createGameSceneStarter.getScene().getWindow());
        guiRef.getVirtualViewClient().getActiveLivingRooms(50, 1);
    }
}
