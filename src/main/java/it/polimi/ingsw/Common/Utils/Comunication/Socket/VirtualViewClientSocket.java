package it.polimi.ingsw.Common.Utils.Comunication.Socket;

import it.polimi.ingsw.Common.Utils.BlackBoard;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VirtualViewClientSocket implements ICommunication {

    private Socket comunicator;
    private IUI UI;
    private PrintWriter out;
    private Scanner in;

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
    }


    public void sendMessage(String msg){
        out.println(msg);
        System.out.println(msg);
    }

    private void handleReturn() {
        Map<String, Object> response = JSONInterface.recreateCommand(in.nextLine());
        if(response.get("command").equals("Success")){
            handleSuccess((List<String>)response.get("args"), (String) response.get("description"));
        }
        else handleError((List<String>)response.get("args"), (String) response.get("description"));
    }

    private void handleError(List<String> args,  String description) {
        System.out.println("Failure : " + args);

        switch (args.get(0)){
            case "NotEnoughSpacesInCol" -> BlackBoard.write("confirmEndTurnReturn", "false");
            case "LoginUnsuccessful" -> BlackBoard.write("loginReturn", "false");
            case "LivingRoomNotFound" -> BlackBoard.write("livingRoomExists", "false");
            case "InvalidGameID", "PlayerOutOfBound" -> BlackBoard.write("GameCreated", args.get(0));
            case "NotDisconnectedPlayer" -> BlackBoard.write("disconnectionReturn", "false");
            case "GameNotStarted" -> BlackBoard.write("createGameReturn", "false");
            case "GameNotEnded" -> BlackBoard.write("endGameReturn", "false");
            case "NotPossiblePick" -> BlackBoard.write("isPossiblePickReturn", "false");
        }


    }

    private void handleSuccess(List<String> args,  String description) {
        System.out.println("Success : " + args);

        switch (args.get(0)){
            case "TurnEndedSuccessfully" -> BlackBoard.write("confirmEndTurnReturn", "true");
            case "LoginDoneSuccessfully" -> BlackBoard.write("loginReturn", "true");
            case "GameCreated", "LivingRoomFound" -> {
                UI.updateLivingRoom(JSONInterface.getLivingRoomFromJsonString(args.get(1)));
                BlackBoard.write("livingRoomFoundReturn", "true");
            }
            case "JoinedGame" -> {
                UI.updatePlayer(JSONInterface.getPlayerFromJson(args.get(1)));
                BlackBoard.write("GameJoined", "true");
            }
            case "DisconnectedPlayer" -> BlackBoard.write("disconnectionReturn", "true");
            case "LivingRoomsList" -> BlackBoard.write("activeLivingRooms", args.get(1));
            case "GameStarted" -> BlackBoard.write("createGameReturn", "true");
            case "GameEnded" -> BlackBoard.write("endGameReturn", "true");
            case "PossiblePick" -> BlackBoard.write("isPossiblePickReturn", "true");
            case "NotifyListener" -> notifyListener();
        }

    }

    /**
     *
     */
    @Override
    public void notifyListener() {
        UI.notifyListener();
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
        sendMessage(JSONInterface.generateCommand("retrieveGame", args, ""));
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
     * @param occurrence
     */
    @Override
    public void getActiveLivingRooms(int listLength, int occurrence) {
        List<String> args = new ArrayList<>();
        args.add(0, String.valueOf(listLength));
        args.add(1, String.valueOf(occurrence));
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
