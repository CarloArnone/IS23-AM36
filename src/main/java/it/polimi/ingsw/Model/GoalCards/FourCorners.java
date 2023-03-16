package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class FourCorners extends CommonGoalCard {

    public FourCorners(List<Integer> points) {
        super(points);
    }

    //CHECKED
    @Override
    public boolean checkGoal(Player p) {
        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        if(shelfCopy[0][0].isEmpty() || shelfCopy[0][shelfCopy[0].length-1].isEmpty() || shelfCopy[shelfCopy.length-1][0].isEmpty() || shelfCopy[shelfCopy.length-1][shelfCopy[0].length-1].isEmpty()) return false;
        if(shelfCopy[0][0].get().getColor() != shelfCopy[0][shelfCopy[0].length-1].get().getColor()) return false;
        if(shelfCopy[0][0].get().getColor() != shelfCopy[shelfCopy.length-1][0].get().getColor()) return false;
        if(shelfCopy[0][0].get().getColor() != shelfCopy[shelfCopy.length-1][shelfCopy[0].length-1].get().getColor()) return false;
        return true;
    }
}
