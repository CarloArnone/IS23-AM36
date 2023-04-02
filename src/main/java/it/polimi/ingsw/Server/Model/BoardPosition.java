package it.polimi.ingsw.Server.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardPosition {
    //TODO TESTING

    private int posX;
    private int posY;

    private ItemCard card;
    private Map<Borders, Boolean> freeBorders = new HashMap<>();

    public BoardPosition(int posX, int posY, ItemCard card, Map<Borders, Boolean> freeBorders) {
        this.card = card;
        this.posX = posX;
        this.posY = posY;
        this.freeBorders = freeBorders;
    }
    public BoardPosition(int posX, int posY, ItemCard card) {
        this.card = card;
        this.posX = posX;
        this.posY = posY;

    }
    public BoardPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;

        if(posX == 0){
            freeBorders.put(Borders.UP, true);
            freeBorders.put(Borders.DOWN, false);
            freeBorders.put(Borders.LEFT, false);
            freeBorders.put(Borders.RIGHT, false);
        }
        else if(posX == 5){
            freeBorders.put(Borders.UP, false);
            freeBorders.put(Borders.DOWN, true);
            freeBorders.put(Borders.LEFT, false);
            freeBorders.put(Borders.RIGHT, false);
        }
        else if(posY == 0){
            freeBorders.put(Borders.UP, false);
            freeBorders.put(Borders.DOWN, false);
            freeBorders.put(Borders.LEFT, true);
            freeBorders.put(Borders.RIGHT, false);
        }
        else if(posY == 4){
            freeBorders.put(Borders.UP, false);
            freeBorders.put(Borders.DOWN, false);
            freeBorders.put(Borders.LEFT, false);
            freeBorders.put(Borders.RIGHT, true);
        }
    }

    public boolean isFree(){
        return freeBorders.entrySet().stream().anyMatch(Map.Entry::getValue);
    }

    public boolean isLonely(){
        return freeBorders.entrySet().stream().allMatch(Map.Entry::getValue);
    }

    public ItemCard getCard(){
        return card;
    }

    public void setCard(ItemCard card){
        this.card = card;
    }

    public void freeBorder(Borders key){
        freeBorders.replace(key, true);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardPosition that)) return false;
        return getPosX() == that.getPosX() && getPosY() == that.getPosY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosX(), getPosY());
    }
}
