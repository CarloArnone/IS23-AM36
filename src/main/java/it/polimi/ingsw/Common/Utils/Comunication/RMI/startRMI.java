package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Client.CLI.CLI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class startRMI {


    public static void main(String[] args) throws Exception {

        if(args[0].equals("server")) {
            VirtualViewRMI_Server mainServer = new VirtualViewRMI_Server(Integer.parseInt(args[1]));
            System.err.println("Server ready--");
        } else if(args[0].equals("client")){
            VirtualViewRMI_Client clientHost = new VirtualViewRMI_Client(new CLI(), Integer.parseInt(args[1]));
            System.err.println("Client ready--");
        }
    }
}
