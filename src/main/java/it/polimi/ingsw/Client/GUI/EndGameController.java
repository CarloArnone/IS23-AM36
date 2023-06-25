package it.polimi.ingsw.Client.GUI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {
    GUI guiRef = GUI.getInstance();
    private int maxScore = 0;
    private List<String> winner = new ArrayList<>();
    private List<String> losers = new ArrayList<>();
    private String winnerString;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label bottomLabel;
    @FXML
    private Label topLabel;
    @FXML
    private ImageView trophy;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trophy.setVisible(false);
        maxScore = guiRef.getViewLivingRoom().getWinner().getScore();
        for (int i = 0; i < guiRef.getViewLivingRoom().getPlayers().size();i++){
            if(guiRef.getViewLivingRoom().getPlayers().get(i).getScore() >= maxScore){
            }
            else{
                losers.add(guiRef.getViewLivingRoom().getPlayers().get(i).getName());
            }
        }
        if(GUI.getInstance().getViewLivingRoom().getWinner().getName().equals(GUI.getInstance().getMySelf().getName())){
            trophy.setVisible(true);
        }

        winnerLabel.setText(GUI.getInstance().getViewLivingRoom().getWinner().getName());
        topLabel.setText("The winner is...");
        if(losers.isEmpty()) {
            bottomLabel.setText("The losers: " + losers);
        }
        else{
            bottomLabel.setText("Everybody won, so everybody lost");
        }
    }
}
