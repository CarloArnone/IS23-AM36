package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonGoalCard implements Goal {

    public CommonGoalCard(List<Integer> points) {
        this.points = points;
    }

    private List<Integer>  points = new ArrayList<>();

    public int getPoints(){
        int temp = this.points.get(0);
        this.points.remove(0);
        return temp;
    }

}
