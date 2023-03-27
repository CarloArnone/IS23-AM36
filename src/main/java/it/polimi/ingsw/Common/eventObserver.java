package it.polimi.ingsw.Common;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface eventObserver {

    boolean confirmEndTurn(List<BoardPosition> pick, int col);
    boolean logInTryEvent(String name);
    LivingRoom previousGamesRequestEvent(String name);
    LivingRoom createGameEvent(String livingRoomID, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException;

    LivingRoom retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException;
    boolean leaveGameEvent(Player p);
    boolean joinGameEvent(String livingRoomID, Player p);
    boolean reconnectPlayer(LivingRoom livingRoom, String name);
    boolean disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft);

    List<String> getActiveLivingRooms(int listLength, int occurency);
    //TODO MAY ADD DISCONNECTION ECC...
}
