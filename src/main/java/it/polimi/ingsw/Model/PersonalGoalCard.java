package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersonalGoalCard extends Goal{

    private List<Integer> points = new ArrayList<>();
    private Map<List<Integer>,  Character> subGoals;
    private int checkedSubGoals;

    public PersonalGoalCard(String name, Map<List<Integer>, Character> subGoals) {
        this.subGoals = subGoals;
        this.name = name;

        checkedSubGoals = 0;

        points.add(0, 0);
        points.add(1, 1);
        points.add(2, 2);
        points.add(3, 4);
        points.add(4, 6);
        points.add(5, 9);
        points.add(6, 12);
    }

    /**
     * return the number of points achieved by the player in possess of the shelf based on how many subGoals of the personalGoal he completed.
     * @return pointsAchievedByThePlayer
     */
    public int getPoints(){
        return points.get(checkedSubGoals);
    }

    /**
     * Check all the subGoals of a personalGoal that correspond to the position of the shelf.
     *
     * @param p player in posses of the shelf
     * @return true
     */
    @Override
    public boolean checkGoal(Player p) {

        // TO PAY ATTENTION ALL THE MATRIX SHOULD HAVE ONE OF Optional.of(ItemCard) OR Optional.empty() OTHERWISE THE CODE EXPLODES.
        //INSIDE THE EXECUTION OF A GAME THIS SHOULD NOT HAPPEN BECAUSE EVERY SHELF IS INITIALIZED AT THE BEGINNING.

        Optional<ItemCard>[][] shelf = p.getMyShelf().getShelf();
        ItemCard falseResult = new ItemCard('Z', "falseResultToCompare");
        checkedSubGoals = Math.toIntExact(subGoals.entrySet().stream().filter(x -> shelf[x.getKey().get(0)/* x */][x.getKey().get(1)/* y */].orElse(falseResult).getColor() == x.getValue()).count());

        return true;
    }

    /**
     * just for testing
     * @return number of subPersonalGoals achieved by the player in posses of the shelf.
     */
    public int getCheckedSubGoals() {
        return checkedSubGoals;
    }

}
