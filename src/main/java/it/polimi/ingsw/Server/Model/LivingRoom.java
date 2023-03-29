package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Common.Exceptions.ToManyCardsException;
import it.polimi.ingsw.Common.Listener;
import it.polimi.ingsw.Common.Utils.JSONInterface;

import java.util.*;

public class LivingRoom {

    private Map<BoardPosition, Boolean> board;
    private List<Listener> viewList;
    private String livingRoomId;
    private List<Player> players;
    private int turn;
    private List<CommonGoalCard> commonGoalSet;


    public LivingRoom(String livingRoomId, Map<BoardPosition, Boolean> board, List<Player> players, List<CommonGoalCard> commonGoalSet) {
        this.board = board;
        this.livingRoomId = livingRoomId;
        this.players = players;
        this.turn = 0;
        this.commonGoalSet = commonGoalSet;
        this.viewList = new ArrayList<>();
        arrangeDesk();
    }
    public LivingRoom(String livingRoomId, Map<BoardPosition, Boolean> board, List<Player> players, List<CommonGoalCard> commonGoalSet, int turn) {
        this.board = board;
        this.livingRoomId = livingRoomId;
        this.players = players;
        this.turn = turn;
        this.commonGoalSet = commonGoalSet;
        this.viewList = new ArrayList<>();
    }
    public LivingRoom(String livingRoomId, Map<BoardPosition, Boolean> board, List<CommonGoalCard> commonGoalset){
        this.livingRoomId = livingRoomId;
        this.board = board;
        this.commonGoalSet = commonGoalset;
        arrangeDesk();
        this.viewList = new ArrayList<>();
    }
    public LivingRoom(String livingRoomId, int playersNum){
        this.livingRoomId = livingRoomId;
        this.turn = 0;
        this.players = new ArrayList<>();
        board = JSONInterface.getBoardFromJson(playersNum);
        this.viewList = new ArrayList<>();
    }
    public LivingRoom(String livingRoomId){
        this.livingRoomId = livingRoomId;
        this.turn = 0;
        this.players = new ArrayList<>();
        board = new HashMap<>();
        this.viewList = new ArrayList<>();
    }

    /** Allows the player to pick a set of Item Cards. */
    public void givePlayerTheirPick(Player p, List<ItemCard> pick) throws ToManyCardsException {
        if(pick.size() > 3){
            throw new ToManyCardsException();
        }

        p.setDrawnCards(pick);
    }
    /** Refills the board with new Item Cards. */
    public void arrangeDesk(){
        for(Map.Entry<BoardPosition, Boolean> position : board.entrySet()){
            position.getKey().setCard(extractACard());
        }
    }
    /**
     * extract a card from the card bag.
     * @return itemcard
     */
    public ItemCard extractACard(){
        Random randomizer = new Random();
        int cardPick = randomizer.nextInt(0, 132);
        if(cardPick <= 22){
            return new ItemCard('P', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 44) {
            return new ItemCard('B', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 66) {
            return new ItemCard('G', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 88) {
            return new ItemCard('C', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 110) {
            return new ItemCard('W', "" + randomizer.nextInt(1, 3));
        } else if (cardPick <= 132) {
            return new ItemCard('Y', "" + randomizer.nextInt(1, 3));
        }
        else return null;
    }
    /** Checks if its necessary to refill the board. Is called at the end/start of each turn. */
    public void checkRearrangeDesk(){
        if(board.entrySet().stream().allMatch(x -> x.getKey().isLonely())){
            arrangeDesk();
        }
    }
    /** Erases the draft that was being done by the player. */
    public void undoDraft(Player p){
        p.withdrawPicks();
    }
    /** Updates the score relative to the CommonGoals for the player p. */
    public void updateGoals(Player p){
        for(CommonGoalCard g : commonGoalSet){
            if(!players.get(getTurn()).getAchievedGoals().contains(g)){
                if(g.checkGoal(p)){
                    p.addPoints(g.getPoints());
                    p.addAchievedGoal(g);
                }
            }
        }

    }
    /** Handles the passage of turns.*/
    public void nextTurn(){
        turn = (turn +1) % players.size();
    }
    /** Returns the current turn number. */
    public int getTurn(){
        return this.turn;
    }
    /** Returns the list of players in the current game. */
    public List<Player> getPlayers(){
        return this.players;
    }
    /** Adds a new player to the game. If the player is already present does nothing*/
    public void addPlayer(Player newPlayer){
        //TODO : NEED TO CHECK WHETHER THE NUMBER OF PLAYERS IS THE LIMIT -- MUST DO IT IN CONTROLLER
        if(!getPlayers().contains(newPlayer)){
            players.add(newPlayer);
        }
    }
    /** Removes a player from the game. */
    public void removePlayer(Player player){
        players.remove(player);
    }
    /** Actually removes a card from a position of the board.*/
    public void removeCard(BoardPosition position){
        board.computeIfPresent(new BoardPosition(position.getPosX() + 1, position.getPosY()), (key, value) -> {key.freeBorder(Borders.UP); return true;});
        board.computeIfPresent(new BoardPosition(position.getPosX() - 1, position.getPosY()), (key, value) -> {key.freeBorder(Borders.DOWN); return true;});
        board.computeIfPresent(new BoardPosition(position.getPosX(), position.getPosY() + 1), (key, value) -> {key.freeBorder(Borders.LEFT); return true;});
        board.computeIfPresent(new BoardPosition(position.getPosX(), position.getPosY() - 1), (key, value) -> {key.freeBorder(Borders.RIGHT); return true;});
        board.remove(position);
    }
    /** Return the ID of the current Living Room. */
    public String getLivingRoomId(){
        return this.livingRoomId;
    }
    /** Sets the ID of the current Living Room. */
    public void setLivingRoomId(String livingRoomId) {
        this.livingRoomId = livingRoomId;
    }
    /** Sets the list of the 2 random Common Goals for the current game. */
    public void setCommonGoalSet(List<CommonGoalCard> commonGoalSet) {
        this.commonGoalSet = commonGoalSet;
    }
    /** Returns the 2 common goals for this game. */
    public List<CommonGoalCard> getCommonGoalSet() {
        return commonGoalSet;
    }
    /**
     * get the board Map<BoardPosition, Boolean>
     * @return board
     */
    public Map<BoardPosition, Boolean> getBoard() {
        return board;
    }
    /**
     * Usage for testing - the real board is set on starting a game
     * @param board
     */
    public void setBoard(Map<BoardPosition, Boolean> board) {
        this.board = board;
    }
    public void setTurn(int newTurn) {
        this.turn = newTurn;
    }

    public void addSupplier(Listener s){
        this.viewList.add(s);
    }

    public void notifyAllListeners() {
        for (Listener s : viewList){
            s.notifyListener();
        }
    }
}
