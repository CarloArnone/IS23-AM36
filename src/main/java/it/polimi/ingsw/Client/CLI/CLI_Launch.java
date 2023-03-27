package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Server.Controller.Controller;

public class CLI_Launch {
    static Controller c = new Controller();
    public static void start() {
        CLI cli1 = new CLI(c);
        cli1.start();
    }
}
