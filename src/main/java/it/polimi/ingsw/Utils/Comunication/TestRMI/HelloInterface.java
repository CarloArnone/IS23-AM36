package it.polimi.ingsw.Utils.Comunication.TestRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloInterface extends Remote{
    public String helloTo(String name) throws RemoteException;
}
