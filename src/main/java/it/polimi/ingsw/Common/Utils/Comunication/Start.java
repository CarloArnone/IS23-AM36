package it.polimi.ingsw.Common.Utils.Comunication;

import it.polimi.ingsw.Common.Utils.Comunication.RMI.Client;
import it.polimi.ingsw.Common.Utils.Comunication.RMI.Server;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Start {


    public static void main(String args[]) throws RemoteException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome, select port: ");
        int port = sc.nextInt();

        System.out.println("Trying to generate a server located on port " + port);
        Server server = new Server(port);

        System.out.println("Trying to generate a Client and connect it to the server ");
        Client client = new Client(port);

        String echoString = client.stub.echo("ECHO");
        System.out.println("Remote method invoked " + echoString);
    }
}
