package it.polimi.ingsw.Model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public boolean checkLine(int x, int y, int hInc, int vInc, int lineLength, int tileMatches, boolean full) {

        Set<Character> foundColors = new HashSet<>();
        int posX = x, posY = y;

        for(int ll = 0; ll < lineLength; ll++){
            //if(!full && shelf[posX][posY].isPresent()) return false; // Useful only when lineLength is very big.
            if(full && shelf[posX][posY].isEmpty()) return false;
            shelf[posX][posY].ifPresent(cell -> foundColors.add(cell.getColor()));
            posX += vInc;
            posY += hInc;
        }
        return foundColors.size() <= tileMatches;
    }
}