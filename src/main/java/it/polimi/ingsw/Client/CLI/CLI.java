package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Common.Exceptions.ToManyCardsException;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.Utils.Printer;
import it.polimi.ingsw.Server.Model.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

public class CLI extends IUI {

    Scanner sc ;


    public CLI() {
        sc = new Scanner(System.in);
        setPick(new ArrayList<>());
    }

    /**
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
    }




    public void startCLI(){

        new Thread(() -> {
            System.out.println((char)27 + "[38;2;255;195;0;48;2;55;6;23m ");
            System.out.println(
                    """
                             .----------------.  .----------------.   .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.\s
                            | .--------------. || .--------------. | | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
                            | | ____    ____ | || |  ____  ____  | | | |    _______   | || |  ____  ____  | || |  _________   | || |   _____      | || |  _________   | || |     _____    | || |  _________   | |
                            | ||_   \\  /   _|| || | |_  _||_  _| | | | |   /  ___  |  | || | |_   ||   _| | || | |_   ___  |  | || |  |_   _|     | || | |_   ___  |  | || |    |_   _|   | || | |_   ___  |  | |
                            | |  |   \\/   |  | || |   \\ \\  / /   | | | |  |  (__ \\_|  | || |   | |__| |   | || |   | |_  \\_|  | || |    | |       | || |   | |_  \\_|  | || |      | |     | || |   | |_  \\_|  | |
                            | |  | |\\  /| |  | || |    \\ \\/ /    | | | |   '.___`-.   | || |   |  __  |   | || |   |  _|  _   | || |    | |   _   | || |   |  _|      | || |      | |     | || |   |  _|  _   | |
                            | | _| |_\\/_| |_ | || |    _|  |_    | | | |  |`\\____) |  | || |  _| |  | |_  | || |  _| |___/ |  | || |   _| |__/ |  | || |  _| |_       | || |     _| |_    | || |  _| |___/ |  | |
                            | ||_____||_____|| || |   |______|   | | | |  |_______.'  | || | |____||____| | || | |_________|  | || |  |________|  | || | |_____|      | || |    |_____|   | || | |_________|  | |
                            | |              | || |              | | | |              | || |              | || |              | || |              | || |              | || |              | || |              | |
                            | '--------------' || '--------------' | | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
                             '----------------'  '----------------'   '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'\s
                            """);
            System.out.println((char)27 + "[0m");
            loginTry(false);

        }).start();

    }

    /**
     *
     */
    @Override
    public void startUI(ICommunication virtualView) {
        initalizeVirtualView(virtualView);
        startCLI();
    }

    /**
     * @param s
     * @param b
     */
    @Override
    public void otherPlayerDisconnected(String s, boolean b) {
        getVirtualViewClient().retrieveOldGameEvent(getViewLivingRoom().getLivingRoomId());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(b){
            updateCLI("Player " + s + " left the game on purpose");
        }
        else{
            updateCLI("Player " + s + " is momentainly disconnected due to some crushes");
        }
    }

    /**
     *
     */
    @Override
    public void retryPlacement() {

    }

    /**
     *
     */
    @Override
    public void retryLogin() {
        loginTry(true);
    }

    /**
     *
     */
    @Override
    public void livingRoomNotFound(String type) {
        if(type.equals("fetchOldGame")){
            createOrJoinGameChoice();
        }
    }

    /**
     *
     */
    @Override
    public void retryCreateGame(String error, String livId) {
        printCreateGameScreen(true, error, livId);
    }

    private void printCreateGameScreen(boolean retry, String error, String livId) {
        if(retry){
            if(error.equals("InvalidGameID")) {

                Random random = new Random();
                System.out.println("\n\n\n\n\n\n\n\n");
                System.out.println("                                    ! Game ID Already Taken !");
                System.out.println("                              What about :  " + livId + random.nextInt(1000, 9999));
                for (int i = 0; i < 10; i++) {
                    System.out.println("                                            " + livId + random.nextInt(1000, 9999));
                }
            }
            else {
                System.out.print("\n");
                System.out.println("\n\n\n\n\n\n\n\n");
                System.out.println("                                    ! Selected players num is incorrect !");
                System.out.print("                                      Please enter a new one");
                System.out.print("\n");
            }
        }

        List<String> parameters = new ArrayList<>();
        System.out.println("                               Game_ID must have at least 5 characters");
        System.out.println("                                Players Num should be one of {2, 3, 4}");
        printNLines(3);
        System.out.print("                                          Game ID:    ");
        parameters.add(0, sc.nextLine().split(" ")[0]);
        System.out.print("\n");
        System.out.print("                                      Players Num:    ");
        parameters.add(1, sc.nextLine().split(" ")[0]);
        System.out.print("\n");

        getVirtualViewClient().createGameEvent(parameters.get(0), getMySelf(), Integer.parseInt(parameters.get(1)));

    }


