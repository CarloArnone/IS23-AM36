package it.polimi.ingsw;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    Stage window;
    Button button;

    int playersNumber = 4;
    static final int littleShelfSize = 150;
    static final int livingroomsize = 800;
    static final int windowHeight = 800;
    static final int gameboardTileSize = 75;
    static final int littleShelfTileSIze = 20;  //TODO: evaluate this constant
    static final int gameBoardSize = 9;
    static final double rescaleWhenSelected = 0.85;

    @Override
    public void start(Stage stage) throws IOException {

        ArrayList<Tile> tiles = new ArrayList<>();
        tiles = CreateTileList(tiles);

        window = stage;
        windowSettings(window);

        StackPane center = new StackPane();

        ArrayList<StackPane> littleShelveStackPane = new ArrayList<>();
        for(int i = 0; i < playersNumber; i++){
            littleShelveStackPane.add(new StackPane());
        }
        GridPane livingRoomBoard = addLivingroomPane();

        HBox topMenu = new HBox();

        FlowPane littleShelvesMaster = new FlowPane();
        littleShelvesMaster.getChildren().addAll(littleShelveStackPane); //Left hierarchy: littleShelvesMaster -> 3 stackpanes
        ImageView shelvesImage = new ImageView("17_MyShelfie_BGA/boards/bookshelf.png");
        for(int i = 0; i < playersNumber; i++){
            //littleShelveStackPane.get(i).getChildren().addAll(shelvesImage,addLittleShelfPane());   //stackPanes -> imageview && gridpane
        }

        FlowPane commonGoals = new FlowPane();
        //Border pane center
        livingRoomBoard.setMaxHeight(livingroomsize);
        livingRoomBoard.setMaxWidth(livingroomsize);
        ImageView livingroomImage = new ImageView("/17_MyShelfie_BGA/boards/livingroom.png");
        livingRoomImageSettings(livingroomImage);
        center.getChildren().add(livingroomImage);
        center.getChildren().add(livingRoomBoard);
        livingroomImage.setOnMouseClicked(e ->
        {
            int x = (int) e.getX();
            int y = (int) e.getY();
            System.out.println("X: " + x + " Y: " + y);
        });

        //Border pane top
        Button playButton = new Button(" Play ");
        Button exitButton = new Button(" Exit ");
        setExitButtonAction(exitButton);
        setPlayButtonAction(playButton);
        topMenu.getChildren().addAll(playButton, exitButton);

        //BorderPane right
        //rightPaneSettings(commonGoals);
        drawCommonGoals(commonGoals);
        commonGoals.setMinWidth(500);
        commonGoals.setMinHeight(500);

        //Border pane left
        leftPaneSettings(littleShelvesMaster);
        //drawLittleShelves(shelves, tiles);
        for(int i = 0; i < playersNumber; i++){
            //littleShelveStackPane.get(i).getChildren().add()
        }
        //shelvesImage.getChildren().add(shelves);

        drawGameboard(livingRoomBoard, tiles);


        //Border pane creation
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(center);    //Sets the livingroom in the middle
        borderPane.setLeft(littleShelvesMaster);
        borderPane.setRight(commonGoals);

        Scene scene = new Scene(borderPane, 100, 100);

        //Default window
        window.setScene(scene);
        window.show();
    }

    private void windowSettings(Stage window) {
        //Window settings
        window.setTitle("My shelfie");
        window.setWidth(1200);
        window.setHeight(900);
        window.setResizable(false);
    }

    private void rightPaneSettings(FlowPane rightPane) {
        rightPane.setPadding(new Insets(5, 0, 5, 0));
        rightPane.setVgap(20);
        rightPane.setHgap(20);
        rightPane.setPrefWrapLength(150); // preferred width allows for two columns
        rightPane.setStyle("-fx-background-image:url('/17_MyShelfie_BGA/misc/sfondo_parquet.jpg')");
        ImageView pages[] = new ImageView[2];
        for (int i = 0; i < 2; i++) {
            Stage subWindow = new Stage();
            subWindow.setHeight(800);   //This is the picture height
            subWindow.setWidth(750);
            pages[i] = new ImageView("17_MyShelfie_BGA/common_goal_cards/1.jpg");
            pages[i].setPreserveRatio(true);
            pages[i].setFitHeight(littleShelfSize);
            pages[i].setFitWidth(littleShelfSize);
            rightPane.getChildren().add(pages[i]);
        }
    }

    private void leftPaneSettings(FlowPane leftPane) {
        leftPane.setPadding(new Insets(5, 0, 5, 0));
        leftPane.setVgap(10);
        leftPane.setHgap(10);
        leftPane.setPrefWrapLength(150); // preferred width allows for two columns
        leftPane.setStyle("-fx-background-image:url('/17_MyShelfie_BGA/misc/sfondo_parquet.jpg')");
        ImageView pages[] = new ImageView[2];
        for (int i = 0; i < 2; i++) {
            Stage subWindow = new Stage();
            subWindow.setHeight(800);   //This is the picture height
            subWindow.setWidth(750);
            pages[i] = new ImageView("/17_MyShelfie_BGA/boards/bookshelf.png");
            pages[i].setPreserveRatio(true);
            pages[i].setFitHeight(littleShelfSize);
            pages[i].setFitWidth(littleShelfSize);
            leftPane.getChildren().add(pages[i]);
            pages[i].setOnMouseClicked(mouseEvent -> {

                StackPane shelf = new StackPane();
                ImageView shelfImage = new ImageView("/17_MyShelfie_BGA/boards/bookshelf.png");
                shelfImage.setFitHeight(750);
                shelfImage.setPreserveRatio(true);
                shelf.getChildren().add(shelfImage);
                Scene scene = new Scene(shelf, 800, 800);
                subWindow.setScene(scene);
                subWindow.showAndWait();

            });
        }


    }

    private void setPlayButtonAction(Button playButton) {
        playButton.setOnAction(e -> {
            List<String> choices = new ArrayList<>();
            choices.add("Restart a previous game");
            choices.add("Start a game");
            choices.add("Join a game");
            System.out.println(ChoiceBox.display("Play", "How do you want to play?", choices));
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

        Tile tile = addTile(tileNumber, x, y);

        tile.getImageView().setFitHeight(littleShelfTileSIze);
        tile.getImageView().setFitWidth(littleShelfTileSIze);
        //GridPane.setConstraints(tile,x,y);
        littleShelf.add(tile.getImageView(), x, y);
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
            commonGoalsList.add(new ImageView("17_MyShelfie_BGA/common_goal_cards/7.jpg"));
            commonGoalsList.get(i).setFitHeight(300);
            commonGoalsList.get(i).setFitWidth(300);
        }

        return commonGoalsList;
    }
    public GridPane addLittleShelfPane(){
        GridPane grid = new GridPane();
        final int numCols = 5 ;
        final int numRows = 5 ;
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
        grid.setPadding(new Insets(5, 5, 5, 5));
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
        ArrayList<ArrayList<Integer>> matrix = nonUsedMatrix(4);
        for(int i = 0; i < tiles.size(); i++){
            drawTileLivingroom(tiles.get(i).getTileNumber(),tiles.get(i).getXpos(), tiles.get(i).getYpos(),gameBoard);
        }

    }
    private void drawLittleShelves(GridPane littleShelve,ArrayList<Tile> tiles){
        ArrayList<ArrayList<Integer>> matrix = nonUsedMatrix(4);
        for(int i = 0; i < tiles.size(); i++){
            drawTileLivingroom(tiles.get(i).getTileNumber(),tiles.get(i).getXpos(), tiles.get(i).getYpos(),littleShelve);
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