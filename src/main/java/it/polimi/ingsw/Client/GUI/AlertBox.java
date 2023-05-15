package it.polimi.ingsw.Client.GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String title,String message){
        Stage gameWindow = new Stage();     //Blank page

        gameWindow.initModality(Modality.APPLICATION_MODAL);    //Blocca gli input events con le altre pagine fincè non risolvi questa pagina
        gameWindow.setTitle(title);
        gameWindow.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close the window");
        closeButton.setOnAction(event -> gameWindow.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,closeButton);
        label.setAlignment(Pos.CENTER); //Allinea al centro

        Scene scene = new Scene(layout);
        gameWindow.setScene(scene);
        gameWindow.showAndWait();   //Blocking function, it does not return to the caller untill window is closed

    }
}
