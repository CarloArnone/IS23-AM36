package it.polimi.ingsw.StartUp;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class ChoiceBox{
    static int answer;

    GridPane gameBoard;

    public static int display(String title, String message, List<String> Choices){
        Stage window = new Stage();     //Blank page
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        VBox layout = new VBox(10);
        label.setText(message);

        List<Button> buttons = new ArrayList<>();
        for(int i = 0;i < Choices.size(); i++){
            Button button = new Button();
            button.setText(Choices.get(i));
            buttons.add(button);
            int finalI = i;
            buttons.get(i).setOnAction(event -> {
                answer = finalI;
                if(answer == 1){

                }
                window.close();
            });
            layout.getChildren().add(buttons.get(i));
        }
        layout.getChildren().add(label);
        label.setAlignment(Pos.CENTER); //Allinea al centro

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();   //Blocking function, it does not return to the caller until window is closed

        return answer;

    }

    public static int display(String title, String message, List<String> Choices,GridPane gameBoard){
        Stage window = new Stage();     //Blank page
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        VBox layout = new VBox(10);
        label.setText(message);

        List<Button> buttons = new ArrayList<>();
        for(int i = 0;i < Choices.size(); i++){
            Button button = new Button();
            button.setText(Choices.get(i));
            buttons.add(button);
            int finalI = i;
            buttons.get(i).setOnAction(event -> {
                answer = finalI;
                window.close();
            });
            layout.getChildren().add(buttons.get(i));
        }
        layout.getChildren().add(label);
        label.setAlignment(Pos.CENTER); //Allinea al centro

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();   //Blocking function, it does not return to the caller until window is closed

        return answer;

    }
}