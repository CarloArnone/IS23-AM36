package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

public class FourCorners extends CommonGoalCard {

    @Override
    public boolean checkGoal(Player p) {
        ItemCard[][] mat = p.getMyShelf().getShelf();
        if(mat[0][0] == null) return false;
        if(mat[0][0] != mat[0][mat[0].length - 1]) return false;
        if(mat[0][0] != mat[mat.length - 1][0]) return false;
        if(mat[0][0] != mat[mat.length - 1][mat[0].length - 1]) return false;
        return true;
    }

    @Override
    public int getPoints(Player p) {
        if(checkGoal(p)){
            setBonus(this.getBonus() - 2);
            return this.getBonus() + 2;
        }
        else return 0;
    }
}
