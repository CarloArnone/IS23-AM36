package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Utils.Command;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.rmi.RemoteException;
import java.util.List;

public class VirtualViewRMI_Server implements ICommunication, RMI_ServerInterface {

    Controller controller = Controller.getInstance();


    @Override
    public void confirmEndTurn(Command args) throws RemoteException {

        LivingRoom livingRoom = controller.getLivingRoomById(args.getArgs().get(0));
        Player player = controller.getPlayerByName(args.getArgs().get(1));
        List<BoardPosition> pick = JSONInterface.recreatePick(args.getArgs().get(2));
        int col = Integer.parseInt(args.getArgs().get(3));

        confirmEndTurn(livingRoom, player, pick, col);
    }

    @Override
    public void logInTryEvent(Command args) throws RemoteException {

        String name = args.getArgs().get(0);

        logInTryEvent(name, this);
    }

    @Override
    public void previousGamesRequestEvent(Command args) throws RemoteException {
        previousGamesRequestEvent(args.getArgs().get(0));
    }

    @Override
    public void createGameEvent(Command args) throws RemoteException {

        String livingRoomID = args.getArgs().get(0);
        Player player = controller.getPlayerByName(args.getArgs().get(1));
        int playersNum = Integer.parseInt(args.getArgs().get(2));

        createGameEvent(livingRoomID, player, playersNum);
    }

    @Override
    public void retrieveOldGameEvent(Command args) throws RemoteException {
        retrieveOldGameEvent(args.getArgs().get(0));
    }

    @Override
    public void joinGameEvent(Command args) throws RemoteException {
        joinGameEvent(args.getArgs().get(0), args.getArgs().get(1));
    }

    @Override
    public void disconnectedPlayer(Command args) throws RemoteException {

        LivingRoom livingRoom = controller.getLivingRoomById(args.getArgs().get(0));
        String name = args.getArgs().get(1);
        boolean voluntaryLeft = Boolean.parseBoolean(args.getArgs().get(2));

        disconnectedPlayer(livingRoom, name, voluntaryLeft, this);
    }

    @Override
    public void getActiveLivingRooms(Command args) throws RemoteException {

        int listLength = Integer.parseInt(args.getArgs().get(0));
        int occurrence = Integer.parseInt(args.getArgs().get(1));

        getActiveLivingRooms(listLength, occurrence);
    }

    @Override
    public void isGamesStarted(Command args) throws RemoteException {
        isGamesStarted(controller.getLivingRoomById(args.getArgs().get(0)));
    }

    @Override
    public void leaveGameEvent(Command args) throws RemoteException {

        String name = args.getArgs().get(0);
        LivingRoom activeLivingRoom = controller.getLivingRoomById(args.getArgs().get(1));

        leaveGameEvent(name, activeLivingRoom, this);
    }

    @Override
    public void isGameEnded(Command args) throws RemoteException {
        isGameEnded(controller.getLivingRoomById(args.getArgs().get(0)));
    }

    @Override
    public void endGame(Command args) throws RemoteException {
        endGame(controller.getLivingRoomById(args.getArgs().get(0)));
    }

    @Override
    public void isPossiblePick(Command args) throws RemoteException {
        Player player = controller.getPlayerByName(args.getArgs().get(0));
        String livingRoomId = args.getArgs().get(1);
        List<BoardPosition> pick = JSONInterface.recreatePick(args.getArgs().get(2));
    }

    @Override
    public String echo(String echoString) {
        return echoString;
    }

    @Override
    public String upper(String upperString) {
        return upperString.toUpperCase();
    }

    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) {
        try {
            controller.confirmEndTurn(livingRoom, p, pick, col);
        } catch (NotEnoughSpacesInCol e) {
            //TODO: CALL CLIENT METHOD
        }
    }

    @Override
    public void logInTryEvent(String name, ICommunication virtualView) {

    }

    @Override
    public void previousGamesRequestEvent(String name) {

    }

    @Override
    public void createGameEvent(String livingRoomID, Player p, int playersNum) {

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
    public void getActiveLivingRooms(int listLength, int occurrence) {

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
}
