package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public class LivingRoomPosition extends Position{

    private ItemCard card;
    public List<Boolean> freeBorder = new ArrayList<Boolean>();

    public LivingRoomPosition(ItemCard card) {
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
}
