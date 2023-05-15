package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private int port;
    public RMI_ServerInterface stub;
    private Registry reg;
    public Client(int port){

        this.port = port;

        try {
            // Getting the registry
            reg = LocateRegistry.getRegistry("127.0.0.1", this.port);
            // Looking up the registry for the remote object
            this.stub = (RMI_ServerInterface) reg.lookup("//localhost/mainServer");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
