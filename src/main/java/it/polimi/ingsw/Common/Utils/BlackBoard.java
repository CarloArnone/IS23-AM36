package it.polimi.ingsw.Common.Utils;

import java.util.HashMap;
import java.util.Map;

public class BlackBoard {
    private static Map<String, String> blackBoard;
    private static BlackBoard Instance;


    public BlackBoard() {
        blackBoard = new HashMap<>();
    }

    public synchronized static void write(String messageName, String message){
        blackBoard.put(messageName, message);
    }

    public synchronized static String read(String messageName){
        return blackBoard.get(messageName);
    }


    public static BlackBoard getInstance() {
        if(Instance == null){
            return new BlackBoard();
        }
        else return Instance;

    }
}
