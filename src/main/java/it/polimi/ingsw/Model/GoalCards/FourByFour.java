package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class FourByFour extends CommonGoalCard {

    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        int ret = 0, nRow = shelfCopy.length, nCol = shelfCopy[0].length;



        return false;
    }

    private boolean checkFour(Optional<ItemCard>[][] shelfCopy){



        return false;
    }
}
