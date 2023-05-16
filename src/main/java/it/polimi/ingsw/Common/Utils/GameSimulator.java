package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Server.Controller.Controller;

import java.io.File;

public class GameSimulator {
    File simulation;
    //CLI cliTest;
    Controller controllerTest;

    public GameSimulator(Controller c) {
        controllerTest = c;
    }


    public boolean simulate(String simulationName){
        simulation = new File(String.valueOf(this.getClass().getClassLoader().getResourceAsStream(simulationName)));

        return true;
    }


}
