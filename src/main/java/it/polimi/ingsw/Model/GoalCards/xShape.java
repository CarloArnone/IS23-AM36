package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class xShape extends CommonGoalCard {

    //CHECKED
    @Override
    public boolean checkGoal(Player p){

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();

        for(int i = 1; i < shelfCopy.length - 1; i++){
            for (int j = 1; j < shelfCopy[0].length - 1; j++) {
                if (shelfCopy[i][j].isPresent() && checkXShape(shelfCopy, i, j)) return true;
            }
        }
        return false;
    }

    private boolean checkXShape(Optional<ItemCard>[][] shelfCopy, int x, int y){

        char control;

        if (shelfCopy[x + 1][y + 1].isEmpty() || shelfCopy[x + 1][y - 1].isEmpty() || shelfCopy[x - 1][y - 1].isEmpty() || shelfCopy[x - 1][y + 1].isEmpty()) return false;

        control = shelfCopy[x][y].get().getColor();

        if (control != shelfCopy[x + 1][y + 1].get().getColor()) return false;
        if (control != shelfCopy[x + 1][y - 1].get().getColor()) return false;
        if (control != shelfCopy[x - 1][y - 1].get().getColor()) return false;
        return control == shelfCopy[x - 1][y + 1].get().getColor();
    }
}


/*
 * @Override
 *     public boolean checkGoal(Player p){
 *
 *         Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
 *
 *         for(int i = 1; i < shelfCopy[0].length - 1; i++){
 *             for (int j = 1; j < shelfCopy.length - 1; j++) {
 *                 if (shelfCopy[i][j].isEmpty() || shelfCopy[i + 1][j + 1].isEmpty() || shelfCopy[i + 1][j - 1].isEmpty() || shelfCopy[i - 1][j - 1].isEmpty() || shelfCopy[i - 1][j + 1].isEmpty()) continue;
 *                 if (shelfCopy[i][j].get().getColor() != shelfCopy[i + 1][j + 1].get().getColor()) continue;
 *                 if (shelfCopy[i][j].get().getColor() != shelfCopy[i + 1][j - 1].get().getColor()) continue;
 *                 if (shelfCopy[i][j].get().getColor() != shelfCopy[i - 1][j - 1].get().getColor()) continue;
 *                 if (shelfCopy[i][j].get().getColor() != shelfCopy[i - 1][j + 1].get().getColor()) continue;
 *                 return true;
 *             }
 *         }
 *         return false;
 *
 */