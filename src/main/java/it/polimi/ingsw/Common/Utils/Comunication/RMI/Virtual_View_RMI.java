package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public class Virtual_View_RMI implements ICommunication, fanculo {

    Controller controller = Controller.getInstance();

    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) {

    }

    @Override
    public void logInTryEvent(String name, ICommunication virtualView) {

    }

    @Override
    public void previousGamesRequestEvent(String name) {

    }

    @Override
    public void createGameEvent(String livingRoomID, Player p, int PlayersNum) {

    }

    @Override
    public void retrieveOldGameEvent(String livingRoomID) {

    }

    @Override
    public void joinGameEvent(String livingRoomID, String name) {

    }

    @Override
    public void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) {

    }

    @Override
    public void getActiveLivingRooms(int listLength, int occurency) {

    }

    @Override
    public void isGamesStarted(LivingRoom livingRoom) {

    }

    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) {

    }

    @Override
    public void isGameEnded(LivingRoom livingRoom) {

    }

    @Override
    public void endGame(LivingRoom livingRoom) {

    }

    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {

    }

    @Override
    public void notifyListener() {

    }

    public String echo(String echoString) {
        return echoString;
    }

    public String upper(String upperString) {
     return upperString.toUpperCase();
    }
}
