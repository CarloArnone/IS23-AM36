package it.polimi.ingsw.Client.GUI;

        import it.polimi.ingsw.Common.Utils.Comunication.ICommunication;
        import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewClientSocket;
        import it.polimi.ingsw.Common.Utils.IUI;
        import it.polimi.ingsw.Common.Utils.JSONInterface;
        import it.polimi.ingsw.Server.Model.BoardPosition;
        import it.polimi.ingsw.Server.Model.LivingRoom;
        import it.polimi.ingsw.Server.Model.Player;
        import javafx.application.Platform;
        import javafx.beans.Observable;
        import javafx.beans.property.SimpleObjectProperty;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.stage.Stage;

        import java.io.File;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;


public class GUI extends IUI {

    Stage stage;

    public static GUI INSTANCE;

    private List<String> livingRoomsList;

    private GameController controller;



    @Override 
    public void start(Stage stage) throws IOException {
        File fxmlFile = new File(JSONInterface.findCorrectPathFromResources("/FXML/login.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlFile.toURL());
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("LoginPage");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public GUI(){
        VirtualViewClientSocket client = VirtualViewClientSocket.getINSTANCE();
        initalizeVirtualView(client);
        client.setUI(this);
        INSTANCE = this;
    }

    public static GUI getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GUI();
        }

        return INSTANCE;
    }

    /**
     *
     */
    @Override
    public void startUI(ICommunication virtualView) {
        launch();
    }

    /**
     * @param s
     * @param b
     */
    @Override 
    public void otherPlayerDisconnected(String s, boolean b) {

    }

    /**
     *
     */
    @Override 
    public void retryPlacement() {

    }

    /**
     *
     */
    @Override 
    public void retryLogin() {

    }

    /**
     * @param type
     */
    @Override 
    public void livingRoomNotFound(String type) {

    }

    /**
     * @param error
     * @param livId
     */
    @Override 
    public void retryCreateGame(String error, String livId) {

    }

    /**
     *
     */
    @Override 
    public void notDisconnected() {

    }

    /**
     *
     */
    @Override 
    public void gameNotStarted() {

    }

    /**
     *
     */
    @Override 
    public void gameNotEnded() {

    }

    /**
     *
     */
    @Override 
    public void retryPick() {

    }

    /**
     *
     */
    @Override 
    public void turnPassed() {

    }

    /**
     *
     */
    @Override 
    public void loginSuccessful() {
        setMySelf(new Player(getName()));
        loadScene("/FXML/CreateOrJoinGame.fxml", "ChoicePage");
    }

    public void loadScene(String pathTofxml, String title){
        Platform.runLater(() -> {
            stage.hide();
            File fxmlFile = new File(JSONInterface.findCorrectPathFromResources(pathTofxml));
            FXMLLoader fxmlLoader = null;
            try {
                fxmlLoader = new FXMLLoader(fxmlFile.toURL());
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle(title);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     *
     */
    @Override 
    public void disconnected() {

    }

    /**
     * @param s
     * @param section
     */
    @Override 
    public void livingRoomsList(String s, int section) {
        String[] livingRoomNames = s.split("-");
        livingRoomsList = new ArrayList<>();
        if (livingRoomsList != null) {
            livingRoomsList.clear();
        }
        livingRoomsList.addAll(Arrays.asList(livingRoomNames));
        loadScene("/FXML/joinGameScene.fxml", "JoinGame");
    }

    /**
     *
     */
    @Override 
    public void gameStarted() {
        loadScene("/FXML/GameScene.fxml", "Game");
    }

    /**
     * @param message
     */
    @Override 
    public void gameEnded(String message) {

    }

    /**
     * @param pick
     */
    @Override 
    public void possiblePick(List<BoardPosition> pick) {
        setPick(pick);
        for(BoardPosition boardPosition : pick){
            getViewLivingRoom().getBoard().remove(boardPosition);
        }
        controller.pickIsPossible();
    }

    /**
     * @param livingRoomFromJsonString
     * @param command
     */
    @Override 
    public void livingRoomFound(LivingRoom livingRoomFromJsonString, String command) {
        setViewLivingRoom(livingRoomFromJsonString);
    }

    /**
     * @param playerFromJson
     * @param livingRoom
     */
    @Override 
    public void joinedGame(Player playerFromJson, LivingRoom livingRoom) {
        setMySelf(playerFromJson);
        setViewLivingRoom(livingRoom);
        for(int i = 0; i < livingRoom.getPlayers().size(); i++){
            if(livingRoom.getPlayers().get(i).equals(playerFromJson)){
                setMyTurn(i);
            }
        }
        //SHOULD LOAD WAITING FOR PLAYERS SCENE
    }

    /**
     * @param arg
     */
    @Override 
    public void gameNotJoined(String arg) {

    }

    /**
     *
     */
    @Override 
    public void serverDiconnected() {

    }


    public void setStage(Stage window) {
        this.stage = window;
    }

    public List<String> getLivingRoomsList() {
        return livingRoomsList;
    }


    public void setController(GameController controller){
        this.controller = controller;
    }

}


