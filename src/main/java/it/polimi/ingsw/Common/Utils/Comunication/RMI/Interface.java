package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Interface extends Remote {

    void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws RemoteException;

    void logInTryEvent(String name, ICommunication virtualView) throws RemoteException;

    void previousGamesRequestEvent(String name) throws RemoteException;

    void createGameEvent(String livingRoomID, Player p, int PlayersNum) throws RemoteException;

    void retrieveOldGameEvent(String livingRoomID) throws RemoteException;

    void joinGameEvent(String livingRoomID, String name) throws RemoteException;

    void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) throws RemoteException;

    void getActiveLivingRooms(int listLength, int occurrence) throws RemoteException;

    void isGamesStarted(LivingRoom livingRoom) throws RemoteException;

    void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) throws RemoteException;

    void isGameEnded(LivingRoom livingRoom) throws RemoteException;

    void endGame(LivingRoom livingRoom) throws RemoteException;

    void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) throws RemoteException;

    public String echo(String echoString) throws RemoteException;

    public String upper(String upperString) throws RemoteException;
}
