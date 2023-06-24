package it.polimi.ingsw.Client.GUI;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;

public class AlertHelper {

    public static void showAlert(String message, Window owner, String title) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initOwner(owner);
            alert.show();
        });

    }

    public static void showChoice(String message, Window owner, String title, Runnable actionYes, Runnable actionNo){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.initOwner(owner);
            Optional<ButtonType> res = alert.showAndWait();
            if(res.isEmpty()){
                actionNo.run();
            }
            else if (res.get() == ButtonType.OK){
                actionYes.run();
            }
            else actionNo.run();
        });
    }
}