    /**
     *
     */
    @Override
    public void notDisconnected() {
        System.out.println("Player Not disconnected");

    }

    /**
     *
     */
    @Override
    public void gameNotStarted() {
        //NOTHING
        //getVirtualViewClient().isGamesStarted(getViewLivingRoom());
    }

    /**
     *
     */
    @Override
    public void gameNotEnded() {
        System.out.println("gameNotEnded");
    }

    /**
     *
     */
    @Override
    public void retryPick() {
        updateCLI("Not possible Pick");
    }

    /**
     *
     */
    @Override
    public void turnPassed() {
        setPick(new ArrayList<>());
        getVirtualViewClient().retrieveOldGameEvent(getViewLivingRoom().getLivingRoomId());
    }

    /**
     *
     */
    @Override
    public void loginSuccessful() {
        setMySelf(new Player(getName()));
        getVirtualViewClient().previousGamesRequestEvent(getName());
    }

    private void searchForOldGame(LivingRoom liv) {
        System.out.println("\n\n\n\n\n\n\n");
        System.out.println("                                            There is a game you were playing : " + liv.getLivingRoomId());
        System.out.print("                                                            Do you want to rejoin ?  y/* ...  ");
        if(sc.nextLine().split(" ")[0].equals("y")){

            setViewLivingRoom(liv);
            for (int i = 0; i < liv.getPlayers().size(); i++) {
                if(liv.getPlayers().get(i).getName().equals(getName())){
                    setMyTurn(i);
                }
            }
            getVirtualViewClient().joinGameEvent(liv.getLivingRoomId(), getName());
            return;
        }
        else getVirtualViewClient().leaveGameEvent(getName(), liv, getVirtualViewClient() );
    }

    /**
     *
     */
    @Override
    public void disconnected() {
        createOrJoinGameChoice();
    }

    private void createOrJoinGameChoice() {
        //sc = new Scanner(System.in);
        System.out.flush();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("                                            Choose:\n");
        System.out.println("                                 Create Game      Join Game");
        System.out.println("                                      c        /      j      ");
        System.out.print("                                               > ");
        String commandCreateOrJoin = sc.nextLine().split(" ")[0];
        if (commandCreateOrJoin.equals("c")) {
            printCreateGameScreen(false, "", "");
        } else if (commandCreateOrJoin.equals("j")) {
            int groupID = 1;
            getVirtualViewClient().getActiveLivingRooms(10, groupID);
        }
    }

    private void startParsingCommands() {
        new Thread(() ->{
            boolean exitIn = false;
            while(!exitIn){
                String command = sc.nextLine();
                exitIn = parseCommand(command);
            }
        }).start();
    }

    public boolean parseCommand(String command) {
        if (command.equals("exit") || command.equals("q")) {
            getVirtualViewClient().disconnectedPlayer(getViewLivingRoom(), getName(), true, getVirtualViewClient());
            return true;
        }

        if(getViewLivingRoom() == null){
            return true;
        }

        String commandPrefix = command.split(" ")[0];
        switch (commandPrefix) {
            case "select" -> selectTiles(command);
            case "col" -> checkColAndPlaceTiles(Integer.parseInt(command.split(" ")[1]));
            case "order" -> {
                orderPick(command);
                updateCLI("");
            }
            case "quit" -> quitAGame();
            case "reset" -> resetBoard();

            //TODO ADD OTHER COMMANDS
        };

        //updateCLI("");
        return false;
    }

