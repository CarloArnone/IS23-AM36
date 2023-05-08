package it.polimi.ingsw.Common.Utils.Comunication;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.Utils.Listener;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface ICommunication extends Listener {

    void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws NotEnoughSpacesInCol;
    void logInTryEvent(String name, ICommunication virtualView);
    void previousGamesRequestEvent(String name);
    void createGameEvent(String livingRoomID, Player p, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException;
    void retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException;
    void joinGameEvent(String livingRoomID, String name);
    void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView);
    void getActiveLivingRooms(int listLength, int occurrence);
    void isGamesStarted(LivingRoom livingRoom);
    void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView);
    void isGameEnded(LivingRoom livingRoom);
    void endGame(LivingRoom livingRoom);
    void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick);

}
