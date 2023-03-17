package it.polimi.ingsw.Model;

import Exceptions.NotEnoughSpacesInCol;

import java.util.List;
import java.util.Optional;

public class Shelf {

    private Optional<ItemCard>[][] shelf;

    public Shelf(Optional<ItemCard>[][] shelf) {
        this.shelf = shelf;
    }

    /** Returns the selected Shelf. */
    public Optional<ItemCard>[][] getShelf() {
        return shelf;
    }

    /**
     * Interface to place a pick into the col -- just makes some controls and then calls the place method.
     * @param pick - should be the players drawnCards
     * @param col - the column that should hold the pick
     * @throws NotEnoughSpacesInCol in case the number of ItemCards is bigger then the remaining spaces inside the col
     */
    public void onClickCol(List<ItemCard> pick, int col) throws NotEnoughSpacesInCol{
        if(remainingSpacesOnCol(col) < pick.size()){
            throw new NotEnoughSpacesInCol();
        }

        place(pick, col);

    }

    /**
     *  compute how many spaces are left in a col
     * @param col -- the col to check
     * @return the number of spaces left in a col - COL
     */
    private int remainingSpacesOnCol(int col) {
        int posY = shelf[0].length;
        int spaceLeft = 0;
        while(shelf[col][posY].isEmpty()){
            if(posY == 0){ //I'M ARRIVED TO THE BOTTOM OF THE COL.
                break;
            }
            posY --;
            spaceLeft ++;
        }

        return spaceLeft;
    }

    public int getPointsForAdjacent(){
     return 0;
    } //TODO :  REVIEW

    /**
     * Actually place the Cards inside the shelf
     * @param pick -- player drawn cards
     * @param col -- col to place in.
     */
    private void place(List<ItemCard> pick, int col){
        int posY = shelf[0].length;
        while(shelf[col][posY].isEmpty()){
            if(posY == 0){ //I'M ARRIVED TO THE BOTTOM OF THE COL.
                break;
            }
            posY --;
        }

        for(ItemCard card : pick){
            shelf[col][posY] = Optional.of(card);   //I START FROM THE BOTTOM AND INSERT THE CARDS IN THE ORDER THE CONTROLLER GAVE ME.
            posY ++;
        }
    }
}
