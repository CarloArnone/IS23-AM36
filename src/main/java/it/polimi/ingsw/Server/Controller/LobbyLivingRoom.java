package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.LivingRoom;

public class LobbyLivingRoom {
    LivingRoom liv;
    int necessaryPLayers;

    public LobbyLivingRoom(LivingRoom liv, int necessaryPLayers) {
        this.liv = liv;
        this.necessaryPLayers = necessaryPLayers;
    }

    public LobbyLivingRoom(LivingRoom liv) {
        this.liv = liv;
        necessaryPLayers = liv.getPlayers().size();
    }

    public boolean isGameStarted(){
        return liv.getPlayers().size() == necessaryPLayers;
    }

    public boolean isGameEnded(){
        if(getLivingRoom().getPlayers().stream().anyMatch(x -> x.getMyShelf().isFull())){
            return getLivingRoom().getTurn() == 0;
        }
        return false;
    }

    public LivingRoom getLivingRoom() {
        return liv;
    }
}
