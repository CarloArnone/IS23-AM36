package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<ItemCard> drawnCards = new ArrayList<ItemCard>();
    private int score;
    private List<Goal> achievedGoals = new ArrayList<Goal>();
    private Shelf myShelf;
    private PersonalGoalCards personalGoals;

    /** Places an Item Card in a selected column of the Player's Shelf. (????) */
    public void PlacePick(){

    }

    /** Registers a Goal completed by the player inside a List of Goals. */
    public void addAchievedGoal(Goal goal){

    }

    /** Returns the score of the Player. */
    public int getScore(){
        return this.score;
    }

    /** Confirms the Draft choice of the Player and creates a List of Item Cards with them. (????) */
    public void setDrawnCards(List<ItemCard> drawnCards) {
        this.drawnCards = drawnCards;
    }


    /**ADD TO UML*/
    /** Returns the Shelf of the selected Player. */
    public Shelf getMyShelf(){
        return myShelf;
    }

    /** Updates the points of the selected Player. */
    public void addPoints(int points){

    }

    /** Returns the list of Drafted Item Cards of the selected Player. */
    public List<ItemCard> getDrawnCards() {
        return drawnCards;
    }

    /** Returns the List that stores the record of all the Goals scored by the selected Player. */
    public List<Goal> getAchievedGoals() {
        return achievedGoals;
    }

    /** ????. */
    public void checkAdjacentPoints(){

    }

    /** Cancels the current process of positioning the Drafted Item Cards in the Shelf. */
    public void withdrawPicks(){

    }
}
