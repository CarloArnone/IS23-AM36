package it.polimi.ingsw.Model;

import java.util.List;
import java.util.Optional;

public class Shelf {

    private Optional<ItemCard>[][] shelf;

    /** Returns the selected Shelf. */
    public Optional<ItemCard>[][] getShelf() {
        return shelf;
    }

    /** . */
    public void onClickCol(List<ItemCard> pick, int col){

    }

    public int getPointsForAdjacent(){
     return 0;
    }

    private void place(List<ItemCard> pick, int col){

    }
}
