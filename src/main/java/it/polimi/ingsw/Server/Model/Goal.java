package it.polimi.ingsw.Server.Model;

import java.util.Objects;

public abstract class Goal {

    protected int obtainedPoints = 0;
    protected String name;
    abstract boolean checkGoal(Player p);
    public int getObtainedPoints() {
        return obtainedPoints;
    }
    public void setObtainedPoints(int obtainedPoints) {
        this.obtainedPoints = obtainedPoints;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(name, goal.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(obtainedPoints, name);
    }
}
