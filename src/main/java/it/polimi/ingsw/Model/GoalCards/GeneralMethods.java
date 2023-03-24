package it.polimi.ingsw.Model.GoalCards;

import it.polimi.ingsw.Model.ItemCard;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class GeneralMethods {

    /**
     *
     * @param shelfCopy copy of the shelf to analyze.
     * @param transpose false to check rows, true to check columns.
     * @param arrayMatches number of arrays that must meet the parameters to return true.
     * @param tileMatches maximum number of tiles that can be different in each array to be considered a match. Set to 0 for all different.
     * @return returns true if at least "arrayMatches" arrays meets the requirement specified in "tileMatches".
     */
    public boolean checkLines(Optional<ItemCard>[][] shelfCopy, boolean transpose, int arrayMatches, int tileMatches){

        int ret = 0, counter;

        if(transpose) shelfCopy = transposeMatrix(shelfCopy);

        for (Optional<ItemCard>[] optionals : shelfCopy) {
            counter = fullArrayOf(optionals);
            if (counter != 0 && counter <= tileMatches) ret++;
            else if (counter != 0 && tileMatches == 0 && counter == optionals.length) ret++;
        }
        return ret >= arrayMatches;
    }

    /**
     *
     * @param vector array to check.
     * @return returns how many colors of tiles are found in the array.
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
     * @param shelf 2D array to transpose.
     * @return returns a new 2D array that has been transposed.
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
}
