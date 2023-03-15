package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class Diagonals extends CommonGoalCard {

    //checked
    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] mat = p.getMyShelf().getShelf();

        for (int i = 0; i < mat.length - 1; i++) {
            if(mat[i][i].isEmpty() || mat[i + 1][i + 1].isEmpty()) break;
            if(mat[i][i].get().getColor() != mat[i + 1][i + 1].get().getColor()) break;
            if(i == mat.length - 2) return true;
        }

        for (int i = 0; i < mat.length - 1; i++) {
            if(mat[i][i+1].isEmpty() || mat[i+1][i+2].isEmpty()) break;
            if(mat[i][i+1].get().getColor() != mat[i+1][i+2].get().getColor()) break;
            if(i == mat.length - 2) return true;
        }

        for (int i = 0; i < mat.length - 1; i++) {
            if(mat[mat.length-i-1][i].isEmpty() || mat[mat.length-i-2][i+1].isEmpty()) break;
            if(mat[mat.length-i-1][i].get().getColor() != mat[mat.length-i-2][i+1].get().getColor()) break;
            if(i == mat.length - 2) return true;
        }

        for (int i = 0; i < mat.length - 1; i++) {
            if(mat[mat.length-i-1][i+1].isEmpty() || mat[mat.length-i-2][i+2].isEmpty()) break;
            if(mat[mat.length-i-1][i+1].get().getColor() != mat[mat.length-i-2][i+2].get().getColor()) break;
            if(i == mat.length - 2) return true;
        }
        return false;
    }

    @Override
    public int getPoints(Player p, List<Integer> points) {
        return 0;
    }
}
