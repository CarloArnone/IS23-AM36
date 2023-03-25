package it.polimi.ingsw.Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CommonGoalCard extends Goal {

    private List<Integer>  points;
    private List<Argument> arguments;
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
        boolean isSatisfied = false;
        for(Argument argument : arguments){
            isSatisfied = isSatisfied || argument.callWithArgumentsOn(p);
        }
        return isSatisfied;
    }


    public List<Integer> getPointsList(){
        return points;
    }

}
