package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.Map;

public abstract class BoardPosition {

    private int posX;
    private int posY;

    private ItemCard card; //Do we keep this??
    private Map<Enum, Boolean> freeBorder = new HashMap<Enum, Boolean>();

    public BoardPosition(ItemCard card) {
        this.card = card;
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
}
