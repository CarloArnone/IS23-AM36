package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends Virtual_View_RMI {

    private int port;
    private Interface stub;
    private Registry reg;

    public Server(int port) {

        System.out.println("Hello from Server!");
        this.port = port;
        try{
            this.stub = (Interface) UnicastRemoteObject.exportObject(this, this.port);
            this.reg = LocateRegistry.createRegistry(this.port);
            reg.bind("//localhost/mainServer", this.stub);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        System.err.println("Server ready--");
    }
}
