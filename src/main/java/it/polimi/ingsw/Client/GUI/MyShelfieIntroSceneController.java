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

public class MyShelfieIntroSceneController {
        private Stage stage;
        private Scene scene;
        private Parent root;

        private GUI Gui = GUI.getInstance();

        ObservableList<String> protocol = FXCollections.observableArrayList("RMI","TCP");
        private String userId;
        @FXML
        private Button playButton;
        @FXML
        private void initialize(){
            protocolSelector.setValue("RMI");
            protocolSelector.setItems(protocol);
        }
        public void switchToLoggedIn(ActionEvent event) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/MyShelfie-Logged-In.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        public void tryLogin(ActionEvent event){
            Gui.getVirtualViewClient().logInTryEvent(userIdBox.getText(),Gui.getVirtualViewClient());
        }
        @FXML
        private ChoiceBox<String> protocolSelector;
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
