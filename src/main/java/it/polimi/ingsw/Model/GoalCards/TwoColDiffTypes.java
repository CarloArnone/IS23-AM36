package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class TwoColDiffTypes extends CommonGoalCard {

    @Override
    public boolean checkGoal(Player p) {

        boolean firstMatchFound = false;
        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();

        for (int i = 0; i <= shelfCopy.length - 1; i++) {
            for (int j = 0; j < shelfCopy[0].length - 1; j++) {
                if(shelfCopy[i][j].isEmpty() || shelfCopy[i][j + 1].isEmpty()) break;
                if (shelfCopy[i][j].get().getColor() == shelfCopy[i][j + 1].get().getColor()) break;
                if (j == shelfCopy[0].length - 2){
                    if(!firstMatchFound) firstMatchFound = true;
                    else return true;
                }
            }
        }
        return false;
    }


    @Override
    public int getPoints(Player p, List<Integer> points) {
        return 0;
    }
}
