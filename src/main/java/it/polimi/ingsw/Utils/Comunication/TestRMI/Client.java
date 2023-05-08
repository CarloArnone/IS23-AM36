package it.polimi.ingsw.Utils.Comunication.TestRMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    private static HelloInterface lookUp;

    public static void main(String[] args) {
        try {
            lookUp = (HelloInterface) Naming.lookup("//localhost/Server");
            while(true) {
                String s = lookUp.helloTo(args[0]);
                System.out.println(s);
            }
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
