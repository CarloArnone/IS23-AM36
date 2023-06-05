package it.polimi.ingsw.Common.Utils;

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

    private boolean firstEnter;
    private ICommunication virtualViewClient;

    public boolean isFirstEnter() {
        return firstEnter;
    }

    public void setFirstEnter(boolean firstEnter) {
        this.firstEnter = firstEnter;
    }

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

    public void selectTilesFromBoard(List<BoardPosition> possiblePick) {
        virtualViewClient.isPossiblePick(mySelf, viewLivingRoom.getLivingRoomId(), possiblePick);
    }

    public void moveFromBoardToShelf(List<BoardPosition> pick) throws ToManyCardsException {
        List<ItemCard> pickList = new ArrayList<>();
        this.pick = pick;
        for (BoardPosition position : pick){
            viewLivingRoom.removeCard(position);
            pickList.add(position.getCard());
        }

        viewLivingRoom.givePlayerTheirPick(viewLivingRoom.getPlayers().get(myTurn), pickList);
        //UPDATE SERVER SIDE ???
    }

    public void checkColAndPlaceTiles(int col){

        List<BoardPosition> pickToSave = new ArrayList<>();
        for(int i = 0; i<pick.size(); i++){
            pickToSave.add(new BoardPosition(pick.get(i).getPosX(), pick.get(i).getPosY(), viewLivingRoom.getPlayers().get(myTurn).getDrawnCards().get(i)));
        }

        virtualViewClient.confirmEndTurn(viewLivingRoom, viewLivingRoom.getPlayers().get(myTurn), pickToSave, col -1);
    }

    public void orderPickInsert(List<Pair<Integer, ItemCard>> order) throws ToManyCardsException {
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

    public void quitAGame() {
        virtualViewClient.leaveGameEvent(mySelf.getName(), viewLivingRoom, virtualViewClient);
    }

    public void resetBoard(){
        virtualViewClient.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId(), mySelf.getName());
    }

    public void updateLivingRoom(LivingRoom livingRoom) {
        this.viewLivingRoom = livingRoom;
    }

    public void updatePlayer(Player player) {
        this.mySelf = player;
    }

    public void notifyListener(String message) {
        String[] messageToControl = message.split(" ");
        switch (messageToControl[0]){
            case "GameStarted" -> gameStarted();
            case "Update" -> getVirtualViewClient().retrieveOldGameEvent(getViewLivingRoom().getLivingRoomId(), mySelf.getName());
            case "LeftGame" -> otherPlayerDisconnected(messageToControl[1], true);
            case "LeftGameCrush" -> otherPlayerDisconnected(messageToControl[1], false);
            case "GameEnded" -> gameEnded( messageToControl[1]);
        }
        //virtualViewClient.retrieveOldGameEvent(viewLivingRoom.getLivingRoomId());
    }

    public abstract void otherPlayerDisconnected(String s, boolean b);

    public void startGame() {
        gameStarted();
        notifyListener("Update");
    }


    public abstract void retryPlacement();

    public abstract void retryLogin();

    public abstract void livingRoomNotFound(String type);

    public abstract void retryCreateGame(String error, String livId);

    public abstract void notDisconnected();

    public abstract void gameNotStarted();

    public abstract void gameNotEnded();

    public abstract void retryPick();

    public abstract void turnPassed();

    public abstract void loginSuccessful();

    public abstract void disconnected();

    public abstract void livingRoomsList(String s, int section);

    public abstract void gameStarted();

    public abstract void gameEnded(String message);

    public abstract void possiblePick(List<BoardPosition> pick);

    public abstract void livingRoomFound(LivingRoom livingRoomFromJsonString, String command);

    public abstract void joinedGame(Player playerFromJson, LivingRoom livingRoom);

    public abstract void gameNotJoined(String arg);
}
