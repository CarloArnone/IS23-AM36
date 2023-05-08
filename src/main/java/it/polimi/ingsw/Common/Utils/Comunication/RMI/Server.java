package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Comunication.Socket.ServerSocketHousing;
import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewServerSocket;
import it.polimi.ingsw.Server.Controller.Controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject {

    private Controller c = Controller.getInstance();
    private static final long serialVersionUID = 1L;

    public Server(int port) throws RemoteException {
        super(port);
        System.out.println("Server started");
        parseCommands();
    }

    public void stop() throws RemoteException {

    }

    public void parseCommands(){

        while(true){
            try {
                new VirtualViewServerSocket(serverSocket.accept(), c).start();
            } catch (IOException e) {
                System.out.println("ClientNotAccepted");
            }
        }


    }

    public static void main(String[] args) {
        try {
            ServerSocketHousing server = new ServerSocketHousing(12334);
        } catch (IOException e) {
            System.out.println("server not started");
        }
    }



}