    private void orderPick(String command) {
        String[] args = command.split(" ");
        List<ItemCard> pickCopy = new ArrayList<>();
        List<Pair<Integer, ItemCard>> order = new ArrayList<>();
        for (int i = 0; i < getViewLivingRoom().getPlayers().get(getMyTurn()).getDrawnCards().size(); i++) {
            order.add(new Pair<>(Integer.parseInt(args[i + 1]) - 1, getViewLivingRoom().getPlayers().get(getMyTurn()).getDrawnCards().get(i)));
        }
        order.sort((x, y) -> {
            return x.getKey() - y.getKey();
        });

        try {
            orderPickInsert(order);
        } catch (ToManyCardsException e) {
            updateCLI("There where to many Tiles in you pick - the maximum is 3 tiles");
        }

    }


    private void selectTiles(String command) {

        if(getMyTurn() != getViewLivingRoom().getTurn()){
            updateCLI("This is not your turn please wait");
            return;
        }
        String[] args = command.split(" ");
        List<BoardPosition> possiblePick = new ArrayList<>();
        for (int i = 1; i<=(args.length -1)/2; i++){
            try{
                if(i == 4){
                    throw new IndexOutOfBoundsException();
                }
                int finalI = i;
                Optional<BoardPosition> pickOne = getViewLivingRoom().getBoard().keySet().stream().filter(aBoolean -> aBoolean.equals(new BoardPosition(Integer.parseInt(args[finalI * 2 -1]), Integer.parseInt(args[finalI * 2])))).findFirst();
                pickOne.ifPresent(possiblePick::add);
                //IF A CARD IS OUT OF BOUND IS AUTOMATICALLY NOT SELECTED
            }
            catch (IndexOutOfBoundsException e){
                updateCLI("To many Tiles in your pick - the maximum is 3 tiles");
            }
        }

        selectTilesFromBoard(possiblePick);

    }


    /**
     * @param s
     */
    @Override
    public void livingRoomsList(String s, int section) {
        List<String> activeLivingRooms = Arrays.stream(s.split("-")).toList();
        printNLines(13);
        System.out.println("                                                 -- Active Games List --");
        for (int i = 0; i < activeLivingRooms.size(); i++) {
            System.out.println("                                                    " + activeLivingRooms.get(i));
        }

        System.out.println("\n\n                                               -- To Update: u , To go Back b, To Choose Digit a Name --");

        String command = sc.nextLine().split(" ")[0];
        if(activeLivingRooms.contains(command)){
            getVirtualViewClient().joinGameEvent(command, getName());
        }
        else if(command.equals("u")){
            getVirtualViewClient().getActiveLivingRooms(10, section);
        }
        else if(command.equals("b")){
            createOrJoinGameChoice();
        }
        else{
            livingRoomsList(s, section);
        }
    }


    /**
     *
     */
    @Override
    public void gameStarted() {
        getVirtualViewClient().retrieveOldGameEvent(getViewLivingRoom().getLivingRoomId());
        startParsingCommands();
    }

    /**
     *
     */
    @Override
    public void gameEnded(String message) {
        printEndGameScreen(message);
    }

    private void printEndGameScreen(String status) {
        switch (status){
            case "winner" -> {
                System.out.println("You win");
                stopParsingCommands();
            }
            case "lonelyPlayer" -> {
                System.out.println("You are the last in your game - disconnecting");
                stopParsingCommands();
                createOrJoinGameChoice();
            }
            default -> {
                System.out.println("Defeat");
                stopParsingCommands();
            }
        }
    }

    private void stopParsingCommands() {
        setViewLivingRoom(null);
    }


    /**
     *
     */
    @Override
    public void possiblePick(List<BoardPosition> pick) {
        //Some changes then update
        try {
            moveFromBoardToShelf(pick);
            updateCLI("");
        } catch (ToManyCardsException e) {
            updateCLI("There where to many Tiles in you pick - the maximum is 3 tiles");
        }
    }

    /**
     * @param livingRoomFromJsonString
     */
    @Override
    public void livingRoomFound(LivingRoom livingRoomFromJsonString, String command) {
        if(command.equals("fetchOldGame")){
            searchForOldGame(livingRoomFromJsonString);
        }
        else{
            updateLivingRoom(livingRoomFromJsonString);
            updateOldValues(livingRoomFromJsonString);
            updateCLI("");
        }
    }

