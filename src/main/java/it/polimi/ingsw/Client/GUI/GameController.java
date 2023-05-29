package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Classes.Tile;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    GUI guiRef = GUI.getInstance();

    @FXML
    private GridPane board;
    @FXML
    private HBox pickPlace;
    @FXML
    private GridPane MyShelf;
    @FXML
    private HBox selectableCols;
    @FXML
    private Button col;
    @FXML
    private Button select;
    @FXML
    private Integer column;

    List<Tile> pickTiles = new ArrayList<>();
    List<Tile> boardTiles = new ArrayList<>();

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiRef.setController(this);
        boardTiles = getBoardTiles();
        updateBoard();
        updateMyShelf();
        col.setVisible(false);

    }

    private void updateMyShelf() {
        Player updateMe = guiRef.getViewLivingRoom().getPlayers().stream().filter(x -> x.equals(guiRef.getMySelf())).findFirst().get();

        List<Tile> MyShelfTiles = getShelfTiles(updateMe.getMyShelf().getShelf(), 30, 30);
        for (Tile tile : MyShelfTiles) {
            MyShelf.add(tile.getImageView(), tile.getPosX(), tile.getPosY());
        }
    }

    private List<Tile> getBoardTiles() {
        List<Tile> tiles = new ArrayList<>();
        guiRef.getViewLivingRoom().getBoard().entrySet().forEach(pos -> tiles.add(new Tile(pos.getKey(), pos.getValue())));
        return tiles;
    }

    private List<Tile> getShelfTiles(Optional<ItemCard>[][] shelf, int h, int w){
        List<Tile> toReturn = new ArrayList<>();
        for(int r = 0; r < shelf.length; r++){
            for(int c = 0; c< shelf[0].length; c++){
                if(shelf[r][c].isPresent()){
                    toReturn.add(new Tile(shelf[r][c].get(), r, c, false, h, w));
                }
            }
        }

        return toReturn;
    }


    @FXML
    public void selectTilesFromBoard(){
        List<BoardPosition> boardPositions = new ArrayList<>();
        for(Tile tile : pickTiles){
            boardPositions.add(new BoardPosition(tile.getPosX(), tile.getPosY(), new ItemCard(tile.getColor(), tile.getImage())));
        }

        guiRef.getVirtualViewClient().isPossiblePick(guiRef.getMySelf(), guiRef.getViewLivingRoom().getLivingRoomId(), boardPositions);
    }

    public void updateBoard(){
        for (Tile boardTile : boardTiles) {
            boardTile.getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    boardTile.trigger();
                    if(boardTile.isSelected()){
                        boardTile.getImageView().setFitWidth(70);
                        boardTile.getImageView().setFitHeight(70);
                        pickTiles.add(boardTile);
                        boardTiles.remove(boardTile);
                    }
                    else{
                        boardTile.getImageView().setFitWidth(77);
                        boardTile.getImageView().setFitHeight(77);
                        pickTiles.remove(boardTile);
                        boardTiles.add(boardTile);
                    }
                }
            });

            boardTile.getImageView().setDisable(! boardTile.isAvailable());
            board.add(boardTile.getImageView(), boardTile.getPosY(), boardTile.getPosX());
        }
    }

    public void pickIsPossible(){
        col.setVisible(true);
        updatePick();
        updateBoard();
        showPick();
    }

    private void showPick() {
        for(int i = 0; i < pickTiles.size(); i++){
            pickPlace.getChildren().add(i, pickTiles.get(i).getImageView());
        }

    }

    private void updatePick() {
        pickTiles.clear();
        for(ItemCard card : guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn()).getDrawnCards()){
            pickTiles.add(new Tile(card, 0, 0, false));
        }
    }


    @FXML
    public void confirmPlacement(){

        LivingRoom livingRoom = guiRef.getViewLivingRoom();
        Player me =  guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());

        guiRef.getVirtualViewClient().confirmEndTurn(livingRoom, me, pickFromItemCards(pickTiles), column);


    }

    public List<BoardPosition> pickFromItemCards(List<Tile> itemCards) {
        List<BoardPosition> Truepick = new ArrayList<>();
        for(int i = 0; i < itemCards.size(); i++){
            Tile t = itemCards.get(i);
            Truepick.add(i, new BoardPosition(t.getPosX(), t.getPosY(), new ItemCard(t.getColor(), t.getImage())));
        }
        return Truepick;
    }


}
