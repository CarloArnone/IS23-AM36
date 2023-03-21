package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.CommonGoalCard;
import it.polimi.ingsw.Model.ItemCard;
import it.polimi.ingsw.Model.Player;

import java.util.Optional;

import static java.util.Optional.empty;

public class FourByFour extends CommonGoalCard {

    @Override
    public boolean checkGoal(Player p) {

        Optional<ItemCard>[][] shelfCopy = p.getMyShelf().getShelf();
        int ret = 0, nRow = shelfCopy.length, nCol = shelfCopy[0].length;



        return false;
    }

    private Optional<ItemCard>[][] checkFour(Optional<ItemCard>[][] shelfCopy, int x, int y, int nRow, int nCol){

        char control = shelfCopy[x][y].get().getColor();
        shelfCopy[x][y] = empty();
        int counter = 1;
        boolean searching = true;

        while(searching){
            if(x != nRow - 1 && shelfCopy[x + 1][y].isPresent()){
                if(shelfCopy[x + 1][y].get().getColor() == control){
                    counter++;
                    x++;
                    shelfCopy[x][y] = empty();
                }
            }
        }

        return shelfCopy;
    }
}
