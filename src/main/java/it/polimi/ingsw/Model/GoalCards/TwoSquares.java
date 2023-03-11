package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

public class TwoSquares extends CommonGoalCard {

    private int points = 8;


    /** I check each cell one by one, up until the second to last column and row.
     * For each cell I check if it's equal with the right one and the bottom one and the diagonal one.
     * If I find one discrepancy, I discard that set and continue.
     * Once I find a suitable square of cells, I store the value of their color and the column in which i find it + 1.
     *
     * In case there is already a color stored in the temp variable, it means I had already found a square.
     * That means I have to firstly check if the colors match and then if the colum and row that I stored are the same as the ones of the cell I'm checking.
     * If that is the case, I have to ignore this square, because it is formed by two cells that also form the fist square.
     *
     * Example:
     * │ 1 │ 2 │ 1 │ - │ - │
     * │ 1 │ 2 │ 1 │ - │ - │
     * │ - │ - │ - │ 1 │ 1 │
     * │ - │ - │ - │ 2 │ 2 │
     * │ - │ - │ - │ 1 │ 1 │
     * │ - │ - │ - │ - │ - │
     *
     * The cells marked with a '2' would be the ones considered 2 times. **/
    @Override
    public boolean checkGoal(Player p) {

        int control[] = {0, 0};
        ItemCard[][] mat = p.getMyShelf().getShelf();
        char temp = 'z';

        for(int i = 0; i < mat.length - 1; i++){
            for(int j = 0; j < mat[0].length - 1; j++) {
                if (mat[i][j] == null) continue;
                if (mat[i][j].getColor() != mat[i][j + 1].getColor()) continue;
                if (mat[i][j].getColor() != mat[i + 1][j].getColor()) continue;
                if (mat[i][j].getColor() != mat[i + 1][j + 1].getColor()) continue;

                if(temp == 'z') {
                    temp = mat[i][j].getColor();
                    control[0] = i + 1;
                    control[1] = j + 1;
                }  else if(control[0] != i || control[1] != j) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getPoints(Player p) {
        if(checkGoal(p) && this.points >= 4) {
            this.points -= 2;
            return this.points + 2;
        }
        else return 0;
    }
}
