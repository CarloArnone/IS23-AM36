package it.polimi.ingsw.Model;

import java.util.List;

public class Shelf {
    private ItemCard[][] shelf;


    /** Returns the selected Shelf. */
    public ItemCard[][] getShelf() {
        return shelf;
    }

    /** . */
    public void onClickCol(List<ItemCard> pick, int col){

    }

    public int checkAdjacent(){
     return 0;
    }

    private void place(List<ItemCard> pick, int col){

    }
}
