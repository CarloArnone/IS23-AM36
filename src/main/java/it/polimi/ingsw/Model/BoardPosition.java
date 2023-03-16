package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardPosition {

    private int posX;
    private int posY;

    private ItemCard card; //Do we keep this??
    private Map<Enum, Boolean> freeBorder = new HashMap<>();

    public BoardPosition(int posX, int posY, ItemCard card) {
        this.card = card;
        this.posX = posX;
        this.posY = posY;
    }
    public BoardPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public boolean isFree(){
        return false;
    }

    public boolean isLonely(){
        return false;
    }

    public ItemCard getCard(){
        return card;
    }

    public void setCard(ItemCard card){
        this.card = card;
    }

    public void freeBorder(Enum e){

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
