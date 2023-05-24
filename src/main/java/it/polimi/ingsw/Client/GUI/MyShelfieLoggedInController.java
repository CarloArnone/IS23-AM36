package it.polimi.ingsw.Client.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MyShelfieLoggedInController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String userId;

    @FXML
    ChoiceBox<String> availableGames;

    ObservableList<String> availableGamesList = FXCollections.observableArrayList();
    @FXML
    private Button playButton;
    @FXML
    private void initialize(){
    }
    public void switchToGameScene(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("MyShelfie-GameScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private TextField userIdBox;
    @FXML
    protected void setPlayButton() throws IOException {
        playButton.setText("Loggin in as " + this.userId);
    }
    @FXML
    protected void setUserIdBox(){
        this.userId = userIdBox.getText();
    }

}
