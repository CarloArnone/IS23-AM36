package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Server.Model.Player;

import java.util.Objects;

public class WaitingPlayer {
    Player player;
    ICommunication virtualView;
    boolean online;

    public WaitingPlayer(Player player, ICommunication virtualView) {
        this.player = player;
        this.virtualView = virtualView;
        online = true;
    }

    public WaitingPlayer(Player player) {
        this.player = player;
        online = true;
    }

    public WaitingPlayer(Player player, boolean state) {
        this.player = player;
        online = state;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ICommunication getView() {
        return virtualView;
    }

    public void setView(ICommunication virtualView) {
        this.virtualView = virtualView;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaitingPlayer that = (WaitingPlayer) o;
        return Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, virtualView);
    }
}
