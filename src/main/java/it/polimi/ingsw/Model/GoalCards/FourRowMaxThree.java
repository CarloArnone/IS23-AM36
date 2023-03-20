package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class FourRowMaxThree extends CommonGoalCard {
    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        int ret = 0;

        for (Optional<ItemCard>[] row : shelfCopy) {
            if (distinctArrayOfN(row, 3)) ret++;
        }
        return ret >= 3;
    }
}
