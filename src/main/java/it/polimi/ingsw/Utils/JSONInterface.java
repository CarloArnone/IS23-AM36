package it.polimi.ingsw.Utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.Model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JSONInterface {
    public static void main(String[] args) throws IOException {
        JSONInterface builder = new JSONInterface();


        Optional<ItemCard>[][] propershelf = new Optional[6][5];

        for(int r = 0; r<propershelf.length; r++){
            for(int c = 0; c <propershelf[0].length; c++){
                propershelf[r][c] = Optional.of(new ItemCard('P', ""));
            }
        }

        Shelf shelfp = new Shelf(propershelf);

        List<CommonGoalCard> commonGoalSet = new ArrayList<>();
        List<Integer> pointsLeft = new ArrayList<>();
        pointsLeft.add(0, 6);
        pointsLeft.add(1, 4);
        CommonGoalCard c1 = new CommonGoalCard("FourByFour", pointsLeft);
        CommonGoalCard c2 = new CommonGoalCard("SixCols", pointsLeft);
        commonGoalSet.add(c1);
        commonGoalSet.add(c2);

        List<Goal> achievedGoals = new ArrayList<>();
        achievedGoals.add(0, c1);
        achievedGoals.add(1, c2);

        Player p = new Player("carlo", 123, achievedGoals, shelfp, builder.getPersonalGoalsFromJson(builder.getJsonStringFrom(builder.getPersonalGoalsPath())));
        Player p1 = new Player("franco", 862, achievedGoals, shelfp, builder.getPersonalGoalsFromJson(builder.getJsonStringFrom(builder.getPersonalGoalsPath())));
        Player p2= new Player("luigi", 56, achievedGoals, shelfp, builder.getPersonalGoalsFromJson(builder.getJsonStringFrom(builder.getPersonalGoalsPath())));
        Player p3 = new Player("mario", 156, achievedGoals, shelfp, builder.getPersonalGoalsFromJson(builder.getJsonStringFrom(builder.getPersonalGoalsPath())));

        List<Player> playerList4 = new ArrayList<>();
        playerList4.add(p);
        playerList4.add(p1);
        playerList4.add(p2);
        playerList4.add(p3);

        List<Player> playerList3 = new ArrayList<>();
        playerList3.add(p);
        playerList3.add(p1);
        playerList3.add(p2);

        List<Player> playerList2 = new ArrayList<>();
        playerList2.add(p);
        playerList2.add(p1);

        Map<BoardPosition, Boolean> board1 = builder.getBoardFromJson(playerList4.size());
        Map<BoardPosition, Boolean> board2 = builder.getBoardFromJson(playerList3.size());
        Map<BoardPosition, Boolean> board3 = builder.getBoardFromJson(playerList2.size());

        builder.writeLivingRoomToJson(new LivingRoom("12345", board1, playerList4, commonGoalSet), "12345");
        builder.writeLivingRoomToJson(new LivingRoom("45678", board2, playerList3, commonGoalSet), "45678");
        builder.writeLivingRoomToJson(new LivingRoom("89076", board3, playerList2, commonGoalSet), "89076");

    }

    Gson converter;
    String shelvesPath = "src/main/resources/JSON/Shelves.json";
    String boardsPath = "src/main/resources/JSON/Boards.json";
    String playersPath = "src/main/resources/JSON/Players.json";
    String personalGoalsPath = "src/main/resources/JSON/PersonalGoals.json";
    String commonGoalsPath = "src/main/resources/JSON/CommonGoals.json";
    String livingRoomsPath = "src/main/resources/JSON/LivingRooms.json";

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

    public String writeAShelfToJson(Shelf shelf, String Shelf_ID) {
        JsonObject jsonObject = new JsonObject();
        for(int r = 0; r<shelf.getShelf().length; r++){
            for(int c = 0; c <shelf.getShelf()[0].length; c++){
                if(shelf.getShelf()[r][c].isPresent()){
                    jsonObject.addProperty("Position (" + r + ", " + c + ")", shelf.getShelf()[r][c].get().getColor());
                }
            }
        }

       saveIntoFile(Shelf_ID, jsonObject, getShelvesPath());
       return converter.toJson(jsonObject);
   }

    public String writeBoardToJson(Map<BoardPosition, Boolean> board, String Board_ID){
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<BoardPosition, Boolean> entry : board.entrySet()){
            jsonObject.addProperty("Position : (" + entry.getKey().getPosX() + ", " + entry.getKey().getPosY() + ")", entry.getValue());
        }
        saveIntoFile(Board_ID, jsonObject, getBoardsPath());

        return converter.toJson(jsonObject);

    }

    public String writePlayerToJson(Player player, String Player_ID){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", player.getName());
        jsonObject.addProperty("score", player.getScore());
        jsonObject.addProperty("personalGoalName", player.getPersonalGoal().getName());

        JsonObject achivedGoals = new JsonObject();
        for(Goal g : player.getAchievedGoals()){
            JsonObject goalG = new JsonObject();

            goalG.addProperty("name", g.getName() );
            goalG.addProperty("points", g.getObtainedPoints());
            JsonElement je = goalG.getAsJsonObject();

            achivedGoals.add("commonGoal_" + player.getAchievedGoals().indexOf(g), je);
        }
        JsonElement je = achivedGoals.getAsJsonObject();
        jsonObject.add("achievedGoals", je);

        JsonElement shelf = converter.fromJson(writeAShelfToJson(player.getMyShelf(), "" + player.getName() + "_shelf"), JsonObject.class).getAsJsonObject();
        jsonObject.add("shelf", shelf);

        saveIntoFile(player.getName(), jsonObject, getPlayersPath());

        return converter.toJson(jsonObject);
    }

    public String writeLivingRoomToJson(LivingRoom livingRoom, String LivingRoom_ID){
        JsonObject livingRoomJObj = new JsonObject();
        livingRoomJObj.addProperty("livingRoomID", livingRoom.getLivingRoomId());

        JsonArray players = new JsonArray();
        for(Player p : livingRoom.getPlayers()){
            JsonElement pJel = converter.fromJson(writePlayerToJson(p, p.getName()), JsonObject.class);
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

            JsonElement commongoalEl = commonGoal.getAsJsonObject();
            commonGoalSet.add(commongoalEl);
        }
        JsonElement commonGoalSetEl = commonGoalSet.getAsJsonArray();
        livingRoomJObj.add("commonGoals", commonGoalSetEl);

        saveIntoFile(livingRoom.getLivingRoomId(), livingRoomJObj, getLivingRoomsPath());
        return converter.toJson(livingRoomJObj);
    }


   public Shelf getShelfFromJson(String jsonString, String Shelf_ID, int Shelf_Width, int Shelf_Height){

       JsonObject jsonObject = converter.fromJson(jsonString, JsonObject.class);
       JsonObject shelfObj = converter.fromJson(jsonObject.get(Shelf_ID), JsonObject.class);
       Optional<ItemCard>[][] opt = new Optional[6][5];

        for(int r = 0; r< Shelf_Height; r++){
           for(int c = 0; c <Shelf_Width; c++){
               if(shelfObj.get("Position (" + r + ", " + c + ")") != null){
                   char ch = shelfObj.get("Position (" + r + ", " + c + ")").getAsCharacter();
                   opt[r][c] = Optional.of(new ItemCard(ch, ""));
               }
               else opt[r][c] = Optional.empty();
           }
       }

        return new Shelf(opt);
   }


    public Map<BoardPosition, Boolean> getBoardFromJson(String jsonString, String Board_ID){
        return null;
    }


    public LivingRoom getLivingRoomFromJson(String jsonString, String LivingRoom_ID){
        return null;
    }

   public Player getPlayerFromJson(String jsonString, String Player_ID){
       return null;
   }

   public  PersonalGoalCard getPersonalGoalsFromJson(String JsonString){
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
            personalGoals.add(new PersonalGoalCard(el.get("filename").getAsString(), elSubGoals));
        }
        Random randomizer = new Random();
        return personalGoals.get(randomizer.nextInt(0, personalGoals.size()));
    }

   public CommonGoalCard getCommonGoalCardFromJson(String jsonString){
       return null;
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