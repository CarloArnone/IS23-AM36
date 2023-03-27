package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public class Controller implements eventObserver {
    List<LivingRoom> livingRooms;

    @Override
    public boolean confirmEndTurn(List<BoardPosition> pick, int col) {
        return false;
    }

    @Override
    public boolean logInTryEvent(String name) {
        return false;
    }

    @Override
    public List<String> previousGamesRequestEvent(String name) {
        return null;
    }

    @Override
    public LivingRoom createGameEvent(String livingRoomID, Player creator) {
        return null;
    }

    @Override
    public LivingRoom retrieveOldGameEvent(String livingRoomID) {
        return null;
    }

    @Override
    public boolean leaveGameEvent(Player p) {
        return false;
    }

    @Override
    public boolean joinGameEvent(String livingRoomID, Player p) {
        return false;
    }
}
