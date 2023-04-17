package it.polimi.ingsw.Utils.Comunication.TestRMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements HelloInterface{

    private static final long serialVersionUID = 1L;

    protected Server () throws RemoteException {
        super();
    }

    @Override
    public String helloTo(String name) throws RemoteException {

        String output = "Server says hello to " + name;
        System.out.println(output);
        return output;
    }

    public static void main(String[] args) {
        try {
            Naming.rebind("//localhost/Server", new Server());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
