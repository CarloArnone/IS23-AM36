package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.Utils.Command;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class VirtualViewRMI_Server implements ICommunication, RMI_ServerInterface {

    Controller controller = Controller.getInstance();

    //TODO: CHECK THE METHODS CASTINGS HERE
    //If I try to change the method argument type into VirtualViewRMI_Client in the RMI_Server interface, it seems to give errors.
    //Take care to check that this doesn't cause trouble.
    @Override
    public void logInTryEvent(String name, ICommunication virtualView) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = (VirtualViewRMI_Client)virtualView;

        if(controller.logInTryEvent(name, virtualView)) {
            command.setCommand("LoginDoneSuccessfully");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.loginDoneSuccessfully(command);
        } else {
            command.setCommand("LoginUnsuccessful");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.loginUnsuccessful(command);
        }
    }

    @Override
    public void createGameEvent(Command command) throws RemoteException {

        String livingRoomID = command.getArgs().get(0);
        Player player = controller.getPlayerByName(command.getArgs().get(1));
        int playersNum = Integer.parseInt(command.getArgs().get(2));

        createGameEvent(livingRoomID, player, playersNum);
    }

    @Override
    public void createGameEvent(String livingRoomID, Player p, int playersNum) {

        VirtualViewRMI_Client tempView = extractViewFromPlayer(p);
        Command command = new Command("", new ArrayList<String>(), "");

        try {
            LivingRoom tempLivingRoom = controller.createGameEvent(livingRoomID, p, playersNum);
            command.setCommand("LivingRoomNotFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            //TODO: -- tempView.GameCreated(command); -- .GameCreated METHOD IS USED FOR SOCKET COMMUNICATION, NOT FOUND HERE
            //Also, to be noted, there is a .CreateGameNotSuccessful method defined in the RMI_ClientInterface, but I couldn't find a correspondence in the socket code.
        } catch (InvalidGameIDException | PlayersOutOfBoundException e) {
            command.setCommand("LivingRoomNotFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.createGameNotSuccessful(command);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void previousGamesRequestEvent(Command command) throws RemoteException {
        previousGamesRequestEvent(command.getArgs().get(0));
    }

    //TODO: DIFFERENCE WITH -- retrieveOldGameEvent -- ?
    @Override
    public void previousGamesRequestEvent(String name) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        if(controller.previousGamesRequestEvent(name) != null){
            command.setCommand("LivingRoomFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.livingRoomFound(command);
        } else {
            command.setCommand("LivingRoomNotFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.livingRoomFound(command);
        }
    }

    @Override
    public void retrieveOldGameEvent(Command command) throws RemoteException {
        retrieveOldGameEvent(command.getArgs().get(0));
    }

    //TODO: DIFFERENCE WITH -- previousGamesRequestEvent -- ?
    @Override
    public void retrieveOldGameEvent(String livingRoomID) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()--)

        try {
            LivingRoom tempLivingRoom = controller.retrieveOldGameEvent(livingRoomID);
            command.setCommand("LivingRoomFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.livingRoomFound(command);
        } catch (NoMatchingIDException e) {
            command.setCommand("LivingRoomNotFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.livingRoomNotFound(command);
        }
    }

    @Override
    public void joinGameEvent(Command command) throws RemoteException {
        joinGameEvent(command.getArgs().get(0), command.getArgs().get(1));
    }

    @Override
    public void joinGameEvent(String livingRoomID, String name) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        Player tempPlayer = controller.joinGameEvent(livingRoomID, name);

        if(controller.getLivingRoomById(livingRoomID).getPlayers().stream().anyMatch(p1 -> p1.getName().equals(name))){
            command.setCommand("JoinedGame");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.joinedGame(command);
        } else {
            //TODO:DO WE HAVE TO CONSIDER THIS CASE?
        }

    }

    @Override
    public void confirmEndTurn(Command command) throws RemoteException {

        LivingRoom livingRoom = controller.getLivingRoomById(command.getArgs().get(0));
        Player player = controller.getPlayerByName(command.getArgs().get(1));
        List<BoardPosition> pick = JSONInterface.recreatePick(command.getArgs().get(2));
        int col = Integer.parseInt(command.getArgs().get(3));

        confirmEndTurn(livingRoom, player, pick, col);
    }

    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) {

        VirtualViewRMI_Client tempView = extractViewFromPlayer(p);
        Command command = new Command("", new ArrayList<String>(), "");

        try {
            if(controller.confirmEndTurn(livingRoom, p, pick, col)){
                command.setCommand("TurnEndedSuccessfully");
                command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
                tempView.turnEndedSuccessfully(command);
            } else {
                command.setCommand("");
                command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
                //TODO: DECIDE HOW TO HANDLE THIS CASE
            }
        } catch (NotEnoughSpacesInCol e) {
            command.setCommand("NotEnoughSpacesInCol");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.notEnoughSpacesInCol(command);
        }
    }

    @Override
    public void isGamesStarted(Command command) throws RemoteException {
        isGamesStarted(controller.getLivingRoomById(command.getArgs().get(0)));
    }

    @Override
    public void isGamesStarted(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        if(controller.isGamesStarted(livingRoom)) {
            command.setCommand("GameStarted");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.gameStarted(command);
        } else {
            command.setCommand("GameNotStarted");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.gameNotStarted(command);
        }
    }

    @Override
    public void isGameEnded(Command command) throws RemoteException {
        isGameEnded(controller.getLivingRoomById(command.getArgs().get(0)));
    }

    @Override
    public void isGameEnded(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        if(controller.isGamesStarted(livingRoom)) {
            command.setCommand("GameEnded");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.gameEnded(command);
        } else {
            command.setCommand("GameNotEnded");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.gameNotEnded(command);
        }
    }

    @Override
    public void endGame(Command command) throws RemoteException {
        endGame(controller.getLivingRoomById(command.getArgs().get(0)));
    }

    //TODO: BROADCAST?
    @Override
    public void endGame(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        if(controller.endGame(livingRoom)) {
            command.setCommand("GameEnded");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            //TODO: BROADCAST?
        } else {
            command.setCommand("GameEnded");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            //TODO: BROADCAST?
        }
    }

    @Override
    public void leaveGameEvent(Command command) throws RemoteException {

        String name = command.getArgs().get(0);
        LivingRoom activeLivingRoom = controller.getLivingRoomById(command.getArgs().get(1));

        leaveGameEvent(name, activeLivingRoom, null);
    }

    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null;

        Player tempPlayer = activeLivingRoom.getPlayers().stream().filter(p1 -> p1.getName().equals(name)).findFirst().orElse(null);
        if(tempPlayer != null) tempView = extractViewFromPlayer(tempPlayer);

        tempView.leaveGameEvent(name, activeLivingRoom, tempView);
    }

    @Override
    public void disconnectedPlayer(Command command) {

        LivingRoom livingRoom = controller.getLivingRoomById(command.getArgs().get(0));
        String name = command.getArgs().get(1);
        boolean voluntaryLeft = Boolean.parseBoolean(command.getArgs().get(2));

        disconnectedPlayer(livingRoom, name, voluntaryLeft, null);
    }

    @Override
    public void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null;
        Player tempPlayer = livingRoom.getPlayers().stream().filter(p1 -> p1.getName().equals(name)).findFirst().orElse(null);
        if(tempPlayer != null) tempView = extractViewFromPlayer(tempPlayer);

        if(controller.disconnectedPlayer(livingRoom, name, voluntaryLeft, tempView)) {
            command.setCommand("DisconnectedPlayer");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.disconnectedPlayerSuccessfully(command);
        } else {
            command.setCommand("NotDisconnectedPlayer");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.notDisconnectedPlayer(command);
        }
    }

    @Override
    public void getActiveLivingRooms(Command command) throws RemoteException {

        int listLength = Integer.parseInt(command.getArgs().get(0));
        int occurrence = Integer.parseInt(command.getArgs().get(1));

        getActiveLivingRooms(listLength, occurrence);
    }

    @Override
    public void getActiveLivingRooms(int listLength, int occurrence) {

        Command command = new Command("", new ArrayList<String>(), "");
        command.setCommand("LivingRoomsList");
        String arg1 = "";

        for(String s : controller.getActiveLivingRooms(listLength, occurrence)){
            arg1 += s + "-";
        }
        command.addArg(arg1);
        livingRoomsList(command);
    }

    @Override
    public void isPossiblePick(Command command) throws RemoteException {
        Player player = controller.getPlayerByName(command.getArgs().get(0));
        String livingRoomId = command.getArgs().get(1);
        List<BoardPosition> pick = JSONInterface.recreatePick(command.getArgs().get(2));
    }

    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = extractViewFromPlayer(player);

        if(controller.isPossiblePick(player, livingRoomId, pick)) {
            command.setCommand("PossiblePick");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.possiblePick(command);
        } else {
            command.setCommand("NotPossiblePick");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.notPossiblePick(command);
        }
    }

    @Override
    public void notifyListener(Command command) {

    }

    @Override
    public void notifyListener(String message) {

    }


    //ERRORS

    @Override
    public void loginUnsuccessful(Command command) {

    }

    @Override
    public void createGameNotSuccessful(Command command) {

    }

    @Override
    public void notEnoughSpacesInCol(Command command) {

    }

    @Override
    public void livingRoomNotFound(Command command) {

    }

    @Override
    public void gameNotStarted(Command command) {

    }

    @Override
    public void gameNotEnded(Command command) {

    }

    @Override
    public void notDisconnectedPlayer(Command command) {

    }

    @Override
    public void notPossiblePick(Command command) {

    }


    //SUCCESSES

    @Override
    public void loginDoneSuccessfully(Command command) {

    }

    @Override
    public void livingRoomFound(Command command) {

    }

    @Override
    public void livingRoomsList(Command command) {

    }

    @Override
    public void gameStarted(Command command) {

    }

    @Override
    public void gameEnded(Command command) {

    }

    @Override
    public void joinedGame(Command command) {

    }

    @Override
    public void turnEndedSuccessfully(Command command) {

    }

    public void disconnectedPlayerSuccessfully(Command command) {

    }

    @Override
    public void possiblePick(Command command) {

    }

    //PRIVATE UTILITY METHODS
    private VirtualViewRMI_Client extractViewFromPlayer(Player p) {
        return (VirtualViewRMI_Client)controller.getWaitingForChoice().stream().filter(p1 ->
                p1.getPlayer().getName().equals(p.getName())).findFirst().get().getView();
    }


    //METHODS FOR TESTING
    @Override
    public String echo(String echoString) {
        return echoString;
    }

    @Override
    public String upper(String upperString) {
        return upperString.toUpperCase();
    }

}
