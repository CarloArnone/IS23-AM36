package it.polimi.ingsw.Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

class PersonalGoalCardTest {

    static Player player;
    static PersonalGoalCard personalGoal;

    static Map<List<Integer>, Character> subGoals = new HashMap<>();
    static List<Integer> pos4 = new ArrayList<>();

    @BeforeAll
    static void setupForTest(){
        //personalGoal Initialization
        List<Integer> pos1 = new ArrayList<>();
            pos1.add(0);
            pos1.add(0);
        List<Integer> pos2 = new ArrayList<>();
            pos2.add(0);
            pos2.add(3);
        List<Integer> pos3 = new ArrayList<>();
            pos3.add(4);
            pos3.add(3);
        //Pos 4 Declaration
            pos4.add(3);
            pos4.add(5);
        List<Integer> pos5 = new ArrayList<>();
            pos5.add(2);
            pos5.add(3);

        subGoals.put(pos1, 'W');
        subGoals.put(pos2, 'P');
        subGoals.put(pos3, 'G');
        subGoals.put(pos4, 'Y');
        subGoals.put(pos5, 'B');

        personalGoal = new PersonalGoalCard(subGoals);

        //Shelf Position Initialization
        Optional<ItemCard>[][] shelf = new Optional[5][6];
        ItemCard card1 = new ItemCard('W', "subGoal1"); // CORRECT COLOR
        ItemCard card2 = new ItemCard('P', "subGoal2"); // CORRECT COLOR
        ItemCard card3 = new ItemCard('G', "subGoal3"); // ABSENT IN SUB1-2
        ItemCard card4 = new ItemCard('Y', "subGoal4"); // CORRECT COLOR IN SUB1 ABSENT IN SUB2
        ItemCard card5 = new ItemCard('C', "subGoal5"); // WRONG COLOR

        shelf[pos1.get(0)][pos1.get(1)] = Optional.of(card1);
        shelf[pos2.get(0)][pos2.get(1)] = Optional.of(card2);
        shelf[pos3.get(0)][pos3.get(1)] = Optional.empty();
        shelf[pos4.get(0)][pos4.get(1)] = Optional.of(card4);
        shelf[pos5.get(0)][pos5.get(1)] = Optional.of(card5);

        player = new Player("Ciccio", new Shelf(shelf));

    }


    @Test
    void getPoints() {

        int score = player.getScore();

        assert player.getScore() == score;

        if(personalGoal.checkGoal(player)){
            player.addPoints(personalGoal.getPoints());
        }

        assert player.getScore() == score + 4;
    }

    @Test
    void checkGoal() {

        personalGoal.checkGoal(player);

        assert personalGoal.getCheckedSubGoals() == 3;

        Map<List<Integer>, Character> subGoals1 = new HashMap<>();
        subGoals1.putAll(subGoals);
        subGoals1.remove(pos4);
        PersonalGoalCard personalGoal1 = new PersonalGoalCard(subGoals1);

        personalGoal1.checkGoal(player);
        assert personalGoal1.getCheckedSubGoals() == 2;

    }
}