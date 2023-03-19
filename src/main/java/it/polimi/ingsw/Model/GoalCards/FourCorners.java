package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class FourCorners extends CommonGoalCard {

    //CHECKED
    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        int nCol = shelfCopy.length, nRow = shelfCopy[0].length;
        char topLeftTile;

        if(shelfCopy[0][0].isEmpty()) return false;

        topLeftTile = shelfCopy[0][0].get().getColor();

        if(shelfCopy[0][nRow-1].isEmpty() || shelfCopy[nCol-1][0].isEmpty() || shelfCopy[nCol-1][nRow-1].isEmpty()) return false;
        if(topLeftTile != shelfCopy[0][nRow-1].get().getColor()) return false;
        if(topLeftTile != shelfCopy[nCol-1][0].get().getColor()) return false;
        return topLeftTile == shelfCopy[nCol - 1][nRow - 1].get().getColor();
    }
}
