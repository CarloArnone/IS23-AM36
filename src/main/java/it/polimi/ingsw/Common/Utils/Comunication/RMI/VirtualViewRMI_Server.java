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
            tempView.LoginDoneSuccessfully(command);
        } else {
            command.setCommand("LoginUnsuccessful");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.LoginUnsuccessful(command);
        }
    }

    @Override
    public void confirmEndTurn(Command args) throws RemoteException {

        LivingRoom livingRoom = controller.getLivingRoomById(args.getArgs().get(0));
        Player player = controller.getPlayerByName(args.getArgs().get(1));
        List<BoardPosition> pick = JSONInterface.recreatePick(args.getArgs().get(2));
        int col = Integer.parseInt(args.getArgs().get(3));

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
                tempView.TurnEndedSuccessfully(command);
            } else {
                command.setCommand("");
                command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
                //TODO: DECIDE HOW TO HANDLE THIS CASE
            }
        } catch (NotEnoughSpacesInCol e) {
            command.setCommand("NotEnoughSpacesInCol");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.NotEnoughSpacesInCol(command);
        }
    }

    @Override
    public void previousGamesRequestEvent(Command args) throws RemoteException {
        previousGamesRequestEvent(args.getArgs().get(0));
    }

    //TODO: DIFFERENCE WITH -- retrieveOldGameEvent -- ?
    @Override
    public void previousGamesRequestEvent(String name) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        if(controller.previousGamesRequestEvent(name) != null){
            command.setCommand("LivingRoomFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.LivingRoomFound(command);
        } else {
            command.setCommand("LivingRoomNotFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.LivingRoomFound(command);
        }
    }

    @Override
    public void createGameEvent(Command args) throws RemoteException {

        String livingRoomID = args.getArgs().get(0);
        Player player = controller.getPlayerByName(args.getArgs().get(1));
        int playersNum = Integer.parseInt(args.getArgs().get(2));

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
            tempView.CreateGameNotSuccessful(command);
            throw new RuntimeException(e);
        }

    }

    @Override
    public void retrieveOldGameEvent(Command args) throws RemoteException {
        retrieveOldGameEvent(args.getArgs().get(0));
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
            tempView.LivingRoomFound(command);
        } catch (NoMatchingIDException e) {
            command.setCommand("LivingRoomNotFound");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.LivingRoomNotFound(command);
        }
    }

    @Override
    public void joinGameEvent(Command args) throws RemoteException {
        joinGameEvent(args.getArgs().get(0), args.getArgs().get(1));
    }

    @Override
    public void joinGameEvent(String livingRoomID, String name) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        Player tempPlayer = controller.joinGameEvent(livingRoomID, name);

        if(controller.getLivingRoomById(livingRoomID).getPlayers().stream().anyMatch(p1 -> p1.getName().equals(name))){
            command.setCommand("JoinedGame");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.JoinedGame(command);
        } else {
            //TODO:DO WE HAVE TO CONSIDER THIS CASE?
        }

    }

    @Override
    public void disconnectedPlayer(Command args) throws RemoteException {

        LivingRoom livingRoom = controller.getLivingRoomById(args.getArgs().get(0));
        String name = args.getArgs().get(1);
        boolean voluntaryLeft = Boolean.parseBoolean(args.getArgs().get(2));

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
            tempView.DisconnectedPlayer(command);
        } else {
            command.setCommand("NotDisconnectedPlayer");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.NotDisconnectedPlayer(command);
        }
    }

    @Override
    public void getActiveLivingRooms(Command args) throws RemoteException {

        int listLength = Integer.parseInt(args.getArgs().get(0));
        int occurrence = Integer.parseInt(args.getArgs().get(1));

        getActiveLivingRooms(listLength, occurrence);
    }

    @Override
    public void getActiveLivingRooms(int listLength, int occurrence) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()--)

        for(String s : controller.getActiveLivingRooms(listLength, occurrence)){
            command.addArg(s); //TODO: CHECK IF THIS CORRECT
        }
        command.setCommand("LivingRoomsList");
        tempView.LivingRoomsList(command);
    }

    @Override
    public void isGamesStarted(Command args) throws RemoteException {
        isGamesStarted(controller.getLivingRoomById(args.getArgs().get(0)));
    }

    @Override
    public void isGamesStarted(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        if(controller.isGamesStarted(livingRoom)) {
            command.setCommand("GameStarted");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.GameStarted(command);
        } else {
            command.setCommand("GameNotStarted");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.GameNotStarted(command);
        }
    }

    @Override
    public void isGameEnded(Command args) throws RemoteException {
        isGameEnded(controller.getLivingRoomById(args.getArgs().get(0)));
    }

    @Override
    public void isGameEnded(LivingRoom livingRoom) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = null; //TODO:WHERE TO GET REFERENCE TO VIRTUAL VIEW OF CALLER? (COULD TRY TO LOOK INTO --RemoteServer.getClientHost()-- )

        if(controller.isGamesStarted(livingRoom)) {
            command.setCommand("GameEnded");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.GameEnded(command);
        } else {
            command.setCommand("GameNotEnded");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.GameNotEnded(command);
        }
    }

    @Override
    public void endGame(Command args) throws RemoteException {
        endGame(controller.getLivingRoomById(args.getArgs().get(0)));
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
    public void leaveGameEvent(Command args) throws RemoteException {

        String name = args.getArgs().get(0);
        LivingRoom activeLivingRoom = controller.getLivingRoomById(args.getArgs().get(1));

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
    public void isPossiblePick(Command args) throws RemoteException {
        Player player = controller.getPlayerByName(args.getArgs().get(0));
        String livingRoomId = args.getArgs().get(1);
        List<BoardPosition> pick = JSONInterface.recreatePick(args.getArgs().get(2));
    }

    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {

        Command command = new Command("", new ArrayList<String>(), "");
        VirtualViewRMI_Client tempView = extractViewFromPlayer(player);

        if(controller.isPossiblePick(player, livingRoomId, pick)) {
            command.setCommand("PossiblePick");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.PossiblePick(command);
        } else {
            command.setCommand("NotPossiblePick");
            command.addArg(""); //TODO: GIVE AN ACTUAL COMMAND
            tempView.NotPossiblePick(command);
        }
    }

    @Override
    public void notifyListener() {

    }

    //PRIVATE UTILITY METHODS
    private VirtualViewRMI_Client extractViewFromPlayer(Player p) {
        return (VirtualViewRMI_Client)controller.getWaitingForChoice().stream().filter(p1 -> p1.getPlayer().getName().equals(p.getName())).findFirst().get().getView();
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
