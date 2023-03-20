package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

public class EightEquals extends CommonGoalCard {


    @Override
    public boolean checkGoal(Player p) {


        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        int[] matchCounter = {0, 0, 0, 0, 0, 0};
        int nCol = shelfCopy[0].length;

        for (Optional<ItemCard>[] optionals : shelfCopy) {
            for (int j = 0; j < nCol; j++) {

                if (optionals[j].isEmpty()) continue;

                switch (optionals[j].get().getColor()) {
                    case 'b':
                        matchCounter[0]++;
                    case 'c':
                        matchCounter[1]++;
                    case 'g':
                        matchCounter[2]++;
                    case 'p':
                        matchCounter[3]++;
                    case 'w':
                        matchCounter[4]++;
                    case 'y':
                        matchCounter[5]++;
                    default:
                        System.out.println("Error: color found = " + optionals[j].get().getColor()); //TODO: make this an exception?
                }
            }
        }

        for (int j : matchCounter) {
            if (j == 8) return true;
        }
        return false;
    }
}

/*
 * public boolean checkGoal(Player p) {
 *
 *         int matchCounter = 0;
 *         Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
 *
 *         for (int i = 0; i < shelfCopy.length; i++) {
 *             for (int j = 0; j < shelfCopy[0].length; j++) {
 *
 *                 if (shelfCopy[i][j].isEmpty()) continue;
 *
 *                 for (int k = 0; k < shelfCopy.length; k++) {
 *                     for (int t = 0; t < shelfCopy[0].length; t++) {
 *
 *                         if (shelfCopy[k][t].isEmpty()) continue;
 *
 *                         if (shelfCopy[i][j].get().getColor() == shelfCopy[k][t].get().getColor()) {
 *                             matchCounter++;
 *                             if (matchCounter == 7) return true;
 *                         }
 *                     }
 *                 }
 *
 *                 for (int k = 0; k < shelfCopy.length; k++) {
 *                     for (int t = 0; t < shelfCopy[0].length; t++) {
 *
 *                         if (shelfCopy[k][t].isEmpty()) continue;
 *
 *                         if (shelfCopy[i][j].get().getColor() == shelfCopy[k][t].get().getColor()) {
 *                             shelfCopy[k][t] = empty();
 *                         }
 *                     }
 *                 }
 *
 *             }
 *         }
 *         return false;
 *     }
 */
