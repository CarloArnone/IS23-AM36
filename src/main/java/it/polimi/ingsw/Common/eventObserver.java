package it.polimi.ingsw.Common;

import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface eventObserver {

    boolean confirmEndTurn(List<BoardPosition> pick, int col);
    boolean logInTryEvent(String name);
    List<String> previousGamesRequestEvent(String name);
    LivingRoom createGameEvent(String livingRoomID, Player creator);
    LivingRoom retrieveOldGameEvent(String livingRoomID);
    boolean leaveGameEvent(Player p);
    boolean joinGameEvent(String livingRoomID, Player p);
    //TODO MAY ADD DISCONNECTION ECC...
}
