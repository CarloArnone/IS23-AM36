package it.polimi.ingsw.Common.Utils.Comunication.Socket;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class VirtualViewServerSocket extends Thread implements ICommunication {
    private Socket clientSocket;
    private Controller controller;
    private PrintWriter out;
    private Scanner in;

    public VirtualViewServerSocket(Socket socket, Controller controller) {
        this.clientSocket = socket;
        this.controller = controller;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            in = new Scanner(clientSocket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String inputLine;
        Map<String, Object> command;
        while (!(inputLine = in.nextLine()).equals("stop")) {

            command = JSONInterface.recreateCommand(inputLine);
            List<String> args = (List<String>)command.get("args");

            System.out.println(command);

            switch ((String) command.get("command")){
                case "confirmEndTurn" -> confirmEndTurn(controller.getLivingRoomById(args.get(0)), controller.getPlayerByName(args.get(1)), JSONInterface.recreatePick(args.get(2)), Integer.parseInt(args.get(3)));
                case "login" -> logInTryEvent(args.get(0), this);
                case "previousGame" -> previousGamesRequestEvent(args.get(0));
                case "createGame" -> createGameEvent(args.get(0), controller.getPlayerByName(args.get(1)), Integer.parseInt(args.get(2)));
                case "retrieveGame" -> retrieveOldGameEvent(args.get(0));
                case "joinGame" -> joinGameEvent(args.get(0), args.get(1));
                case "disconnectPlayer" -> disconnectedPlayer(controller.getLivingRoomById(args.get(0)), args.get(1), Boolean.parseBoolean(args.get(2)), this);
                case "getLivingRoomsList" -> getActiveLivingRooms(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)));
                case "isGameStarted" -> isGamesStarted(controller.getLivingRoomById(args.get(0)));
                case "isGameEnded" -> isGameEnded(controller.getLivingRoomById(args.get(0)));
                case "leaveGame" -> leaveGameEvent(args.get(0), controller.getLivingRoomById(args.get(0)), this );
                case "endGame" -> endGame(controller.getLivingRoomById(args.get(0)));
                case "isPossiblePick" -> isPossiblePick(controller.getPlayerByName(args.get(0)), args.get(1), JSONInterface.recreatePick(args.get(2)));
                default -> out.println("command invalid");
            }


        }


        in.close();
        out.close();
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return;
    }




    /**
     * @param livingRoom
     * @param p
     * @param pick
     * @param col
     */
    @Override
    public void confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col){
        try {
            controller.confirmEndTurn(livingRoom, p, pick, col);
            List<String> args = new ArrayList<>();
            args.add("TurnEndedSuccessfully");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        } catch (NotEnoughSpacesInCol e) {
            List<String> args = new ArrayList<>();
            args.add("NotEnoughSpacesInCol");
            out.println(JSONInterface.generateCommand("Error", args, "Player " + p.getName() + " tried to insert " + pick.size() + " tiles in column " + col));
        }
    }

    /**
     * @param name
     * @param virtualView
     */
    @Override
    public void logInTryEvent(String name, ICommunication virtualView) {
        if(controller.logInTryEvent(name, virtualView)){
            List<String> args = new ArrayList<>();
            args.add("LoginDoneSuccessfully");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else {
            List<String> args = new ArrayList<>();
            args.add("LoginUnsuccessful");
            out.println(JSONInterface.generateCommand("Error", args, "There is a user named " + name));
        }
    }

    /**
     * @param name
     */
    @Override
    public void previousGamesRequestEvent(String name) {
        LivingRoom livingRoom = controller.previousGamesRequestEvent(name);
        List<String> args = new ArrayList<>();
        if(livingRoom != null){
            String livingRoomJson = JSONInterface.writeLivingRoomToJson(livingRoom);
            args.add(0, "LivingRoomFound");
            args.add(1, livingRoomJson);
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else {
            args.add("LivingRoomNotFound");
            out.println(JSONInterface.generateCommand("Error", args, "The id is incorrect"));
        }
    }

    /**
     * @param livingRoomID
     * @param p
     * @param PlayersNum
     */
    @Override
    public void createGameEvent(String livingRoomID, Player p, int PlayersNum) {
        try {
            String livingRoomJson = JSONInterface.writeLivingRoomToJson(controller.createGameEvent(livingRoomID, p, PlayersNum));
            List<String> args = new ArrayList<>();
            args.add(0, "GameCreated");
            args.add(1, livingRoomJson);
            out.println(JSONInterface.generateCommand("Success", args, ""));
        } catch (InvalidGameIDException e) {
            List<String> args = new ArrayList<>();
            args.add("InvalidGameID");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        } catch (PlayersOutOfBoundException e) {
            List<String> args = new ArrayList<>();
            args.add("PlayerOutOfBound");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        }
    }

    /**
     * @param livingRoomID
     */
    @Override
    public void retrieveOldGameEvent(String livingRoomID) {
        try {
            String livingRoomJson = JSONInterface.writeLivingRoomToJson(controller.retrieveOldGameEvent(livingRoomID));
            List<String> args = new ArrayList<>();
            args.add(0, "LivingRoomFound");
            args.add(1, livingRoomJson);
            out.println(JSONInterface.generateCommand("Success", args, ""));
        } catch (NoMatchingIDException e) {
            List<String> args = new ArrayList<>();
            args.add("LivingRoomNotFound");
            out.println(JSONInterface.generateCommand("Error", args, "The id is incorrect"));
        }
    }

    /**
     * @param livingRoomID
     * @param name
     */
    @Override
    public void joinGameEvent(String livingRoomID, String name) {
        String playerString = JSONInterface.writePlayerToJson(controller.joinGameEvent(livingRoomID, name));
        List<String> args = new ArrayList<>();
        args.add(0, "JoinedGame");
        args.add(1, playerString);
        out.println(JSONInterface.generateCommand("Success", args, ""));

        //TODO ADD SOME NOT CORRECT INPUT CASES (LIVID NOT VALID)
    }


    /**
     * @param livingRoom
     * @param name
     * @param voluntaryLeft
     * @param virtualView
     */
    @Override
    public void disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, ICommunication virtualView) {
        if(controller.disconnectedPlayer(livingRoom, name, voluntaryLeft, virtualView)){
            List<String> args = new ArrayList<>();
            args.add(0, "DisconnectedPlayer");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "NotDisconnectedPlayer");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        }
    }

    /**
     * @param listLength
     * @param occurency
     */
    @Override
    public void getActiveLivingRooms(int listLength, int occurency) {
        List<String> args = new ArrayList<>();
        args.add(0, "LivingRoomsList");
        args.addAll(controller.getActiveLivingRooms(listLength, occurency));
        out.println(JSONInterface.generateCommand("Success", args, ""));
    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGamesStarted(LivingRoom livingRoom) {
        if(controller.isGamesStarted(livingRoom)){
            List<String> args = new ArrayList<>();
            args.add(0, "GameStarted");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "GameNotStarted");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        }
    }

    /**
     * @param name
     * @param activeLivingRoom
     * @param virtualView
     */
    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) {
        if(controller.disconnectedPlayer(activeLivingRoom, name, true, virtualView)){
            List<String> args = new ArrayList<>();
            args.add(0, "DisconnectedPlayer");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "NotDisconnectedPlayer");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        }
    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGameEnded(LivingRoom livingRoom) {
        if(controller.isGameEnded(livingRoom)){
            List<String> args = new ArrayList<>();
            args.add(0, "GameEnded");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "GameNotEnded");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        }
    }

    /**
     * @param livingRoom
     */
    @Override
    public void endGame(LivingRoom livingRoom) {
        if(controller.endGame(livingRoom)){
            List<String> args = new ArrayList<>();
            args.add(0, "GameEnded");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "GameNotEnded");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        }
    }

    /**
     * @param player
     * @param livingRoomId
     * @param pick
     */
    @Override
    public void isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick) {
        if(controller.isPossiblePick(player, livingRoomId, pick)){
            List<String> args = new ArrayList<>();
            args.add(0, "PossiblePick");
            out.println(JSONInterface.generateCommand("Success", args, ""));
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "NotPossiblePick");
            out.println(JSONInterface.generateCommand("Error", args, ""));
        }
    }

    /**
     *
     */
    @Override
    public void notifyListener() {
        List<String> args = new ArrayList<>();
        args.add(0, "NotifyListener");
        out.println(JSONInterface.generateCommand("Success", args, ""));
    }
}
