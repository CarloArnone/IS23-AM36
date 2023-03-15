package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.CommonGoalCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;

public class SixCouples extends CommonGoalCard {

    private final ItemCard control = new ItemCard();


    /** First I check the columns one by one, up until the second to last Item Card and until I reach the second to last column.
     * For each Item Card I check if it's equal with the right one and the bottom one.
     * Then for the last column, I only check the bottom Item Card, because there is no right one.
     * Finally, I check the last row, confronting each Item Card only with its right one because there are none below.
     * The last Item Card, the one on the bottom right, has no Item Card under or to the right, so it's only confronted by its two adjacent one and doesn't need to check anything itself.
     * Each time I consider an Item Card, I firstly see if it's null. In that case I continue the loop.**/
    //checked
    @Override
    public boolean checkGoal(Player p){

        int ret = 0;
        Optional<ItemCard>[][] mat = p.getMyShelf().getShelf();
        for(int i = 0; i < mat.length - 1; i++){
            for(int j = 0; j < mat[0].length - 1; j++){
                if(mat[i][j].isEmpty()) continue;
                if(mat[i][j].get().getColor() == mat[i][j+1].orElse(control).getColor()){
                    mat[i][j] = empty();
                    mat[i][j+1] = empty();
                    ret++;
                    continue;
                }
                if(mat[i][j].get().getColor() == mat[i+1][j].orElse(control).getColor()){
                    mat[i][j] = empty();
                    mat[i+1][j] = empty();
                    ret++;
                }
            }
            if(mat[i][mat[0].length-1].isEmpty()) continue;
            if(mat[i][mat[0].length-1].get().getColor() == mat[i+1][mat[0].length-1].orElse(control).getColor()){
                mat[i][mat[0].length-1] = empty();
                mat[i+1][mat[0].length-1] = empty();
                ret++;
            }
        }

        for(int i = 0; i < mat.length - 1; i++){
            if(mat[mat.length-1][i].isEmpty()) continue;
            if(mat[mat.length-1][i].get().getColor() == mat[mat.length-1][i+1].orElse(control).getColor()){
                mat[mat.length-1][i] = empty();
                mat[mat.length-1][i+1] = empty();
                ret++;
            }
        }

        if(ret/6 == 0) return false;
        else if(ret/6 >= 1) return true;
        else return false; //TODO: consider this exception
    }

    //PLACEHOLDER
    @Override
    public int getPoints(Player p, List<Integer> points) {
        return 0;
    }
}
