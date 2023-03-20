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

    /**
     * When Called the function call a recursive algorithm to calculate the points made in the lib.
     * @return the total of points accumulated by the player possessing the shelf.
     */
    public int getPointsForAdjacent(){

        int points = 0;
        ItemCard falseMatch = new ItemCard('Z', "falseMatch");

        //Initialization of alreadySeen matrix useful to the optimality of the adjacent algorithm.
        boolean[][] alreadySeen = new boolean[6][5];
        for(int r = 0; r < shelf.length; r++){
            for(int c = 0; c < shelf[0].length; c++){
                //Initialize true because if the cell is empty, because I don't want to check anything on it
                alreadySeen[r][c] = shelf[r][c].isEmpty();
            }
        }


        for(int r = 0; r < shelf.length; r++){
            for(int c = 0; c < shelf[0].length; c++){
                if(!alreadySeen[r][c]){
                    int adjacentCards = hasAdiacent(r, c, shelf[r][c].orElse(falseMatch).getColor(), alreadySeen, falseMatch);
                    if(adjacentCards == 3){
                        points += 2;
                    }
                    else if(adjacentCards == 4){
                        points += 3;
                    }
                    else if(adjacentCards == 5){
                        points += 5;
                    }
                    else if(adjacentCards >= 6){
                        points += 8;
                    }
                }
            }
        }

        return points;
    }

    /**
     *  The function applies a common color visit of the matrix.
     *
     * @param i Y coordinate of the masterTile
     * @param j X coordinate of the masterTile
     * @param color Color of the master Tile
     * @param alreadySeen matrix to check whether I already checked a cell
     * @param falseMatch ItemCard useful for matching and security of the code.
     * @return number of the element of the group of the tile in position (i, j)
     */
    public int hasAdiacent(int i, int j, char color, boolean[][] alreadySeen, ItemCard falseMatch){
        if(alreadySeen[i][j] || color != shelf[i][j].orElse(falseMatch).getColor()){
            return 0;
        }

        if(i == 0 && j == 0){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i + 1, j, color, alreadySeen, falseMatch)/* Down */ + hasAdiacent(i, j + 1, color, alreadySeen, falseMatch)/* Right */;
        }
        else if(i == shelf.length -1 && j == 0){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i - 1, j, color, alreadySeen, falseMatch) /* Up */ + hasAdiacent(i, j + 1, color, alreadySeen, falseMatch)/* Right */;
        }
        else if(i == 0 && j == shelf[0].length){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i + 1, j, color, alreadySeen, falseMatch) /* Down */ + hasAdiacent(i, j - 1, color, alreadySeen, falseMatch)/* Left */;
        }
        else if(i == shelf.length && j == shelf[0].length){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i - 1, j, color, alreadySeen, falseMatch) /* Up */ + hasAdiacent(i, j - 1, color, alreadySeen, falseMatch)/* Left */;
        }
        else if(i == 0){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i + 1, j, color, alreadySeen, falseMatch)/* Down */ +
                       hasAdiacent(i, j + 1, color, alreadySeen, falseMatch)/* Right */ +
                       hasAdiacent(i, j - 1, color, alreadySeen, falseMatch)/* Left */;
        }
        else if(i == shelf.length -1){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i - 1, j, color, alreadySeen, falseMatch) /* Up */ +
                       hasAdiacent(i, j + 1, color, alreadySeen, falseMatch)/* Right */ +
                       hasAdiacent(i, j - 1, color, alreadySeen, falseMatch)/* Left */;
        }
        else if(j == 0){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i - 1, j, color, alreadySeen, falseMatch) /* Up */ +
                       hasAdiacent(i + 1, j, color, alreadySeen, falseMatch)/* Down */ +
                       hasAdiacent(i, j + 1, color, alreadySeen, falseMatch)/* Right */;
        }
        else if(j == shelf[0].length - 1){
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i - 1, j, color, alreadySeen, falseMatch) /* Up */ +
                       hasAdiacent(i + 1, j, color, alreadySeen, falseMatch)/* Down */ +
                       hasAdiacent(i, j - 1, color, alreadySeen, falseMatch)/* Left */;
        }
        else {
            alreadySeen[i][j] = true;
            return 1 + hasAdiacent(i - 1, j, color, alreadySeen, falseMatch) /* Up */ +
                       hasAdiacent(i + 1, j, color, alreadySeen, falseMatch)/* Down */ +
                       hasAdiacent(i, j + 1, color, alreadySeen, falseMatch)/* Right */ +
                       hasAdiacent(i, j - 1, color, alreadySeen, falseMatch)/* Left */;
        }

    }

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

