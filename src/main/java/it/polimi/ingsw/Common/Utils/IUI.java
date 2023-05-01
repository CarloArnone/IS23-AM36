package it.polimi.ingsw.Common.Utils;

import it.polimi.ingsw.Common.Exceptions.NoMatchingIDException;
import it.polimi.ingsw.Common.Exceptions.NotEnoughSpacesInCol;
import it.polimi.ingsw.Common.Exceptions.ToManyCardsException;
import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class IUI implements Listener {
    private LivingRoom viewLivingRoom;
    private List<BoardPosition> pick;
    private int myTurn; // It means MyTURN
    private Player mySelf;

    private ICommunication virtualViewClient;

    public IUI(ICommunication virtualViewClient){
        this.virtualViewClient = virtualViewClient;
    }

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

    public int getMyTurn() {
        return myTurn;
    }

    public void setMyTurn(int me) {
        this.myTurn = me;
    }

    public Player getMySelf() {
        return mySelf;
    }

    public void setMySelf(Player mySelf) {
        this.mySelf = mySelf;
    }

    public ICommunication getVirtualViewClient() {
        return virtualViewClient;
    }

    public void addVirtualViewClient(ICommunication virtualViewClient) {
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

        viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(myTurn), pickList);
        //UPDATE SERVER SIDE ???
    }


    public boolean isEligiblePick(List<BoardPosition> possiblePick){
        virtualViewClient.isPossiblePick(mySelf, viewLivingRoom.getLivingRoomId(), possiblePick);
        return Boolean.parseBoolean(BlackBoard.read("isPossiblePickReturn"));
    }

    private void checkColAndPlaceTiles(int col) throws NotEnoughSpacesInCol {

        List<BoardPosition> pickToSave = new ArrayList<>();
        for(int i = 0; i<pick.size(); i++){
            pickToSave.add(new BoardPosition(pick.get(i).getPosX(), pick.get(i).getPosY(), viewLivingRoom.getPlayers().get(myTurn).getDrawnCards().get(i)));
        }

        virtualViewClient.confirmEndTurn(viewLivingRoom, viewLivingRoom.getPlayers().get(myTurn), pickToSave, col -1);
        if(!Boolean.parseBoolean(BlackBoard.read("disconnectionReturn"))){
            throw new NotEnoughSpacesInCol();
        }
        pick.clear();
    }

    private void orderPickInsert(List<Pair<Integer, ItemCard>> order) throws ToManyCardsException {
        List<ItemCard> pickCopy = new ArrayList<>();
        order.sort(Comparator.comparingInt(Pair::getKey));

        for(Pair<Integer, ItemCard> positionsOrdered : order){
            pickCopy.add(positionsOrdered.getKey(), positionsOrdered.getValue());
        }

        try {
            viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(myTurn), pickCopy );
        } catch (ToManyCardsException e) {
            throw new ToManyCardsException();
        }
    }

    private boolean quitAGame() {
        virtualViewClient.leaveGameEvent(mySelf.getName(), viewLivingRoom, virtualViewClient);
        return Boolean.parseBoolean(BlackBoard.read("disconnectionReturn"));
    }

    private boolean resetBoard(){
        virtualViewClient.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId());
        return Boolean.parseBoolean(BlackBoard.read("livingRoomFoundReturn"));
    }

    public void updateLivingRoom(LivingRoom livingRoom) {
        this.viewLivingRoom = livingRoom;
    }

    public void updatePlayer(Player player) {
        this.mySelf = player;
    }

    public void notifyListener() {
        virtualViewClient.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId());
    }

}
