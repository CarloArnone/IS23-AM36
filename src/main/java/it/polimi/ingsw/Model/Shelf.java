package it.polimi.ingsw.Model;

import Exceptions.NotEnoughSpacesInCol;

import java.util.ArrayList;
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
        if(remainingSpacesOnCol(col) < pick.size() || col < 0 || col > 4){
            throw new NotEnoughSpacesInCol();
        }

        place(pick, col);

    }

    /**
     *  compute how many spaces are left in a col
     * @param col -- the col to check
     * @return the number of spaces left in a col - COL
     */
    public int remainingSpacesOnCol(int col) {
        int posY = 0;

        while(shelf[posY][col].isEmpty()){
            posY ++;
            if(posY == shelf.length){
                break;
            }
        }

        return posY;
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
        int posY = remainingSpacesOnCol(col);
        posY = posY -1;


        for(ItemCard card : pick){
            shelf[posY][col] = Optional.of(card);   //I START FROM THE BOTTOM AND INSERT THE CARDS IN THE ORDER THE CONTROLLER GAVE ME.
            posY --;
        }

    }

    /**
     * Compute the sequence of selectable cols
     * @param pickSize - size of the player drawnCards
     * @return a list containing if the col in pos i is selectable or not.
     */
    public List<Boolean>  getSelectableCols(int pickSize){
        List<Boolean> toReturn = new ArrayList<>();
        for(int i = 0; i < shelf[0].length; i++){
                System.out.println("On col " + i + " are " + remainingSpacesOnCol(i) + " spaces left");
               if(remainingSpacesOnCol(i) < pickSize){
                   toReturn.add(i, false);
               }
               else toReturn.add(i, true);
        }

        System.out.println(toReturn);
        return toReturn;

    }
}
