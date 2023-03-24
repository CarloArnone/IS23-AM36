package it.polimi.ingsw.Model;

import Exceptions.NotEnoughSpacesInCol;
import com.google.gson.Gson;

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
        myShelf = new Shelf(new Optional[6][5]);
        personalGoal = new PersonalGoalCard(new HashMap<>());
    }
    public Player(String name, Shelf shelf){
        this.name = name;
        this.myShelf = shelf;
    }

    /** Places an Item Card in a selected column of the Player's Shelf. (????) */
    public void PlacePick(int col){
        try{
            myShelf.onClickCol(getDrawnCards(), col);
        }
        catch(NotEnoughSpacesInCol nes){
            //TODO DEFINE BEHAVIOR
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


    /**ADD TO UML*/
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

    /** ????. */
    public void checkAdjacentPoints(){

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

    public String toJSON(){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", this.name);
        jsonMap.put("drawnCards", getDrawnCards());
        jsonMap.put("score", this.score);
        jsonMap.put("achievedGoals", getAchievedGoals());
        jsonMap.put("personalGoal", this.personalGoal);
        jsonMap.put("myShelf", this.myShelf);

        Gson converter = new Gson();
        return converter.toJson(jsonMap);
    }

}
