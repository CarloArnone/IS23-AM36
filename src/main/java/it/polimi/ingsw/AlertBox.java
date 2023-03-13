package it.polimi.ingsw;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String title,String message){
        Stage window = new Stage();     //Blank page

        window.initModality(Modality.APPLICATION_MODAL);    //Blocca gli input events con le altre pagine fincè non risolvi questa pagina
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Close the window");
        closeButton.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,closeButton);
        label.setAlignment(Pos.CENTER); //Allinea al centro

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();   //Blocking function, it does not return to the caller untill window is closed

    }
}
