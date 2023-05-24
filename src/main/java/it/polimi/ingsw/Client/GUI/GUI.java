package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
import it.polimi.ingsw.Common.Utils.IUI;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Controller.Controller;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import it.polimi.ingsw.Server.Model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.security.spec.ECField;
import java.util.*;

import static java.lang.Integer.parseInt;
import static javafx.application.Application.launch;

public class GUI extends IUI {
    Stage window;
    static GUI Gui;
    int playersNumber;
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
    static final double inputSceneSize = 100;
    static final int pointsImageSize = 90;
    LivingRoom livingRoom;
    Scene scene;
    GridPane livingRoomBoard;
    Controller controller;

    public void start(Stage stage) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/MyShelfie-Intro-Scene.fxml"));
            //Parent root1 = FXMLLoader.load;
            scene = new Scene(root);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
            window = stage;
            //windowSettingsMainScene(window);
            //Default window
            Scene firstScene = generateFirstScene();
            Scene mainScene = generateGameBoardScene();
            window.show();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static GUI getInstance(){
        if(Gui == null) return new GUI();
        else return Gui;
    }

    public static GUI getInstance(ICommunication virtualView){
        if(Gui == null)
            Gui = new GUI(virtualView);

        return Gui;
    }

    public GUI(){

    }

    public GUI(ICommunication virtualView){
        addVirtualViewClient(virtualView);
    }
    private Scene generateGameBoardScene(){
        ArrayList<Tile> tiles = new ArrayList<>();
        StackPane general = new StackPane();
        StackPane center = new StackPane();
        livingRoomBoard = addLivingroomPane();
        HBox topMenu = new HBox();
        FlowPane littleShelvesMaster = new FlowPane();
        FlowPane commonGoalsPane = new FlowPane();
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
        playButton.setTranslateY(3);
        exitButton.setTranslateY(3);

        //BorderPane right
        List<CommonGoalCard> commonGoalCards = new ArrayList<>();   //TODO: crea una funzione che estrae queste info dal json
        for(int i = 0; i < 2; i++){
            CommonGoalCard commonGoalCard = new CommonGoalCard(3*i + 1,2*i);
            commonGoalCards.add(commonGoalCard);
        }
        rightPaneSettings(commonGoalsPane);
        commonGoalsPane.setMinWidth(littleShelfSize);
        commonGoalsPane.setMinHeight(littleShelfSize);

        //Border pane left
        HBox leftMenu = new HBox();
        leftMenu.setMaxHeight(livingroomsize);
        leftMenu.getChildren().add(littleShelvesMaster);
        leftMenu.getChildren().add(personalGoals);
        ArrayList<GridPane> shelvesGridPanes = new ArrayList<>();   //This list gets filled inside the leftSidesettings function
        shelvePaneSettings(littleShelvesMaster,shelvesGridPanes);
        personalGoalsPaneSettings(personalGoals);

        //drawGameboard, only the center
        //drawGameboard(livingRoomBoard);
        //drawShelves
        if(livingRoom != null) {
            drawShelves(shelvesGridPanes);
            //drawCommonGoals
            drawCommonGoals(commonGoalsPane);
        }
        //Border pane creation
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);
        borderPane.setCenter(center);    //Sets the livingroom in the middle
        borderPane.setLeft(leftMenu);
        borderPane.setRight(commonGoalsPane);

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
        button.setOnAction(clickEvent -> {
            //Send name to controller
            //wait for response
            Stage inputWindow = new Stage();
            Scene mainScene = generateGameBoardScene();
            inputWindow.setScene(mainScene);
            windowSettingsMainScene(inputWindow);

        });
        firstScene.getChildren().addAll(label,button);
        ImageView inputBackground = new ImageView("17_MyShelfie_BGA/misc/base_pagina2.jpg");
        inputBackground.setPreserveRatio(false);
        inputBackground.setFitWidth(inputSceneSize);
        inputBackground.setFitHeight(inputSceneSize);
        Scene scene = new Scene(firstScene,inputSceneSize,inputSceneSize);
        return scene;
    }

    private void windowSettingsMainScene(Stage window) {
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
        rightPane.setPadding(new Insets(10, 0, 0, 0));
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
                        clearGameBoard(livingroomBoard);
                        //drawGameboard(livingroomBoard);
                    }else if(returnValue == 1){
                        clickPlay();
                    }
                });
    }

    private void clickPlay(){
        getVirtualViewClient().createGameEvent("Pero",new Player("Davide"),3);
    }

    private void setExitButtonAction(Button exitButton) {
        exitButton.setOnAction(event -> {
            if (ConfirmBox.display("Warning", "Are you sure you want to quit?")) {
                window.close();
            }
        });
    }

    private void drawTileLivingroom(ItemCard itemCard, int x, int y, GridPane livingroomBoard) {
        //Blue = (0,B),Green = (1,G),Yellow = (2,Y),white = (3,W),Purple = (4,P),Cyan = (5,C)
        Tile tile = addTile(itemCard, x, y);
        tile.setAvailable(livingRoom.getBoard().get(new BoardPosition(x,y)));
        tile.getImageView().setFitHeight(gameboardTileSize);
        tile.getImageView().setFitWidth(gameboardTileSize);
        tile.getImageView().setOnMouseClicked(mouseEvent -> {
            tile.toggle();
            if(tile.isAvailable()) {
                if (tile.isSelected()) {
                    tile.getImageView().setScaleX(rescaleWhenSelected);
                    tile.getImageView().setScaleY(rescaleWhenSelected);
                }
                if (!tile.isSelected()) {
                    tile.getImageView().setScaleX(1);
                    tile.getImageView().setScaleY(1);
                }
            }
            else{
                tile.setSelected(false);
            }
        });
        //GridPane.setConstraints(tile,x,y);
        livingroomBoard.add(tile.getImageView(),x,8 - y);
    }

    private void drawTileLittleShelf(ItemCard itemCard, int x, int y, GridPane littleShelf) {

        Tile tile = addTileLittleShelf(itemCard, x, y);

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

        for(int i = 0; i < playersNumber; i++) {
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
    }

    private List<StackPane> createCommonGoalsList(){
        List<StackPane> stackPanes = new ArrayList<>();
        List<ImageView> commonGoalsList = new ArrayList<>();
        for (int i = 0; i < 2; i++){
            stackPanes.add(new StackPane());
        }
        for(int i = 0; i < 2; i++){
            commonGoalsList.add(new ImageView("17_MyShelfie_BGA/common_goal_cards/"+ livingRoom.getCommonGoalSet().get(i).getName() +".jpg"));
            commonGoalsList.get(i).setPreserveRatio(true);
            commonGoalsList.get(i).setFitWidth(commonGoalTileSize);
            stackPanes.get(i).getChildren().add(commonGoalsList.get(i));
            drawPointsOnCommonGoal(stackPanes.get(i),livingRoom.getCommonGoalSet().get(i).getPointsList().get(0));
        }
        return stackPanes;
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
    private void drawGameboard(GridPane gameBoard){
        drawEndgameToken(gameBoard);    //TODO: disegna solo se necessario
        livingRoom.getBoard().forEach((key, value) -> { //key = position e card, boolean = selectable
            drawTileLivingroom(key.getCard(),key.getPosX(),key.getPosY(),gameBoard);
        });
    }
    private void drawShelves(ArrayList<GridPane> shelves){
        for(int i = 0; i < livingRoom.getPlayers().size(); i++){

            Optional<ItemCard>[][] shelf = livingRoom.getPlayers().get(i).getMyShelf().getShelf();
            for(int x = 0; x < shelfHeight;x++){
                for(int y=0; y < shelfWidth;y++){
                    if(shelf[x][y].isPresent()) {
                        drawTileLittleShelf(shelf[x][y].get(), x, y, shelves.get(i));
                    }
                }
            }
        }
    }
    private boolean isBoardPositionAvailable(int x, int y){
        if(!livingRoom.getBoard().get(new BoardPosition(x,y)).equals(null))
            return livingRoom.getBoard().get(new BoardPosition(x,y));
        else
            return false;
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
    private void drawPointsOnCommonGoal(StackPane commonGoalTile,int points){
        if(points < 10 && points % 2 == 0) {
            ImageView pointsImage = new ImageView("17_MyShelfie_BGA/scoring tokens/scoring_" + points + ".jpg");
            pointsImage.setPreserveRatio(true);
            pointsImage.setFitWidth(pointsImageSize);
            pointsImage.setFitHeight(pointsImageSize);
            pointsImage.setTranslateX(69);
            pointsImage.setTranslateY(-9);
            pointsImage.setRotate(-8);
            commonGoalTile.getChildren().add(pointsImage);
        }
        else{
            //TODO: aggiungi l'eccezione
        }
    }
    public void clearGameBoard(GridPane gameBoard){
                gameBoard.getChildren().clear();
    }
    private Tile addTile(ItemCard fatherCard, int x, int y){
        int tileNumber = 0;
        switch (fatherCard.getColor()){
            case 'B' -> tileNumber = 0;
            case 'G' -> tileNumber = 3;
            case 'Y' -> tileNumber = 6;
            case 'W' -> tileNumber = 9;
            case 'P' -> tileNumber = 12;
            case 'C' -> tileNumber = 15;
        }
        if(!fatherCard.getImage().equals(""))
            tileNumber = tileNumber + parseInt(fatherCard.getImage());
        Tile tile = new Tile(fatherCard,x,y,new ImageView("17_MyShelfie_BGA/item tiles/"+ tileNumber % 18 +".png"),isBoardPositionAvailable(x,y));
        return tile;
    }
    private Tile addTileLittleShelf(ItemCard fatherCard, int x, int y){
        int tileNumber = 0;
        switch (fatherCard.getColor()){
            case 'B' -> tileNumber = 0;
            case 'G' -> tileNumber = 3;
            case 'Y' -> tileNumber = 6;
            case 'W' -> tileNumber = 9;
            case 'P' -> tileNumber = 12;
            case 'C' -> tileNumber = 15;
        }
        if(!fatherCard.getImage().equals(""))
            tileNumber = tileNumber + parseInt(fatherCard.getImage());
        Tile tile = new Tile(fatherCard,x,y,new ImageView("17_MyShelfie_BGA/item tiles/"+ tileNumber % 18 +".png"),false);
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
        //TODO: add other players
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
        IUI Gui = new GUI();
        Gui.startIUI();
    }
    @Override
    public void startIUI() {
        launch();
    }
    @Override
    public void retryPlacement() {

    }

    @Override
    public void retryLogin() {

    }

    @Override
    public void livingRoomNotFound(String type) {

    }

    @Override
    public void retryCreateGame(String error, String livId) {

    }

    @Override
    public void notDisconnected() {

    }

    @Override
    public void gameNotStarted() {

    }

    @Override
    public void gameNotEnded() {

    }

    @Override
    public void retryPick() {

    }

    @Override
    public void turnPassed() {

    }

    @Override
    public void loginSuccessful() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/MyShelfie-Logged-In.fxml"));
            scene = new Scene(root);
        }
        catch (Exception e){
            System.out.println(e);
        }
        window.close();
        Stage window = new Stage();
        window.setScene(scene);
        window.show();
    }

    @Override
    public void disconnected() {

    }

    @Override
    public void livingRoomsList(String s, int section) {

    }

    @Override
    public void gameStarted() {

    }

    @Override
    public void gameEnded() {

    }

    @Override
    public void possiblePick(List<BoardPosition> pick) {

    }

    @Override
    public void livingRoomFound(LivingRoom livingRoomFromJsonString, String command) {
        setViewLivingRoom(livingRoomFromJsonString);
        playersNumber = livingRoomFromJsonString.getPlayers().size();
        drawGameboard(livingRoomBoard);
    }

    @Override
    public void joinedGame(Player playerFromJson, String livingRoomId) {

    }
}