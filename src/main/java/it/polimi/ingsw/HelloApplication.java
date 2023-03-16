package it.polimi.ingsw;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HelloApplication extends Application {
    Stage window;
    Button button;

    int playersNumber = 4;
    static final int shelfWidth = 5;
    static final int shelfHeight = 6;
    static final int littleShelfSize = 175;
    static final int livingroomsize = 800;
    static final int windowHeight = 830;
    static final int windowLenght = 1400;
    static final int gameboardTileSize = 75;
    static final int endgameTokenSize = 100;
    static final int commonGoalTileSize = 300;
    static final int littleShelfTileSIze = 22;  //TODO: evaluate this constant
    static final int gameBoardSize = 9;
    static final double rescaleWhenSelected = 0.85;
    static final double inputSceneSize = 700;

    @Override
    public void start(Stage stage) throws IOException {


        window = stage;
        windowSettings(window);


        //Default window
        window.setScene(generateGameBoardScene());
        window.show();
    }

    private Scene generateGameBoardScene(){
        AtomicInteger PlayButtonChoice;
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles = CreateTileList(tiles);
        StackPane general = new StackPane();
        StackPane center = new StackPane();
        GridPane livingRoomBoard = addLivingroomPane();
        HBox topMenu = new HBox();
        FlowPane littleShelvesMaster = new FlowPane();
        FlowPane commonGoals = new FlowPane();
        FlowPane personalGoals = new FlowPane();

        //Border pane center
        livingRoomBoard.setMaxHeight(livingroomsize);
        livingRoomBoard.setMaxWidth(livingroomsize);
        ImageView livingroomImage = new ImageView("/17_MyShelfie_BGA/boards/livingroom.png");
        livingRoomImageSettings(livingroomImage);
        center.getChildren().add(livingroomImage);
        center.getChildren().add(livingRoomBoard);

        //Border pane top
        Button playButton = new Button(" Play ");
        Button exitButton = new Button(" X ");
        playButton.setStyle("-fx-background-color: green;-fx-font-weight: bold");
        exitButton.setStyle("-fx-background-color: red;  -fx-font-weight: bold");
        setExitButtonAction(exitButton);
        setPlayButtonAction(playButton,livingRoomBoard,tiles);
        topMenu.getChildren().addAll(playButton,exitButton);
        topMenu.setAlignment(Pos.CENTER);

        //BorderPane right
        rightPaneSettings(commonGoals);
        drawCommonGoals(commonGoals);
        commonGoals.setMinWidth(littleShelfSize);
        commonGoals.setMinHeight(littleShelfSize);

        //Border pane left
        HBox leftMenu = new HBox();
        leftMenu.setMaxHeight(livingroomsize);
        leftMenu.getChildren().add(littleShelvesMaster);
        leftMenu.getChildren().add(personalGoals);
        ArrayList<GridPane> shelvesGridPanes = new ArrayList<>();   //This list gets filled inside the leftSidesettings function
        shelvePaneSettings(littleShelvesMaster,shelvesGridPanes);
        personalGoalsPaneSettings(personalGoals);

        drawGameboard(livingRoomBoard, tiles);
        for(int i = 0; i < shelfWidth; i++) {
            for (int j = 0; j <shelfHeight; j++)
                drawTileLittleShelf(i*j,i,j,shelvesGridPanes.get(0));
        }

        //Border pane creation
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(center);    //Sets the livingroom in the middle
        borderPane.setLeft(leftMenu);
        borderPane.setRight(commonGoals);

        ImageView backGround = new ImageView("17_MyShelfie_BGA/misc/base_pagina2.jpg");
        //TODO: setta le dimensioni
        general.getChildren().add(backGround);
        general.getChildren().add(borderPane);
        Scene scene = new Scene(general, 100, 100);
        return scene;
    }
    private Scene generateFirstScene(){
        StackPane firstScene = new StackPane();
        Label label = new Label("Welcome to shefie");
        Button button = new Button("Start playing");
        firstScene.getChildren().addAll(label,button);
        ImageView inputBackground = new ImageView("17_MyShelfie_BGA/misc/base_pagina2.jpg");
        inputBackground.setPreserveRatio(false);
        inputBackground.setFitWidth(inputSceneSize);
        inputBackground.setFitHeight(inputSceneSize);
        Scene scene = new Scene(firstScene,inputSceneSize,inputSceneSize);
        return scene;
    }

    private void windowSettings(Stage window) {
        //Window settings
        window.setTitle("My shelfie");
        window.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            if (ConfirmBox.display("Warning", "Are you sure you want to quit?")) {
                window.close();
            }
        });
        window.initStyle(StageStyle.UNDECORATED);
        window.setWidth(1480);
        window.setHeight(windowHeight);
        window.setResizable(false);
    }

    private void rightPaneSettings(FlowPane rightPane) {
        rightPane.setMaxHeight(800);
        rightPane.setMinWidth(commonGoalTileSize);
        rightPane.setPadding(new Insets(20, 0, 0, 0));
        rightPane.setVgap(0);
        rightPane.setHgap(20);
        rightPane.setPrefWrapLength(150); // preferred width allows for two columns
        //rightPane.setStyle("-fx-background-image:url('/17_MyShelfie_BGA/misc/sfondo_parquet.jpg')");
        /*
        ImageView pages[] = new ImageView[2];
        for (int i = 0; i < 2; i++) {
            pages[i] = new ImageView("17_MyShelfie_BGA/common_goal_cards/"+ String.valueOf(i+1) +".jpg");
            pages[i].setPreserveRatio(true);
            pages[i].setFitHeight(littleShelfSize);
            pages[i].setFitWidth(littleShelfSize);
            rightPane.getChildren().add(pages[i]);
        }
         */
    }

    private void shelvePaneSettings(FlowPane shelvePane, ArrayList<GridPane> gridPanes) {
        shelvePane.setPadding(new Insets(30, 0, 5, 10));
        shelvePane.setVgap(10);
        shelvePane.setHgap(10);
        shelvePane.setPrefWrapLength(150); // preferred width allows for two columns
        //shelvePane.setStyle("-fx-background-image:url('/17_MyShelfie_BGA/misc/sfondo_parquet.jpg')");
        ArrayList<StackPane> stackPanes = new ArrayList<>();
        ArrayList<ImageView> imageviews = new ArrayList<>();
        for (int i = 0; i < playersNumber; i++){
            gridPanes.add(addLittleShelfGridPane());
        }
        for(int i = 0; i < playersNumber; i++){
            stackPanes.add(new StackPane());
        }
        for (int i = 0; i < playersNumber; i++) {
            imageviews.add(new ImageView("/17_MyShelfie_BGA/boards/bookshelf_orth.png"));
            imageviews.get(i).setPreserveRatio(true);
            imageviews.get(i).setFitHeight(littleShelfSize);
            imageviews.get(i).setFitWidth(littleShelfSize);
            imageviews.get(i).setOnMouseClicked(mouseEvent -> {
                AlertBox.display("Helo","Sei un coglione"); //TODO: togli
            });
        }
        for(int k = 0; k < playersNumber; k++){
            stackPanes.get(k).getChildren().add(imageviews.get(k));
            stackPanes.get(k).getChildren().add(gridPanes.get(k));
            shelvePane.getChildren().add(stackPanes.get(k));

        }
    }

    private void setPlayButtonAction(Button playButton,GridPane livingroomBoard,ArrayList<Tile> tiles) {


        playButton.setOnAction(clickEvent -> {
                    int returnValue;
                    List<String> choices = new ArrayList<>();
                    choices.add("Restart a previous game");
                    choices.add("Start a game");
                    choices.add("Join a game");
                    returnValue = (ChoiceBox.display("Play", "How do you want to play?", choices));
                    if(returnValue == 0) {
                        drawGameboard(livingroomBoard, tiles);
                    }else if(returnValue == 1){
                        clearGameBoard(livingroomBoard);
                    }
                });
    }

    private void setExitButtonAction(Button exitButton) {
        exitButton.setOnAction(event -> {
            if (ConfirmBox.display("Warning", "Are you sure you want to quit?")) {
                window.close();
            }
        });
    }

    private void drawTileLivingroom(int tileNumber, int x, int y, GridPane livingroom) {

        Tile tile = addTile(tileNumber, x, y);

        tile.getImageView().setFitHeight(gameboardTileSize);
        tile.getImageView().setFitWidth(gameboardTileSize);
        tile.getImageView().setOnMouseClicked(mouseEvent -> {
            tile.toggle();
            if (tile.isSelected()) {
                tile.getImageView().setScaleX(rescaleWhenSelected);
                tile.getImageView().setScaleY(rescaleWhenSelected);
            }
            if (!tile.isSelected()) {
                tile.getImageView().setScaleX(1);
                tile.getImageView().setScaleY(1);
            }
        });
        //GridPane.setConstraints(tile,x,y);
        livingroom.add(tile.getImageView(), x, y);
    }

    private void drawTileLittleShelf(int tileNumber, int x, int y, GridPane littleShelf) {

        Tile tile = addTile(tileNumber % 18, x, y);

        tile.getImageView().setFitHeight(littleShelfTileSIze);
        tile.getImageView().setFitWidth(littleShelfTileSIze);
        //GridPane.setConstraints(tile,x,y);
        if(x < shelfWidth && y < shelfHeight) {
            littleShelf.add(tile.getImageView(), x, y);
        }
    }
    private void personalGoalsPaneSettings(FlowPane personalGoalsPane){
        personalGoalsPane.setPadding(new Insets(30, 0, 5, 30));
        personalGoalsPane.setVgap(10);
        personalGoalsPane.setHgap(10);
        personalGoalsPane.setPrefWrapLength(150); // preferred width allows for two columns
        //personalGoalsPane.setStyle("-fx-background-image:url('/17_MyShelfie_BGA/misc/sfondo_parquet.jpg')");

        ArrayList<StackPane> stackPanes = new ArrayList<>();
        ArrayList<ImageView> imageviews = new ArrayList<>();

        for(int i = 0; i < playersNumber; i++){
            stackPanes.add(new StackPane());
        }
        for (int i = 0; i < playersNumber; i++) {
            if(i == 0) {
                imageviews.add(new ImageView("17_MyShelfie_BGA/personal goal cards/Personal_Goals.png"));
            }
            else {
                imageviews.add(new ImageView("17_MyShelfie_BGA/personal goal cards/back.jpg"));

            }
            imageviews.get(i).setPreserveRatio(true);
            imageviews.get(i).setFitHeight(littleShelfSize);
            imageviews.get(i).setFitWidth(littleShelfSize);
            imageviews.get(i).setOnMouseClicked(mouseEvent -> {
                AlertBox.display("Helo","Sei un coglione"); //TODO: togli
            });
            personalGoalsPane.getChildren().add(imageviews.get(i));
        }

    }
    private ArrayList<Tile> CreateTileList(ArrayList<Tile> tiles){
        for(int i = 0; i < gameBoardSize; i++){
            for(int j = 0; j < gameBoardSize; j++){
                Tile tile = addTile(i*j,i,j);
                if(nonUsedMatrix(4).get(i).get(j).equals(1)) {
                    tiles.add(tile);
                }
            }
        }
        return tiles;
    }
    private void livingRoomImageSettings(ImageView livingroomImage){
        livingroomImage.setPreserveRatio(true);
        livingroomImage.setFitWidth(livingroomsize);
        livingroomImage.setFitHeight(livingroomsize);
    }
    private void setSceneBackground(Pane scene,ImageView background){
        scene.getChildren().add(background);
    }
    private void drawCommonGoals(Pane Scene){
        Scene.getChildren().addAll(createCommonGoalsList());
        //TODO: create a createCommonGoalsList function which creates a list of common goals from a json file
        // Scene.getChildren().addAll(createCommonGoalsList);
    }

    private List<ImageView> createCommonGoalsList(){
        List<ImageView> commonGoalsList = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            commonGoalsList.add(new ImageView("17_MyShelfie_BGA/common_goal_cards/" + String.valueOf(i +1) +".jpg"));
            commonGoalsList.get(i).setPreserveRatio(true);
            commonGoalsList.get(i).setFitWidth(commonGoalTileSize);
        }

        return commonGoalsList;
    }
    public GridPane addLittleShelfGridPane(){
        GridPane grid = new GridPane();
        final int numCols = shelfWidth;
        final int numRows = shelfHeight;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            grid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            grid.getRowConstraints().add(rowConst);
        }
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPadding(new Insets(11, 14.5, 20, 20));
        grid.setPrefSize(littleShelfSize, littleShelfSize);

        return grid;
    }
    public GridPane addLivingroomPane() {
        GridPane grid = new GridPane();
        final int numCols = gameBoardSize ;
        final int numRows = gameBoardSize ;
        for (int i = 0; i < numCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            grid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < numRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            grid.getRowConstraints().add(rowConst);
        }
        grid.setHgap(0);
        grid.setVgap(0);
        grid.setPadding(new Insets(37, 37, 37, 37));
        grid.setPrefSize(gameboardTileSize, gameboardTileSize);

        return grid;
    }

    /**
     * /forall tiles in json
     *      drawTile(tile);
     * @param gameBoard
     *
     */
    private void drawGameboard(GridPane gameBoard,ArrayList<Tile> tiles){
        drawEndgameToken(gameBoard);    //TODO: disegna solo se necessario
        ArrayList<ArrayList<Integer>> matrix = nonUsedMatrix(4);
        for(int i = 0; i < tiles.size(); i++){
            drawTileLivingroom(tiles.get(i).getTileNumber(),tiles.get(i).getXpos(), tiles.get(i).getYpos(),gameBoard);
        }
    }
    private void drawEndgameToken(GridPane gameBoard){
        StackPane stackPane = new StackPane();
        ImageView endgameToken = new ImageView("17_MyShelfie_BGA/scoring tokens/end game.jpg");
        endgameToken.setFitHeight(endgameTokenSize);
        endgameToken.setFitWidth(endgameTokenSize);
        endgameToken.setRotate(15);
        endgameToken.setTranslateX(-45);
        stackPane.getChildren().add(endgameToken);
        stackPane.setMaxSize(225,225);

        GridPane.setColumnSpan(stackPane,4);

        gameBoard.add(stackPane,8,6,2,2);
    }
    public void clearGameBoard(GridPane gameBoard){
                gameBoard.getChildren().clear();
    }
    private void drawLittleShelves(List<GridPane> littleShelve,ArrayList<Tile> tiles){
        for(int i = 0; i < littleShelve.size(); i++){
            drawTileLittleShelf(tiles.get(i).getTileNumber(),tiles.get(i).getXpos(), tiles.get(i).getYpos(),littleShelve.get(i));
        }

    }
    private Tile addTile(int tileNumber,int x,int y){
        Tile tile = new Tile(tileNumber,x,y,new ImageView("17_MyShelfie_BGA/item tiles/"+ String.valueOf(tileNumber % 18)+".png"));
        return tile;
    }
    private ArrayList<ArrayList<Integer>> nonUsedMatrix(int playersNumber){
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        ArrayList<Integer> row0 = new ArrayList<>();
        ArrayList<Integer> row1 = new ArrayList<>();
        ArrayList<Integer> row2 = new ArrayList<>();
        ArrayList<Integer> row3 = new ArrayList<>();
        ArrayList<Integer> row4 = new ArrayList<>();
        ArrayList<Integer> row5 = new ArrayList<>();
        ArrayList<Integer> row6 = new ArrayList<>();
        ArrayList<Integer> row7 = new ArrayList<>();
        ArrayList<Integer> row8 = new ArrayList<>();
        if(playersNumber == 4) {
            row0.add(0, 0);
            row0.add(1, 0);
            row0.add(2, 0);
            row0.add(3, 0);
            row0.add(4, 1);
            row0.add(5, 1);
            row0.add(6, 0);
            row0.add(7, 0);
            row0.add(8, 0);

            row1.add(0, 0);
            row1.add(1, 0);
            row1.add(2, 0);
            row1.add(3, 1);
            row1.add(4, 1);
            row1.add(5, 1);
            row1.add(6, 0);
            row1.add(7, 0);
            row1.add(8, 0);

            row2.add(0, 0);
            row2.add(1, 0);
            row2.add(2, 1);
            row2.add(3, 1);
            row2.add(4, 1);
            row2.add(5, 1);
            row2.add(6, 1);
            row2.add(7, 0);
            row2.add(8, 0);

            row3.add(0, 1);
            row3.add(1, 1);
            row3.add(2, 1);
            row3.add(3, 1);
            row3.add(4, 1);
            row3.add(5, 1);
            row3.add(6, 1);
            row3.add(7, 1);
            row3.add(8, 0);

            row4.add(0, 1);
            row4.add(1, 1);
            row4.add(2, 1);
            row4.add(3, 1);
            row4.add(4, 1);
            row4.add(5, 1);
            row4.add(6, 1);
            row4.add(7, 1);
            row4.add(8, 1);

            row5.add(0, 0);
            row5.add(1, 1);
            row5.add(2, 1);
            row5.add(3, 1);
            row5.add(4, 1);
            row5.add(5, 1);
            row5.add(6, 1);
            row5.add(7, 1);
            row5.add(8, 1);

            row6.add(0, 0);
            row6.add(1, 0);
            row6.add(2, 1);
            row6.add(3, 1);
            row6.add(4, 1);
            row6.add(5, 1);
            row6.add(6, 1);
            row6.add(7, 0);
            row6.add(8, 0);

            row7.add(0, 0);
            row7.add(1, 0);
            row7.add(2, 0);
            row7.add(3, 1);
            row7.add(4, 1);
            row7.add(5, 1);
            row7.add(6, 0);
            row7.add(7, 0);
            row7.add(8, 0);

            row8.add(0, 0);
            row8.add(1, 0);
            row8.add(2, 0);
            row8.add(3, 1);
            row8.add(4, 1);
            row8.add(5, 0);
            row8.add(6, 0);
            row8.add(7, 0);
            row8.add(8, 0);
        }

        matrix.add(row0);
        matrix.add(row1);
        matrix.add(row2);
        matrix.add(row3);
        matrix.add(row4);
        matrix.add(row5);
        matrix.add(row6);
        matrix.add(row7);
        matrix.add(row8);

        return matrix;
    }

    public static void main(String[] args) {
        launch();
    }
}