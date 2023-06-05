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
        command.addArg(name);

        if(controller.logInTryEvent(name, virtualView)) {
            command.setCommand("LoginDoneSuccessfully");
            loginDoneSuccessfully(command);
        } else {
            command.setCommand("LoginUnsuccessful");
            loginUnsuccessful(command);
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
        Command command = new Command("", new ArrayList<String>(), "");
        command.addArg(p.getName()); //ARG -> 0

        try {
            LivingRoom tempLivingRoom = controller.createGameEvent(livingRoomID, p, playersNum);
            command.setCommand("LivingRoomFound");
            command.addArg(JSONInterface.writeLivingRoomToJson(tempLivingRoom)); //ARG -> 1
            livingRoomFound(command);
            //To be noted, there is a .CreateGameNotSuccessful method defined in the RMI_ClientInterface, but I couldn't find a correspondence in the socket code.
        } catch (InvalidGameIDException e) {
            command.setCommand("LivingRoomNotFound");
            command.addArg("invalidId"); //ARG -> 1
            command.addArg(livingRoomID); //ARG -> 2
            createGameNotSuccessful(command);
            throw new RuntimeException(e);
        } catch (PlayersOutOfBoundException e) {
            command.setCommand("LivingRoomNotFound");
            command.addArg("outOfBound"); //ARG -> 1
            command.addArg(livingRoomID); //ARG -> 2
            createGameNotSuccessful(command);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void previousGamesRequestEvent(Command command) throws RemoteException {
        previousGamesRequestEvent(command.getArgs().get(0));
    }

    @Override
    public void previousGamesRequestEvent(String playerName) {

        Command command = new Command("", new ArrayList<String>(), "");
        command.addArg(playerName); //ARG -> 0
        LivingRoom tempLiv = controller.previousGamesRequestEvent(playerName);

        if(tempLiv != null){
            command.setCommand("LivingRoomFound");
            command.addArg(JSONInterface.writeLivingRoomToJson(tempLiv)); //ARG -> 1
            command.addArg("fetchOldGame"); //ARG -> 2
            livingRoomFound(command);
        } else {
            command.setCommand("GetActiveLivingRooms");
            //ARG -> 1
            //ARG -> 2
            //getActiveLivingRooms();y
            //TODO: FINISH THIS
        }
    }

    @Override
    public void retrieveOldGameEvent(Command command) throws RemoteException {
        retrieveOldGameEvent(command.getArgs().get(0), command.getArgs().get(1));
    }

    @Override
    public void retrieveOldGameEvent(String livingRoomID, String playerName) {

        Command command = new Command("", new ArrayList<String>(), "");
        command.addArg(playerName); //ARG -> 0\
        try {
            LivingRoom tempLivingRoom = controller.retrieveOldGameEvent(livingRoomID);
            command.setCommand("LivingRoomFound");
            command.addArg(JSONInterface.writeLivingRoomToJson(tempLivingRoom)); //ARG -> 1
            livingRoomFound(command);
        } catch (NoMatchingIDException e) {
            command.setCommand("LivingRoomNotFound");
            command.addArg("fetchOldGame"); //ARG -> 1
            livingRoomNotFound(command);
        }
    }

    @Override
    public void joinGameEvent(Command command) throws RemoteException {
        joinGameEvent(command.getArgs().get(0), command.getArgs().get(1));
    }

    @Override
    public void joinGameEvent(String livingRoomID, String playerName) {

        Command command = new Command("", new ArrayList<String>(), "");

        Player tempPlayer = controller.joinGameEvent(livingRoomID, playerName);
        command.addArg(tempPlayer != null ? tempPlayer.getName() : null); //ARG -> 0

        LivingRoom tempLiv = controller.getLivingRoomById(livingRoomID);

        if(tempLiv.getPlayers().stream().anyMatch(p1 -> p1.getName().equals(playerName))){
            command.setCommand("JoinedGame");
            command.addArg(JSONInterface.writePlayerToJson(tempPlayer)); //ARG -> 1
            command.addArg(JSONInterface.writeLivingRoomToJson(tempLiv)); //ARG -> 2
            command.addArg(tempLiv.getLivingRoomId()); //ARG -> 3
            joinedGame(command);
        } else {
            command.setCommand("JoinedGame");
            command.addArg("GameFull"); //ARG -> 1
            notJoinedGame(command);
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

        Command command = new Command("", new ArrayList<String>(), "");
        command.addArg(p.getName()); //ARG -> 0

        try {
            if(controller.confirmEndTurn(livingRoom, p, pick, col)) {
                command.setCommand("TurnEndedSuccessfully");
                turnEndedSuccessfully(command);
            }
        } catch (NotEnoughSpacesInCol e) {
            command.setCommand("NotEnoughSpacesInCol");
            notEnoughSpacesInCol(command);
        }
    }

    @Override
    public void isGamesStarted(Command command) throws RemoteException {
        isGamesStarted(controller.getLivingRoomById(command.getArgs().get(0)));
    }

    @Override
    public void isGamesStarted(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");

        if(controller.isGamesStarted(livingRoom)) {
            command.setCommand("GameStarted");
            for(Player p : livingRoom.getPlayers()) {
                command.removeArg(0);
                command.addArg(p.getName()); //ARG -> 0
                gameStarted(command);
            }
        } else {
            command.setCommand("GameNotStarted");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            //gameNotStarted in CLI does nothing for now
            gameNotStarted(command);
        }
    }

    @Override
    public void isGameEnded(Command command) throws RemoteException {
        isGameEnded(controller.getLivingRoomById(command.getArgs().get(0)));
    }

    @Override
    public void isGameEnded(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");

        if(controller.isGameEnded(livingRoom)) {
            command.setCommand("GameEnded");
            //TODO: GIVE AN ACTUAL COMMAND
            command.addArg("Ciao chicchi è stato un piacere"); //ARG -> 1
            for(Player p : livingRoom.getPlayers()){
                command.getArgs().add(0, p.getName()); //ARG -> 0
                gameEnded(command);
                command.getArgs().remove(0);
            }
        } else {
            command.setCommand("GameNotEnded");
            for(Player p : livingRoom.getPlayers()){
                command.addArg(p.getName());
                gameNotEnded(command);
                command.removeArg(0);
            }
        }
    }

    @Override
    public void endGame(Command command) throws RemoteException {
        endGame(controller.getLivingRoomById(command.getArgs().get(0)));
    }

    @Override
    public void endGame(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");

        //TODO: endGame RETURN ALWAYS FALSE FOR NOW
        if(controller.endGame(livingRoom)) {
            command.setCommand("GameEnded");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
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
    public void leaveGameEvent(String playerName, LivingRoom activeLivingRoom, ICommunication virtualView) {

        Command command = new Command("", new ArrayList<String>(), "");
        ICommunication tempView = extractViewFromName(playerName);
        command.addArg(playerName); //ARG -> 0

        if(controller.disconnectedPlayer(activeLivingRoom, playerName, true, tempView)){
            command.setCommand("DisconnectedPlayerSuccessfully");
            disconnectedPlayerSuccessfully(command);
        } else {
            command.setCommand("NotDisconnectedPlayer");
            notDisconnectedPlayer(command);
        }
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
        ICommunication tempView = null;
        Player tempPlayer = livingRoom.getPlayers().stream().filter(p1 -> p1.getName().equals(name)).findFirst().orElse(null);
        if(tempPlayer != null) tempView = extractViewFromPlayer(tempPlayer);
        command.addArg(name);


        if(controller.disconnectedPlayer(livingRoom, name, voluntaryLeft, tempView)) {
            command.setCommand("DisconnectedPlayerSuccessfully");
            disconnectedPlayerSuccessfully(command);
            isGameEnded(livingRoom);
        } else {
            command.setCommand("NotDisconnectedPlayer");
            notDisconnectedPlayer(command);
        }
    }

    @Override
    public void getActiveLivingRooms(Command command) throws RemoteException {

        String playerName = command.getArgs().get(0);
        int listLength = Integer.parseInt(command.getArgs().get(0));
        int occurrence = Integer.parseInt(command.getArgs().get(1));

        getActiveLivingRooms(playerName, listLength, occurrence);
    }

    @Override
    public void getActiveLivingRooms(String playerName, int listLength, int occurrence) {

        Command command = new Command("", new ArrayList<String>(), "");
        command.addArg(playerName); //ARG -> 0
        command.setCommand("LivingRoomsList");
        String arg1 = "";

        for(String s : controller.getActiveLivingRooms(listLength, occurrence)){
            arg1 += s + "-";
        }
        command.addArg(arg1); //ARG -> 1
        command.addArg(Integer.toString(occurrence)); //ARG -> 1
        livingRoomsList(command);
    }

    @Override
    public void isPossiblePick(Command command) throws RemoteException {
        Player player = controller.getPlayerByName(command.getArgs().get(0));
        String livingRoomId = command.getArgs().get(1);
        List<BoardPosition> pick = JSONInterface.recreatePick(command.getArgs().get(2));

        isPossiblePick(player, livingRoomId, pick);
    }

    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {

        Command command = new Command("", new ArrayList<String>(), "");
        command.addArg(player.getName()); //ARG -> 0

        if(controller.isPossiblePick(player, livingRoomId, pick)) {
            command.setCommand("PossiblePick");
            command.addArg(JSONInterface.generatePick(pick)); //ARG -> 1
            possiblePick(command);
        } else {
            command.setCommand("NotPossiblePick");
            notPossiblePick(command);
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
        extractViewFromName(command.getArgs().get(0)).loginUnsuccessful(command);
    }

    @Override
    public void createGameNotSuccessful(Command command) {
        extractViewFromName(command.getArgs().get(0)).createGameNotSuccessful(command);
    }

    @Override
    public void notJoinedGame(Command command) {
        extractViewFromName(command.getArgs().get(0)).notJoinedGame(command);
    }

    @Override
    public void notEnoughSpacesInCol(Command command) {
        extractViewFromName(command.getArgs().get(0)).notEnoughSpacesInCol(command);
    }

    @Override
    public void livingRoomNotFound(Command command) {
        extractViewFromName(command.getArgs().get(0)).livingRoomNotFound(command);
    }

    @Override
    public void gameNotStarted(Command command) {
        extractViewFromName(command.getArgs().get(0)).gameNotStarted(command);
    }

    @Override
    public void gameNotEnded(Command command) {
        extractViewFromName(command.getArgs().get(0)).gameNotEnded(command);
    }

    @Override
    public void notDisconnectedPlayer(Command command) {
        extractViewFromName(command.getArgs().get(0)).notDisconnectedPlayer(command);
    }

    @Override
    public void notPossiblePick(Command command) {
        extractViewFromName(command.getArgs().get(0)).notPossiblePick(command);
    }


    //SUCCESSES

    @Override
    public void loginDoneSuccessfully(Command command) {
        extractViewFromName(command.getArgs().get(0)).loginDoneSuccessfully(command);
    }

    @Override
    public void livingRoomFound(Command command) {
        extractViewFromName(command.getArgs().get(0)).livingRoomFound(command);
    }

    @Override
    public void livingRoomsList(Command command) {
        extractViewFromName(command.getArgs().get(0)).livingRoomsList(command);
    }

    @Override
    public void gameStarted(Command command) {
        extractViewFromName(command.getArgs().get(0)).gameStarted(command);
    }

    @Override
    public void gameEnded(Command command) {
        extractViewFromName(command.getArgs().get(0)).gameEnded(command);
    }

    @Override
    public void joinedGame(Command command) {
        extractViewFromName(command.getArgs().get(0)).joinedGame(command);
    }

    @Override
    public void turnEndedSuccessfully(Command command) {
        extractViewFromName(command.getArgs().get(0)).turnEndedSuccessfully(command);
    }

    public void disconnectedPlayerSuccessfully(Command command) {
        extractViewFromName(command.getArgs().get(0)).disconnectedPlayerSuccessfully(command);
    }

    @Override
    public void possiblePick(Command command) {
        extractViewFromName(command.getArgs().get(0)).possiblePick(command);
    }

    //PRIVATE UTILITY METHODS
    private VirtualViewRMI_Client extractViewFromPlayer(Player p) {
        return (VirtualViewRMI_Client)controller.getWaitingForChoice().stream().filter(p1 ->
                p1.getPlayer().getName().equals(p.getName())).findFirst().get().getView();
    }

    private VirtualViewRMI_Client extractViewFromName(String name) {
        return (VirtualViewRMI_Client)controller.getWaitingForChoice().stream().filter(p1 ->
                p1.getPlayer().getName().equals(name)).findFirst().get().getView();
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
