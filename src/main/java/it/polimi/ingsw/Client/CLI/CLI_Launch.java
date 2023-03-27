package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Common.LobbyLivingRoom;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.LivingRoom;

import java.util.ArrayList;
import java.util.List;

public class CLI_Launch {

    static Controller c = new Controller();

    public static void main(String[] args) {
        CLI cli1 = new CLI(c);

        new Thread(cli1::start).start();
    }
}