    private void updateOldValues(LivingRoom livingRoomFromJsonString) {
        int myNewTurn = livingRoomFromJsonString.getPlayers().indexOf(getMySelf());
        setMyTurn(myNewTurn);
        setMySelf(livingRoomFromJsonString.getPlayers().get(myNewTurn));
    }

    private void updateCLI(String message) {
        printBoard();
        printInfo();
        printPickIfPresent();
        printShelves();
        if(!message.equals("")){
            System.out.println(message);
        }
        System.out.print("Command: ");
        //TODO ADD THE GAME SCREEN PRINTING
    }

    private void printPickIfPresent() {
        printNLines(3);
            if(! getPick().contains(new BoardPosition(9, 9))){
                int pickSize = getPick().size();
                printNSpaces(getMyTurn() * 39);
                printNSpaces(12 - pickSize*2);
                for(ItemCard tile : getViewLivingRoom().getPlayers().get(getMyTurn()).getDrawnCards()){
                    switch (tile.getColor()){
                        case 'P' -> System.out.print((char)27 + Printer.purpleCard.escape() + (char)27 + "[0m ");
                        case 'W' -> System.out.print((char)27 + Printer.whiteCard.escape() + (char)27 + "[0m ");
                        case 'B' -> System.out.print((char)27 + Printer.blueCard.escape() + (char)27 + "[0m ");
                        case 'Y' -> System.out.print((char)27 + Printer.yellowCard.escape() + (char)27 + "[0m ");
                        case 'G' -> System.out.print((char)27 + Printer.greenCard.escape() + (char)27 + "[0m ");
                        case 'C' -> System.out.print((char)27 + Printer.cyanCard.escape() + (char)27 + "[0m ");
                    }
                }
                printNLines(1);
                printNSpaces(getMyTurn() * 39);
                printNSpaces(12 - pickSize*2);
                if(pickSize != 0){
                    System.out.print(" 1  ");
                }
                for (int i = 2; i <= getPick().size(); i++) {
                    System.out.print(" " + i + "  ");
                }
                System.out.println("");
            }


    }

    private void printInfo() {
        printNLines(3);
        if(getViewLivingRoom().getTurn() == getMyTurn()){
            System.out.println("It's My Turn");
        }
        else System.out.println("It's " + getViewLivingRoom().getPlayers().get(getViewLivingRoom().getTurn()).getName() + " turn.");

    }

