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

    private boolean[][] getFalseMatrix(int x, int y){
        boolean[][] mat = new boolean[x][y];
        while (x >= 0) {
            x--;
            while (y >= 0) {
                y--;
                mat[x][y] = false;
            }
        }
        return mat;
    }

    private Optional<ItemCard>[][] checkAdjacent(Optional<ItemCard>[][] shelfCopy, int counter, boolean[][] alreadySeen){



        return shelfCopy;
    }
}
