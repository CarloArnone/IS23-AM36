package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.List;
import java.util.Optional;

public class TwoRowDiffTypes extends CommonGoalCard {

    //checked
    @Override
    public boolean checkGoal(Player p) {

        boolean firstMatchFound = false;
        Optional<ItemCard>[][] mat = p.getMyShelf().getShelf();

        for(int k = 0; k < mat[0].length - 1; k++) {
            for (int j = 0; j < mat.length - 2; j++) {
                if(k == mat[0].length) break;
                for(int i = j+1; i < mat.length - 1; i++) {
                    if(mat[j][k].isEmpty() || mat[i][k].isEmpty()){
                        k++;
                        j = 0;
                        break;
                    }
                    if(mat[j][k].get().getColor() == mat[i][k].get().getColor()){
                        k++;
                        j = 0;
                        break;
                    }
                    if(i == mat.length - 1 && j == mat.length - 2){
                        if(firstMatchFound) return true;
                        else firstMatchFound = true;
                    }
                }
            }
        }
        return false;
    }

    //PLACEHOLDER
    @Override
    public int getPoints(Player p, List<Integer> points) {
        return 0;
    }
}
