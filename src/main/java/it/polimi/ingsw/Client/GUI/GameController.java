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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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

    @FXML
    private StackPane BigShelfStackPane;
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
    private HBox messageBox;

    @FXML
    private VBox commonGoalsView;

    @FXML
            private ImageView personalGoal;


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

        pickTiles.addListener((ListChangeListener<Tile>) change -> {
            while(change.next()){
                if (change.wasAdded() || change.wasRemoved()) {
                    updatePickPlace();
                    break;
                }
            }
        } );

        selectableCols.setDisable(true);
        showPersonalGoal();

    }

    public void showMessage(String message){
        Platform.runLater(() -> {
            messageBox.getChildren().clear();
            messageBox.getChildren().add(new Text(message));
        });

    }

    public void updateLivingRoomView() {
        Platform.runLater(() -> {
            updateBoard();
            updateMyShelf();
            drawLittleShelves();
            updateTurn();
            updateCommonGoals();
        });

    }
    @FXML
    public void quitGame(){
        Platform.runLater(() -> {
            guiRef.quitAGame();
        });
    }

    private void updateCommonGoals() {
        commonGoalsView.getChildren().clear();
        for(CommonGoalCard commonGoalCard : guiRef.getViewLivingRoom().getCommonGoalSet()){
            StackPane commonGoalX = new StackPane();
            String commonGoalNameForPath = "/17_MyShelfie_BGA/commongoalcards/" + commonGoalCard.getName() + ".jpg";
            ImageView commonGoalImage = new ImageView(JSONInterface.findCorrectPathFromResources(commonGoalNameForPath));
            commonGoalImage.setPreserveRatio(true);
            commonGoalImage.setFitHeight(250);
            commonGoalX.getChildren().add(commonGoalImage);
            HBox toPlaceTokens = new HBox();
            toPlaceTokens.setAlignment(Pos.CENTER_RIGHT);
            toPlaceTokens.setPadding(new Insets(0, 120, 0, 0));
            List<Integer> pointsAvailable = commonGoalCard.getPointsList();
            pointsAvailable.sort((i1, i2) -> i1 - i2 >= 0 ? i1 : i2);
            StackPane placement = new StackPane();
            String path = "/17_MyShelfie_BGA/scoringtokens/scoring_" + pointsAvailable.get(0) + ".jpg";
            ImageView pointToken = new ImageView(path);
            pointToken.setPreserveRatio(true);
            pointToken.setFitHeight(100);
            placement.getChildren().add(pointToken);

            toPlaceTokens.getChildren().add(placement);
            commonGoalX.getChildren().add(toPlaceTokens);
            commonGoalsView.getChildren().add(commonGoalX);
        }

    }

    private void updateTurn() {
        String turnOfPlayer = "";
        if(guiRef.getViewLivingRoom().getTurn() == guiRef.getViewLivingRoom().getPlayerTurn(guiRef.getMySelf())){
            turnOfPlayer = "It's my turn";
        }
        else turnOfPlayer = "It's " +livingRoomRep.get().getPlayers().get(livingRoomRep.get().getTurn()).getName() + " turn.";
        showMessage(turnOfPlayer);
    }

    private void drawLittleShelves() {
        littleShelvesPlace.getChildren().clear();
        List<Player> playerNotMe = guiRef.getViewLivingRoom().getPlayers().stream().filter(x -> ! x.equals(guiRef.getMySelf())).toList();
        for(Player player : playerNotMe){
            drawShelf(player, 200);
        }
    }

    private void drawShelf(Player player, int height) {
        int tileHeight = (int) (height/9.85);
        int rowSpace = height/45;
        int colSpace = rowSpace/3;
        double pTop = height/9.09;
        double pRight = height/9.85;
        double pBottom = height/11.82;
        double pLeft = height/10.85;
        VBox toCenter = new VBox();
        toCenter.setAlignment(Pos.CENTER);
        List<Tile> shelfTiles = getShelfTiles(player.getMyShelf().getShelf(), tileHeight, tileHeight);
        StackPane shelfStackPane = new StackPane();
        shelfStackPane.setPadding(new Insets(pTop, pRight, pBottom, pLeft));
        GridPane shelfGrid = new GridPane();
        shelfGrid.setHgap(rowSpace);
        shelfGrid.setVgap(colSpace);
        for(int r = 0; r < 6; r ++){
            RowConstraints row = new RowConstraints();
            row.setPercentHeight((double) 100 /6);
            row.setValignment(VPos.CENTER);
            shelfGrid.getRowConstraints().add(row);
        }
        for(int r = 0; r < 5; r ++){
            ColumnConstraints row = new ColumnConstraints();
            row.setPercentWidth((double) 100 /5);
            row.setHalignment(HPos.CENTER);
            shelfGrid.getColumnConstraints().add(row);
        }

        shelfGrid.setPadding(new Insets(32, 30, 25, 30));
        String path = "/17_MyShelfie_BGA/boards/bookshelf_orth.png";
        ImageView shelfImage = new ImageView(JSONInterface.findCorrectPathFromResources(path));
        shelfImage.setPreserveRatio(true);
        shelfImage.setFitHeight(height);


        for(Tile tile : shelfTiles){
            shelfGrid.add(tile.getImageView(), tile.getPosY(), tile.getPosX());
        }

        shelfStackPane.getChildren().addAll(shelfGrid, shelfImage);
        toCenter.getChildren().add(shelfStackPane);
        littleShelvesPlace.getChildren().add(toCenter);
    }

    private void updatePickPlace() {
        pickPlace.getChildren().clear();
        for(Tile tile : pickTiles){
            pickPlace.getChildren().add(tile.getImageViewCopy(60, 60));
        }
    }

    private void updateMyShelf() {
        Player updateMe = guiRef.getViewLivingRoom().getPlayers().stream().filter(x -> x.equals(guiRef.getMySelf())).findFirst().get();

        List<Tile> MyShelfTiles = getShelfTiles(updateMe.getMyShelf().getShelf(), 60, 60);
        for (Tile tile : MyShelfTiles) {
            MyShelf.add(tile.getImageView(), tile.getPosY(), tile.getPosX());
        }
    }

    private List<Tile> getBoardTiles() {
        List<Tile> tiles = new ArrayList<>();
        guiRef.getViewLivingRoom().getBoard().entrySet().forEach(pos -> tiles.add(new Tile(pos.getKey(), pos.getValue())));
        //livingRoomRep.get().getBoard().entrySet().forEach(pos -> tiles.add(new Tile(pos.getKey(), pos.getValue())));
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

        guiRef.selectTilesFromBoard(boardPositions);
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
                            boardTile.getImageView().getStyleClass().clear();
                            boardTile.getImageView().getStyleClass().add("selectedCard");
                            pickTiles.add(boardTile);
                        }
                        else{
                            boardTile.getImageView().getStyleClass().clear();
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
            updateBoard();
            showPossibleCols();
        });

    }

    private void showPossibleCols() {
        Platform.runLater(() -> {
            selectableCols.setDisable(false);
            //BigShelfStackPane.getChildren().stream().filter(x -> x.getId() != null && x.getId().equals("cols_Box")).findFirst().get().toFront();
            Player me = guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());
            List<Boolean> possibleCols = me.getMyShelf().getSelectableCols(pickTiles.size());
            ObservableList<Node> possibleColsRegions = selectableCols.getChildren();
            for(int i = 0; i< possibleCols.size(); i++){
                possibleColsRegions.get(i).setDisable(! possibleCols.get(i));
            }
        });
    }

    void resetCols(){
        Platform.runLater(() -> {
            selectableCols.setDisable(true);
            ObservableList<Node> possibleColsRegions = selectableCols.getChildren();
            for(int i = 0; i< possibleColsRegions.size(); i++){
                possibleColsRegions.get(i).setDisable(false);
            }
        });
    }


    public List<BoardPosition> pickFromItemCards(List<Tile> itemCards) {
        List<BoardPosition> Truepick = new ArrayList<>();
        for(int i = 0; i < itemCards.size(); i++){
            Tile t = itemCards.get(i);
            Truepick.add(i, new BoardPosition(t.getPosX(), t.getPosY(), new ItemCard(t.getColor(), t.getImage())));
        }
        return Truepick;
    }


    public void clearPick() {
        Platform.runLater(() -> {
            this.pickTiles.clear();
        });

    }
    @FXML
    public void placeInOne(){
        LivingRoom livingRoom = guiRef.getViewLivingRoom();
        Player me =  guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());

        guiRef.getVirtualViewClient().confirmEndTurn(livingRoom, me, pickFromItemCards(pickTiles), 0);
    }

    @FXML
    public void placeInTwo(){
        LivingRoom livingRoom = guiRef.getViewLivingRoom();
        Player me =  guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());

        guiRef.getVirtualViewClient().confirmEndTurn(livingRoom, me, pickFromItemCards(pickTiles), 1);
    }

    @FXML
    public void placeInThree(){
        LivingRoom livingRoom = guiRef.getViewLivingRoom();
        Player me =  guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());

        guiRef.getVirtualViewClient().confirmEndTurn(livingRoom, me, pickFromItemCards(pickTiles), 2);
    }

    @FXML
    public void placeInFour(){
        LivingRoom livingRoom = guiRef.getViewLivingRoom();
        Player me =  guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());

        guiRef.getVirtualViewClient().confirmEndTurn(livingRoom, me, pickFromItemCards(pickTiles), 3);
    }

    @FXML
    public void placeInFive(){
        LivingRoom livingRoom = guiRef.getViewLivingRoom();
        Player me =  guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn());

        guiRef.getVirtualViewClient().confirmEndTurn(livingRoom, me, pickFromItemCards(pickTiles), 4);
    }

    public void showPersonalGoal(){
        Platform.runLater(() -> {
            PersonalGoalCard personalGoalCard = guiRef.getViewLivingRoom().getPlayers().get(guiRef.getMyTurn()).getPersonalGoal();

            Image ps = new Image(JSONInterface.findCorrectPathFromResources("/17_MyShelfie_BGA/personalgoalcards/" + personalGoalCard.getName() + ".png"));
            personalGoal.setImage(ps);
        });
    }

    @FXML
    public void closeWindow(){
        System.exit(0);
    }
}
