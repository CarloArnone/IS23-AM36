package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface eventObserver {

    boolean confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws NotEnoughSpacesInCol;
    boolean logInTryEvent(String name, ICommunication virtualView);
    LivingRoom previousGamesRequestEvent(String name);
    LivingRoom createGameEvent(String livingRoomID, Player p, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException;
    LivingRoom retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException;
    Player joinGameEvent(String livingRoomID, String name);
    boolean disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView);
    List<String> getActiveLivingRooms(int listLength, int occurency);
    boolean isGamesStarted(LivingRoom livingRoom);
    void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView);
    boolean isGameEnded(LivingRoom livingRoom);
    boolean endGame(LivingRoom livingRoom);
    boolean isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick);

    //TODO MAY ADD DISCONNECTION ECC...
}
