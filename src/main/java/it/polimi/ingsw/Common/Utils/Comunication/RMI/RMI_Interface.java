package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RMI_Interface extends Remote, ICommunication{

    @Override
    void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws RemoteException;
    @Override
    void logInTryEvent(String name, ICommunication virtualView) throws RemoteException;
    @Override
    void previousGamesRequestEvent(String name) throws RemoteException;
    @Override
    void createGameEvent(String livingRoomID, Player p, int PlayersNum) throws RemoteException;
    @Override
    void retrieveOldGameEvent(String livingRoomID) throws RemoteException;
    @Override
    void joinGameEvent(String livingRoomID, String name) throws RemoteException;
    @Override
    void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) throws RemoteException;
    @Override
    void getActiveLivingRooms(int listLength, int occurrence) throws RemoteException;
    @Override
    void isGamesStarted(LivingRoom livingRoom) throws RemoteException;
    @Override
    void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) throws RemoteException;
    @Override
    void isGameEnded(LivingRoom livingRoom) throws RemoteException;
    @Override
    void endGame(LivingRoom livingRoom) throws RemoteException;
    @Override
    void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) throws RemoteException;

}
