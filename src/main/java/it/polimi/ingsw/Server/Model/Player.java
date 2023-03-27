package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Utils.JSONInterface;

import java.util.*;

public class Player {
    private String name;
    private List<ItemCard> drawnCards = new ArrayList<>();
    private int score;
    private List<Goal> achievedGoals = new ArrayList<>();
    private Shelf myShelf;
    private PersonalGoalCard personalGoal;


    public Player(String name) {
        this.name = name;
        JSONInterface builder = new JSONInterface();
        myShelf = new Shelf(new Optional[6][5]);
        personalGoal = builder.getPersonalGoalsFromJson(builder.getJsonStringFrom(builder.getPersonalGoalsPath()));
    }

    //TODO ADD CONSTRUCTOR WITH PERSONALGOAL NAME FOR PERSISTANCE
    public Player(String name, Shelf shelf){
        this.name = name;
        this.myShelf = shelf;
    }

    public Player(String name, int score, List<Goal> achievedGoals, Shelf myShelf, PersonalGoalCard personalGoal) {
        this.name = name;
        this.score = score;
        this.achievedGoals = achievedGoals;
        this.myShelf = myShelf;
        this.personalGoal = personalGoal;
    }

    /** Places an Item Card in a selected column of the Player's Shelf. (????) */
    public void PlacePick(int col) throws NotEnoughSpacesInCol {
        try{
            myShelf.onClickCol(getDrawnCards(), col);
        }
        catch(NotEnoughSpacesInCol nes){
            throw new NotEnoughSpacesInCol();
        }
    }
    /** Registers a Goal completed by the player inside a List of Goals. */
    public void addAchievedGoal(Goal goal){
        this.addAchievedGoal(goal);
    }
    /** Returns the score of the Player. */
    public int getScore(){
        return this.score;
    }
    /** Confirms the Draft choice of the Player and creates a List of Item Cards with them. (????) */
    public void setDrawnCards(List<ItemCard> drawnCards) {
        this.drawnCards = drawnCards;
    }
    /** Returns the Shelf of the selected Player. */
    public Shelf getMyShelf(){
        return myShelf;
    }
    /** Updates the points of the selected Player. */
    public void addPoints(int points){
        this.score += points;
    }
    /** Returns the list of Drafted Item Cards of the selected Player. */
    public List<ItemCard> getDrawnCards() {
        return drawnCards;
    }
    /** Returns the List that stores the record of all the Goals scored by the selected Player. */
    public List<Goal> getAchievedGoals() {
        return achievedGoals;
    }
    /** update the Players points relative to the adjacentGroup */
    public void updateAdjacentPoints(){
       addPoints(getMyShelf().getPointsForAdjacent());
       return;
    }
    /** Cancels the current process of positioning the Drafted Item Cards in the Shelf. */
    public void withdrawPicks(){
        this.drawnCards.clear();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return Objects.equals(name, player.name);
    }
    public String getName() {
        return name;
    }
    public PersonalGoalCard getPersonalGoal() {
        return personalGoal;
    }

    public void updateScore() {
        score = 0;
        for(Goal g : achievedGoals){
            score += g.getObtainedPoints();
        }

        score += getMyShelf().getPointsForAdjacent();
    }
}
