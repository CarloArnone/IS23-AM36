package it.polimi.ingsw.Common.Utils.Comunication.RMI;

import it.polimi.ingsw.Common.Utils.Command;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class VirtualViewRMI_Client implements RMI_ClientInterface{

    private IUI ui;
    private int port;
    private RMI_ServerInterface clientStub;
    private Registry reg;
    private String clientName;

    public VirtualViewRMI_Client(IUI ui, int port) throws Exception {

        this.ui = ui;

        this.port = port;
        System.out.println("Port saved.");

        reg = LocateRegistry.getRegistry("127.0.0.1", this.port);
        // Looking up the registry for the remote object
        this.clientStub = (RMI_ServerInterface) reg.lookup("//localhost/mainServer");
    }

    @Override
    public void logInTryEvent(String name, ICommunication virtualView) {
        clientStub.logInTryEvent(name, this);
    }

    @Override
    public void createGameEvent(String livingRoomID, Player p, int PlayersNum) {
        List<String> args = new ArrayList<>();
        args.add(livingRoomID);
        args.add(p.getName());
        args.add(Integer.toString(PlayersNum));

        try {
            clientStub.createGameEvent(new Command("createGameEvent", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void previousGamesRequestEvent(String playerName) {
        List<String> args = new ArrayList<>();
        args.add(playerName);
        try {
            clientStub.previousGamesRequestEvent(new Command("PreviousGameRequest", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void retrieveOldGameEvent(String livingRoomID, String playerName) {
        List<String> args = new ArrayList<>();
        args.add(livingRoomID);
        args.add(this.clientName);

        try {
            clientStub.retrieveOldGameEvent(new Command("retrieveOldGameEvent", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinGameEvent(String livingRoomID, String name) {
        List<String> args = new ArrayList<>();
        args.add(livingRoomID);
        args.add(name);

        try {
            clientStub.joinGameEvent(new Command("joinGameEvent", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) {

        List<String> args = new ArrayList<>();
        args.add(0, livingRoom.getLivingRoomId());
        args.add(1, p.getName());
        args.add(2, JSONInterface.generatePick(pick));
        args.add(3, String.valueOf(col));

        Command command = new Command("confirmEndTurn", args, "1)Livingroom 2)Player 3)PickList 4)Columns");

        try {
            clientStub.confirmEndTurn(command);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void isGamesStarted(LivingRoom livingRoom) {
        List<String> args = new ArrayList<>();
        args.add(livingRoom.getLivingRoomId());

        try {
            clientStub.isGamesStarted(new Command("isGameStarted", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void isGameEnded(LivingRoom livingRoom) {
        List<String> args = new ArrayList<>();
        args.add(livingRoom.getLivingRoomId());

        try {
            clientStub.isGameEnded(new Command("isGameEnded", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endGame(LivingRoom livingRoom) {
        List<String> args = new ArrayList<>();
        args.add(livingRoom.getLivingRoomId());

        try {
            clientStub.endGame(new Command("endGame", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) {
        List<String> args = new ArrayList<>();
        args.add(name);
        args.add(activeLivingRoom.getLivingRoomId());

        try {
            clientStub.leaveGameEvent(new Command("leaveGameEvent", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) {
        List<String> args = new ArrayList<>();
        args.add(livingRoom.getLivingRoomId());

        clientStub.disconnectedPlayer(new Command("retrieveOldGameEvent", args, ""));
    }

    @Override
    public void getActiveLivingRooms(int listLength, int occurrence) {
        List<String> args = new ArrayList<>();
        args.add(Integer.toString(listLength));
        args.add(Integer.toString(occurrence));

        try {
            clientStub.getActiveLivingRooms(new Command("getActiveLivingRooms", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {
        List<String> args = new ArrayList<>();
        args.add(player.getName());
        args.add(livingRoomId);
        args.add(JSONInterface.generatePick(pick));

        try {
            clientStub.isPossiblePick(new Command("isPossiblePick", args, ""));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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
        ui.retryLogin();
    }

    @Override
    public void createGameNotSuccessful(Command command) {
        ui.retryCreateGame(command.getArgs().get(1), command.getArgs().get(2));
    }

    @Override
    public void notEnoughSpacesInCol(Command command) {
        ui.retryPlacement();
    }

    @Override
    public void livingRoomNotFound(Command command) {
        ui.livingRoomNotFound(command.getArgs().get(1));
    }

    @Override
    public void gameNotStarted(Command command) {
        ui.gameNotStarted();
    }

    @Override
    public void gameNotEnded(Command command) {
        ui.gameNotEnded();
    }

    @Override
    public void notDisconnectedPlayer(Command command) {
        ui.notDisconnected();
    }

    @Override
    public void notPossiblePick(Command command) {
        ui.retryPick();
    }


    //SUCCESSES

    @Override
    public void loginDoneSuccessfully(Command command) {
        ui.loginSuccessful();
    }

    @Override
    public void livingRoomFound(Command command) {
        LivingRoom tempLiv = JSONInterface.getLivingRoomFromJsonString(command.getArgs().get(1));
        ui.livingRoomFound(tempLiv, command.getArgs().get(2));
    }

    @Override
    public void livingRoomsList(Command command) {
        ui.livingRoomsList(command.getArgs().get(1), Integer.parseInt(command.getArgs().get(2)));
    }

    @Override
    public void gameStarted(Command command) {
        ui.gameStarted();
    }

    @Override
    public void gameEnded(Command command) {
        ui.gameEnded(command.getArgs().get(1));
    }

    @Override
    public void joinedGame(Command command) {
        Player p = JSONInterface.getPlayerFromJson(command.getArgs().get(1));
        LivingRoom liv = JSONInterface.getLivingRoomFromJson(command.getArgs().get(2), command.getArgs().get(3));
        ui.joinedGame(p, liv);
    }

    @Override
    public void turnEndedSuccessfully(Command command) {
        ui.turnPassed();
    }

    @Override
    public void disconnectedPlayerSuccessfully(Command command) {
        ui.disconnected();
    }

    @Override
    public void possiblePick(Command command) {
        List<BoardPosition> pick = JSONInterface.recreatePick(command.getArgs().get(1));
        ui.possiblePick(pick);
    }

    //TESTING
    @Override
    public String echo(String echoString) throws RemoteException {
        return echoString;
    }

    @Override
    public String upper(String upperString) throws RemoteException {
        return upperString.toUpperCase();
    }
}
