package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.IUI;

public class CLI extends IUI {


    public CLI(ICommunication virtualViewClient) {
        super(virtualViewClient);
    }

    public void start(){

    }


    public void printNSpaces(int n){
        for (int i = 0; i < n; i++) {
            System.out.print(" ");
        }
    }


}
