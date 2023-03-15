package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class FourCorners extends CommonGoalCard {

    //checked
    @Override
    public boolean checkGoal(Player p) {
        Optional<ItemCard>[][] mat = p.getMyShelf().getShelf();
        if(mat[0][0].isEmpty() || mat[0][mat[0].length-1].isEmpty() || mat[mat.length-1][0].isEmpty() || mat[mat.length-1][mat[0].length-1].isEmpty()) return false;
        if(mat[0][0].get().getColor() != mat[0][mat[0].length-1].get().getColor()) return false;
        if(mat[0][0].get().getColor() != mat[mat.length-1][0].get().getColor()) return false;
        if(mat[0][0].get().getColor() != mat[mat.length-1][mat[0].length-1].get().getColor()) return false;
        return true;
    }

    //PLACEHOLDER
    @Override
    public int getPoints(Player p, List<Integer> points) {
        return 0;
    }
}
