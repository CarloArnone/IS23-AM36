package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Server.Controller.Controller;

import java.io.File;

public class GameSimulator {
    File simulation;
    CLI cliTest;
    Controller controllerTest;

    public GameSimulator(Controller c) {
        controllerTest = c;
    }


    public boolean simulate(String simulationName){
        simulation = new File("src/main/resources/TestCommands/" + simulationName);
        cliTest = new CLI(simulation, controllerTest);
        cliTest.start();
        return true;
    }


}
