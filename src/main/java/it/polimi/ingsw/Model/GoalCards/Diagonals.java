package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class Diagonals extends CommonGoalCard {

    //CHECKED
    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();

        for (int i = 0; i < shelfCopy.length - 1; i++) {
            if(shelfCopy[i][i].isEmpty() || shelfCopy[i + 1][i + 1].isEmpty()) break;
            if(shelfCopy[i][i].get().getColor() != shelfCopy[i + 1][i + 1].get().getColor()) break;
            if(i == shelfCopy.length - 2) return true;
        }

        for (int i = 0; i < shelfCopy.length - 1; i++) {
            if(shelfCopy[i][i+1].isEmpty() || shelfCopy[i+1][i+2].isEmpty()) break;
            if(shelfCopy[i][i+1].get().getColor() != shelfCopy[i+1][i+2].get().getColor()) break;
            if(i == shelfCopy.length - 2) return true;
        }

        for (int i = 0; i < shelfCopy.length - 1; i++) {
            if(shelfCopy[shelfCopy.length-i-1][i].isEmpty() || shelfCopy[shelfCopy.length-i-2][i+1].isEmpty()) break;
            if(shelfCopy[shelfCopy.length-i-1][i].get().getColor() != shelfCopy[shelfCopy.length-i-2][i+1].get().getColor()) break;
            if(i == shelfCopy.length - 2) return true;
        }

        for (int i = 0; i < shelfCopy.length - 1; i++) {
            if(shelfCopy[shelfCopy.length-i-1][i+1].isEmpty() || shelfCopy[shelfCopy.length-i-2][i+2].isEmpty()) break;
            if(shelfCopy[shelfCopy.length-i-1][i+1].get().getColor() != shelfCopy[shelfCopy.length-i-2][i+2].get().getColor()) break;
            if(i == shelfCopy.length - 2) return true;
        }
        return false;
    }
}
