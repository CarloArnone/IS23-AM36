package it.polimi.ingsw.Client.CLI;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Common.Exceptions.*;
import it.polimi.ingsw.Common.Listener;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.Utils.TestGenerator;
import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.*;
import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class CLI implements Listener {

    private LivingRoom viewLivingRoom;
    private List<BoardPosition> pick;
    private int me; // It means MyTURN
    private String name;
    private Player mySelf;

    private eventObserver controller;
    private File inputFile = null;

    private final String blueCard = "[48;2;0;119;182m   ";
    private final String purpleCard = "[48;2;255;0;110m   ";
    private final String whiteCard = "[48;2;255;236;209m   ";
    private final String greenCard = "[48;2;106;153;69m   ";
    private final String cyanCard = "[48;2;0;180;216m   ";
    private final String yellowCard = "[48;2;232;170;20m   ";


    private final String blueCardNot = "[38;2;0;0;0;48;2;0;119;182m X ";
    private final String purpleCardNot = "[38;2;0;0;0;48;2;255;0;110m X ";
    private final String whiteCardNot = "[38;2;0;0;0;48;2;255;236;209m X ";
    private final String greenCardNot = "[38;2;0;0;0;48;2;106;153;69m X ";
    private final String cyanCardNot = "[38;2;0;0;0;48;2;0;180;216m X ";
    private final String yellowCardNot = "[38;2;0;0;0;48;2;232;170;20m X ";

    private final String blueCardDark = "[38;2;0;0;0;48;2;0;119;182m * ";
    private final String purpleCardDark = "[38;2;0;0;0;48;2;255;0;110m * ";
    private final String whiteCardDark = "[38;2;0;0;0;48;2;255;236;209m * ";
    private final String greenCardDark = "[38;2;0;0;0;48;2;106;153;69m * ";
    private final String cyanCardDark = "[38;2;0;0;0;48;2;0;180;216m * ";
    private final String yellowCardDark = "[38;2;0;0;0;48;2;232;170;20m * ";

    private final String shelfBackGorund = "[48;2;164;113;72m   ";
    private final String shelfSeparator = "[48;2;96;56;8m ";
    private final String shelfBase = "[48;2;96;56;8m    ";

    private final String whiteBlock = "[48;2;255;255;255m   ";
    private final String equalBlock = "[38;2;225;6;0;48;2;255;236;209m = ";
    private final String emptyBlock = "[38;2;225;6;0;48;2;255;236;209m % ";
    private final String dfrntBlock = "[38;2;225;6;0;48;2;255;236;209m ≠ ";
    private final String vLink = "[38;2;0;0;0;48;2;255;255;255m | ";
    private final String hLink =  "[38;2;0;0;0;48;2;255;255;255m---";
    private final String whiteBackground = "[38;2;0;0;0;48;2;255;255;255m";
    private final String trasparentText = "[38;2;255;255;255;48;2;255;255;255m";




    public CLI(LivingRoom viewLivingRoom) {
        this.viewLivingRoom = viewLivingRoom;
        controller = new Controller();
        pick = new ArrayList<>();
    }

    public CLI(int me) {
        this.me = me;
        controller = new Controller();
        pick = new ArrayList<>();
    }

    public CLI(eventObserver controller) {
        this.controller = controller;
        pick = new ArrayList<>();
    }

    public CLI(File inputFile, eventObserver controller){
        this.controller = controller;
        pick = new ArrayList<>();
        this.inputFile = inputFile;
    }

    public void start() {
        AtomicBoolean exit = new AtomicBoolean(false);
        Scanner sc = null;

        try {
            sc = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            sc = new Scanner(System.in);
        }

        String message;
        boolean canProceed = false;
        while(!canProceed){
            canProceed = loginView(sc);
        }


        mySelf = new Player(name);
        //viewLivingRoom = TestGenerator.generateLivingRoom(3);//JSONInterface.getRandomLivingForTest();

        //TODO CHOICE TO JOIN OR CREATE.
        startingChoicesView(sc);

        /*while(controller.isGamesStarted(viewLivingRoom)){
            try {
                updateView("Waiting for PLayers to join");
            } catch (IOException e) {
                exit.set(true);
            }
        }*/

        try {
            updateView("new Command >\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner finalSc = sc;
        new Thread(() ->{
            boolean exitIn = false;
            while(!exitIn){
                String command = finalSc.nextLine();
                exitIn = parseCommand(command);
            }
            exit.set(true);
        }).run();
    }

    private void startingChoicesView(Scanner sc) {
        LivingRoom activeLivingRoom = controller.previousGamesRequestEvent(name);
        if(activeLivingRoom != null){
            System.out.println("\n\n\n\n\n\n\n");
            System.out.println("                                            There is a game you were playing : " + activeLivingRoom.getLivingRoomId());
            System.out.print("                                                            Do you want to rejoin ?  y/* ...  ");
            if(sc.nextLine().split(" ")[0].equals("y")){
                try {
                    viewLivingRoom = controller.retrieveOldGameEvent(activeLivingRoom.getLivingRoomId());
                } catch (NoMatchingIDException e) {
                    return;
                }
                controller.reconnectPlayer(viewLivingRoom, name);
                for (int i = 0; i < viewLivingRoom.getPlayers().size(); i++) {
                    if(viewLivingRoom.getPlayers().get(i).getName().equals(name)){
                        me = i;
                    }
                }
                return;
            }
            else controller.leaveGameEvent(name, activeLivingRoom, this );
        }
        System.out.flush();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("                                            Choose:\n");
        System.out.println("                                 Create Game      Join Game");
        System.out.println("                                      c        /      j      ");
        System.out.print("                                               > ");
        String commandCreateOrJoin = sc.nextLine().split(" ")[0];
        if(commandCreateOrJoin.equals("c")){
            List<String> parameters = new ArrayList<>();
            System.out.print("                                          Game ID:    "); parameters.add(0, sc.nextLine().split(" ")[0]); System.out.print("\n");
            System.out.print("                                      Players Num:    " ); parameters.add(1, sc.nextLine().split(" ")[0]); System.out.print("\n");
            System.out.println("                               Game_ID must have at least 5 characters");
            System.out.println("                                Players Num should be one of {2, 3, 4}");
            boolean hasPassedControls = false;
            while(!hasPassedControls){
                try {
                    viewLivingRoom = controller.createGameEvent(parameters.get(0), mySelf , Integer.parseInt(parameters.get(1)));
                    hasPassedControls = true;
                } catch (InvalidGameIDException e) {
                    Random random = new Random();
                    System.out.println("\n\n\n\n\n\n\n\n");
                    System.out.println("                                    ! Game ID Already Taken !");
                    System.out.println("                              What about :  " + parameters.get(0) + random.nextInt(1000, 9999));
                    for (int i = 0; i < 10; i++) {
                        System.out.println("                                            " + parameters.get(0) + random.nextInt(1000, 9999));
                    }
                    System.out.print("\n\n                                        New Name > "); parameters.remove(0); parameters.add(0, sc.nextLine().split(" ")[0]); System.out.print("\n");
                } catch (PlayersOutOfBoundException e) {
                    System.out.println("\n\n\n\n\n\n\n\n");
                    System.out.println("                                    ! Selected players num is incorrect !");
                    System.out.print("                              Please enter a new one:   "); parameters.remove(1); parameters.add(1, sc.nextLine().split(" ")[0]); System.out.print("\n");
                }

            }

            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n                                                                Game Created Successfully !");
        }
        else if(commandCreateOrJoin.equals("j")){
            boolean hasJoinedAGame = false;
            int groupID = 1;
            while(!hasJoinedAGame){
                List<String> activeLivingRooms = controller.getActiveLivingRooms(10, groupID);
                System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.println("                                                 -- Active Games List --");
                for (int i = 0; i < activeLivingRooms.size(); i++) {
                    System.out.println("                                                    " + activeLivingRooms.get(i));
                }

                System.out.println("\n\n                                               -- To Update: u , To Choose Digit a Name --");

                String command = sc.nextLine().split(" ")[0];
                if(activeLivingRooms.contains(command)){
                    mySelf =  controller.joinGameEvent(command, name);

                    try {
                        viewLivingRoom = controller.retrieveOldGameEvent(command);
                    } catch (NoMatchingIDException e) {
                        continue; //TODO PRINT NO LIVINGROOMS WITH THE NAME ID
                    }

                    for (int i = 0; i < viewLivingRoom.getPlayers().size(); i++){
                        if (mySelf.equals(viewLivingRoom.getPlayers().get(i))){
                            me = i;
                            break;
                        }
                    }
                    hasJoinedAGame = true;
                }
                else if(command.equals("u")){
                    groupID ++;
                }
            }

        }

    }

    private boolean loginView(Scanner sc) {
        System.out.flush();
        System.out.println("\n\n\n\n\n\n");
        System.out.print("                            UserName: ");
        String name = sc.nextLine();

        if(controller.logInTryEvent(name, this)){
            this.name = name;
            return true;
        }
        else{
            Random random = new Random();
            System.out.print("UserName Already Taken.                       What about : " + name + String.valueOf(random.nextInt(100, 999)) +"\n");
            for (int i = 0; i < 5; i++) {
                System.out.println("                                                           " + name + String.valueOf(random.nextInt(100, 999)));
            }
            return false;
        }
    }

    private void updateView(String message) throws IOException {
        Pair<Character, Boolean>[][] pb = getPrintableBoard(viewLivingRoom.getBoard());
        System.out.flush();
        List<List<List<String>>> commonGoalsRepresentation = new ArrayList<>();
        commonGoalsRepresentation.add(getCommonGoalRepresentation(viewLivingRoom.getCommonGoalSet().get(0)));
        commonGoalsRepresentation.add(getCommonGoalRepresentation(viewLivingRoom.getCommonGoalSet().get(1)));
        System.out.println("    0  1  2  3  4  5  6  7  8");
        for(int r = 0; r<9; r++){
            System.out.print(" " + r + " ");
            for(int c = 0; c<9; c++){
                if(pb[r][c] != null){
                    if(pb[r][c].getValue()){
                        if(pb[r][c].getKey() == 'P'){
                            System.out.print((char)27 + purpleCard + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'W') {
                            System.out.print((char)27 + whiteCard + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'C') {
                            System.out.print((char)27 + cyanCard + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'B') {
                            System.out.print((char)27 + blueCard + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'G') {
                            System.out.print((char)27 + greenCard + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'Y') {
                            System.out.print((char)27 + yellowCard + (char)27 + "[0m");
                        }
                    }
                    else {
                        if(pb[r][c].getKey() == 'P'){
                            System.out.print((char)27 + purpleCardNot + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'W') {
                            System.out.print((char)27 + whiteCardNot + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'C') {
                            System.out.print((char)27 + cyanCardNot + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'B') {
                            System.out.print((char)27 + blueCardNot + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'G') {
                            System.out.print((char)27 + greenCardNot + (char)27 + "[0m");
                        }else if (pb[r][c].getKey() == 'Y') {
                            System.out.print((char)27 + yellowCardNot + (char)27 + "[0m");
                        }
                    }
                }
                else System.out.print((char)27 + shelfBackGorund + (char)27 + "[0m");
            }
            System.out.print(" " + r);
            for(int i = 0; i<commonGoalsRepresentation.get(0).get(r).size(); i++){
                System.out.print(commonGoalsRepresentation.get(0).get(r).get(i));
            }
            for(int i = 0; i<commonGoalsRepresentation.get(1).get(r).size(); i++){
                System.out.print(commonGoalsRepresentation.get(1).get(r).get(i));
            }

            System.out.print("\n");
        }
        System.out.println("    0  1  2  3  4  5  6  7  8");

        System.out.println("\n\n\n");
        System.out.println("It's " + viewLivingRoom.getPlayers().get(viewLivingRoom.getTurn()).getName() + " turn");
        System.out.print("    ");
        for(int i = 0; i < me; i++){
            System.out.print("                                       ");
        }
        if (viewLivingRoom.getPlayers().size() != 0) {
            for(ItemCard cardSelected : viewLivingRoom.getPlayers().get(me).getDrawnCards()){
                if(cardSelected.getColor() == 'P'){
                    System.out.print(" "); // space BetweenTiles
                    System.out.print((char)27 + purpleCard + (char)27 + "[0m");
                }else if (cardSelected.getColor() == 'W') {
                    System.out.print(" "); // space BetweenTiles
                    System.out.print((char)27 + whiteCard + (char)27 + "[0m");
                }else if (cardSelected.getColor() == 'C') {
                    System.out.print(" "); // space BetweenTiles
                    System.out.print((char)27 + cyanCard + (char)27 + "[0m");
                }else if (cardSelected.getColor() == 'B') {
                    System.out.print(" "); // space BetweenTiles
                    System.out.print((char)27 + blueCard + (char)27 + "[0m");
                }else if (cardSelected.getColor() == 'G') {
                    System.out.print(" "); // space BetweenTiles
                    System.out.print((char)27 + greenCard + (char)27 + "[0m");
                }else if (cardSelected.getColor() == 'Y') {
                    System.out.print(" "); // space BetweenTiles
                    System.out.print((char)27 + yellowCard + (char)27 + "[0m");
                }
            }
            System.out.println("");
            if(viewLivingRoom.getPlayers().get(me).getDrawnCards().size() != 0){
                System.out.print("    ");
                for(int i = 0; i < me; i++){
                    System.out.print("                                       ");
                }

                System.out.print("  1");
                for(int i = 2; i <= viewLivingRoom.getPlayers().get(me).getDrawnCards().size(); i++){
                    System.out.print("   " + i);
                }
                System.out.println("");
            }
        }




        for(int p = 0; p<viewLivingRoom.getPlayers().size(); p++){
            System.out.print("" + viewLivingRoom.getPlayers().get(p).getName() + "                                 ");
        }
        System.out.println("");

        for(int p = 0; p<viewLivingRoom.getPlayers().size(); p++){
            System.out.print("" + viewLivingRoom.getPlayers().get(p).getScore() + "                                      ");
        }
        System.out.println("");

        for(int p = 0; p<viewLivingRoom.getPlayers().size(); p++){
            for(int c = 0; c < viewLivingRoom.getPlayers().get(0).getMyShelf().getShelf()[0].length; c++){
                System.out.print((char)27 + shelfBase + (char)27 + "[0m"); //brownOfLib
            }
            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
            System.out.print("                  ");
        }
        System.out.println("");

        for (int r = 0; r < viewLivingRoom.getPlayers().get(0).getMyShelf().getShelf().length; r++){
            for(int p = 0; p<viewLivingRoom.getPlayers().size(); p++){
                Optional<ItemCard>[][] playerShelf = viewLivingRoom.getPlayers().get(p).getMyShelf().getShelf();
                for(int c = 0; c < viewLivingRoom.getPlayers().get(0).getMyShelf().getShelf()[0].length; c++){
                    if(playerShelf[r][c].isPresent()){
                        if(playerShelf[r][c].get().getColor() == 'P'){
                            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + purpleCard + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'W') {
                            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + whiteCard + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'C') {
                            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + cyanCard + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'B') {
                            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + blueCard + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'G') {
                            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + greenCard + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'Y') {
                            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + yellowCard+ (char)27 + "[0m");
                        }
                    }
                    else {
                        if(p == me){
                            List<Integer> position = new ArrayList<>();
                            position.add(0, r);
                            position.add(1, c);
                            if(viewLivingRoom.getPlayers().get(me).getPersonalGoal().getSubGoals().containsKey(position)){
                                char color = viewLivingRoom.getPlayers().get(me).getPersonalGoal().getSubGoals().get(position);
                                if(color == 'P'){
                                    System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + purpleCardDark + (char)27 + "[0m");
                                }else if (color == 'W') {
                                    System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + whiteCardDark + (char)27 + "[0m");
                                }else if (color == 'C') {
                                    System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + cyanCardDark + (char)27 + "[0m");
                                }else if (color == 'B') {
                                    System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + blueCardDark + (char)27 + "[0m");
                                }else if (color == 'G') {
                                    System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + greenCardDark + (char)27 + "[0m");
                                }else if (color == 'Y') {
                                    System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + yellowCardDark + (char)27 + "[0m");
                                }
                            }
                            else{
                                System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                                System.out.print((char)27 + shelfBackGorund + (char)27 + "[0m"); //shelfBackground
                            }


                        }
                        else{
                            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + shelfBackGorund + (char)27 + "[0m"); //shelfBackground
                        }

                    }

                }
                System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
                System.out.print("                  ");
            }

            System.out.println("");
        }
        for(int p = 0; p<viewLivingRoom.getPlayers().size(); p++){
            for(int c = 0; c < viewLivingRoom.getPlayers().get(0).getMyShelf().getShelf()[0].length; c++){
                System.out.print((char)27 + shelfBase + (char)27 + "[0m"); //brownOfLib
            }
            System.out.print((char)27 + shelfSeparator + (char)27 + "[0m"); //brownOfLib
            System.out.print("                  ");
        }
        System.out.print("\n" + message);
    }

    public static Pair<Character, Boolean>[][] getPrintableBoard(Map<BoardPosition, Boolean> b){
        Pair<Character, Boolean>[][] mat = new Pair[9][9];
        for(Map.Entry<BoardPosition, Boolean> entry : b.entrySet()){
            mat[entry.getKey().getPosX()][entry.getKey().getPosY()] = new Pair<>(entry.getKey().getCard().getColor(), entry.getValue());
        }
        return mat;
    }

    public boolean parseCommand(String command) {
        if(command.equals("exit") || command.equals("q")){
            controller.disconnectedPlayer(viewLivingRoom,name,false, this);
            return true;
        }

        String commandPrefix = command.split(" ")[0];
        String message;
        switch (commandPrefix){
            case "select":
                message = selectTilesFromBoard(command);
                break;
            case "col":
                message = checkColAndPlaceTiles(Integer.parseInt(command.split(" ")[1]));
                break;
            case "order":
                message = orderPickInsert(command);
                break;
            case "play-as":
                message = playAsPLayer(command);
                break;
            case "quit":
                message = quitAGame();
                break;
            case "reset":
                message = resetBoard();
                break;
            default: message = "newCommand > ";

                //TODO ADD OTHER COMMANDS
        }


        if(command.startsWith("remove")){
            int posX = Integer.parseInt(command.substring(7, 8));
            int posy = Integer.parseInt(command.substring(10, 11));
            viewLivingRoom.removeCard(new BoardPosition(posX, posy));
            try {
                updateView("new Command");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else if(command.startsWith("generate")){
            boolean canProcede = false;
            while(!canProcede){
                try {
                    viewLivingRoom = TestGenerator.generateLivingRoom(Integer.parseInt(command.substring(command.length() -1)));
                    canProcede = true;
                }
                catch (Exception e){
                    System.out.println("Command Not Found");
                    System.out.println("Maybe the command that you are looking for is: generate n");
                    System.out.print("new Command > ");
                    command = new Scanner(System.in).nextLine();
                }
            }
        }

        try {
            updateView(message + "\n");
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    private String resetBoard() {
        try {
            viewLivingRoom = controller.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId());
        } catch (NoMatchingIDException e) {
            return "Couldn't reset the board please retry. \n newCommand > ";
        }

        return "newCommand > ";
    }

    private String quitAGame() {
        controller.leaveGameEvent(name, viewLivingRoom,this );
        startingChoicesView(new Scanner(System.in));
        return "newCommand > ";
    }

    private String selectTilesFromBoard(String command) {

            if(me != viewLivingRoom.getTurn()){
                return "This is not your turn, so please do not play until it's yours.\n" +
                        "Please Wait your turn";
            }
            String[] args = command.split(" ");
            List<BoardPosition> possiblePick = new ArrayList<>();
            for (int i = 1; i<=3; i++){
                try{
                    int finalI = i;
                    Optional<BoardPosition> pickOne = viewLivingRoom.getBoard().keySet().stream().filter(aBoolean -> aBoolean.equals(new BoardPosition(Integer.parseInt(args[finalI * 2 -1]), Integer.parseInt(args[finalI * 2])))).findFirst();
                    pickOne.ifPresent(possiblePick::add);
                    //IF A CARD IS OUT OF BOUND IS AUTOMATICALLY NOT SELECTED
                }
                catch (IndexOutOfBoundsException e){
                    continue;
                }
            }


            if(isElegiblePick(possiblePick)){
                moveFromBoardToShelf(possiblePick);
            }
            else {
                return "You have selected a non possible pick.\n" +
                        "newCommand > ";
            }

            return "newCommand > ";
    }
    private String orderPickInsert(String command) {
        String[] args = command.split(" ");
        List<ItemCard> pickCopy = new ArrayList<>();
        List<Pair<Integer, ItemCard>> order = new ArrayList<>();
        for(int i = 0; i<viewLivingRoom.getPlayers().get(me).getDrawnCards().size(); i++){
            order.add(new Pair<>(Integer.parseInt(args[i+1])-1, viewLivingRoom.getPlayers().get(me).getDrawnCards().get(i)));
        }
        order.sort((x, y) -> { return x.getKey() - y.getKey();});

        for(Pair<Integer, ItemCard> positionsOrdered : order){
            pickCopy.add(positionsOrdered.getKey(), positionsOrdered.getValue());
        }

        try {
            viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(me), pickCopy );
        } catch (ToManyCardsException e) {
            return "You Selected to many tiles. The maximum number is 3\n" +
                    "newCommand > ";
        }
        return "newCommand > ";
    }
    private String checkColAndPlaceTiles(int col) {

        List<BoardPosition> pickToSave = new ArrayList<>();
        for(int i = 0; i<pick.size(); i++){
            pickToSave.add(new BoardPosition(pick.get(i).getPosX(), pick.get(i).getPosY(), viewLivingRoom.getPlayers().get(me).getDrawnCards().get(i)));
        }

        try {
            controller.confirmEndTurn(viewLivingRoom, viewLivingRoom.getPlayers().get(me), pickToSave, col -1);
        } catch (NotEnoughSpacesInCol e) {
            return """
                    The col you selected has too few spaces left
                    Select another Col
                    newCommand >\s""";
        }
        pick.clear();

        viewLivingRoom.undoDraft(viewLivingRoom.getPlayers().get(me));
        return "\n newCommand > ";
    }
    private void moveFromBoardToShelf(List<BoardPosition> pick) {
        List<ItemCard> pickList = new ArrayList<>();
        this.pick = pick;
        for (BoardPosition position : pick){
            viewLivingRoom.removeCard(position);
            pickList.add(position.getCard());
        }

        try {
            viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(me), pickList);
        } catch (ToManyCardsException e) {
            return;
        }
    }
    public LivingRoom initializeGame(int playerNum){
        return TestGenerator.generateLivingRoom(playerNum);
    }
    public boolean isElegiblePick(List<BoardPosition> possiblePick){
        //TODO
        return true;
    }
    public String playAsPLayer(String command){
        int newTurn = Integer.parseInt(command.split(" ")[1]);
        if(newTurn > viewLivingRoom.getPlayers().size()){
            return "The player you selected is a ghost\n" +
                    "WOOOOOOOOOOOOOOOOOOOOSH\n" +
                    "newCommand > ";
        }
        if(newTurn == 0){
            return "Yes you are the big boss.";
        }
        me = newTurn - 1;
        viewLivingRoom.setTurn(newTurn - 1);
        return "You are now playing as player" + me +
                "newCommand > ";
    }
    private List<List<String>> getCommonGoalRepresentation(CommonGoalCard c){

        String commonGoalName = c.getName();
        Gson converter = new Gson();

        JsonArray commonGoalArray = converter.fromJson(JSONInterface.getJsonStringFrom("src/main/resources/JSON/CommonGoalCLI.json"), JsonObject.class).getAsJsonArray(commonGoalName);

        List<List<String>> finalRep = new ArrayList<>();
        int rr = 0;
        for(JsonElement row : commonGoalArray){
            List<String> semiFinalRep = new ArrayList<>();
            JsonArray rowi = row.getAsJsonArray();
            for(int i = 0; i<rowi.size(); i++){
                switch (rowi.get(i).getAsString()) {
                    case "whiteSpace" ->                semiFinalRep.add(i, (char) 27 + whiteBlock + (char) 27 + "[0m");
                    case "equalBlock" ->                semiFinalRep.add(i, (char) 27 + equalBlock + (char) 27 + "[0m");
                    case "emptyBlock" ->                semiFinalRep.add(i, (char) 27 + emptyBlock + (char) 27 + "[0m");
                    case "dfrntBlock" ->                semiFinalRep.add(i, (char) 27 + dfrntBlock + (char) 27 + "[0m");
                    case "GoalName" ->                  semiFinalRep.add(i, (char) 27 + whiteBackground + commonGoalName + (char) 27 + "[0m");
                    case "GoalNameWhite" ->             semiFinalRep.add(i, (char) 27 + trasparentText + commonGoalName + (char) 27 + "[0m");
                    case "vLink" ->                     semiFinalRep.add(i, (char) 27 + vLink + (char) 27 + "[0m");
                    case "hLink" ->                     semiFinalRep.add(i, (char) 27 + hLink + (char) 27 + "[0m");
                    case "                      " ->    semiFinalRep.add(i, rowi.get(i).getAsString());
                    default ->                          semiFinalRep.add(i, (char) 27 + whiteBackground + rowi.get(i).getAsString() + (char) 27 + "[0m");
                }
            }
            finalRep.add(rr, semiFinalRep);
            rr++;
        }
        finalRep.add(rr, new ArrayList<>());

        return finalRep;
    }
    @Override
    public void notifyListener() {
        try {
            viewLivingRoom = controller.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId());
            updateView("LivingRoom UpToDate\n newCommand > ");
        } catch (NoMatchingIDException | IOException e) {
            return;
        }
    }
}
