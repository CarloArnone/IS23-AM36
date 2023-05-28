package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewServerSocket;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;

import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class LobbyLivingRoom {
    LivingRoom liv;
    private int necessaryPLayers;

    Controller controller = Controller.INSTANCE;

    public LobbyLivingRoom(LivingRoom liv, int necessaryPLayers) {
        this.liv = liv;
        this.necessaryPLayers = necessaryPLayers;
    }

    public LobbyLivingRoom(LivingRoom liv) {
        this.liv = liv;
        necessaryPLayers = liv.getPlayers().size(); //TODO THIS SHOULD BE THE PROBLEM
    }

    public boolean isGameStarted(){
        List<WaitingPlayer> players = liv.getPlayers().stream().map(x -> controller.getWaitingPlayerByName(x.getName())).filter(WaitingPlayer::isOnline).toList();
        return players.size() == necessaryPLayers;
    }

    public boolean isGameEnded(){
        if(getLivingRoom().getPlayers().stream().anyMatch(x -> x.getMyShelf().isFull()) && liv.getTurn() == liv.getPlayers().size()-1){
            if(getLivingRoom().getTurn() == 0){
                //JSONInterface.removeLivingRoom(getLivingRoom());
                return true;
            }
            return false;

        }
        if(isLonelyPlayer(getLivingRoom())){
            JSONInterface.removeLivingRoom(getLivingRoom());
            return true;
        }
        return false;
    }

    private boolean isLonelyPlayer(LivingRoom livingRoom) {
        return livingRoom.getPlayers().stream().filter(player -> controller.getWaitingPlayerByName(player.getName()).isOnline()).toList().size() == 1;
    }





    public LivingRoom getLivingRoom() {
        return liv;
    }

    public boolean isPossibleJoin() {
        return liv.getPlayers().size() < necessaryPLayers;
    }

    public Player getWinner(){
        return liv.getWinner();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LobbyLivingRoom that)) return false;

        return Objects.equals(liv.getLivingRoomId(), that.liv.getLivingRoomId());
    }

    @Override
    public int hashCode() {
        return liv != null ? liv.hashCode() : 0;
    }

    public int getNecessaryPLayers() {
        return necessaryPLayers;
    }

    public void removeOneNecessaryPlayer() {
        necessaryPLayers -= 1;
    }
}
