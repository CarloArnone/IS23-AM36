package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.ItemCard;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.Math.abs;


public class GeneralMethods {

    /**
     *
     * @param shelfCopy Copy of the shelf to analyze.
     * @param transpose False to check rows, true to check columns.
     * @param arrayMatches Number of arrays that must meet the parameters to return true.
     * @param tileMatches Maximum number of tiles that can be different in each array to be considered a match. Set to 0 to check for all different tiles.
     * @return Returns true if at least "arrayMatches" arrays meets the requirement specified in "tileMatches".
     */
    public boolean checkLines(Optional<ItemCard>[][] shelfCopy, boolean transpose, int arrayMatches, int tileMatches){

        int ret = 0, counter;

        if(transpose) shelfCopy = transposeMatrix(shelfCopy);

        for (Optional<ItemCard>[] row : shelfCopy) {
            counter = fullArrayOf(row);
            if (counter != 0 && counter <= tileMatches) ret++;
            else if (counter != 0 && tileMatches == 0 && counter == row.length) ret++;
        }
        return ret >= arrayMatches;
    }

    /**
     *
     * @param vector Array to check.
     * @return Returns how many colors of tiles are found in the array. Returns 0 if any of the tiles is empty.
     */
    public int fullArrayOf(Optional<ItemCard>[] vector){

        Set<Character> foundColors = new HashSet<>();

        for (Optional<ItemCard> itemCard : vector) {
            if (itemCard.isEmpty()) return 0;
            foundColors.add(itemCard.get().getColor());
        }
        return foundColors.size();
    }

    /**
     *
     * @param vector Array of optionals to check.
     * @param full Set to 0 to check if vector is completely empty, set to 1 to check if vector is completely full.
     * @return Returns true if the array of optionals is either full or empty (depending on "full"), false otherwise.
     */
    public boolean fullOrEmpty(Optional<ItemCard>[] vector, boolean full){

        for (Optional<ItemCard> itemCard : vector) {
            if (full && itemCard.isEmpty()) return false;
            if(!full && itemCard.isPresent()) return false;
        }
        return true;
    }

    /**
     *
     * @param shelf 2D array of optionals to transpose.
     * @return Returns a new 2D array of optionals that has been transposed.
     */

    public Optional<ItemCard>[][] transposeMatrix(Optional<ItemCard>[][] shelf){

        int nCol = shelf[0].length, nRow = shelf.length;
        Optional<ItemCard>[][] transposedShelf = new Optional[nCol][nRow];

        for(int i = 0; i < nCol; i++){
            for(int j = 0; j < nRow; j++){
                transposedShelf[i][j] = Optional.of(shelf[j][i].get());
            }
        }
        return transposedShelf;
    }

    /**
     *
     * @param shelfCopy 2D array of optionals from which are extrapolated the diagonals.
     * @return Returns a 2D array of optionals where each line is a diagonal of "shelfCopy".
     * The order in the return matrix is always TLBR (top-left to bottom-right) diagonals first.
     * The diagonals are always read left to right from "shelfCopy".
     * If "shelfCopy" is TALL, you get the first TLBR diagonal (starts in [0][0]) and then the ones under it. Then the last BLTR (starts in [nRow - 1][0]) and then the ones over it.
     * If "shelfCopy" is WIDE, you get the first TLBR diagonal (starts in [0][0]) and then the ones to the right of it. Then the same for the first BLTR (starts in [nRow - 1][0]).
     */
    public Optional<ItemCard>[][] getDiagonalsMatrix(Optional<ItemCard>[][] shelfCopy){

        int nCol = shelfCopy[0].length, nRow = shelfCopy.length, nDiag = 2 + abs(nRow - nCol)*2;
        Optional<ItemCard>[][] diagonals = new Optional[nDiag][nCol];

        if(nRow > nCol) {
            nDiag = 2 + (nRow - nCol)*2;
            for(int i = 0; i < nDiag/2; i++) diagonals[i] = getDiagonal(shelfCopy, i, 0, nCol, 1);
            for(int i = 0; i < nDiag/2; i++) diagonals[i] = getDiagonal(shelfCopy, nRow - 1 - i, 0, nCol, -1);
        } else if(nCol > nRow) {
            nDiag = 2 + (nCol - nRow)*2;
            for(int i = 0; i < nDiag/2; i++) diagonals[i] = getDiagonal(shelfCopy, 0, i, nRow, 1);
            for(int i = 0; i < nDiag/2; i++) diagonals[i] = getDiagonal(shelfCopy, nRow - 1, i, nRow, -1);
        } else {
            diagonals[0] = getDiagonal(shelfCopy, 0, 0, nRow, 1);
            diagonals[1] = getDiagonal(shelfCopy, 0, nRow - 1, nRow, -1);
        }
        return diagonals;
    }

    private Optional<ItemCard>[] getDiagonal(Optional<ItemCard>[][] shelfCopy, int x, int y, int diagLength, int side){

        Optional<ItemCard>[] diagonal = new Optional[diagLength];

        while (y < diagLength - 1) {
            diagonal[y] = shelfCopy[x][y];
            x += side;
            y++;
        }
        return diagonal;
    }
}
