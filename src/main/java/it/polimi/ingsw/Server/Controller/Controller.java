package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.LobbyLivingRoom;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.WaitingPlayer;
import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller implements eventObserver {
    List<LobbyLivingRoom> livingRooms;
    Set<WaitingPlayer> waitingForChoice;

    public Controller() {
        livingRooms = new ArrayList<>();
        waitingForChoice = new HashSet<>();

        List<String> allLivingRooms = JSONInterface.getLivingRoomsList();
        for(String livingRoomID : allLivingRooms){
            LivingRoom liv = JSONInterface.getLivingRoomFromJson(JSONInterface.getJsonStringFrom(JSONInterface.getLivingRoomsPath()), livingRoomID);
            livingRooms.add(new LobbyLivingRoom(liv, liv.getPlayers().size()));
        }
    }

    public Controller(List<LobbyLivingRoom> livingRooms) {
        this.livingRooms = livingRooms;
    }

    @Override
    public synchronized boolean confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) throws NotEnoughSpacesInCol{
        for (LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                List<ItemCard> pickItemCards = new ArrayList<>();
                liv.getLivingRoom().nextTurn();
                //liv.getLivingRoom().updateGoals(p); //TODO TESTING
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
                        player.updateScore();
                        break;
                    }
                }
                liv.getLivingRoom().notifyAllListeners();
                JSONInterface.writeLivingRoomToJson(liv.getLivingRoom());
                return true;
            }
        }
        return true;
    }

    @Override
    public synchronized boolean logInTryEvent(String name, CLI c) {
        //TODO FIX IS IMPOSIIBLE TO RETRIEVE AN OLD GAME

        for (LobbyLivingRoom liv : livingRooms){
            for(Player p : liv.getLivingRoom().getPlayers()){
                if(p.getName().equals(name)){
                    return false;
                }
            }
        }

        for(WaitingPlayer wp : waitingForChoice){
            if(wp.getPlayer().getName().equals(name)){
                if(wp.isOnline()){
                    return false;
                }
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
        waitingForChoice.remove(new WaitingPlayer(p));
        livingRooms.add(new LobbyLivingRoom(l, PlayersNum));
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
    public synchronized void leaveGameEvent(String name, LivingRoom livingRoom, CLI c) {
        disconnectedPlayer(livingRoom, name, true, c);
    }

    @Override
    public synchronized boolean joinGameEvent(String livingRoomID, Player p) {
        for(LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().getLivingRoomId().equals(livingRoomID)){
                liv.getLivingRoom().addSupplier(getPlayerView(p));
                liv.getLivingRoom().addPlayer(p);
            }
        }
        waitingForChoice.remove(new WaitingPlayer(p));
        return true;
    }

    @Override
    public synchronized boolean reconnectPlayer(LivingRoom livingRoom, String name) {
        return true;
    }

    @Override
    public synchronized boolean disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft, CLI c) {
        for(LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                for(Player p : liv.getLivingRoom().getPlayers()){
                    if(p.getName().equals(name)){
                        liv.getLivingRoom().removePlayer(p);
                        waitingForChoice.add(new WaitingPlayer(new Player(name), c));
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

    public synchronized CLI getPlayerView(Player p){
        return waitingForChoice.stream().filter(x -> x.getPlayer().equals(p)).findFirst().get().getView();
    }

}
