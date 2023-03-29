package it.polimi.ingsw.Common;

import it.polimi.ingsw.Client.CLI.CLI;
import it.polimi.ingsw.Server.Model.Player;

import java.util.Objects;

public class WaitingPlayer {
    Player player;
    CLI view;
    boolean online;

    public WaitingPlayer(Player player, CLI view) {
        this.player = player;
        this.view = view;
        online = true;
    }

    public WaitingPlayer(Player player) {
        this.player = player;
        online = true;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CLI getView() {
        return view;
    }

    public void setView(CLI view) {
        this.view = view;
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
        return Objects.hash(player, view);
    }
}
