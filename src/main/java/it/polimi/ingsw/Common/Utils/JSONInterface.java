package it.polimi.ingsw.Common.Utils;

import com.google.gson.*;
import it.polimi.ingsw.Server.Model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JSONInterface {
    Gson converter;
    String shelvesPath = "src/main/resources/JSON/ShelvesTEST.json";
    String boardsPath = "src/main/resources/JSON/BoardsTEST.json";
    String playersPath = "src/main/resources/JSON/PlayersTEST.json";
    String personalGoalsPath = "src/main/resources/JSON/PersonalGoals.json";
    String commonGoalsPath = "src/main/resources/JSON/CommonGoals.json";
    String livingRoomsPath = "src/main/resources/JSON/LivingRoomsTEST.json";

   public JSONInterface() {
        converter = new Gson();
    }
   public String getJsonStringFrom(String filePath){
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
   public String writeShelfToJson(Shelf shelf, String Shelf_ID) {

        JsonArray jsonArray = new JsonArray();


        for(int r = 0; r<shelf.getShelf().length; r++){
            for(int c = 0; c <shelf.getShelf()[0].length; c++){
                if(shelf.getShelf()[r][c].isPresent()){
                    JsonObject jsonObject = new JsonObject();
                    JsonArray coordinates = new JsonArray();
                    coordinates.add(r);
                    coordinates.add(c);
                    jsonObject.add("position", coordinates);
                    jsonObject.addProperty("color", shelf.getShelf()[r][c].get().getColor());
                    jsonArray.add(jsonObject);
                }
            }
        }

       saveIntoFile(Shelf_ID, jsonArray, getShelvesPath());
       return converter.toJson(jsonArray);
   }
   public String writeShelfToJson(Shelf shelf, String Shelf_ID, String filePath) {

        JsonArray jsonArray = new JsonArray();


        for(int r = 0; r<shelf.getShelf().length; r++){
            for(int c = 0; c <shelf.getShelf()[0].length; c++){
                if(shelf.getShelf()[r][c].isPresent()){
                    JsonObject jsonObject = new JsonObject();
                    JsonArray coordinates = new JsonArray();
                    coordinates.add(r);
                    coordinates.add(c);
                    jsonObject.add("position", coordinates);
                    jsonObject.addProperty("color", shelf.getShelf()[r][c].get().getColor());
                    jsonArray.add(jsonObject);
                }
            }
        }

        saveIntoFile(Shelf_ID, jsonArray, "src/main/resources/JSONForTesting/ShelvesTEST.json");
        return converter.toJson(jsonArray);
    }
   public String writeBoardToJson(Map<BoardPosition, Boolean> board, String Board_ID){

        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<BoardPosition, Boolean> entry : board.entrySet()){
            JsonObject jsonObject = new JsonObject();
            JsonArray coordinates = new JsonArray();
            coordinates.add(entry.getKey().getPosX());
            coordinates.add(entry.getKey().getPosY());
            jsonObject.add("position", coordinates);
            jsonObject.addProperty("isAvailable", entry.getValue());
            jsonObject.addProperty("color", entry.getKey().getCard().getColor());
            jsonArray.add(jsonObject);
        }

        saveIntoFile(Board_ID, jsonArray, getBoardsPath());

        return converter.toJson(jsonArray);

    }
   public String writeBoardToJson(Map<BoardPosition, Boolean> board, String Board_ID, String filePath){

        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<BoardPosition, Boolean> entry : board.entrySet()){
            JsonObject jsonObject = new JsonObject();
            JsonArray coordinates = new JsonArray();
            coordinates.add(entry.getKey().getPosX());
            coordinates.add(entry.getKey().getPosY());
            jsonObject.add("position", coordinates);
            jsonObject.addProperty("isAvailable", entry.getValue());
            jsonObject.addProperty("color", entry.getKey().getCard().getColor());
            jsonArray.add(jsonObject);
        }

        saveIntoFile(Board_ID, jsonArray, "src/main/resources/JSONForTesting/BoardsTEST.json");

        return converter.toJson(jsonArray);

    }
   public String writePlayerToJson(Player player){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", player.getName());
        jsonObject.addProperty("score", player.getScore());
        jsonObject.addProperty("personalGoalName", player.getPersonalGoal().getName());

        JsonArray achievedGoals = new JsonArray();
        for(Goal g : player.getAchievedGoals()){
            JsonObject goalG = new JsonObject();

            goalG.addProperty("name", g.getName() );
            goalG.addProperty("points", g.getObtainedPoints());
            JsonElement je = goalG.getAsJsonObject();

            achievedGoals.add(je);
        }
        JsonElement je = achievedGoals.getAsJsonArray();
        jsonObject.add("achievedGoals", je);

        JsonElement shelf = converter.fromJson(writeShelfToJson(player.getMyShelf(), "" + player.getName() + "_shelf"), JsonObject.class).getAsJsonObject();
        jsonObject.add("shelf", shelf);

        saveIntoFile(player.getName(), jsonObject, getPlayersPath());

        return converter.toJson(jsonObject);
    }
   public String writePlayerToJson(Player player, String filePath){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", player.getName());
        jsonObject.addProperty("score", player.getScore());
        jsonObject.addProperty("personalGoalName", player.getPersonalGoal().getName());

        JsonArray achievedGoals = new JsonArray();
        for(Goal g : player.getAchievedGoals()){
            JsonObject goalG = new JsonObject();

            goalG.addProperty("name", g.getName() );
            goalG.addProperty("points", g.getObtainedPoints());
            JsonElement je = goalG.getAsJsonObject();

            achievedGoals.add(je);
        }
        JsonElement je = achievedGoals.getAsJsonArray();
        jsonObject.add("achievedGoals", je);

        JsonElement shelf = converter.fromJson(writeShelfToJson(player.getMyShelf(), "" + player.getName() + "_shelf", filePath), JsonArray.class);
        jsonObject.add("shelf", shelf);

        saveIntoFile(player.getName(), jsonObject, "src/main/resources/JSONForTesting/PlayersTEST.json");

        return converter.toJson(jsonObject);
    }
   public String writeLivingRoomToJson(LivingRoom livingRoom){
        JsonObject livingRoomJObj = new JsonObject();
        livingRoomJObj.addProperty("livingRoomID", livingRoom.getLivingRoomId());

        JsonArray players = new JsonArray();
        for(Player p : livingRoom.getPlayers()){
            JsonElement pJel = converter.fromJson(writePlayerToJson(p), JsonObject.class);
            players.add(pJel);
        }
        JsonElement playersEl = players.getAsJsonArray();

        livingRoomJObj.add("players", playersEl);

        livingRoomJObj.addProperty("turn", livingRoom.getTurn());


        JsonElement board = converter.fromJson(writeBoardToJson(livingRoom.getBoard(), "" + livingRoom.getLivingRoomId() + "_board"), JsonObject.class);
        livingRoomJObj.add("board", board);


        JsonArray commonGoalSet = new JsonArray();
        for(CommonGoalCard cg : livingRoom.getCommonGoalSet()){
            JsonArray points = new JsonArray();
            for(Integer point : cg.getPointsList()){
                points.add(point);
            }

            JsonObject commonGoal = new JsonObject();
            JsonElement pointsEl = points.getAsJsonArray();

            commonGoal.addProperty("goalName", cg.getName());
            commonGoal.add("pointsLeft", pointsEl);

            JsonElement commonGoalEl = commonGoal.getAsJsonObject();
            commonGoalSet.add(commonGoalEl);
        }
        JsonElement commonGoalSetEl = commonGoalSet.getAsJsonArray();
        livingRoomJObj.add("commonGoals", commonGoalSetEl);

        saveIntoFile(livingRoomJObj, getLivingRoomsPath(), "livingRooms");
        return converter.toJson(livingRoomJObj);
    }
   public String writeLivingRoomToJson(LivingRoom livingRoom, String filePath){
        JsonObject livingRoomJObj = new JsonObject();
        livingRoomJObj.addProperty("livingRoomID", livingRoom.getLivingRoomId());

        JsonArray players = new JsonArray();
        for(Player p : livingRoom.getPlayers()){
            JsonElement pJel = converter.fromJson(writePlayerToJson(p, filePath), JsonObject.class);
            players.add(pJel);
        }
        JsonElement playersEl = players.getAsJsonArray();

        livingRoomJObj.add("players", playersEl);

        livingRoomJObj.addProperty("turn", livingRoom.getTurn());


        JsonElement board = converter.fromJson(writeBoardToJson(livingRoom.getBoard(), "" + livingRoom.getLivingRoomId() + "_board", filePath), JsonArray.class);
        livingRoomJObj.add("board", board);


        JsonArray commonGoalSet = new JsonArray();
        for(CommonGoalCard cg : livingRoom.getCommonGoalSet()){
            JsonArray points = new JsonArray();
            for(Integer point : cg.getPointsList()){
                points.add(point);
            }

            JsonObject commonGoal = new JsonObject();
            JsonElement pointsEl = points.getAsJsonArray();

            commonGoal.addProperty("goalName", cg.getName());
            commonGoal.add("pointsLeft", pointsEl);

            JsonElement commonGoalEl = commonGoal.getAsJsonObject();
            commonGoalSet.add(commonGoalEl);
        }
        JsonElement commonGoalSetEl = commonGoalSet.getAsJsonArray();
        livingRoomJObj.add("commonGoals", commonGoalSetEl);

        saveIntoFile(livingRoomJObj, "src/main/resources/JSONForTesting/LivingRoomsTEST.json", "livingRooms");
        return converter.toJson(livingRoomJObj);
    }
   public Shelf getShelfFromJson(String jsonString, String Shelf_ID, int Shelf_Width, int Shelf_Height){

       JsonObject jsonObject = converter.fromJson(jsonString, JsonObject.class);
       JsonArray shelfArray = converter.fromJson(jsonObject.get(Shelf_ID), JsonArray.class);
       Optional<ItemCard>[][] opt = new Optional[6][5];

       for(JsonElement el : shelfArray){
           opt[el.getAsJsonObject().get("position").getAsJsonArray().get(0).getAsInt()][el.getAsJsonObject().get("position").getAsJsonArray().get(1).getAsInt()] = Optional.of(new ItemCard(el.getAsJsonObject().get("color").getAsCharacter(), ""));
       }

       for(int r = 0; r<Shelf_Height; r++){
           for (int c = 0; c<Shelf_Width; c++){
               if(opt[r][c] == null){
                   opt[r][c] = Optional.empty();
               }
           }
       }

        return new Shelf(opt);
   }
   public Shelf getShelfFromJson(String jsonString, int Shelf_Width, int Shelf_Height){

        JsonArray shelfArray = converter.fromJson(jsonString, JsonArray.class);
        Optional<ItemCard>[][] opt = new Optional[6][5];

        for(JsonElement el : shelfArray){
            opt[el.getAsJsonObject().get("position").getAsJsonArray().get(0).getAsInt()][el.getAsJsonObject().get("position").getAsJsonArray().get(1).getAsInt()] = Optional.of(new ItemCard(el.getAsJsonObject().get("color").getAsCharacter(), ""));
        }

        for(int r = 0; r<Shelf_Height; r++){
            for (int c = 0; c<Shelf_Width; c++){
                if(opt[r][c] == null){
                    opt[r][c] = Optional.empty();
                }
            }
        }

        return new Shelf(opt);
    }
   public Map<BoardPosition, Boolean> getBoardFromJson(String jsonString, String Board_ID){
        JsonArray board = converter.fromJson(getJsonStringFrom(getBoardsPath()), JsonObject.class).getAsJsonArray("" + Board_ID + "_board");
        Map<BoardPosition, Boolean> toReturn = new HashMap<>();
        for(JsonElement position : board){
            toReturn.put(new BoardPosition( position.getAsJsonObject().getAsJsonArray("position").get(0).getAsInt(),
                                            position.getAsJsonObject().getAsJsonArray("position").get(1).getAsInt(),
                                            new ItemCard(position.getAsJsonObject().get("color").getAsCharacter(), "")),
                                            position.getAsJsonObject().get("isAvailable").getAsBoolean());
        }

        return toReturn;
    }
   public Map<BoardPosition, Boolean> getBoardFromJson(String jsonString){
        JsonArray board = converter.fromJson(jsonString, JsonArray.class);
        Map<BoardPosition, Boolean> toReturn = new HashMap<>();
        for(JsonElement position : board){
            toReturn.put(new BoardPosition( position.getAsJsonObject().getAsJsonArray("position").get(0).getAsInt(),
                            position.getAsJsonObject().getAsJsonArray("position").get(1).getAsInt(),
                            new ItemCard(position.getAsJsonObject().get("color").getAsCharacter(), "")),
                    position.getAsJsonObject().get("isAvailable").getAsBoolean());
        }

        return toReturn;
    }
   public LivingRoom getLivingRoomFromJson(String jsonString, String LivingRoom_ID){
        JsonArray livingRoomsArray = converter.fromJson(jsonString, JsonObject.class).getAsJsonObject().getAsJsonArray("livingRooms");
        JsonObject livingRoomJson = new JsonObject();
        for(JsonElement livingRoom : livingRoomsArray){
            if(livingRoom.getAsJsonObject().get("livingRoomID").getAsString().equals(LivingRoom_ID)){
                livingRoomJson = livingRoom.getAsJsonObject();
            }
        }
        Map<BoardPosition, Boolean> board = getBoardFromJson(converter.toJson(livingRoomJson.get("board")));

        JsonArray players = livingRoomJson.getAsJsonArray("players");
        List<Player> playersList = new ArrayList<>();
        for(JsonElement player : players){
            playersList.add(getPlayerFromJson(converter.toJson(player)));
        }

        int turn = livingRoomJson.get("turn").getAsInt();
        List<CommonGoalCard> commonGoalSet = new ArrayList<>();

        for (JsonElement commonGoal : livingRoomJson.getAsJsonArray("commonGoals")){
            commonGoalSet.add(getCommonGoalCardFromString(converter.toJson(commonGoal)));
        }

        return new LivingRoom(LivingRoom_ID, board, playersList, commonGoalSet, turn);

    }
   public Player getPlayerFromJson(String jsonString, String Player_ID){
        JsonObject playerObj = converter.fromJson(getJsonStringFrom(getPlayersPath()), JsonObject.class).getAsJsonObject(Player_ID);
        List<Goal> achievedGoals = new ArrayList<>();

        for(JsonElement el : playerObj.getAsJsonArray("achievedGoals")){
            achievedGoals.add( new CommonGoalCard(el.getAsJsonObject().get("name").getAsString(), el.getAsJsonObject().get("points").getAsInt()));
        }

        return new Player(  playerObj.get("name").getAsString(),
                            playerObj.get("score").getAsInt(),
                            achievedGoals,
                            getShelfFromJson(converter.toJson(playerObj.getAsJsonArray("shelf")), 5, 6),
                            getPersonalGoalsFromJson(getJsonStringFrom(getPersonalGoalsPath()), playerObj.get("personalGoalName").getAsString()));
   }
   public Player getPlayerFromJson(String jsonString){
        JsonObject playerObj = converter.fromJson(jsonString, JsonObject.class);
        List<Goal> achievedGoals = new ArrayList<>();

        for(JsonElement el : playerObj.getAsJsonArray("achievedGoals")){
            achievedGoals.add( new CommonGoalCard(el.getAsJsonObject().get("name").getAsString(), el.getAsJsonObject().get("points").getAsInt()));
        }

        return new Player(  playerObj.get("name").getAsString(),
                playerObj.get("score").getAsInt(),
                achievedGoals,
                getShelfFromJson(converter.toJson(playerObj.getAsJsonArray("shelf")), 5, 6),
                getPersonalGoalsFromJson(getJsonStringFrom(getPersonalGoalsPath()), playerObj.get("personalGoalName").getAsString()));
    }
   public PersonalGoalCard getPersonalGoalsFromJson(String JsonString){
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
            elSubGoals.put(new ArrayList<>(list), 'G');
            list.clear();
            personalGoals.add(new PersonalGoalCard(el.get("filename").getAsString(), elSubGoals));
        }
        Random randomizer = new Random();
        return personalGoals.get(randomizer.nextInt(0, personalGoals.size()));
    }
   public  PersonalGoalCard getPersonalGoalsFromJson(String JsonString, String personalGoal_ID){
        JsonObject obj = converter.fromJson(JsonString, JsonObject.class);
        List<PersonalGoalCard> personalGoals =  new ArrayList<>();
        JsonArray array = obj.get("personalGoals").getAsJsonArray();

        Map<List<Integer>, Character> elSubGoals = new HashMap<>();

        for (JsonElement jsonElement : array) {
            JsonObject el = (JsonObject) jsonElement;

            if(el.get("filename").getAsString().equals(personalGoal_ID)){
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
                elSubGoals.put(new ArrayList<>(list), 'G');
                list.clear();
            }

        }

        return new PersonalGoalCard(personalGoal_ID, elSubGoals);
    }
   public CommonGoalCard getCommonGoalCardFromJson(int playersNum){
        JsonArray commonGoalsArray = converter.fromJson(getJsonStringFrom(getCommonGoalsPath()), JsonObject.class).get("commonGoals").getAsJsonArray();

        Random randomizer = new Random();

        JsonObject commonGoalJObj = commonGoalsArray.get(randomizer.nextInt(0, commonGoalsArray.size())).getAsJsonObject();
       return getCommonGoalCard(playersNum, commonGoalJObj);
   }
   public CommonGoalCard getCommonGoalCardFromID(String ID, int playersNum) {

       JsonArray commonGoalsArray = converter.fromJson(getJsonStringFrom(getCommonGoalsPath()), JsonObject.class).get("commonGoals").getAsJsonArray();

       JsonObject commonGoalJObj = commonGoalsArray.get(0).getAsJsonObject();
       for (JsonElement el : commonGoalsArray.getAsJsonArray()) {
           if (el.getAsJsonObject().get("id").getAsString().equals(ID)) {
               commonGoalJObj = el.getAsJsonObject();
           }
       }

       return getCommonGoalCard(playersNum, commonGoalJObj);
   }
   public CommonGoalCard getCommonGoalCardFromString(String jsonString) {

       JsonObject commonGoalJObj = converter.fromJson(jsonString, JsonObject.class);
       CommonGoalCard cmg = getCommonGoalCardFromID(commonGoalJObj.get("goalName").getAsString(), 2);
       cmg.setPoints(getAsListOfT(commonGoalJObj.getAsJsonArray("pointsLeft"), Integer.class));

       return cmg;
    }
   private CommonGoalCard getCommonGoalCard(int playersNum, JsonObject commonGoalJObj) {
        List<Argument> argumentList = new ArrayList<>();

        for (JsonElement arrayOfArgumentsArray : commonGoalJObj.get("argumentsList").getAsJsonArray()) {
            List<List<Object>> constructorOfArgument = new ArrayList<>();
            for (JsonElement argumentsArray : arrayOfArgumentsArray.getAsJsonArray()) {
                List<Object> properArgumentsList = new ArrayList<>();
                for (int i = 0; i < argumentsArray.getAsJsonArray().size(); i++) {
                    properArgumentsList.add(i, argumentsArray.getAsJsonArray().get(i));
                }
                constructorOfArgument.add(properArgumentsList);
            }
            argumentList.add(new Argument(commonGoalJObj.get("type").getAsString(), constructorOfArgument));
        }
        List<Integer> pointsOfCommonGoal = new ArrayList<>();
        int i = 0;
        for (JsonElement points : converter.fromJson(getJsonStringFrom("src/main/resources/JSON/GameScratch.json"), JsonObject.class).get("GamesSetup")
                .getAsJsonArray().get(playersNum - 2)
                .getAsJsonObject().get("commonGoalPoints")
                .getAsJsonArray()) {
            pointsOfCommonGoal.add(i, points.getAsInt());
            i++;
        }

        return new CommonGoalCard(commonGoalJObj.get("id").getAsString(), pointsOfCommonGoal, argumentList);
    }
   public Map<BoardPosition, Boolean> getBoardFromJson(int playersNum){
        Map<BoardPosition, Boolean> toReturn = new HashMap<>();
        JsonArray gameSetups = converter.fromJson(getJsonStringFrom("src/main/resources/JSON/GameScratch.json"), JsonObject.class)
                                        .getAsJsonObject().get("GamesSetup")
                                        .getAsJsonArray();

        JsonObject correctSetUp = new JsonObject();
        for(int i = 0; i< gameSetups.size(); i++){
            if(gameSetups.get(i).getAsJsonObject().get("playersNumber").getAsInt() == playersNum){
                correctSetUp = converter.fromJson(gameSetups.get(i).getAsJsonObject(), JsonObject.class);
            }
        }

        for(JsonElement jObj : correctSetUp.getAsJsonObject().get("board").getAsJsonArray()){
            toReturn.put(new BoardPosition(jObj.getAsJsonObject().get("position").getAsJsonArray().get(0).getAsInt(), jObj.getAsJsonObject().get("position").getAsJsonArray().get(1).getAsInt()), jObj.getAsJsonObject().get("isGood").getAsBoolean());
        }

        return toReturn;
   }
   public <T> List<T> getAsListOfT(JsonArray jsonArray, Type T){
        List<T> toReturn = new ArrayList<>();
        for(JsonElement el : jsonArray){
            toReturn.add((T)el.getAsJsonPrimitive());
        }
        return toReturn;
   }
   public List<String> getLivingRoomsList(){
       JsonArray livingRoomsArray = converter.fromJson(getJsonStringFrom(getLivingRoomsPath()), JsonObject.class).getAsJsonArray("livingRooms");
       List<String> toReturn = new ArrayList<>();

       for(JsonElement livingRoom : livingRoomsArray){
           toReturn.add(livingRoom.getAsJsonObject().get("livingRoomID").getAsString());
       }

       return toReturn;
   }
   private void saveIntoFile(String ID, JsonObject jsonObject, String filePath){
        File newFile = new File(filePath);
        JsonObject js1 = converter.fromJson(getJsonStringFrom(filePath), JsonObject.class);
        JsonElement n = jsonObject.getAsJsonObject();
        js1.add(ID, n);
        FileWriter writer = null;
        try {
            writer = new FileWriter(newFile);
            writer.write(converter.toJson(js1));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   private void saveIntoFile(String ID, JsonArray jsonArray, String filePath){
        File newFile = new File(filePath);
        JsonObject js1 = converter.fromJson(getJsonStringFrom(filePath), JsonObject.class);
        JsonElement n = jsonArray.getAsJsonArray();
        js1.add(ID, n);
        FileWriter writer = null;
        try {
            writer = new FileWriter(newFile);
            writer.write(converter.toJson(js1));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   private void saveIntoFile(JsonObject jsonObject, String filePath, String ID_array){
        File newFile = new File(filePath);
        JsonArray js1 = converter.fromJson(getJsonStringFrom(filePath), JsonObject.class).getAsJsonArray(ID_array);
        JsonElement n = jsonObject.getAsJsonObject();
        js1.add(n);
        JsonObject toSave = new JsonObject();
        toSave.add(ID_array, js1);
        FileWriter writer = null;
        try {
            writer = new FileWriter(newFile);
            writer.write(converter.toJson(toSave));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   public String getShelvesPath() {
        return shelvesPath;
    }
   public String getBoardsPath() {
        return boardsPath;
    }
   public String getPlayersPath() {
        return playersPath;
    }
   public String getPersonalGoalsPath() {
        return personalGoalsPath;
    }
   public String getCommonGoalsPath() {
        return commonGoalsPath;
    }
   public String getLivingRoomsPath() {
        return livingRoomsPath;
    }
}
