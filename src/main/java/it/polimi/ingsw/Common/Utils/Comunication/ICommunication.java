package it.polimi.ingsw.Common.Utils.Comunication;

import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Utils.Listener;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;

public interface ICommunication extends Listener {

    void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col);
    void logInTryEvent(String name, ICommunication virtualView);
    void previousGamesRequestEvent(String name);
    void createGameEvent(String livingRoomID, Player p, int PlayersNum);
    void retrieveOldGameEvent(String livingRoomID);
    void joinGameEvent(String livingRoomID, String name);
    void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView);
    void getActiveLivingRooms(int listLength, int occurency);
    void isGamesStarted(LivingRoom livingRoom);
    void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView);
    void isGameEnded(LivingRoom livingRoom);
    void endGame(LivingRoom livingRoom);
    void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick);

 /*   //Errors
    void notEnoughtSpacesInCol();
    void loginUnsuccessful();

    void notJoinedGame(String arg1);
    void livingRoomNotFound(String arg1);
    void createGameNotSuccessful();
    void notDisconnectedPlayer();
    void gameNotstarted();
    void gameNotEnded();
    void notPossiblePick();

    //Success

    void turnEndedSuccessfully();
    void loginDoneSuccessfully();
    void livingRoomFound();
    void joinedGame();
    void disconnectedPlayer();
    void livingRoomsList();
    void gameStarted();
    void gameEnded();
    void possiblePick();
*/


}
