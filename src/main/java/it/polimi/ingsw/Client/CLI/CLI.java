package it.polimi.ingsw.Client.CLI;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Common.Utils.BlackBoard;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.Utils.Printer;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.CommonGoalCard;
import it.polimi.ingsw.Server.Model.Player;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

import static java.lang.Thread.sleep;

public class CLI extends IUI {

    Scanner sc ;


    public CLI() {
        sc = new Scanner(System.in);
    }


    public void launch(){

        new Thread(() -> {

            loginTry(false);

            getVirtualViewClient().createGameEvent("provaGame", getMySelf(), 3);

            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            printBoard();

        }).start();

    }

    public void loginTry(boolean retry){
        printLoginScreen(retry);
        getVirtualViewClient().logInTryEvent(BlackBoard.readAsync("name"), getVirtualViewClient());

        if(Boolean.parseBoolean(BlackBoard.readNew("loginReturn"))){
            setMySelf(new Player(BlackBoard.readAsync("name")));
            centerContent("Login Successful");
        }
        else loginTry(true);
    }

    private void printLoginScreen(boolean retry) {
        if(retry){
            Random random = new Random();
            centerHorizontaly("UserName Already Taken.");
            printNLines(2);
            centerHorizontaly("What about :     " + BlackBoard.readAsync("name") + String.valueOf(random.nextInt(100, 999)));
            printNLines(1);
            for (int i = 0; i < 5; i++) {
                centerHorizontaly(BlackBoard.readAsync("name") + String.valueOf(random.nextInt(100, 999)));
                printNLines(1);
            }

        }

        printNLines(7);
        centerHorizontaly("UserName: ");
        BlackBoard.write("name", sc.nextLine());

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
