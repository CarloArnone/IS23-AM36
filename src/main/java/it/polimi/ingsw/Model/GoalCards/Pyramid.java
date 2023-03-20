package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class Pyramid extends CommonGoalCard {
    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();

        if(checkPyramid(shelfCopy, 1)) return true;
        return checkPyramid(shelfCopy, -1);
    }

    private boolean checkPyramid(Optional<ItemCard>[][] shelfCopy, int side){

        int x = 0, y = 0, nCol = shelfCopy[0].length;

        if(side == -1) x = 4;

        while (y < nCol) {
            if(shelfCopy[x][y].isPresent() || shelfCopy[x+1][y].isEmpty()) return false;
            x += side;
            y++;
        }
        return true;
    }
}


