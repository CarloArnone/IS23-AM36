package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

import static java.util.Optional.empty;

public class SixCouples extends CommonGoalCard {

    /** First I check the columns one by one, up until the second to last Item Card and until I reach the second to last column.
     * For each Item Card I check if it's equal with the right one and the bottom one.
     * Then for the last column, I only check the bottom Item Card, because there is no right one.
     * Finally, I check the last row, confronting each Item Card only with its right one because there are none below.
     * The last Item Card, the one on the bottom right, has no Item Card under or to the right, so it's only confronted by its two adjacent one and doesn't need to check anything itself.
     * Each time I consider an Item Card, I firstly see if it's null. In that case I continue the loop.**/

    //CHECKED
    @Override
    public boolean checkGoal(Player p){

        int ret = 0;
        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        int nCol = shelfCopy.length, nRow = shelfCopy[0].length;

        for(int i = 0; i < nCol - 1; i++){
            for(int j = 0; j < nRow - 1; j++){
                if(checkPair(shelfCopy[i][j], shelfCopy[i][j+1])) {
                    shelfCopy[i][j] = empty();
                    shelfCopy[i][j+1] = empty();
                    ret++;
                }
                if(checkPair(shelfCopy[i][j], shelfCopy[i+1][j])) {
                    shelfCopy[i][j] = empty();
                    shelfCopy[i+1][j] = empty();
                    ret++;
                }
            }
            if(checkPair(shelfCopy[i][nRow-1], shelfCopy[i+1][nRow-1])){
                shelfCopy[i][nRow-1] = empty();
                shelfCopy[i+1][nRow-1] = empty();
                ret++;
            }
        }

        for(int i = 0; i < nRow - 1; i++){
            if(checkPair(shelfCopy[nCol-1][i], shelfCopy[nCol-1][i+1])){
                shelfCopy[nCol-1][i] = empty();
                shelfCopy[nCol-1][i+1] = empty();
                ret++;
            }
        }

        if(ret/6 == 0) return false;
        else if(ret/6 == 1) return true;
        else return false; //TODO: consider this exception?
    }

    private boolean checkPair(Optional<ItemCard> x, Optional<ItemCard> y){

        if(x.isEmpty() || y.isEmpty()) return false;
        return x.get().getColor() == y.get().getColor();
    }
}


/**
 * @Override
 *     public boolean checkGoal(Player p){
 *
 *         int ret = 0;
 *         Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
 *         for(int i = 0; i < shelfCopy.length - 1; i++){
 *             for(int j = 0; j < shelfCopy[0].length - 1; j++){
 *                 if(shelfCopy[i][j].isEmpty()) continue;
 *                 if(shelfCopy[i][j].get().getColor() == shelfCopy[i][j+1].orElse(control).getColor()){
 *                     shelfCopy[i][j] = empty();
 *                     shelfCopy[i][j+1] = empty();
 *                     ret++;
 *                     continue;
 *                 }
 *                 if(shelfCopy[i][j].get().getColor() == shelfCopy[i+1][j].orElse(control).getColor()){
 *                     shelfCopy[i][j] = empty();
 *                     shelfCopy[i+1][j] = empty();
 *                     ret++;
 *                 }
 *             }
 *             if(shelfCopy[i][shelfCopy[0].length-1].isEmpty()) continue;
 *             if(shelfCopy[i][shelfCopy[0].length-1].get().getColor() == shelfCopy[i+1][shelfCopy[0].length-1].orElse(control).getColor()){
 *                 shelfCopy[i][shelfCopy[0].length-1] = empty();
 *                 shelfCopy[i+1][shelfCopy[0].length-1] = empty();
 *                 ret++;
 *             }
 *         }
 *
 *         for(int i = 0; i < shelfCopy.length - 1; i++){
 *             if(shelfCopy[shelfCopy.length-1][i].isEmpty()) continue;
 *             if(shelfCopy[shelfCopy.length-1][i].get().getColor() == shelfCopy[shelfCopy.length-1][i+1].orElse(control).getColor()){
 *                 shelfCopy[shelfCopy.length-1][i] = empty();
 *                 shelfCopy[shelfCopy.length-1][i+1] = empty();
 *                 ret++;
 *             }
 *         }
 *
 *         if(ret/6 == 0) return false;
 *         else if(ret/6 == 1) return true;
 *         else return false; //TODO: consider this exception?
 *     }
 */