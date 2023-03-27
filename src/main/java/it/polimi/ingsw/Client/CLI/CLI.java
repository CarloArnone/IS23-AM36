package it.polimi.ingsw.Client.CLI;


import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.ToManyCardsException;
import it.polimi.ingsw.Common.Utils.TestGenerator;
import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class CLI {

    private LivingRoom viewLivingRoom;
    private List<BoardPosition> pick;
    private int me; // It means MyTURN

    private eventObserver controller;

    private final String blueCard = "[48;2;0;119;182m   ";
    private final String purpleCard = "[48;2;255;0;110m   ";
    private final String whiteCard = "[48;2;255;236;209m   ";
    private final String greenCard = "[48;2;106;153;69m   ";
    private final String cyanCard = "[48;2;0;180;216m   ";
    private final String yellowCard = "[48;2;232;170;20m   ";

    private final String blueCardDark = "[48;2;0;119;182m   ";
    private final String purpleCardDark = "[48;2;0;119;182m   ";
    private final String whiteCardDark = "[48;2;237;237;233m   ";
    private final String greenCardDark = "[48;2;163;177;138m   ";
    private final String cyanCardDark = "[48;2;173;232;244m   ";
    private final String yellowCardDark = "[48;2;246;189;96m   ";

    private final String shelfBackGorund = "[48;2;164;113;72m   ";
    private final String shelfSeparator = "[48;2;96;56;8m ";
    private final String shelfBase = "[48;2;96;56;8m    ";

    public CLI(LivingRoom viewLivingRoom) {
        this.viewLivingRoom = viewLivingRoom;
        controller = new Controller();
    }

    public CLI(int me) {
        this.me = me;
        controller = new Controller();
    }

    public void start() {
        AtomicBoolean exit = new AtomicBoolean(false);
        viewLivingRoom = initializeGame(3);
        Scanner sc = new Scanner(System.in);
        //TODO LOGIN.

        //TODO CHOICE TO JOIN OR CREATE.
        new Thread(() ->{
            boolean exitIn = false;
            while(!exitIn){
                String command = sc.nextLine();
                exitIn = parseCommand(command);
            }
            exit.set(true);
        }).run();
    }

    private void updateView() throws IOException {
        Character[][] pb = getPrintableBoard(viewLivingRoom.getBoard());
        System.out.flush();
        System.out.println("    0  1  2  3  4  5  6  7  8");
        for(int r = 0; r<9; r++){
            System.out.print(" " + r + " ");
            for(int c = 0; c<9; c++){
                if(pb[r][c] != null){

                    /*if(pb[r][c] == 'P'){
                        System.out.print((char)27 + "[48;2;255;0;111m \uD83C\uDF3C " + (char)27 + "[0m");
                    }else if (pb[r][c] == 'W') {
                        System.out.print((char)27 + "[48;2;241;250;238m \uD83D\uDCD8 " + (char)27 + "[0m");
                    }else if (pb[r][c] == 'C') {
                        System.out.print((char)27 + "[48;2;168;218;220m \uD83C\uDFC6 " + (char)27 + "[0m");
                    }else if (pb[r][c] == 'B') {
                        System.out.print((char)27 + "[48;2;69;123;157m \uD83D\uDDBC " + (char)27 + "[0m");
                    }else if (pb[r][c] == 'G') {
                        System.out.print((char)27 + "[48;2;74;120;86m \uD83D\uDC08 " + (char)27 + "[0m");
                    }else if (pb[r][c] == 'Y') {
                        System.out.print((char)27 + "[48;2;232;170;20m \uD83D\uDD79 " + (char)27 + "[0m");
                    }*/

                    if(pb[r][c] == 'P'){
                        System.out.print((char)27 + purpleCard + (char)27 + "[0m");
                    }else if (pb[r][c] == 'W') {
                        System.out.print((char)27 + whiteCard + (char)27 + "[0m");
                    }else if (pb[r][c] == 'C') {
                        System.out.print((char)27 + cyanCard + (char)27 + "[0m");
                    }else if (pb[r][c] == 'B') {
                        System.out.print((char)27 + blueCard + (char)27 + "[0m");
                    }else if (pb[r][c] == 'G') {
                        System.out.print((char)27 + greenCard + (char)27 + "[0m");
                    }else if (pb[r][c] == 'Y') {
                        System.out.print((char)27 + yellowCard + (char)27 + "[0m");
                    }
                }
                else System.out.print((char)27 + shelfBackGorund + (char)27 + "[0m");

            }
            System.out.print(" " + r + " \n");
        }
        System.out.println("    0  1  2  3  4  5  6  7  8");

        System.out.println("\n");
        System.out.print("    ");
        for(int i = 0; i < me; i++){
            System.out.print("                                       ");
        }
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


        for(int p = 0; p<viewLivingRoom.getPlayers().size(); p++){
            System.out.print("" + viewLivingRoom.getPlayers().get(p).getName() + "                                 ");
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
        System.out.print("\nnewCommand > ");
    }

    public static Character[][] getPrintableBoard(Map<BoardPosition, Boolean> b){
        Character[][] mat = new Character[9][9];
        for(Map.Entry<BoardPosition, Boolean> entry : b.entrySet()){
            mat[entry.getKey().getPosX()][entry.getKey().getPosY()] = entry.getKey().getCard().getColor();
        }
        return mat;
    }

    private boolean parseCommand(String command) {
        if(command.equals("exit") || command.equals("q")){
            controller.leaveGameEvent(viewLivingRoom.getPlayers().get(viewLivingRoom.getTurn()));
            return true;
        }

        String commandPrefix = command.split(" ")[0];

        switch (commandPrefix){
            case "select":
                selectTilesFromBoard(command);
                break;
            case "col":
                checkColAndPlaceTiles(Integer.parseInt(command.split(" ")[1]));
                break;
            case "order":
                orderPickInsert(command);
                break;

                //TODO ADD OTHER COMMANDS
        }


        if(command.startsWith("remove")){
            int posX = Integer.parseInt(command.substring(7, 8));
            int posy = Integer.parseInt(command.substring(10, 11));
            viewLivingRoom.removeCard(new BoardPosition(posX, posy));
            try {
                updateView();
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
            updateView();
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    private void selectTilesFromBoard(String command) {
        boolean canProcede = false;
        String updatedCommand = command;
        while(!canProcede){
            String[] args = updatedCommand.split(" ");
            List<BoardPosition> possiblePick = new ArrayList<>();
            for (int i = 1; i<=3; i++){
                try{
                    int finalI = i;
                    Optional<BoardPosition> pickOne = viewLivingRoom.getBoard().entrySet().stream().filter(x -> x.getKey().equals(new BoardPosition(Integer.parseInt(args[finalI *2]), Integer.parseInt(args[finalI *2 - 1])))).map(x -> x.getKey()).findFirst();
                    pickOne.ifPresent(possiblePick::add);
                    //TODO IF A CARD IS OUT OF BOUND IS AUTOMATICALLY NOT SELECTED
                }
                catch (IndexOutOfBoundsException e){
                    break;
                }
            }

            if(isElegiblePick(possiblePick)){
                moveFromBoardToShelf(possiblePick);
                canProcede = true;
            }
            else {
                System.out.println("You have selected a non possible pick.");
                System.out.print("new Command > ");
                updatedCommand = new Scanner(System.in).nextLine();
            }
        }
    }
    private void orderPickInsert(String command) {
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
            return;
        }
    }
    private void checkColAndPlaceTiles(int tryCol) {
        int col = tryCol;
        boolean canPass = false;
        while(!canPass){
            try{
                viewLivingRoom.getPlayers().get(me).getMyShelf().onClickCol(viewLivingRoom.getPlayers().get(me).getDrawnCards(), col -1);

                canPass = true;
                controller.confirmEndTurn(pick, col);
                viewLivingRoom.nextTurn();
            }
            catch (NotEnoughSpacesInCol nes){
                System.out.println("The Selected Col was not elegible please select a correct one");
                col = Integer.parseInt(new Scanner(System.in).nextLine().split(" ")[1]);
            }
        }

        viewLivingRoom.getPlayers().get(me).withdrawPicks();

    }
    private void moveFromBoardToShelf(List<BoardPosition> pick) {
        for (BoardPosition position : pick){
            viewLivingRoom.removeCard(position);
        }
        try {
            viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(me), pick.stream().map(x -> x.getCard()).toList());
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

}
