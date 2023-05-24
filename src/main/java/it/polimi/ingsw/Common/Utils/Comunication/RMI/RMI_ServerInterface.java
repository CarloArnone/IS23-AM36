package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Command;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_ServerInterface extends Remote, ICommunication {

    void confirmEndTurn(Command command) throws RemoteException;

    void previousGamesRequestEvent(Command command) throws RemoteException;

    void createGameEvent(Command command) throws RemoteException;

    void retrieveOldGameEvent(Command command) throws RemoteException;

    void joinGameEvent(Command command) throws RemoteException;

    void disconnectedPlayer(Command command);

    void getActiveLivingRooms(Command command) throws RemoteException;

    void isGamesStarted(Command command) throws RemoteException;

    void leaveGameEvent(Command command) throws RemoteException;

    void isGameEnded(Command command) throws RemoteException;

    void endGame(Command command) throws RemoteException;

    void isPossiblePick(Command command) throws RemoteException;

    @Override
    void createGameNotSuccessful(Command command);

    public String echo(String echoString) throws RemoteException;

    public String upper(String upperString) throws RemoteException;
}
