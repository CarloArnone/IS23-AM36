package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Server.Controller.Controller;

public class CLI_Launch {

    public static Controller c;

    public static void main(String[] args) {
        Controller c = Controller.getInstance();
        CLI cli1 = new CLI(c);
        new Thread(cli1::start).start();
    }
}
