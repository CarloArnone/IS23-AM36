package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.ToManyCardsException;
import it.polimi.ingsw.Common.Listener;
import it.polimi.ingsw.Common.eventObserver;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class IUI implements Listener {
    private LivingRoom viewLivingRoom;
    private List<BoardPosition> pick;
    private int me; // It means MyTURN
    private String name;
    private Player mySelf;

    private eventObserver virtualViewClient;

    public LivingRoom getViewLivingRoom() {
        return viewLivingRoom;
    }

    public void setViewLivingRoom(LivingRoom viewLivingRoom) {
        this.viewLivingRoom = viewLivingRoom;
    }

    public List<BoardPosition> getPick() {
        return pick;
    }

    public void setPick(List<BoardPosition> pick) {
        this.pick = pick;
    }

    public int getMe() {
        return me;
    }

    public void setMe(int me) {
        this.me = me;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getMySelf() {
        return mySelf;
    }

    public void setMySelf(Player mySelf) {
        this.mySelf = mySelf;
    }

    public eventObserver getVirtualViewClient() {
        return virtualViewClient;
    }

    public void addVirtualViewClient(eventObserver virtualViewClient) {
        this.virtualViewClient = virtualViewClient;
    }

    private void selectTilesFromBoard(List<BoardPosition> possiblePick) throws ToManyCardsException {

        if(isEligiblePick(possiblePick)){
            try {
                moveFromBoardToShelf(possiblePick);
            } catch (ToManyCardsException e) {
                throw new ToManyCardsException();
            }
        }
    }

    private void moveFromBoardToShelf(List<BoardPosition> pick) throws ToManyCardsException {
        List<ItemCard> pickList = new ArrayList<>();
        this.pick = pick;
        for (BoardPosition position : pick){
            viewLivingRoom.removeCard(position);
            pickList.add(position.getCard());
        }

        viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(me), pickList);
        //UPDATE SERVER SIDE ???
    }


    public boolean isEligiblePick(List<BoardPosition> possiblePick){
        return virtualViewClient.isPossiblePick(mySelf, viewLivingRoom.getLivingRoomId(), possiblePick);
    }

    private void checkColAndPlaceTiles(int col) throws NotEnoughSpacesInCol {

        List<BoardPosition> pickToSave = new ArrayList<>();
        for(int i = 0; i<pick.size(); i++){
            pickToSave.add(new BoardPosition(pick.get(i).getPosX(), pick.get(i).getPosY(), viewLivingRoom.getPlayers().get(me).getDrawnCards().get(i)));
        }

        try {
            virtualViewClient.confirmEndTurn(viewLivingRoom, viewLivingRoom.getPlayers().get(me), pickToSave, col -1);
        } catch (NotEnoughSpacesInCol e) {
            throw new NotEnoughSpacesInCol();
        }
        pick.clear();
    }

    private void orderPickInsert(List<Pair<Integer, ItemCard>> order) throws ToManyCardsException {
        List<ItemCard> pickCopy = new ArrayList<>();
        order.sort((x, y) -> { return x.getKey() - y.getKey();});

        for(Pair<Integer, ItemCard> positionsOrdered : order){
            pickCopy.add(positionsOrdered.getKey(), positionsOrdered.getValue());
        }

        try {
            viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(me), pickCopy );
        } catch (ToManyCardsException e) {
            throw new ToManyCardsException();
        }
    }

    private void quitAGame() {
        virtualViewClient.leaveGameEvent(name, viewLivingRoom,this );
    }

    private void resetBoard() throws NoMatchingIDException {
        try {
            viewLivingRoom = virtualViewClient.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId());
        } catch (NoMatchingIDException e) {
            throw new NoMatchingIDException();
        }

    }
}