    private void printShelves() {
        for(Player player : getViewLivingRoom().getPlayers()){
            String name = player.getName();
            System.out.print(name);
            printNSpaces(21 - name.length() - String.valueOf(player.getScore()).length());
            System.out.print(player.getScore());
            printNSpaces(18);
        }
        printNLines(1);



        for(int p = 0; p<getViewLivingRoom().getPlayers().size(); p++){
            for(int c = 0; c < getViewLivingRoom().getPlayers().get(p).getMyShelf().getShelf()[0].length; c++){
                System.out.print((char)27 + Printer.shelfBase.escape() + (char)27 + "[0m"); //brownOfLib
            }
            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
            printNSpaces(18);
        }
        printNLines(1);


        int height = getViewLivingRoom().getPlayers().get(0).getMyShelf().getShelf().length;

            for (int r = 0; r < height; r++){
                for(Player player: getViewLivingRoom().getPlayers()){
                    Optional<ItemCard>[][] playerShelf = player.getMyShelf().getShelf();
                for (int c = 0; c < playerShelf[0].length; c++){
                    if(playerShelf[r][c].isPresent()){
                        if(playerShelf[r][c].get().getColor() == 'P'){
                            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + Printer.purpleCardShelf.escape() + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'W') {
                            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + Printer.whiteCardShelf.escape() + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'C') {
                            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + Printer.cyanCardShelf.escape() + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'B') {
                            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + Printer.blueCardShelf.escape() + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'G') {
                            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + Printer.greenCardShelf.escape() + (char)27 + "[0m");
                        }else if (playerShelf[r][c].get().getColor() == 'Y') {
                            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + Printer.yellowCardShelf.escape() + (char)27 + "[0m");
                        }
                    }
                    else {
                        if(player.equals(getMySelf())){
                            List<Integer> position = new ArrayList<>();
                            position.add(0, r);
                            position.add(1, c);
                            if(player.getPersonalGoal().getSubGoals().containsKey(position)){
                                char color = player.getPersonalGoal().getSubGoals().get(position);
                                if(color == 'P'){
                                    System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + Printer.purpleCardDark.escape() + (char)27 + "[0m");
                                }else if (color == 'W') {
                                    System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + Printer.whiteCardDark.escape() + (char)27 + "[0m");
                                }else if (color == 'C') {
                                    System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + Printer.cyanCardDark.escape() + (char)27 + "[0m");
                                }else if (color == 'B') {
                                    System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + Printer.blueCardDark.escape() + (char)27 + "[0m");
                                }else if (color == 'G') {
                                    System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + Printer.greenCardDark.escape() + (char)27 + "[0m");
                                }else if (color == 'Y') {
                                    System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                                    System.out.print((char)27 + Printer.yellowCardDark.escape() + (char)27 + "[0m");
                                }
                            }
                            else{
                                System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                                System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m"); //shelfBackground
                            }
                        }
                        else{
                            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
                            System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m"); //shelfBackground
                        }
                    }
                }
                System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m");
                printNSpaces(18);
            }
            printNLines(1);
        }

        for(int p = 0; p<getViewLivingRoom().getPlayers().size(); p++){
            for(int c = 0; c < getViewLivingRoom().getPlayers().get(p).getMyShelf().getShelf()[0].length; c++){
                System.out.print((char)27 + Printer.shelfBase.escape() + (char)27 + "[0m"); //brownOfLib
            }
            System.out.print((char)27 + Printer.shelfSeparator.escape() + (char)27 + "[0m"); //brownOfLib
            printNSpaces(18);
        }
        printNLines(1);


    }

    private void printExpectedPrompt(int playersNum) {
        System.out.flush();
        System.out.print("Waiting For Players To Join: " + getViewLivingRoom().getPlayers().size() + "/" + playersNum);
        getVirtualViewClient().isGamesStarted(getViewLivingRoom());

    }

    /**
     * @param playerFromJson
     */
    @Override
    public void joinedGame(Player playerFromJson, LivingRoom livingRoom) {
        //getVirtualViewClient().retrieveOldGameEvent(livingRoomId);
        updateLivingRoom(livingRoom);
        setMySelf(playerFromJson);
        //getVirtualViewClient().isGamesStarted(getViewLivingRoom());
        System.out.println("Player Found");
    }

    /**
     * @param arg
     */
    @Override
    public void gameNotJoined(String arg) {
        System.out.println(arg);
        getVirtualViewClient().getActiveLivingRooms(10, 1);
    }

    /**
     *
     */
    @Override
    public void serverDiconnected() {
        System.out.println("Server disconnected, 2 seconds and the game will crush");
        System.exit(0);
    }

    public void loginTry(boolean retry){
        printLoginScreen(retry);
        getVirtualViewClient().logInTryEvent(getName(), getVirtualViewClient());
    }

    private void printLoginScreen(boolean retry) {
        if(retry){
            Random random = new Random();
            centerHorizontaly("UserName Already Taken.");
            printNLines(2);
            centerHorizontaly("What about :     " + getName() + String.valueOf(random.nextInt(100, 999)));
            printNLines(1);
            for (int i = 0; i < 5; i++) {
                centerHorizontaly(getName() + String.valueOf(random.nextInt(100, 999)));
                printNLines(1);
            }

        }

        printNLines(7);
        centerHorizontaly("UserName: ");
        setName(sc.nextLine());
    }


    public void printNSpaces(int n){
        for (int i = 0; i < n; i++) {
            System.out.print(" ");
        }
    }

    public void printNLines(int n){
        for (int i = 0; i < n; i++) {
            System.out.print("\n");
        }
    }

    public void updateEnvVar(){
            new  Thread(() -> {
                try {
                    Runtime.getRuntime().exec("bash -c \"export COLUMNS LINES\"");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();

    }


    public void centerContent(String content){

        //updateEnvVar();

        int ROWS = Integer.parseInt(System.getenv("LINES"));
        System.out.println(ROWS);
        int contentLength = content.length();
        List<String> toPrint = content.lines().toList();

        int contentHeight = toPrint.size();

        clearTerminal();
        printNLines(ROWS/2 - contentHeight/2);

        centerHorizontaly(content);

        printNLines(ROWS/2 - contentHeight/2);

    }

    public void centerHorizontaly(String content){
        //updateEnvVar();
        int COLS = Integer.parseInt(System.getenv("COLUMNS"));
        System.out.println(COLS);
        List<String> toPrint = content.lines().toList();

        for(String line : toPrint){
            printNSpaces(COLS/2 - line.length());
            System.out.print(line);
        }

    }

    private void clearTerminal() {
        try {
            Runtime.getRuntime().exec("clear");

        } catch (IOException e) {
            System.out.println("notclearED");
        }
    }

    public void printBoard(){
        Pair<Character, Boolean>[][] printableBoard = getPrintableBoard(getViewLivingRoom().getBoard());

        printNLines(1);
        for(int r = 0; r < 9; r++){
            for(int complete = 0; complete < 3; complete++){
                if(complete == 1){
                    for(int c = 0; c< 9; c++){
                        if(printableBoard[r][c] == null){
                            printTileBoard('F', false);
                        }
                        else printTile(printableBoard[r][c].getKey(), printableBoard[r][c].getValue(), r, c);
                    }
                }
                else {
                    for(int c = 0; c< 9; c++){
                        if(printableBoard[r][c] == null){
                            printTileBoard('F', false);
                        }
                        else printTileBoard(printableBoard[r][c].getKey(), printableBoard[r][c].getValue());
                    }
                }
                System.out.println("");
            }
        }



    }

    private void printTileBoard(Character key, Boolean value) {
                if(key.equals('P')){
                    System.out.print((char)27 + Printer.purpleCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.purpleCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.purpleCard.escape() + (char)27 + "[0m");
                }else if (key == 'W') {
                    System.out.print((char)27 + Printer.whiteCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.whiteCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.whiteCard.escape() + (char)27 + "[0m");
                }else if (key == 'C') {
                    System.out.print((char)27 + Printer.cyanCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.cyanCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.cyanCard.escape() + (char)27 + "[0m");
                }else if (key == 'B') {
                    System.out.print((char)27 + Printer.blueCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.blueCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.blueCard.escape() + (char)27 + "[0m");
                }else if (key == 'G') {
                    System.out.print((char)27 + Printer.greenCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.greenCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.greenCard.escape() + (char)27 + "[0m");
                }else if (key == 'Y') {
                    System.out.print((char)27 + Printer.yellowCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.yellowCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.yellowCard.escape() + (char)27 + "[0m");
                }
                else if (key == 'F'){
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                }
    }

    private void printTile(Character key, Boolean value, int r, int c) {
            if(value){
                if(key == 'P'){
                    System.out.print((char)27 + Printer.purpleCard.escape().replace("   ", "  (") + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.purpleCard.escape().replace("   ", r + "," + c) + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.purpleCard.escape().replace("   ", ")  ") + (char)27 + "[0m");
                }else if (key == 'W') {
                    System.out.print((char)27 + Printer.whiteCard.escape().replace("   ", "  (") + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.whiteCard.escape().replace("   ", r + "," + c) + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.whiteCard.escape().replace("   ", ")  ") + (char)27 + "[0m");
                }else if (key == 'C') {
                    System.out.print((char)27 + Printer.cyanCard.escape().replace("   ", "  (") + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.cyanCard.escape().replace("   ", r + "," + c) + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.cyanCard.escape().replace("   ", ")  ") + (char)27 + "[0m");
                }else if (key == 'B') {
                    System.out.print((char)27 + Printer.blueCard.escape().replace("   ", "  (") + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.blueCard.escape().replace("   ", r + "," + c) + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.blueCard.escape().replace("   ", ")  ") + (char)27 + "[0m");
                }else if (key == 'G') {
                    System.out.print((char)27 + Printer.greenCard.escape().replace("   ", "  (") + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.greenCard.escape().replace("   ", r + "," + c) + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.greenCard.escape().replace("   ", ")  ") + (char)27 + "[0m");
                }else if (key == 'Y') {
                    System.out.print((char)27 + Printer.yellowCard.escape().replace("   ", "  (") + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.yellowCard.escape().replace("   ", r + "," + c) + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.yellowCard.escape().replace("   ", ")  ") + (char)27 + "[0m");
                }
                else if (key == 'F'){
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                }
            }
            else {
                if(key == 'P'){
                    System.out.print((char)27 + Printer.purpleCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.purpleCardNot.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.purpleCard.escape() + (char)27 + "[0m");
                }else if (key == 'W') {
                    System.out.print((char)27 + Printer.whiteCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.whiteCardNot.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.whiteCard.escape() + (char)27 + "[0m");
                }else if (key == 'C') {
                    System.out.print((char)27 + Printer.cyanCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.cyanCardNot.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.cyanCard.escape() + (char)27 + "[0m");
                }else if (key == 'B') {
                    System.out.print((char)27 + Printer.blueCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.blueCardNot.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.blueCard.escape() + (char)27 + "[0m");
                }else if (key == 'G') {
                    System.out.print((char)27 + Printer.greenCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.greenCardNot.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.greenCard.escape() + (char)27 + "[0m");

                }else if (key == 'Y') {
                    System.out.print((char)27 + Printer.yellowCard.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.yellowCardNot.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.yellowCard.escape() + (char)27 + "[0m");

                }
                else if (key == 'F'){
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                    System.out.print((char)27 + Printer.shelfBackGorund.escape() + (char)27 + "[0m");
                }
            }
    }


    public static Pair<Character, Boolean>[][] getPrintableBoard(Map<BoardPosition, Boolean> b){
        Pair<Character, Boolean>[][] mat = new Pair[9][9];
        for(Map.Entry<BoardPosition, Boolean> entry : b.entrySet()){
            mat[entry.getKey().getPosX()][entry.getKey().getPosY()] = new Pair<>(entry.getKey().getCard().getColor(), entry.getValue());
        }
        return mat;
    }

    private List<List<String>> getCommonGoalRepresentation(CommonGoalCard c){

        String commonGoalName = c.getName();
        Gson converter = new Gson();

        JsonArray commonGoalArray = converter.fromJson(JSONInterface.getJsonStringFrom(JSONInterface.findCorrectPathFromResources("/JSON/CommonGoalCLI.json")), JsonObject.class).getAsJsonArray(commonGoalName);

        List<List<String>> finalRep = new ArrayList<>();
        int rr = 0;
        for(JsonElement row : commonGoalArray){
            List<String> semiFinalRep = new ArrayList<>();
            JsonArray rowi = row.getAsJsonArray();
            for(int i = 0; i<rowi.size(); i++){
                switch (rowi.get(i).getAsString()) {
                    case "whiteSpace" ->                semiFinalRep.add(i, (char) 27 + Printer.whiteBlock.escape() + (char) 27 + "[0m");
                    case "equalBlock" ->                semiFinalRep.add(i, (char) 27 + Printer.equalBlock.escape() + (char) 27 + "[0m");
                    case "emptyBlock" ->                semiFinalRep.add(i, (char) 27 + Printer.emptyBlock.escape() + (char) 27 + "[0m");
                    case "dfrntBlock" ->                semiFinalRep.add(i, (char) 27 + Printer.dfrntBlock.escape() + (char) 27 + "[0m");
                    case "GoalName" ->                  semiFinalRep.add(i, (char) 27 + Printer.whiteBackground.escape() + commonGoalName + (char) 27 + "[0m");
                    case "GoalNameWhite" ->             semiFinalRep.add(i, (char) 27 + Printer.trasparentText.escape() + commonGoalName + (char) 27 + "[0m");
                    case "vLink" ->                     semiFinalRep.add(i, (char) 27 + Printer.vLink.escape() + (char) 27 + "[0m");
                    case "hLink" ->                     semiFinalRep.add(i, (char) 27 + Printer.hLink.escape() + (char) 27 + "[0m");
                    case "                      " ->    semiFinalRep.add(i, rowi.get(i).getAsString());
                    default ->                          semiFinalRep.add(i, (char) 27 + Printer.whiteBackground.escape() + rowi.get(i).getAsString() + (char) 27 + "[0m");
                }
            }
            finalRep.add(rr, semiFinalRep);
            rr++;
        }
        finalRep.add(rr, new ArrayList<>());

        return finalRep;
    }


}
