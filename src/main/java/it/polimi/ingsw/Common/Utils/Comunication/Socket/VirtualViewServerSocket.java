package it.polimi.ingsw.Common.Utils.Comunication.Socket;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.Utils.Listener;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;
import javafx.css.converter.LadderConverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class VirtualViewServerSocket extends Thread implements ICommunication {
    private Socket clientSocket;
    private Controller controller;
    private PrintWriter out;
    private Scanner in;
    private String name;

    private InetAddress clientAddress;


    public VirtualViewServerSocket(Socket socket, Controller controller) {
        this.clientSocket = socket;
        this.controller = controller;
        name = this.toString();
        clientAddress = socket.getInetAddress();
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

        String inputLine = "";
        Map<String, Object> command;
        ping();


            while (true) {

                try {
                    if((inputLine = in.nextLine()).equals("stop")){
                        break;
                    }
                }
                catch(java.util.NoSuchElementException nee){
                    //This is needed in the case the client crushes to handle it
                    LivingRoom livingRoom = controller.findLivingRoomWithVirtualView(this);
                    Player player = controller.getPlayerByVirtualView(this);
                    if(livingRoom != null){
                        disconnectedPlayer(livingRoom, player.getName(), false, this);
                        System.out.println("Player " + player.getName() + " left the Game ( " + livingRoom.getLivingRoomId() + " ).");
                    }
                    else if(player != null){
                        controller.removePlayerFromServer(player);
                    }
                    break;
                }

                if(controller == null){
                    break;
                }


                command = JSONInterface.recreateCommand(inputLine);
                List<String> args = (List<String>) command.get("args");



                if(controller.getPlayerByVirtualView(this) != null){
                    name = controller.getPlayerByVirtualView(this).getName();
                }

                System.out.println("Recieved from " + name + " : " + (char)27 + "[38;2;0;119;182m " + command + (char)27 + "[0m");

                switch ((String) command.get("command")) {
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
                    case "leaveGame" -> leaveGameEvent(args.get(1), controller.getLivingRoomById(args.get(0)), this);
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


    public void ping(){
        new Thread(() -> {
            while(true){
                try {
                    if (!clientAddress.isReachable(5000)) pingFailed();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

    }


    private void pingFailed() {
        disconnectedPlayer(controller.findLivingRoomWithVirtualView(this), controller.getPlayerByVirtualView(this).getName(), false, this);
        controller = null;
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
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
            livingRoom.notifyAllListeners("Update");
        } catch (NotEnoughSpacesInCol e) {
            List<String> args = new ArrayList<>();
            args.add("NotEnoughSpacesInCol");
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
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
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
        }
        else {
            List<String> args = new ArrayList<>();
            args.add("LoginUnsuccessful");
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
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
            String livingRoomJson = JSONInterface.writeNotSaveLivingRoomToJson(livingRoom, "");
            args.add(0, "LivingRoomFound");
            args.add(1, livingRoomJson);
            args.add(2, "fetchOldGame");
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
        }
        else {
            args.add("LivingRoomNotFound");
            args.add(1, "fetchOldGame");
            String command = JSONInterface.generateCommand("Error", args, "id is incorrect");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
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
            String livingRoomJson = JSONInterface.writeNotSaveLivingRoomToJson(controller.createGameEvent(livingRoomID, p, PlayersNum), "");
            List<String> args = new ArrayList<>();
            args.add(0, "GameCreated");
            args.add(1, livingRoomJson);
            args.add(2, "createGame");
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
        } catch (InvalidGameIDException e) {
            List<String> args = new ArrayList<>();
            args.add("InvalidGameID");
            args.add(1, livingRoomID);
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
        } catch (PlayersOutOfBoundException e) {
            List<String> args = new ArrayList<>();
            args.add("PlayerOutOfBound");
            args.add(1, String.valueOf(PlayersNum));
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
        }
    }

    /**
     * @param livingRoomID
     */
    @Override
    public void retrieveOldGameEvent(String livingRoomID) {
        try {
            String livingRoomJson = JSONInterface.writeNotSaveLivingRoomToJson(controller.retrieveOldGameEvent(livingRoomID), "");
            List<String> args = new ArrayList<>();
            args.add(0, "LivingRoomFound");
            args.add(1, livingRoomJson);
            args.add(2, "retrieveOldGame");
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
        } catch (NoMatchingIDException e) {
            List<String> args = new ArrayList<>();
            args.add("LivingRoomNotFound");
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
        }
    }

    /**
     * @param livingRoomID
     * @param name
     */
    @Override
    public void joinGameEvent(String livingRoomID, String name) {
        Player player = controller.joinGameEvent(livingRoomID, name);
        List<String> args = new ArrayList<>();
        String command;
        if(player == null){
            args.add(0, "NotJoinedGame");
            args.add(1, "GameFull");
            command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");

        }
        else{
            String playerString = JSONInterface.writePlayerToJson(player);
            args.add(0, "JoinedGame");
            args.add(1, playerString);
            args.add(2, JSONInterface.writeNotSaveLivingRoomToJson(controller.getLivingRoomById(livingRoomID), ""));
            command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
            if (controller.isGamesStarted(controller.getLivingRoomById(livingRoomID))){
                controller.getLivingRoomById(livingRoomID).notifyAllListeners("GameStarted");
            }
            //TODO INSERT ELSE WITH GAME NOT STARTED
        }

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
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
            isGameEnded(livingRoom);
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "NotDisconnectedPlayer");
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
        }
    }

    /**
     * @param listLength
     * @param section
     */
    @Override
    public void getActiveLivingRooms(int listLength, int section) {
        List<String> args = new ArrayList<>();
        args.add(0, "LivingRoomsList");
        List<String> actimel = controller.getActiveLivingRooms(listLength, section);
        String livstring = "";
        for (String st: actimel) {
            livstring += st + "-";
        }
        args.add(1, livstring);
        args.add(2, String.valueOf(section+1));
        String command = JSONInterface.generateCommand("Success", args, "");
        out.println(command);
        System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGamesStarted(LivingRoom livingRoom) {
        if(controller.isGamesStarted(livingRoom)){
            List<String> args = new ArrayList<>();
            args.add(0, "GameStarted");
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
            livingRoom.notifyAllListeners("GameStarted");
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "GameNotStarted");
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
        }
    }

    /**
     * @param name
     * @param activeLivingRoom
     * @param virtualView
     */
    @Override
    public void leaveGameEvent(String name, LivingRoom activeLivingRoom, ICommunication virtualView) {
        disconnectedPlayer(activeLivingRoom, name, true, virtualView);
    }

    /**
     * @param livingRoom
     */
    @Override
    public void isGameEnded(LivingRoom livingRoom) {
        if(controller.isGameEnded(livingRoom)){
            endGame(livingRoom);
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "GameNotEnded");
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");
        }
    }


    /**
     * @param livingRoom
     */
    @Override
    public void endGame(LivingRoom livingRoom) {
        gameEndedMessageBuilder(livingRoom);
        controller.endGame(livingRoom);
    }

    private void gameEndedMessageBuilder(LivingRoom livingRoom) {

        livingRoom.getPlayers().forEach(player -> {
            player.getPersonalGoal().checkGoal(player);
            player.updateScore();
        });


        for(ICommunication player : livingRoom.getViewList()){
            if(player != null){
                String message = "GameEnded";
                if(isThisPlayerTheWinner(player)){
                    message += " winner";
                } else if (isLonelyPlayer(player)) {
                    message += " lonelyPlayer";
                }
                else {
                    message += " loser";
                }
                livingRoom.notifyListener(message, player);
            }
            else System.out.println("SENDING A MESSAGE TO A PLAYER THAT DOES NOT EXISTS");
        }

    }


    private boolean isLonelyPlayer(ICommunication virtualViewServerSocket) {
        LivingRoom livingRoom = controller.findLivingRoomWithVirtualView(virtualViewServerSocket);
        return livingRoom.getPlayers().stream().filter(player -> controller.getWaitingPlayerByName(player.getName()).isOnline()).toList().size() == 1;
    }

    private boolean isThisPlayerTheWinner(ICommunication virtualViewServerSocket) {
        Player player = controller.getPlayerByVirtualView(virtualViewServerSocket);
        Player winner = controller.findLivingRoomWithVirtualView(virtualViewServerSocket).getWinner();
        if(player == null){
            return false;
        }
        else return player.equals(winner);
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
            args.add(1, JSONInterface.generatePick(pick));
            String command = JSONInterface.generateCommand("Success", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;156;196;178m " + command + (char)27 + "[0m");
        }
        else{
            List<String> args = new ArrayList<>();
            args.add(0, "NotPossiblePick");
            String command = JSONInterface.generateCommand("Error", args, "");
            out.println(command);
            System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;231;109;131m " + command + (char)27 + "[0m");

        }
    }

    /**
     *
     */
    @Override
    public void notifyListener(String message) {
        List<String> args = new ArrayList<>();
        args.add(0, "NotifyListener");
        args.add(1, message);
        String command = JSONInterface.generateCommand("Success", args, "");
        out.println(command);
        System.out.println("Sent to " + name + " : " + (char)27 + "[38;2;213;187;177m " + command + (char)27 + "[0m");
    }
}
