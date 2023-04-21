package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.LobbyLivingRoom;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.WaitingPlayer;
import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Model.*;
import javafx.util.Pair;

import java.util.*;

public enum Controller implements eventObserver {

    INSTANCE;
    List<LobbyLivingRoom> livingRooms;
    Set<WaitingPlayer> waitingForChoice;


    public static Controller getInstance(){
        INSTANCE.buildController();
        return INSTANCE;
    }
    public void buildController() {
        livingRooms = new ArrayList<>();
        waitingForChoice = new HashSet<>();

        List<String> allLivingRooms = JSONInterface.getLivingRoomsList();
        for(String livingRoomID : allLivingRooms){
            LivingRoom liv = JSONInterface.getLivingRoomFromJson(JSONInterface.getJsonStringFrom(JSONInterface.getLivingRoomsPath()), livingRoomID);
            livingRooms.add(new LobbyLivingRoom(liv, liv.getPlayers().size()));
        }
    }
    @Override
    public synchronized boolean confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws NotEnoughSpacesInCol{
        for (LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                List<ItemCard> pickItemCards = new ArrayList<>();

                int i = 0;
                for(BoardPosition bps : pick){
                    pickItemCards.add(i, bps.getCard());
                    liv.getLivingRoom().removeCard(bps);
                    i++;
                }
                for(Player player : liv.getLivingRoom().getPlayers()){
                    if (player.equals(p)){
                        try {
                            p.getMyShelf().onClickCol(pickItemCards, col);
                        } catch (NotEnoughSpacesInCol e) {
                            throw new NotEnoughSpacesInCol();
                        }
                        liv.getLivingRoom().updateGoals(player); //TODO TESTING
                        player.updateScore();
                        liv.getLivingRoom().undoDraft(player);
                        break;
                    }
                }
                liv.getLivingRoom().nextTurn();
                liv.getLivingRoom().checkRearrangeDesk();
                liv.getLivingRoom().notifyAllListeners();
                JSONInterface.writeLivingRoomToJson(liv.getLivingRoom());
                return true;
            }
        }
        return true;
    }
    @Override
    public synchronized boolean logInTryEvent(String name, IUI c) {
        //TODO FIX IS IMPOSSIBLE TO RETRIEVE AN OLD GAME

        for(WaitingPlayer wp : waitingForChoice){
            if(wp.getPlayer().getName().equals(name)){
                if(wp.isOnline()){
                    return false;
                }
                break;
            }
        }

        waitingForChoice.add(new WaitingPlayer(new Player(name), c));
        return true;
    }
    @Override
    public synchronized LivingRoom previousGamesRequestEvent(String name) {
        for(LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().getPlayers().contains(new Player(name))){
                return liv.getLivingRoom();
            }
        }

        return null;
    }
    @Override
    public synchronized LivingRoom createGameEvent(String livingRoomID, Player p, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException {

        if(livingRooms.contains(new LivingRoom(livingRoomID))){
            throw new InvalidGameIDException();
        }
        if(PlayersNum != 2 && PlayersNum != 3  && PlayersNum != 4){
            throw new PlayersOutOfBoundException();
        }
        LivingRoom l = JSONInterface.generateLivingRoom(PlayersNum, livingRoomID);
        l.addPlayer(p);
        l.addSupplier(getPlayerView(p));
        livingRooms.add(new LobbyLivingRoom(l, PlayersNum));
        saveGame(l);
        return  l;
    }
    @Override
    public synchronized LivingRoom retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException {
        try{
            return livingRooms.stream().filter(x -> x.getLivingRoom().getLivingRoomId().equals(livingRoomID)).findFirst().get().getLivingRoom();
        }
        catch(Exception e){
            throw new NoMatchingIDException();
        }

    }
    @Override
    public void leaveGameEvent(String name, LivingRoom livingRoom, IUI c) {
        disconnectedPlayer(livingRoom, name, true, c);
    }
    @Override
    public synchronized Player joinGameEvent(String livingRoomID, String name) {
        Player p = new Player(name);
        for (LobbyLivingRoom liv : livingRooms) {
            if (liv.getLivingRoom().getLivingRoomId().equals(livingRoomID)) {
                if(liv.getLivingRoom().getPlayers().contains(p)){
                    p = JSONInterface.getPlayerFromJson(JSONInterface.getJsonStringFrom(JSONInterface.getPlayersPath()), name);
                    liv.getLivingRoom().addSupplier(getPlayerView(p));
                    saveGame(liv.getLivingRoom());
                    break;
                }
                else{
                    liv.getLivingRoom().addSupplier(getPlayerView(p));
                    liv.getLivingRoom().addPlayer(p);
                    saveGame(liv.getLivingRoom());
                    return p;
                }
            }
        }
        return p;
    }
    @Override
    public synchronized boolean reconnectPlayer(LivingRoom livingRoom, String name) {
        return true;
    }
    @Override
    public synchronized boolean disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, IUI c) {
        for(LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                for(Player p : liv.getLivingRoom().getPlayers()){
                    if(p.getName().equals(name)){
                        if(voluntaryLeft){
                            liv.getLivingRoom().removePlayer(p);
                        }
                        else waitingForChoice.stream().filter(x -> x.getPlayer().equals(p)).findFirst().get().setOnline(false);

                        waitingForChoice.remove(new WaitingPlayer(new Player(name), c)); //TODO MODIFY SET PLAYER OFFLINE
                        saveGame(liv.getLivingRoom());
                        return true;
                    }
                }
            }
        }

        return false;
    }
    @Override
    public synchronized List<String> getActiveLivingRooms(int listLength, int occurency) {
        if(listLength * occurency < livingRooms.size()){
            return livingRooms.subList(listLength * (occurency -1), listLength * occurency).stream().map(x -> x.getLivingRoom().getLivingRoomId()).toList();
        }
        else return livingRooms.stream().map(x -> x.getLivingRoom().getLivingRoomId()).toList();
    }
    @Override
    public synchronized boolean isGamesStarted(LivingRoom livingRoom) {
        for(LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                return liv.isGameStarted();
            }
        }
        return false;
    }
    @Override
    public synchronized  boolean isGameEnded(LivingRoom livingRoom){
        for(LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                return liv.isGameEnded();
            }
        }
        return false;
    }
    @Override
    public synchronized boolean endGame(LivingRoom livingRoom){
        return false;
    }
    private synchronized IUI getPlayerView(Player p){
        return waitingForChoice.stream().filter(x -> x.getPlayer().equals(p)).findFirst().get().getView();
    }
    private boolean saveGame(LivingRoom liv){
        JSONInterface.writeLivingRoomToJson(liv);
        return true;
    }
    @Override
    public boolean isPossiblePick(Player player, String livingRoomId, List<BoardPosition> pick){
        LivingRoom livingRoom = null;
        try {
            livingRoom = getLivingRoomFromLobby(livingRoomId);
        } catch (NoMatchingIDException e) {
            return false;
        }

        return  playerIsValid(livingRoom, player) &&
                !hasAlreadySomeCards(livingRoom, player) &&
                checkPickCardsVeridicity(livingRoom, pick) &&
                areCorrectPositions(pick);
    }
    private boolean checkPickCardsVeridicity(LivingRoom liv,  List<BoardPosition> pick) {
        Map<Character, Integer> colorCount = new HashMap<>();

        pick.forEach((b) -> {
            colorCount.computeIfPresent(b.getCard().getColor(), (Color, Num) -> {
                return Num + 1;
            });

            if(! colorCount.containsKey(b.getCard().getColor())){
                colorCount.put(b.getCard().getColor(), 1);
            }
        });

        liv.getBoard().forEach(
                (pos, validity) ->{
                    if(pick.contains(pos)){
                        if(!validity){
                            return;
                        }

                        colorCount.computeIfPresent(pos.getCard().getColor(), (Color, Num) -> {
                            return Num - 1;
                        });

                    }
                }
        );

        return colorCount.entrySet().stream().allMatch(color -> color.getValue()==0);

    }
    private LivingRoom getLivingRoomFromLobby(String livingRoomID) throws NoMatchingIDException {
        for(LobbyLivingRoom livingRoom1 : livingRooms){
            if(livingRoom1.getLivingRoom().getLivingRoomId().equals(livingRoomID)){
                return livingRoom1.getLivingRoom();
            }
        }
        throw new NoMatchingIDException();
    }
    private boolean playerIsValid(LivingRoom livingRoom, Player player){
        return livingRoom.getPlayers().contains(player);
    }
    private boolean hasAlreadySomeCards(LivingRoom livingRoom, Player player){
        for(Player p : livingRoom.getPlayers()){
            if(p.equals(player)){
                return !p.getDrawnCards().isEmpty();
            }
        }
        return true;
    }
    private boolean areCorrectPositions(List<BoardPosition> pick){
        if(pick.stream().allMatch(pos -> pos.getPosX() == pick.get(0).getPosX() || pos.getPosY() == pick.get(0).getPosY())){
            return pick.stream().allMatch(pos -> hasNearPosition(pick, pos));
        }
        else return false;
    }
    private boolean hasNearPosition(List<BoardPosition> pick, BoardPosition pos) {
        return pick.contains(new BoardPosition(pos.getPosX() -1, pos.getPosY())) ||
                pick.contains(new BoardPosition(pos.getPosX() +1, pos.getPosY())) ||
                pick.contains(new BoardPosition(pos.getPosX(), pos.getPosY() -1)) ||
                pick.contains(new BoardPosition(pos.getPosX(), pos.getPosY() +1));
    }

}
