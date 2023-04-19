module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw.StartUp;
    opens it.polimi.ingsw.StartUp to javafx.fxml;
}