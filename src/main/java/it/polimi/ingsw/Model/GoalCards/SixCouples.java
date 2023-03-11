package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.CommonGoalCard;

public class SixCouples extends CommonGoalCard {

    private int points = 8;


    /** First I check the columns one by one, up until the second to last Item Card and until I reach the second to last column.
     * For each Item Card I check if it's equal with the right one and the bottom one.
     * Then for the last column, I only check the bottom Item Card, because there is no right one.
     * Finally, I check the last row, confronting each Item Card only with its right one because there are none below.
     * The last Item Card, the one on the bottom right, has no Item Card under or to the right, so it's only confronted by its two adjacent one and doesn't need to check anything itself.
     * Each time I consider an Item Card, I firstly see if it's null. In that case I continue the loop.**/
    @Override
    public boolean checkGoal(Player p){

        int ret = 0;
        ItemCard[][] mat = p.getMyShelf().getShelf();
        for(int i = 0; i < mat.length - 1; i++){
            for(int j = 0; j < mat[0].length - 1; j++){
                if(mat[i][j] == null) continue;
                if(mat[i][j] == mat[i][j + 1]) ret++;
                if(mat[i][j] == mat[i+1][j]) ret++;
            }
            if(mat[i][mat[0].length - 1] == null) continue;
            if(mat[i][mat[0].length - 1] == mat[i + 1][mat[0].length - 1]) ret++;
        }

        for(int i = 0; i < mat.length - 1; i++){
            if(mat[mat.length - 1][i] == null) continue;
            if(mat[mat.length - 1][i] == mat[mat.length - 1][i + 1]) ret++;
        }

        if(ret/6 == 0) return false;
        else if(ret/6 == 1) return true;
        else return false; //consider this exception
    }

    @Override
    public int getPoints(Player p) {
        if(checkGoal(p) && this.points >= 4) return this.points;
        else return 0;
    }
}
