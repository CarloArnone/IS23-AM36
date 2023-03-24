package it.polimi.ingsw.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.PersonalGoalCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Shelf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

public class JSONInterface {
    public static void main(String[] args) throws FileNotFoundException {
        JSONInterface builder = new JSONInterface();
        File personalGoalJson = new File("src/main/resources/JSON/personalGoals.json");
        String jsonString = "";
        Scanner reader = new Scanner(personalGoalJson);
        while(reader.hasNextLine()){
            jsonString += reader.nextLine();
        }
        PersonalGoalCard pgc = builder.buildPersonalGoalsFromJson(jsonString);

        System.out.println(pgc.toString());
    }

    Gson converter;

    public JSONInterface() {
        converter = new Gson();
    }
    public static String getJsonStringFrom(String filePath){
        File file = new File(filePath);
        String jsonString = "";
        try{
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                jsonString += reader.nextLine();
            }
        }
        catch(FileNotFoundException fnf){
            return "Invalid String";
        }

        return jsonString;
    }
    
    public PersonalGoalCard buildPersonalGoalsFromJson(String JsonString){
        JsonObject obj = converter.fromJson(JsonString, JsonObject.class);
        List<PersonalGoalCard> personalGoals =  new ArrayList<>();
        JsonArray array = obj.get("personalGoals").getAsJsonArray();

        for (JsonElement jsonElement : array) {
            JsonObject el = (JsonObject) jsonElement;
            Map<List<Integer>, Character> elSubGoals = new HashMap<>();
            List<Integer> list = new ArrayList<>();
            list.add(0, el.get("purple").getAsJsonArray().get(0).getAsInt());
            list.add(1, el.get("purple").getAsJsonArray().get(1).getAsInt());
            elSubGoals.put(new ArrayList<>(list), 'P');
            list.clear();
            list.add(0, el.get("blue").getAsJsonArray().get(0).getAsInt());
            list.add(1, el.get("blue").getAsJsonArray().get(1).getAsInt());
            elSubGoals.put(new ArrayList<>(list), 'B');
            list.clear();
            list.add(0, el.get("yellow").getAsJsonArray().get(0).getAsInt());
            list.add(1, el.get("yellow").getAsJsonArray().get(1).getAsInt());
            elSubGoals.put(new ArrayList<>(list), 'Y');
            list.clear();
            list.add(0, el.get("white").getAsJsonArray().get(0).getAsInt());
            list.add(1, el.get("white").getAsJsonArray().get(1).getAsInt());
            elSubGoals.put(new ArrayList<>(list), 'W');
            list.clear();
            list.add(0, el.get("cyan").getAsJsonArray().get(0).getAsInt());
            list.add(1, el.get("cyan").getAsJsonArray().get(1).getAsInt());
            elSubGoals.put(new ArrayList<>(list), 'C');
            list.clear();
            list.add(0, el.get("green").getAsJsonArray().get(0).getAsInt());
            list.add(1, el.get("green").getAsJsonArray().get(1).getAsInt());
            elSubGoals.put(new ArrayList<>(list), 'C');
            list.clear();
            personalGoals.add(new PersonalGoalCard(elSubGoals));
        }
        Random randomizer = new Random();

        return personalGoals.get(randomizer.nextInt(0, 11));
    }
}
