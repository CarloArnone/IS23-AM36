package it.polimi.ingsw.Model;

public abstract class Goal {

    int obtainedPoints = 0;
    String name;
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
}
