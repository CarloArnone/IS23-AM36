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

    public CommonGoalCard(String name, int obtainedPoints){
        this.name = name;
        this.obtainedPoints = obtainedPoints;
    }

    public CommonGoalCard(String name, List<Integer> points, List<Argument> arguments) {
        this.name = name;
        this.points = points;
        this.arguments = arguments;
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
            if(isSatisfied){
                return true;
            }
            isSatisfied = argument.callWithArgumentsOn(p);
        }
        return isSatisfied;
    }


    public List<Integer> getPointsList(){
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonGoalCard that = (CommonGoalCard) o;
        return Objects.equals(name, that.name);
    }

}
