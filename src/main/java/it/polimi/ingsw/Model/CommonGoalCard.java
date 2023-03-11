package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonGoalCard implements Goal {

    private List<Integer> point = new ArrayList<Integer>();

    public abstract int getPoints(Player p);
}
