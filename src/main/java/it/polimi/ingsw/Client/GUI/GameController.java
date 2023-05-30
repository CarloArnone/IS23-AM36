package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.GUI.Classes.Tile;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

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

    @FXML
    private HBox littleShelvesPlace;
    @FXML
    private GridPane infoGame;


    @FXML
    private VBox commonGoalsView;


    ObservableWrapper<LivingRoom> livingRoomRep;
    ObservableList<Tile> pickTiles = FXCollections.observableArrayList();

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        livingRoomRep = new ObservableWrapper<>(guiRef.getViewLivingRoom());
        livingRoomRep.addListener(new ChangeListener<LivingRoom>() {
            @Override
            public void changed(ObservableValue<? extends LivingRoom> observableValue, LivingRoom livingRoom, LivingRoom t1) {
                updateLivingRoomView();
            }
        });
        guiRef.setController(this);
        updateLivingRoomView();
        col.setVisible(false);

        pickTiles.addListener((ListChangeListener<Tile>) change -> {
            while(change.next()){
                if (change.wasAdded() || change.wasRemoved()) {
                    updatePickPlace();
                    break;
                }
            }
        } );



    }

    private void updateLivingRoomView() {
        updateBoard();
        updateMyShelf();
        drawLittleShelves();
        updateTurn();
        updateCommonGoals();
    }

    private void updateCommonGoals() {
        commonGoalsView.getChildren().clear();
        for(CommonGoalCard commonGoalCard : livingRoomRep.get().getCommonGoalSet()){
            StackPane commonGoalX = new StackPane();
            String commonGoalNameForPath = "/17_MyShelfie_BGA/commongoalcards/" + commonGoalCard.getName() + ".jpg";
            ImageView commonGoalImage = new ImageView(JSONInterface.findCorrectPathFromResources(commonGoalNameForPath));
            commonGoalImage.setPreserveRatio(true);
            commonGoalImage.setFitHeight(250);
            commonGoalX.getChildren().add(commonGoalImage);
            HBox toPlaceTokens = new HBox();
            toPlaceTokens.setAlignment(Pos.CENTER_RIGHT);
            toPlaceTokens.setPadding(new Insets(0, 50, 0, 0));
            List<Integer> pointsAvailable = commonGoalCard.getPointsList();
            StackPane placement = new StackPane();
            for(Integer point : pointsAvailable){
                String path = "/17_MyShelfie_BGA/scoringtokens/scoring_" + point + ".jpg";
                ImageView pointToken = new ImageView(path);
                pointToken.setPreserveRatio(true);
                pointToken.setFitHeight(100);
                placement.getChildren().add(pointToken);
            }
            toPlaceTokens.getChildren().add(placement);
            commonGoalX.getChildren().add(toPlaceTokens);
            commonGoalsView.getChildren().add(commonGoalX);
        }

    }

    private void updateTurn() {
        infoGame.getChildren().clear();
        String turnOfPlayer = "It's " +livingRoomRep.get().getPlayers().get(livingRoomRep.get().getTurn()).getName() + " turn.";
        Text turnInfo = new Text(turnOfPlayer);
        infoGame.add(turnInfo, 3, 0);
    }

    private void drawLittleShelves() {
        littleShelvesPlace.getChildren().clear();
        List<Player> playerNotMe = livingRoomRep.get().getPlayers().stream().filter(x -> x.equals(guiRef.getMySelf())).toList();
        for(Player player : playerNotMe){
            drawShelf(player, 200);
        }
    }

    private void drawShelf(Player player, int height) {
        List<Tile> shelfTiles = getShelfTiles(player.getMyShelf().getShelf(), height/6, height/6);
        StackPane shelfStackPane = new StackPane();
        GridPane shelfGrid = new GridPane();
        shelfGrid.setHgap(15);
        shelfGrid.setVgap(3);
        for(int r = 0; r < 6; r ++){
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100/6);
            shelfGrid.getRowConstraints().add(row);
        }
        for(int r = 0; r < 5; r ++){
            ColumnConstraints row = new ColumnConstraints();
            row.setPercentWidth(100/5);
            shelfGrid.getColumnConstraints().add(row);
        }

        shelfGrid.setPadding(new Insets(32, 30, 25, 30));
        String path = "/17_MyShelfie_BGA/boards/bookshelf_orth.png";
        ImageView shelfImage = new ImageView(JSONInterface.findCorrectPathFromResources(path));
        shelfImage.setPreserveRatio(true);
        shelfImage.setFitHeight(height);


        for(Tile tile : shelfTiles){
            shelfGrid.add(tile.getImageViewCopy(), tile.getPosX(), tile.getPosY());
        }

        shelfStackPane.getChildren().addAll(shelfGrid, shelfImage);

        littleShelvesPlace.getChildren().add(shelfStackPane);
    }

    private void updatePickPlace() {
        pickPlace.getChildren().clear();
        for(Tile tile : pickTiles){
            pickPlace.getChildren().add(tile.getImageViewCopy(60, 60));
        }
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
        //guiRef.getViewLivingRoom().getBoard().entrySet().forEach(pos -> tiles.add(new Tile(pos.getKey(), pos.getValue())));
        livingRoomRep.get().getBoard().entrySet().forEach(pos -> tiles.add(new Tile(pos.getKey(), pos.getValue())));
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
        board.getChildren().clear();
        List<Tile> boardTiles = getBoardTiles();
            for (Tile boardTile : boardTiles) {
                boardTile.getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        boardTile.trigger();
                        if(boardTile.isSelected()){
                            boardTile.getImageView().setFitWidth(70);
                            boardTile.getImageView().setFitHeight(70);
                            pickTiles.add(boardTile);
                        }
                        else{
                            boardTile.getImageView().setFitWidth(77);
                            boardTile.getImageView().setFitHeight(77);
                            pickTiles.remove(boardTile);
                        }
                    }
                });

                boardTile.getImageView().setDisable(! boardTile.isAvailable());
                board.add(boardTile.getImageView(), boardTile.getPosY(), boardTile.getPosX());
            }
    }

    public void pickIsPossible(){
        Platform.runLater(() -> {
            col.setVisible(true);
            updateBoard();
            showPossibleCols();
        });

    }

    private void showPossibleCols() {
        Platform.runLater(() -> {

            Player me = guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());
            List<Boolean> possibleCols = me.getMyShelf().getSelectableCols(pickTiles.size());

            for(int i = 0; i< possibleCols.size(); i++){

                RadioButton butt = new RadioButton(String.valueOf(i));
                butt.setVisible(possibleCols.get(i));
                selectableCols.getChildren().add(butt);
            }
        });
    }


    @FXML
    public void confirmPlacement(){

        LivingRoom livingRoom = guiRef.getViewLivingRoom();
        Player me =  guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());
        selectableCols.getChildren().clear();

        guiRef.getVirtualViewClient().confirmEndTurn(livingRoom, me, pickFromItemCards(pickTiles), 1);


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
