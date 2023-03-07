module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw;
}