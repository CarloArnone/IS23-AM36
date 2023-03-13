package it.polimi.ingsw;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
    static boolean answer;

    public static boolean display(String title,String message){
        Stage window = new Stage();     //Blank page
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        //Include two buttons
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(event -> {
            answer = true;
            window.close();
        });
         noButton.setOnAction(event -> {
             answer = false;
             window.close();
         });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,yesButton,noButton);
        label.setAlignment(Pos.CENTER); //Allinea al centro

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();   //Blocking function, it does not return to the caller until window is closed

        return answer;

    }
}