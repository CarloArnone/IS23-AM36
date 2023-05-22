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

public class VirtualViewRMI_Client implements RMI_ClientInterface, ICommunication {

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
    public void NotEnoughSpacesInCol(Command args) {

    }

    @Override
    public void LoginUnsuccessful(Command args) {

    }

    @Override
    public void LivingRoomNotFound(Command args) {

    }

    @Override
    public void CreateGameNotSuccessful(Command args) {

    }

    @Override
    public void NotDisconnectedPlayer(Command args) {

    }

    @Override
    public void GameNotStarted(Command args) {

    }

    @Override
    public void GameNotEnded(Command args) {

    }

    @Override
    public void NotPossiblePick(Command args) {

    }

    @Override
    public void TurnEndedSuccessfully(Command args) {

    }

    @Override
    public void LoginDoneSuccessfully(Command args) {

    }

    @Override
    public void LivingRoomFound(Command args) {

    }

    @Override
    public void JoinedGame(Command args) {

    }

    @Override
    public void DisconnectedPlayer(Command args) {

    }

    @Override
    public void LivingRoomsList(Command args) {

    }

    @Override
    public void GameStarted(Command args) {

    }

    @Override
    public void GameEnded(Command args) {

    }

    @Override
    public void PossiblePick(Command args) {

    }

    @Override
    public void NotifyListener(Command args) {

    }

    @Override
    public String echo(String echoString) throws RemoteException {
        return null;
    }

    @Override
    public String upper(String upperString) throws RemoteException {
        return null;
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
    public void logInTryEvent(String name, ICommunication virtualView) {

        List<String> args = new ArrayList<>();
        args.add(0, name);
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
