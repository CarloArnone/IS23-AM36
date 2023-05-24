package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Command;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_ClientInterface extends Remote, ICommunication {

    //TESTING
    public String echo(String echoString) throws RemoteException;

    public String upper(String upperString) throws RemoteException;

}
