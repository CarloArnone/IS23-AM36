package it.polimi.ingsw.Common.Utils.Comunication.Socket;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class VirtualViewClientSocket implements ICommunication {

    private Socket comunicator;
    private IUI UI;
    private PrintWriter out;
    private Scanner in;
    InetAddress serverAddress ;

    public VirtualViewClientSocket(String ip, int port, IUI UI) throws IOException {
        comunicator = new Socket(ip, port);
        this.UI = UI;
        out = new PrintWriter(comunicator.getOutputStream(), true);
        in = new Scanner(comunicator.getInputStream());

        new Thread(() ->{
                            while(true){
                                handleReturn();
                                }
                            }).start();
        serverAddress = comunicator.getInetAddress();
        ping();
    }


    public void sendMessage(String msg){
        out.println(msg);
        //System.out.println(msg);
    }

    public IUI getUI(){
        return UI;
    }

    private void handleReturn() {
        if(in.hasNextLine()){
            Map<String, Object> response = JSONInterface.recreateCommand(in.nextLine());
            if (response.get("command").equals("Success")) {
                handleSuccess((List<String>) response.get("args"), (String) response.get("description"));
            } else handleError((List<String>) response.get("args"), (String) response.get("description"));
        }
    }

    private void handleError(List<String> args,  String description) {
        //System.out.println("Failure : " + args);

        switch (args.get(0)){
            case "NotEnoughSpacesInCol" -> UI.retryPlacement();
            case "LoginUnsuccessful" -> UI.retryLogin();
            case "NotJoinedGame" -> UI.gameNotJoined(args.get(1));
            case "LivingRoomNotFound" -> UI.livingRoomNotFound(args.get(1));
            case "InvalidGameID", "PlayerOutOfBound" -> UI.retryCreateGame(args.get(0), args.get(1));
            case "NotDisconnectedPlayer" -> UI.notDisconnected();
            case "GameNotStarted" -> UI.gameNotStarted();
            case "GameNotEnded" ->  UI.gameNotEnded();
            case "NotPossiblePick" ->  UI.retryPick();
        }


    }


    private void handleSuccess(List<String> args,  String description) {
        //System.out.println("Success : " + args);

        switch (args.get(0)){
            case "TurnEndedSuccessfully" -> UI.turnPassed();
            case "LoginDoneSuccessfully" -> UI.loginSuccessful();
            case "GameCreated", "LivingRoomFound" -> UI.livingRoomFound(JSONInterface.getLivingRoomFromJsonString(args.get(1)), args.get(2));
            case "JoinedGame" -> UI.joinedGame(JSONInterface.getPlayerFromJson(args.get(1)), JSONInterface.getLivingRoomFromJsonString(args.get(2)));
            case "DisconnectedPlayer" -> UI.disconnected();
            case "LivingRoomsList" ->  UI.livingRoomsList(args.get(1), Integer.parseInt(args.get(2)));
            case "GameStarted" -> UI.gameStarted();
            case "PossiblePick" -> UI.possiblePick(JSONInterface.recreatePick(args.get(1)));
            case "NotifyListener" -> notifyListener(args.get(1));
        }

    }

    /**
     *
     */
    @Override
    public void notifyListener(String message) {
        UI.notifyListener(message);
    }
    //TODO SEE BETTER UNDERSTANDING
    public void ping(){
        new Thread(() -> {
            while(true){
                try {
                    if (!serverAddress.isReachable(5000)) {
                        pingFailed();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void pingFailed() {
        UI.serverDiconnected();
    }


    /**
     * @param livingRoom
     * @param p
     * @param pick
     * @param col
     */
    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoom.getLivingRoomId());
        args.add(1, p.getName());
        args.add(2, JSONInterface.generatePick(pick));
        args.add(3, String.valueOf(col));

        sendMessage(JSONInterface.generateCommand("confirmEndTurn", args, ""));
    }

    /**
     * @param name
     * @param virtualView
     */
    @Override
    public void logInTryEvent(String name, ICommunication virtualView) {
        List<String> args = new ArrayList<>();
        args.add(0, name);
        sendMessage(JSONInterface.generateCommand("login", args, ""));
    }

    /**
     * @param name
     */
    @Override
    public void previousGamesRequestEvent(String name) {
        List<String> args = new ArrayList<>();
        args.add(0, name);
        sendMessage(JSONInterface.generateCommand("previousGame", args, ""));
    }

    /**
     * @param livingRoomID
     * @param p
     * @param PlayersNum
     */
    @Override
    public void createGameEvent(String livingRoomID, Player p, int PlayersNum) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoomID);
        args.add(1, p.getName());
        args.add(String.valueOf(PlayersNum));
        sendMessage(JSONInterface.generateCommand("createGame", args, ""));
    }

    /**
     * @param livingRoomID
     */
    @Override
    public void retrieveOldGameEvent(String livingRoomID) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoomID);
        sendMessage(JSONInterface.generateCommand("retrieveGame", args, ""));
    }

    /**
     * @param livingRoomID
     * @param name
     */
    @Override
    public void joinGameEvent(String livingRoomID, String name) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoomID);
        args.add(1, name);
        sendMessage(JSONInterface.generateCommand("joinGame", args, ""));
    }

    /**
     * @param livingRoom
     * @param name
     * @param voluntaryLeft
     * @param virtualView
     */
    @Override
    public void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoom.getLivingRoomId());
        args.add(1, name);
        args.add(String.valueOf(voluntaryLeft));
        sendMessage(JSONInterface.generateCommand("disconnectPlayer", args, ""));
    }

    /**
     * @param listLength
     * @param occurency
     */
    @Override
    public void getActiveLivingRooms(int listLength, int occurency) {
        List<String> args = new ArrayList<>();
        args.add(0, String.valueOf(listLength));
        args.add(1, String.valueOf(occurency));
        sendMessage(JSONInterface.generateCommand("getLivingRoomsList", args, ""));

    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGamesStarted(LivingRoom livingRoom) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoom.getLivingRoomId());
        sendMessage(JSONInterface.generateCommand("isGameStarted", args, ""));
    }

    /**
     * @param name
     * @param activeLivingRoom
     * @param virtualView
     */
    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) {
        List<String> args = new ArrayList<>();
        args.add(0, activeLivingRoom.getLivingRoomId());
        args.add(1, name);
        args.add(String.valueOf(true));
        sendMessage(JSONInterface.generateCommand("leaveGame", args, ""));

    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGameEnded(LivingRoom livingRoom) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoom.getLivingRoomId());
        sendMessage(JSONInterface.generateCommand("isGameEnded", args, ""));
    }

    /**
     * @param livingRoom
     */
    @Override
    public void endGame(LivingRoom livingRoom) {
        List<String> args = new ArrayList<>();
        args.add(0, livingRoom.getLivingRoomId());
        sendMessage(JSONInterface.generateCommand("endGame", args, ""));
    }

    /**
     * @param player
     * @param livingRoomId
     * @param pick
     */
    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {
        List<String> args = new ArrayList<>();
        args.add(0, player.getName());
        args.add(1, livingRoomId);
        args.add(2, JSONInterface.generatePick(pick));
        sendMessage(JSONInterface.generateCommand("isPossiblePick", args, ""));
    }

}


