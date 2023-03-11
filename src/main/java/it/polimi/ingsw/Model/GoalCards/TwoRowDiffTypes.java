package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.Player;

public class TwoRowDiffTypes extends CommonGoalCard {
    @Override
    public int getPoints(Player p) {
        return 0;
    }

    @Override
    public boolean checkGoal(Player p) {
        return false;
    }
}
