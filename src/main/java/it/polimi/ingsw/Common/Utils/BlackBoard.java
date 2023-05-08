package it.polimi.ingsw.Common.Utils;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BlackBoard {
    private static final Map<String, Pair<String, Boolean>> blackBoard = new ConcurrentHashMap<>();
    private static BlackBoard Instance;


    public static void write(String messageName, String message){
            blackBoard.put(messageName, new Pair<>(message, true));
    }

    public static String readAsync(String messageName){
        String toReturn = blackBoard.get(messageName).getKey();
        blackBoard.put(messageName, new Pair<>(blackBoard.get(messageName).getKey(), false));
        return toReturn;
    }

    public static String readNew(String messageName){
        while(!blackBoard.containsKey(messageName)){
            continue;
        }
        while(!blackBoard.get(messageName).getValue()){
            continue;
        }

        return readAsync(messageName);
    }


    public static BlackBoard getInstance() {
        if(Instance == null){
            return new BlackBoard();
        }
        else return Instance;

    }

}
