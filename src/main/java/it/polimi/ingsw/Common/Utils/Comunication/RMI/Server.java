package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Server.Controller.Controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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

    private static RMI_Interface lookUp;

    public static void main(String[] args) {
        try {

            if(args[0].equals("Server")) {
                Naming.rebind("//localhost/server", new Server());
            } else {
                lookUp = (RMI_Interface) Naming.lookup("//localhost/server");
                String s = lookUp.echo("args[0]");
                while(true) System.out.println(s);
            }
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Server not started");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String echo(String echoString){
        System.out.println(echoString);
        return echoString;
    }
//
//    public String upper(String upperString){
//        System.out.println(upperString);
//        return upperString.toUpperCase();
//    }
}
