package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_ServerInterface extends Remote {

    void confirmEndTurn(Command args) throws RemoteException;

    void logInTryEvent(Command args) throws RemoteException;

    void previousGamesRequestEvent(Command args) throws RemoteException;

    void createGameEvent(Command args) throws RemoteException;

    void retrieveOldGameEvent(Command args) throws RemoteException;

    void joinGameEvent(Command args) throws RemoteException;

    void disconnectedPlayer(Command args) throws RemoteException;

    void getActiveLivingRooms(Command args) throws RemoteException;

    void isGamesStarted(Command args) throws RemoteException;

    void leaveGameEvent(Command args) throws RemoteException;

    void isGameEnded(Command args) throws RemoteException;

    void endGame(Command args) throws RemoteException;

    void isPossiblePick(Command args) throws RemoteException;

    public String echo(String echoString) throws RemoteException;

    public String upper(String upperString) throws RemoteException;
}
