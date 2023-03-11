package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonGoalCard implements Goal {

    private List<Integer> point = new ArrayList<Integer>();
    private int bonus = 8;

    public int getBonus(){
        return this.bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public abstract int getPoints(Player p);
}
