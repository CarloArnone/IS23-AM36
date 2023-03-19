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
        int nCol = shelfCopy[0].length, nRow = shelfCopy.length;
        char topLeftTile;

        if(shelfCopy[0][0].isEmpty()) return false;

        topLeftTile = shelfCopy[0][0].get().getColor();

        if(shelfCopy[nRow-1][0].isEmpty() || shelfCopy[0][nCol-1].isEmpty() || shelfCopy[nRow-1][nCol-1].isEmpty()) return false;
        if(topLeftTile != shelfCopy[nRow-1][0].get().getColor()) return false;
        if(topLeftTile != shelfCopy[0][nCol-1].get().getColor()) return false;
        return topLeftTile == shelfCopy[nRow - 1][nCol - 1].get().getColor();
    }
}
