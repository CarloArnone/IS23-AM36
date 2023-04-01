package it.polimi.ingsw.Common;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface eventObserver {

    boolean confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws NotEnoughSpacesInCol;

    boolean logInTryEvent(String name, CLI c);
    LivingRoom previousGamesRequestEvent(String name);
    LivingRoom createGameEvent(String livingRoomID, Player p, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException;

    LivingRoom retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException;
    Player joinGameEvent(String livingRoomID, String name);
    boolean reconnectPlayer(LivingRoom livingRoom, String name);
    boolean disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, CLI c);

    List<String> getActiveLivingRooms(int listLength, int occurency);

    boolean isGamesStarted(LivingRoom livingRoom);

    void leaveGameEvent(String name, LivingRoom activeLivingRoom, CLI c);

    boolean isGameEnded(LivingRoom livingRoom);

    //TODO MAY ADD DISCONNECTION ECC...
}
