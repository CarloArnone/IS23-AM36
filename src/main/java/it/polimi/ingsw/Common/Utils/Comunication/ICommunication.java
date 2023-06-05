package it.polimi.ingsw.Common.Utils.Comunication;

import it.polimi.ingsw.Common.Utils.Command;
import it.polimi.ingsw.Common.Utils.Listener;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface ICommunication extends Listener {

    void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col);
    void logInTryEvent(String name, ICommunication virtualView);
    void previousGamesRequestEvent(String playerName);
    void createGameEvent(String livingRoomID, Player p, int PlayersNum);
    void retrieveOldGameEvent(String livingRoomID, String playerName);
    void joinGameEvent(String livingRoomID, String name);
    void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView); //TODO: CHANGE THIS NAME, SAME AS A SUCCESS CASE
    void getActiveLivingRooms(int listLength, int occurrence);
    void isGamesStarted(LivingRoom livingRoom);
    void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView);
    void isGameEnded(LivingRoom livingRoom);
    void endGame(LivingRoom livingRoom);
    void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick);

    //ERRORS
    void notEnoughSpacesInCol(Command command);
    void loginUnsuccessful(Command command);
    void livingRoomNotFound(Command command);
    void createGameNotSuccessful(Command command);
    void notDisconnectedPlayer(Command command);
    void gameNotStarted(Command command);
    void gameNotEnded(Command command);
    void notPossiblePick(Command command);


    //SUCCESSES
    void turnEndedSuccessfully(Command command);
    void loginDoneSuccessfully(Command command);
    void livingRoomFound(Command command);
    void joinedGame(Command command);
    void disconnectedPlayerSuccessfully(Command command);
    void livingRoomsList(Command command);
    void gameStarted(Command command);
    void gameEnded(Command command);
    void possiblePick(Command command);
    void notifyListener(Command command);

}
