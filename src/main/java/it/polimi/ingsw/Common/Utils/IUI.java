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
    private String name;

    private ICommunication virtualViewClient;


    public LivingRoom getViewLivingRoom() {
        return viewLivingRoom;
    }

    public void initalizeVirtualView(ICommunication virtualViewClient){
        this.virtualViewClient = virtualViewClient;
    }

    public void launch(){
        this.launch();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    private void selectTilesFromBoard(List<BoardPosition> possiblePick) {
        virtualViewClient.isPossiblePick(mySelf, viewLivingRoom.getLivingRoomId(), possiblePick);

        /*if(isEligiblePick(possiblePick)){
            try {
            Add in return of is ElegiblePick
                moveFromBoardToShelf(possiblePick);
            } catch (ToManyCardsException e) {
                throw new ToManyCardsException();
            }
        }*/
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

    private void checkColAndPlaceTiles(int col){

        List<BoardPosition> pickToSave = new ArrayList<>();
        for(int i = 0; i<pick.size(); i++){
            pickToSave.add(new BoardPosition(pick.get(i).getPosX(), pick.get(i).getPosY(), viewLivingRoom.getPlayers().get(myTurn).getDrawnCards().get(i)));
        }

        virtualViewClient.confirmEndTurn(viewLivingRoom, viewLivingRoom.getPlayers().get(myTurn), pickToSave, col -1);
        // To add in the return of conferimend turn successfull
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

    private void quitAGame() {
        virtualViewClient.leaveGameEvent(mySelf.getName(), viewLivingRoom, virtualViewClient);
    }

    private void resetBoard(){
        virtualViewClient.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId());
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

    public void startGame() {
        notifyListener();
    }


    public abstract void retryPlacement();

    public abstract void retryLogin();

    public abstract void livingRoomNotFound();

    public abstract void retryCreateGame();

    public abstract void notDisconnected();

    public abstract void gameNotStarted();

    public abstract void gameNotEnded();

    public abstract void retryPick();

    public abstract void turnPassed();

    public abstract void loginSuccessful();

    public abstract void disconnected();

    public abstract void livingRoomsList(String s);

    public abstract void gameStarted();

    public abstract void gameEnded();

    public abstract void possiblePick();

    public abstract void livingRoomFound(LivingRoom livingRoomFromJsonString);

    public abstract void joinedGame(Player playerFromJson, String livingRoomId);
}
