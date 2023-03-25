package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public class CommonGoalCard extends Goal {

    private List<Integer>  points;
    private List<List<String>> arguments;
    public CommonGoalCard(String name, List<Integer> points) {
        this.name = name;
        this.points = points;
    }
    public int getPoints(){
        int temp = this.points.get(0);
        this.points.remove(0);
        return temp;
    }

    @Override
    boolean checkGoal(Player p) {
        for(List<String> arguments : arguments){
            return false;
        }
        return false;
    }

    public List<Integer> getPointsList(){
        return points;
    }
}
