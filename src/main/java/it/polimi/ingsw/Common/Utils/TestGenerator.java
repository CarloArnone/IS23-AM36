package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Server.Model.*;

import java.util.*;

public class TestGenerator {
    static Random randomizer = new Random();
    static JSONInterface builder = new JSONInterface();

    public static LivingRoom generateLivingRoom(int playersNum){
        LivingRoom l = builder.getLivingRoomFromJson(builder.getJsonStringFrom("src/main/resources/JSONForTesting/LivingRoomsTEST.json"), "00000");
        //<Map<BoardPosition, Boolean> testBoard = builder.getBoardFromJson(3);
        l.setBoard(builder.getBoardFromJson(playersNum));
        l.arrangeDesk();
        for (int i = 0; i < playersNum; i++) {
            l.addPlayer(generatePlayer(randomizer.nextBoolean()));
        }
        List<CommonGoalCard> commonGoals = new ArrayList<>();
        commonGoals.add(builder.getCommonGoalCardFromJson(playersNum));
        commonGoals.add(builder.getCommonGoalCardFromJson(playersNum));

        l.setCommonGoalSet(commonGoals);

        l.setLivingRoomId( String.valueOf(randomizer.nextInt(10000, 99999)));
        builder.writeLivingRoomToJson(l, "src/main/resources/JSONForTesting/LivingRoomsTEST.json");
        return l;
    }

    private static Player generatePlayer(boolean hasAchievedGoals) {
        String name = String.valueOf(randomizer.nextInt(100000, 999999));
        int score = randomizer.nextInt(100, 2000);
        List<Goal> achievedGoals = new ArrayList<>();
        if(hasAchievedGoals){
            achievedGoals.add(builder.getCommonGoalCardFromJson(2));
        }
        PersonalGoalCard personalGoalCard = builder.getPersonalGoalsFromJson(builder.getJsonStringFrom(builder.personalGoalsPath));
        Shelf shelf = generateShelf();

        return new Player(name, score, achievedGoals, shelf, personalGoalCard);
    }

    public static Shelf generateShelf(){
        Optional<ItemCard>[][] shelf = new Optional[6][5];
        for(int r = shelf.length -1; r >= 0; r--){
            for (int c = shelf[0].length -1; c >= 0; c--){
                if(r == shelf.length -1){
                    if(randomizer.nextBoolean()){
                        shelf[r][c] = Optional.ofNullable(extractACard());
                    }
                    else shelf[r][c] = Optional.empty();
                }
                else{
                    if(shelf[r +1][c].isPresent()){
                        if(randomizer.nextBoolean()){
                            shelf[r][c] = Optional.ofNullable(extractACard());
                        }
                        else shelf[r][c] = Optional.empty();
                    }
                    else shelf[r][c] = Optional.empty();
                }
            }
        }
        return new Shelf(shelf);
    }

    public static ItemCard extractACard(){
        Random randomizer = new Random();
        int cardPick = randomizer.nextInt(0, 132);
        if(cardPick <= 22){
            return new ItemCard('P', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 44) {
            return new ItemCard('B', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 66) {
            return new ItemCard('G', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 88) {
            return new ItemCard('C', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 110) {
            return new ItemCard('W', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 132) {
            return new ItemCard('Y', "" + randomizer.nextInt(1, 3));
        }
        else return null;
    }
}
