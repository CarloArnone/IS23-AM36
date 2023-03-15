package it.polimi.ingsw.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LivingRoom {

    private Map<BoardPosition, ItemCard> desk = new HashMap<>();
    private String livingRoomId;
    private List<Player> players;
    private int turn;
    //private List<LivingRoomPosition> cardDraft; should be dealt with by the view
    private List<CommonGoalCard> commonGoalSet;
    private List<ItemCard> deck;

    /** Allows the player to pick a set of Item Cards. */
    public void givePlayerTheirPick(Player p, List<ItemCard> pick){
        p.setDrawnCards(pick);
    }

    /** Refills the board with new Item Cards. */
    private void arrangeDesk(){

    }

    /** Checks if its necessary to refill the board. Is called at the end/start of each turn. */
    public void checkRearrangeDesk(){

    }

    /** Erases the draft that was being done by the player. */
    public void undoDraft(Player p){
        p.withdrawPicks();
    }

    /** Updates the view of the goals for the players. (All the goals at once? Both personal and common?) */
    public void updateGoals(){

    }

    /** Handles the passage of turns.
     * (Check if it's only a matter of incrementing the counter or if there are other things that we could have this do and make sense at the same time)*/
    public void nextTurn(){

    }

    /** Returns the current turn number. */
    public int getTurn(){
        return this.turn;
    }

    /** Returns the list of players in the current game. */
    public List<Player> getPlayers(){
        return this.players;
    }

    /** Adds a new player to the game. */
    public void addPlayer(Player newPlayer){

    }

    /** Removes a player from the game. */
    public void removePlayer(Player player){

    }
    /** Actually removes a card from a position of the board. (Does this happen after you confirm the draft, or after you finish positioning the Item Cards in your Shelf?) */
    public void removeCard(int x, int y){

    }

    /** Return the ID of the current Living Room. */
    public String getLivingRoomId(){
        return this.livingRoomId;
    }

    /** Sets the ID of the current Living Room. */
    public void setLivingRoomId(String livingRoomId) {
        this.livingRoomId = livingRoomId;
    }

    /** !!!PLACEHOLDER!!!
     * Creates a list of 2 random Common Goals for the current game. */
    public void setCommonGoalSet(List<CommonGoalCard> commonGoalSet) {
        this.commonGoalSet = commonGoalSet;
    }
    /** Returns the 2 common goals for this game. */
    public List<CommonGoalCard> getCommonGoalSet() {
        return commonGoalSet;
    }
}
