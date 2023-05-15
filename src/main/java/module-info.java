module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens it.polimi.ingsw to javafx.fxml;
    exports it.polimi.ingsw.Server.Model;
    opens it.polimi.ingsw.Server.Model to javafx.fxml, com.google.gson;
    opens it.polimi.ingsw.Common.Utils to com.google.gson;
    exports it.polimi.ingsw.Client.GUI;
    opens it.polimi.ingsw.Client.GUI to javafx.fxml;
}