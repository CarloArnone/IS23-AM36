package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Common.Exceptions.ToManyCardsException;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.Utils.Printer;
import it.polimi.ingsw.Server.Model.*;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.getAllStackTraces;
import static java.lang.Thread.sleep;

public class CLI extends IUI {

    Scanner sc ;


    public CLI() {
        sc = new Scanner(System.in);
    }

    @Override
    public void start(Stage stage) throws Exception {
        startIUI();
    }

    @Override
    public void startIUI() {
        new Thread(() -> {

            loginTry(false);

        }).start();

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
            if(error.equals("invalidId")) {

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
        System.out.println("not Possible Pick");
    }

    /**
     *
     */
    @Override
    public void turnPassed() {
        setPick(new ArrayList<>());
        System.out.println("TurnCompleted");
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
            Scanner scannerCommand = sc;
            boolean exitIn = false;
            while(!exitIn){
                String command = scannerCommand.nextLine();
                exitIn = parseCommand(command);
            }
        }).start();
    }

    public boolean parseCommand(String command) {
        if (command.equals("exit") || command.equals("q")) {
            getVirtualViewClient().disconnectedPlayer(getViewLivingRoom(), getName(), false, getVirtualViewClient());
            return true;
        }

        String commandPrefix = command.split(" ")[0];
        switch (commandPrefix) {
            case "select" -> selectTilesFromBoard(command);
            case "col" -> checkColAndPlaceTiles(Integer.parseInt(command.split(" ")[1]));
            case "order" -> orderPickInsert(command);
            case "quit" -> quitAGame();
            case "reset" -> resetBoard();

            //TODO ADD OTHER COMMANDS
        };

        updateCLI();
        return false;
    }

    private void orderPickInsert(String command) {
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
            updateCLI();
            System.out.println("There were to many cards");
        }

    }


    private void selectTilesFromBoard(String command) {

        if(getMyTurn() != getViewLivingRoom().getTurn()){
            updateCLI();
            System.out.println("This is not your turn please wait");
            return;
        }
        String[] args = command.split(" ");
        List<BoardPosition> possiblePick = new ArrayList<>();
        for (int i = 1; i<=(args.length -1)/2; i++){
            if(i == 4){
                throw new IndexOutOfBoundsException();
            }
            try{
                int finalI = i;
                Optional<BoardPosition> pickOne = getViewLivingRoom().getBoard().keySet().stream().filter(aBoolean -> aBoolean.equals(new BoardPosition(Integer.parseInt(args[finalI * 2 -1]), Integer.parseInt(args[finalI * 2])))).findFirst();
                pickOne.ifPresent(possiblePick::add);
                //IF A CARD IS OUT OF BOUND IS AUTOMATICALLY NOT SELECTED
            }
            catch (IndexOutOfBoundsException e){
                updateCLI();
                System.out.println("To many cards");
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

        System.out.println("\n\n                                               -- To Update: u , To Choose Digit a Name --");

        String command = sc.nextLine().split(" ")[0];
        if(activeLivingRooms.contains(command)){
            getVirtualViewClient().joinGameEvent(command, getName());
        }
        else if(command.equals("u")){
            getVirtualViewClient().getActiveLivingRooms(10, section);
        }
    }


    /**
     *
     */
    @Override
    public void gameStarted() {
        startParsingCommands();
        //TODO RESOLVE PULLING OUT OF A GAME STOP THREAD AND FIRST ENTER = TRUE
    }

    /**
     *
     */
    @Override
    public void gameEnded() {
        printEndGameScreen();
    }

    private void printEndGameScreen() {
        System.out.println("Gioco Finito");
    }

    /**
     *
     */
    @Override
    public void possiblePick(List<BoardPosition> pick) {
        //Some changes then update
        try {
            moveFromBoardToShelf(pick);
            updateCLI();
        } catch (ToManyCardsException e) {
            updateCLI();
            System.out.println("There where to many cards");
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
            updateCLI();
        }
    }

    private void updateCLI() {
        printBoard();
        getVirtualViewClient().isGamesStarted(getViewLivingRoom());
        //TODO ADD THE GAME SCREEN PRINTING
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
    public void joinedGame(Player playerFromJson, String livingRoomId) {
        getVirtualViewClient().retrieveOldGameEvent(livingRoomId);
        setMySelf(playerFromJson);
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

        for(int r = 0; r < 9; r++){
            for(int complete = 0; complete < 3; complete++){
                if(complete == 1){
                    for(int c = 0; c< 9; c++){
                        if(printableBoard[r][c] == null){
                            printTileBoard('F', false);
                        }
                        else printTile(printableBoard[r][c].getKey(), printableBoard[r][c].getValue());
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

    private void printTile(Character key, Boolean value) {
            if(value){
                if(key == 'P'){
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
