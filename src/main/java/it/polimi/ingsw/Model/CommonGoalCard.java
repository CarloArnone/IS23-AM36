package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonGoalCard implements Goal {

    private List<Integer>  points;
    public CommonGoalCard(List<Integer> points) {
        this.points = points;
    }
    public int getPoints(){
        int temp = this.points.get(0);
        this.points.remove(0);
        return temp;
    }

}
