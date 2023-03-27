package it.polimi.ingsw.Common;

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
        return liv.getPlayers().size() == 1;
    }

    public LivingRoom getLivingRoom() {
        return liv;
    }
}
