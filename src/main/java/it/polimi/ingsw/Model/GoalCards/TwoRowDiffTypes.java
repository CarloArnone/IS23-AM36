package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TwoRowDiffTypes extends CommonGoalCard {

    //CHECKED
    @Override
    public boolean checkGoal(Player p) {

        boolean firstMatchFound = false;
        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();

        for (Optional<ItemCard>[] optionals : shelfCopy) {
            if (checkRow(optionals)) {
                if (firstMatchFound) return true;
                firstMatchFound = true;
            }
        }
        return false;
    }

    private boolean checkRow(Optional<ItemCard>[] columnCopy){

        Set<Character> foundColors = new HashSet<>();

        for (Optional<ItemCard> itemCard : columnCopy) {
            if (itemCard.isEmpty()) return false;
            foundColors.add(itemCard.get().getColor());
        }
        return foundColors.size() == 5;
    }
}


/*
 * public boolean checkGoal(Player p) {
 *
 *         boolean firstMatchFound = false;
 *         Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
 *
 *         for(int k = 0; k < shelfCopy.length; k++) {
 *             for (int j = 0; j < shelfCopy[0].length - 1; j++) {
 *                 if(k == shelfCopy[0].length) return false;
 *                 for(int i = j+1; i < shelfCopy[0].length; i++) {
 *                     if(shelfCopy[k][j].isEmpty() || shelfCopy[k][i].isEmpty()){
 *                         k++;
 *                         j = 0;
 *                         break;
 *                     }
 *                     if(shelfCopy[k][j].get().getColor() == shelfCopy[k][i].get().getColor()){
 *                         k++;
 *                         j = 0;
 *                         break;
 *                     }
 *                     if(i == shelfCopy[0].length - 1 && j == shelfCopy[0].length - 2){
 *                         if(firstMatchFound) return true;
 *                         else firstMatchFound = true;
 *                     }
 *                 }
 *             }
 *         }
 *         return false;
 *     }
 */