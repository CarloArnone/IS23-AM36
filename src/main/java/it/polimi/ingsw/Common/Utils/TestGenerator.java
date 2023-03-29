package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Server.Model.*;

import java.util.*;

public class TestGenerator {
    static Random randomizer = new Random();

    public static LivingRoom generateLivingRoom(int playersNum){
        LivingRoom l = JSONInterface.getLivingRoomFromJson(JSONInterface.getJsonStringFrom("src/main/resources/JSONForTesting/LivingRoomsTEST.json"), "00000");
        l.setBoard(JSONInterface.getBoardFromJson(playersNum));
        l.arrangeDesk();
        for (int i = 0; i < playersNum; i++) {
            l.addPlayer(generatePlayer(randomizer.nextBoolean()));
        }
        List<CommonGoalCard> commonGoals = new ArrayList<>();
        CommonGoalCard c0 = JSONInterface.getCommonGoalCardFromJson(playersNum);
        CommonGoalCard c1 = JSONInterface.getCommonGoalCardFromJson(playersNum);
        while(c1.equals(c0)){
            c1 = JSONInterface.getCommonGoalCardFromJson(playersNum);
        }

        commonGoals.add(c0);
        commonGoals.add(c1);

        l.setCommonGoalSet(commonGoals);

        l.setLivingRoomId( String.valueOf(randomizer.nextInt(10000, 99999)));
        return l;
    }

    private static Player generatePlayer(boolean hasAchievedGoals) {
        String name = String.valueOf(randomizer.nextInt(100000, 999999));
        int score = randomizer.nextInt(100, 2000);
        List<Goal> achievedGoals = new ArrayList<>();
        if(hasAchievedGoals){
            achievedGoals.add(JSONInterface.getCommonGoalCardFromJson(2));
        }
        PersonalGoalCard personalGoalCard = JSONInterface.getPersonalGoalsFromJson(JSONInterface.getJsonStringFrom(JSONInterface.personalGoalsPath));
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
