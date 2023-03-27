package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Common.Exceptions.InvalidGameIDException;
import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.PlayersOutOfBoundException;
import it.polimi.ingsw.Common.Utils.TestGenerator;
import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller implements eventObserver {
    List<LivingRoom> livingRooms;

    public Controller() {
        livingRooms = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Random rand = new Random();
            livingRooms.add(TestGenerator.generateLivingRoom(rand.nextInt(2, 4)));
        }
    }

    public Controller(List<LivingRoom> livingRooms) {
        this.livingRooms = livingRooms;
    }

    @Override
    public boolean confirmEndTurn(List<BoardPosition> pick, int col) {
        return false;
    }

    @Override
    public boolean logInTryEvent(String name) {
        return !name.equals("carlo");
    }

    @Override
    public LivingRoom previousGamesRequestEvent(String name) {
        for(LivingRoom liv : livingRooms){
            if(liv.getPlayers().contains(new Player(name))){
                return liv;
            }
        }

        return null;
    }

    @Override
    public LivingRoom createGameEvent(String livingRoomID, int PlayersNum) throws InvalidGameIDException, PlayersOutOfBoundException {

        if(livingRooms.contains(new LivingRoom(livingRoomID))){
            throw new InvalidGameIDException();
        }
        if(PlayersNum != 2 && PlayersNum != 3  && PlayersNum != 4){
            throw new PlayersOutOfBoundException();
        }
        return  new LivingRoom(livingRoomID, PlayersNum);
    }

    @Override
    public LivingRoom retrieveOldGameEvent(String livingRoomID) throws NoMatchingIDException {
        try{
            livingRooms.stream().filter(x -> x.getLivingRoomId().equals(livingRoomID)).findFirst().get();
        }
        catch(Exception e){
            throw new NoMatchingIDException();
        }
        throw  new NoMatchingIDException();

    }

    @Override
    public boolean leaveGameEvent(Player p) {
        return false;
    }

    @Override
    public boolean joinGameEvent(String livingRoomID, Player p) {
        return true;
    }

    @Override
    public boolean reconnectPlayer(LivingRoom livingRoom, String name) {
        return false;
    }

    @Override
    public boolean disconnectedPlayer(LivingRoom livingRoom, String name, boolean voluntaryLeft) {
        return false;
    }

    @Override
    public List<String> getActiveLivingRooms(int listLength, int occurency) {
        return livingRooms.subList(listLength * (occurency -1), listLength * occurency).stream().map(LivingRoom::getLivingRoomId).toList();
    }
}
