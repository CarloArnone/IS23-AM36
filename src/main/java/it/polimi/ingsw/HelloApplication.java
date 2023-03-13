package it.polimi.ingsw;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    int littleShelfSize = 150;
    int livingroomsize = 800;
    int windowHeight = 800;
    int tileSize = 75;
    int gameBoardSize = 9;
    @Override
    public void start(Stage stage) throws IOException {


        window = stage;
        windowSettings(window);

        StackPane center = new StackPane();
        GridPane livingRoomBoard = addGridPane();

        HBox topMenu = new HBox();
        FlowPane shelves = new FlowPane();
        FlowPane commonGoals = new FlowPane();

        //Border pane center
        //livingRoomBoard.setStyle("-fx-background-image:url('/17_MyShelfie_BGA/boards/livingroom.png')");
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
            System.out.println("X: "+ x +" Y: "+ y);
        });

        //Border pane top
        Button playButton = new Button(" Play ");
        Button exitButton = new Button(" Exit ");
        setExitButtonAction(exitButton);
        setPlayButtonAction(playButton);
        topMenu.getChildren().addAll(playButton,exitButton);

        //BorderPane right
        //rightPaneSettings(commonGoals);
        drawCommonGoals(commonGoals);
        commonGoals.setMinWidth(500);
        commonGoals.setMinHeight(500);

        //Border pane left
        leftPaneSettings(shelves);

        //Draw the tiles
        for (int i = 0;i < gameBoardSize;i++) {
            for(int j = 0; j < gameBoardSize; j++){
                drawTileLivingroom(i,j,livingRoomBoard, "17_MyShelfie_BGA/item tiles/"+ i*j % 18+".png"); //TODO:usare questa funzione
            }

        }


        //Border pane creation
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(center);    //Sets the livingroom in the middle
        borderPane.setLeft(shelves);
        borderPane.setRight(commonGoals);

        Scene scene = new Scene(borderPane,100,100);

        //Default window
        window.setScene(scene);
        window.show();
    }
    private void windowSettings(Stage window){
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
        ImageView pages[] = new ImageView[playersNumber];
        for (int i = 0; i < playersNumber; i++) {
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
    private void leftPaneSettings(FlowPane rightPane){
        rightPane.setPadding(new Insets(5, 0, 5, 0));
        rightPane.setVgap(10);
        rightPane.setHgap(10);
        rightPane.setPrefWrapLength(150); // preferred width allows for two columns
        rightPane.setStyle("-fx-background-image:url('/17_MyShelfie_BGA/misc/sfondo_parquet.jpg')");
        ImageView pages[] = new ImageView[2];
        for (int i=0; i<2; i++) {
            Stage subWindow = new Stage();
            subWindow.setHeight(800);   //This is the picture height
            subWindow.setWidth(750);
            pages[i] = new ImageView("/17_MyShelfie_BGA/boards/bookshelf.png");
            pages[i].setPreserveRatio(true);
            pages[i].setFitHeight(littleShelfSize);
            pages[i].setFitWidth(littleShelfSize);
            rightPane.getChildren().add(pages[i]);
            pages[i].setOnMouseClicked(mouseEvent -> {

                StackPane shelf = new StackPane();
                ImageView shelfImage = new ImageView("/17_MyShelfie_BGA/boards/bookshelf.png");
                shelfImage.setFitHeight(750);
                shelfImage.setPreserveRatio(true);
                shelf.getChildren().add(shelfImage);
                Scene scene = new Scene(shelf,800,800);
                subWindow.setScene(scene);
                subWindow.showAndWait();

            });
        }


    }
    private void setPlayButtonAction(Button playButton){
        playButton.setOnAction(e->{
            List<String> choices = new ArrayList<>();
            choices.add("Restart a previous game");
            choices.add("Start a game");
            choices.add("Join a game");
            System.out.println(ChoiceBox.display("Play","How do you want to play?",choices));
        });
    }
    private void setExitButtonAction(Button exitButton){
        exitButton.setOnAction(event -> {
            if(ConfirmBox.display("Warning","Are you sure you want to quit?")) {
                window.close();
            }
        });
    }
    private void drawTileLivingroom(int x, int y, String tileUrl, Pane livingRoomBoard){
        ImageView tile = new ImageView(tileUrl);
        tile.setPreserveRatio(true);
        tile.setFitWidth(tileSize);
        tile.setFitHeight(tileSize);
        tile.setLayoutX(x * 64);
        tile.setLayoutX(y * 64);
        livingRoomBoard.getChildren().add(tile);
    }
    private void drawTileLivingroom(int x,int y,Pane livingroom,String imageUrl){
        Canvas tileBoard = new Canvas(tileSize,tileSize);
        GraphicsContext tileCanvas = tileBoard.getGraphicsContext2D();
        ImageView tile = new ImageView(imageUrl);
        tile.setPreserveRatio(true);
        tile.setFitWidth(tileSize);
        tile.setFitHeight(tileSize);
        tileCanvas.drawImage(tile.getImage(),x*64,y*64);
        livingroom.getChildren().add(tile);
    }
    private void drawTileLivingroom(int x,int y,GridPane livingroom,String imageUrl){
        ImageView tile = new ImageView(imageUrl);
        tile.setFitHeight(tileSize);
        tile.setFitWidth(tileSize);
        //GridPane.setConstraints(tile,x,y);
        livingroom.add(tile,x,y);
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
    public GridPane addGridPane() {
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
        grid.setPrefSize(tileSize,tileSize);

        return grid;
    }

    public static void main(String[] args) {
        launch();
    }
}