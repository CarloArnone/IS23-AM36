package it.polimi.ingsw.Common.Utils.Comunication;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface ICommunication {
    
    ICommunication comunicator = null;

    void initializeComunicator(ICommunication com);
    void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws NotEnoughSpacesInCol;
    void logInTryEvent(String name, IUI c);
    void previousGamesRequestEvent(String name);
    void createGameEvent(String livingRoomID, Player p, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException;
    void retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException;
    void joinGameEvent(String livingRoomID, String name);
    void reconnectPlayer(LivingRoom livingRoom, String name);
    void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, IUI c);
    void getActiveLivingRooms(int listLength, int occurency);
    void isGamesStarted(LivingRoom livingRoom);
    void leaveGameEvent(String name, LivingRoom activeLivingRoom, IUI c);
    void isGameEnded(LivingRoom livingRoom);
    void endGame(LivingRoom livingRoom);
    void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick);

}
