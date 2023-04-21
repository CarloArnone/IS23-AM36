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

public class SocketComunicationMethod implements ICommunication {


    /**
     * @param com
     */
    @Override
    public void initializeComunicator(ICommunication com) {

    }

    /**
     * @param livingRoom
     * @param p
     * @param pick
     * @param col
     * @throws NotEnoughSpacesInCol
     */
    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws NotEnoughSpacesInCol {

    }

    /**
     * @param name
     * @param c
     */
    @Override
    public void logInTryEvent(String name, IUI c) {

    }

    /**
     * @param name
     */
    @Override
    public void previousGamesRequestEvent(String name) {

    }

    /**
     * @param livingRoomID
     * @param p
     * @param PlayersNum
     * @throws InvalidGameIDException
     * @throws PlayersOutOfBoundException
     */
    @Override
    public void createGameEvent(String livingRoomID, Player p, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException {

    }

    /**
     * @param livingRoomID
     * @throws NoMatchingIDException
     */
    @Override
    public void retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException {

    }

    /**
     * @param livingRoomID
     * @param name
     */
    @Override
    public void joinGameEvent(String livingRoomID, String name) {

    }

    /**
     * @param livingRoom
     * @param name
     */
    @Override
    public void reconnectPlayer(LivingRoom livingRoom, String name) {

    }

    /**
     * @param livingRoom
     * @param name
     * @param voluntaryLeft
     * @param c
     */
    @Override
    public void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, IUI c) {

    }

    /**
     * @param listLength
     * @param occurency
     */
    @Override
    public void getActiveLivingRooms(int listLength, int occurency) {

    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGamesStarted(LivingRoom livingRoom) {

    }

    /**
     * @param name
     * @param activeLivingRoom
     * @param c
     */
    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, IUI c) {

    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGameEnded(LivingRoom livingRoom) {

    }

    /**
     * @param livingRoom
     */
    @Override
    public void endGame(LivingRoom livingRoom) {

    }

    /**
     * @param player
     * @param livingRoomId
     * @param pick
     */
    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {

    }
}
