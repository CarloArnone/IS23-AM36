package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class TwoRowDiffTypes extends CommonGoalCard {

    //CHECKED
    @Override
    public boolean checkGoal(Player p) {

        boolean firstMatchFound = false;
        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();

        for(int k = 0; k < shelfCopy[0].length; k++) {
            for (int j = 0; j < shelfCopy.length - 1; j++) {
                if(k == shelfCopy[0].length) return false;
                for(int i = j+1; i < shelfCopy.length; i++) {
                    if(shelfCopy[j][k].isEmpty() || shelfCopy[i][k].isEmpty()){
                        k++;
                        j = 0;
                        break;
                    }
                    if(shelfCopy[j][k].get().getColor() == shelfCopy[i][k].get().getColor()){
                        k++;
                        j = 0;
                        break;
                    }
                    if(i == shelfCopy.length - 1 && j == shelfCopy.length - 2){
                        if(firstMatchFound) return true;
                        else firstMatchFound = true;
                    }
                }
            }
        }
        return false;
    }
}
