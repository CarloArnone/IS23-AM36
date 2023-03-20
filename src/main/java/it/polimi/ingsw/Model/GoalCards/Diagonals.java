package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class Diagonals extends CommonGoalCard {

    //CHECKED
    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        int nCol = shelfCopy[0].length, nRow = shelfCopy.length;

        if(shelfCopy[0][0].isPresent()){
            if(checkDiagonal(shelfCopy, 0, 0, nCol, 1)) return true;
        }

        if(shelfCopy[0][1].isPresent()){
            if(checkDiagonal(shelfCopy, 1, 0, nCol, 1)) return true;
        }

        if(shelfCopy[nRow - 2][0].isPresent()){
            if(checkDiagonal(shelfCopy, nRow - 2, 0, nCol, -1)) return true;
        }

        if(shelfCopy[nRow - 1][0].isPresent()){
            if(checkDiagonal(shelfCopy, nRow - 1, 0, nCol, -1)) return true;
        }
        return false;
    }

    private boolean checkDiagonal(Optional<ItemCard>[][] shelfCopy, int x, int y, int nCol, int side){

        char control = shelfCopy[x][y].get().getColor();

        while (y < nCol - 1) {
            x += side;
            y++;
            if(shelfCopy[x][y].isEmpty()) return false;
            if(control != shelfCopy[x][y].get().getColor()) return false;
        }
        return true;
    }

}


/*
 * public boolean checkGoal(Player p) {
 *
 *         Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
 *         int nCol = shelfCopy.length;
 *
 *         for (int i = 0; i < nCol - 1; i++) {
 *             if(shelfCopy[i][i].isEmpty() || shelfCopy[i+1][i+1].isEmpty()) break;
 *             if(shelfCopy[i][i].get().getColor() != shelfCopy[i+1][i+1].get().getColor()) break;
 *             if(i == nCol - 2) return true;
 *         }
 *
 *         for (int i = 0; i < nCol - 1; i++) {
 *             if(shelfCopy[i][i+1].isEmpty() || shelfCopy[i+1][i+2].isEmpty()) break;
 *             if(shelfCopy[i][i+1].get().getColor() != shelfCopy[i+1][i+2].get().getColor()) break;
 *             if(i == nCol - 2) return true;
 *         }
 *
 *         for (int i = 0; i < nCol - 1; i++) {
 *             if(shelfCopy[nCol-i-1][i].isEmpty() || shelfCopy[nCol-i-2][i+1].isEmpty()) break;
 *             if(shelfCopy[nCol-i-1][i].get().getColor() != shelfCopy[nCol-i-2][i+1].get().getColor()) break;
 *             if(i == nCol - 2) return true;
 *         }
 *
 *         for (int i = 0; i < nCol - 1; i++) {
 *             if(shelfCopy[nCol-i-1][i+1].isEmpty() || shelfCopy[nCol-i-2][i+2].isEmpty()) break;
 *             if(shelfCopy[nCol-i-1][i+1].get().getColor() != shelfCopy[nCol-i-2][i+2].get().getColor()) break;
 *             if(i == nCol - 2) return true;
 *         }
 *         return false;
 *     }
 */