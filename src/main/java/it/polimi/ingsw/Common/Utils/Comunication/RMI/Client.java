package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    private static RMI_Interface lookUp;

    public static void main(String[] args){
        try{
            lookUp = (RMI_Interface) Naming.lookup("//localhost/server");
            String s = lookUp.echo(args[0]);
            while(true) System.out.println(s);
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
