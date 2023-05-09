package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Server.Controller.Controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject {

    private Controller c = Controller.getInstance();
    private static final long serialVersionUID = 1L;

    public Server() throws RemoteException {
        super();
        System.out.println("Server started");
    }

    public void stop() throws RemoteException {

    }

    public static void main(String[] args) {
        try {
            Naming.rebind("//localhost/server", new Server());
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Server not started");
        }
    }

//    public String echo(String echoString){
//        System.out.println(echoString);
//        return echoString;
//    }
//
//    public String upper(String upperString){
//        System.out.println(upperString);
//        return upperString.toUpperCase();
//    }
}
