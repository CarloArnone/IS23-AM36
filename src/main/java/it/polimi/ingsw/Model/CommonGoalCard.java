package it.polimi.ingsw.Model;

import java.util.*;

public abstract class CommonGoalCard implements Goal {

    private List<Integer>  points = new ArrayList<>();
    public int getPoints(){
        int temp = this.points.get(0);
        this.points.remove(0);
        return temp;
    }

    public Optional<ItemCard>[][] transposeMatrix(Optional<ItemCard>[][] shelfCopy){

        Optional<ItemCard>[][] transposedShelf = new Optional[shelfCopy[0].length][shelfCopy.length];

        for(int i = 0; i < shelfCopy[0].length; i++){
            for(int j = 0; j < shelfCopy.length; j++){
                transposedShelf[i][j] = Optional.of(shelfCopy[j][i].get());
            }
        }
        return transposedShelf;
    }

    public boolean distinctArray(Optional<ItemCard>[] columnCopy, int n){

        Set<Character> foundColors = new HashSet<>();

        for (Optional<ItemCard> itemCard : columnCopy) {
            if (itemCard.isEmpty()) return false;
            foundColors.add(itemCard.get().getColor());
        }
        return foundColors.size() == n;
    }

    public boolean distinctArrayOfN(Optional<ItemCard>[] colCopy, int n){

        Set<Character> foundColors = new HashSet<>();

        for(Optional<ItemCard> tile : colCopy){
            if(tile.isEmpty()) return false;
            foundColors.add(tile.get().getColor());
        }
        return foundColors.size() <= n;
    }
}
