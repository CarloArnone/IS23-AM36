package it.polimi.ingsw.StartUp;

public class CommonGoalCard {
    private int points;
    private int commonGoalCardNumber;

    public CommonGoalCard(int commonGoalCardNumber,int points) {
        this.points = points;
        this.commonGoalCardNumber = commonGoalCardNumber;
    }

    public int getPoints() {
        return points;
    }

    public int getCommonGoalCardNumber() {
        return commonGoalCardNumber;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setCommonGoalCardNumber(int commonGoalCardNumber) {
        this.commonGoalCardNumber = commonGoalCardNumber;
    }
}
