package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Server extends UnicastRemoteObject implements RMI_Interface {

    protected Server() throws RemoteException {
    }

    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws RemoteException {

    }

    @Override
    public void logInTryEvent(String name, ICommunication virtualView) throws RemoteException {

    }

    @Override
    public void previousGamesRequestEvent(String name) throws RemoteException {

    }

    @Override
    public void createGameEvent(String livingRoomID, Player p, int PlayersNum) throws RemoteException {

    }

    @Override
    public void retrieveOldGameEvent(String livingRoomID) throws RemoteException {

    }

    @Override
    public void joinGameEvent(String livingRoomID, String name) throws RemoteException {

    }

    @Override
    public void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) throws RemoteException {

    }

    @Override
    public void getActiveLivingRooms(int listLength, int occurrence) throws RemoteException {

    }

    @Override
    public void isGamesStarted(LivingRoom livingRoom) throws RemoteException {

    }

    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) throws RemoteException {

    }

    @Override
    public void isGameEnded(LivingRoom livingRoom) throws RemoteException {

    }

    @Override
    public void endGame(LivingRoom livingRoom) throws RemoteException {

    }

    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) throws RemoteException {

    }

    @Override
    public void notifyListener() {

    }
}
