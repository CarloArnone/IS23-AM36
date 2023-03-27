package it.polimi.ingsw.Server.Controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.LobbyLivingRoom;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.ArrayList;
import java.util.List;

public class Controller implements eventObserver {
    List<LobbyLivingRoom> livingRooms;

    public Controller() {
        livingRooms = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            LivingRoom livingRoom = JSONInterface.getRandomLivingForTest();
            livingRooms.add(new LobbyLivingRoom(livingRoom, livingRoom.getPlayers().size()));
        }
    }

    public Controller(LivingRoom liv) {
        livingRooms = new ArrayList<>();
        livingRooms.add(new LobbyLivingRoom(JSONInterface.getLivingRoomFromJson(
                                                JSONInterface.getJsonStringFrom(
                                                    JSONInterface.getLivingRoomsPath()), "PartitaDiProva")));
    }

    public Controller(List<LobbyLivingRoom> livingRooms) {
        this.livingRooms = livingRooms;
    }

    @Override
    public synchronized boolean confirmEndTurn(LivingRoom livingRoom, Player p, List<BoardPosition> pick, int col) {
        for (LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                List<ItemCard> pickItemCards = new ArrayList<>();
                liv.getLivingRoom().nextTurn();
                liv.getLivingRoom().updateGoals(p);
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
                            return false;
                        }
                        player.updateScore();
                        liv.getLivingRoom().notifyAllListeners();
                        return true;
                    }
                }

                JSONInterface.writeLivingRoomToJson(liv.getLivingRoom());
            }
        }
        return true;
    }

    @Override
    public synchronized boolean logInTryEvent(String name) {
        for (LobbyLivingRoom liv : livingRooms){
            for(Player p : liv.getLivingRoom().getPlayers()){
                if(p.getName().equals(name)){
                    return false;
                }
            }
        }

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
    public synchronized LivingRoom createGameEvent(String livingRoomID, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException {

        if(livingRooms.contains(new LivingRoom(livingRoomID))){
            throw new InvalidGameIDException();
        }
        if(PlayersNum != 2 && PlayersNum != 3  && PlayersNum != 4){
            throw new PlayersOutOfBoundException();
        }
        LivingRoom l = new LivingRoom(livingRoomID, PlayersNum);
        livingRooms.add(new LobbyLivingRoom(l, PlayersNum));
        return  l;
    }

    @Override
    public synchronized LivingRoom retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException {
        try{
            livingRooms.stream().filter(x -> x.getLivingRoom().getLivingRoomId().equals(livingRoomID)).findFirst().get();
        }
        catch(Exception e){
            throw new NoMatchingIDException();
        }
        throw  new NoMatchingIDException();

    }

    @Override
    public synchronized void leaveGameEvent(String name, LivingRoom livingRoom) {
        disconnectedPlayer(livingRoom, name, true);
    }

    @Override
    public synchronized boolean joinGameEvent(String livingRoomID, Player p) {
        for(LobbyLivingRoom liv : livingRooms){
            liv.getLivingRoom().addPlayer(p);
        }
        return true;
    }

    @Override
    public synchronized boolean reconnectPlayer(LivingRoom livingRoom, String name) {
        return true;
    }

    @Override
    public synchronized boolean disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft) {
        for(LobbyLivingRoom liv : livingRooms){
            if(liv.getLivingRoom().equals(livingRoom)){
                for(Player p : liv.getLivingRoom().getPlayers()){
                    if(p.getName().equals(name)){
                        liv.getLivingRoom().removePlayer(p);
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

}
