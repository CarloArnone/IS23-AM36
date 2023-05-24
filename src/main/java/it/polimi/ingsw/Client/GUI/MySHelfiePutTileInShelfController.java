package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Common.Utils.Comunication.Socket.VirtualViewClientSocket;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Model.ItemCard;
import it.polimi.ingsw.Server.Model.LivingRoom;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Optional;

public class MySHelfiePutTileInShelfController {
    @FXML
    RadioButton coloumn0,coloumn1,coloumn2,coloumn3,coloumn4;
    @FXML
    ImageView personalGoal,commonGoal0,commonGoal1;
    @FXML
    GridPane shelf;
    public void initialize(){

    }
    public void getSelectedColoumn(){
        if(coloumn0.isSelected()){
            System.out.println("0");
        }
        if(coloumn1.isSelected()){
            System.out.println("1");
        }
        if(coloumn2.isSelected()){
            System.out.println("2");
        }
        if(coloumn3.isSelected()){
            System.out.println("3");
        }
        if(coloumn4.isSelected()){
            System.out.println("4");
        }
    }
    public void confirmButtonAction() {
        shelf.getChildren().clear();
        LivingRoom livingRoom = JSONInterface.getRandomLivingForTest();
        Optional<ItemCard>[][] shelfMatrix = livingRoom.getPlayers().get(0).getMyShelf().getShelf();
        for (int i = 0; i < shelfMatrix.length; i++) {
            for (int j = 0; j < shelfMatrix[0].length; j++) {
                int finalI = i;
                int finalJ = j;
                shelf.setPadding(new Insets(0, 0, 0, 3));
                ImageView imageView = new ImageView();
                imageView.setFitWidth(70);
                imageView.setFitHeight(70);
                imageView.setImage(new Image("/17_MyShelfie_BGA/item tiles/4.png"));
                shelfMatrix[i][j].ifPresent(e -> shelf.add(imageView, finalJ, finalI));

            }
            livingRoom.getPlayers().get(0).getMyShelf().getShelf();
            personalGoal.setImage(new Image("/17_MyShelfie_BGA/personal goal cards/Personal_Goals2.png"));
            commonGoal0.setImage(new Image("/17_MyShelfie_BGA/common_goal_cards/DiagonalsOfFive.jpg"));
            commonGoal1.setImage(new Image("/17_MyShelfie_BGA/common_goal_cards/FourRowsMaxThreeTypes.jpg"));
        }
    }
}
