package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Server.Model.ItemCard;
import javafx.scene.image.ImageView;

import static java.lang.Integer.parseInt;

/**
 *
 */
public class Tile extends ItemCard {
    private final int tileNumber;
    private boolean selected = false;
    private boolean available = false;
    private int xpos;
    private int ypos;
    ImageView imageView;

    public Tile(ItemCard fatherCard, int xpos, int ypos, ImageView imageView,boolean available) {
        int startingTileNumber = 0;
        switch (fatherCard.getColor()) {
            case 'B' -> startingTileNumber = 0;
            case 'G' -> startingTileNumber = 3;
            case 'Y' -> startingTileNumber = 6;
            case 'W' -> startingTileNumber = 9;
            case 'P' -> startingTileNumber = 12;
            case 'C' -> startingTileNumber = 15;
        }
        if(!fatherCard.getImage().equals("")) {
            this.tileNumber = startingTileNumber + parseInt(fatherCard.getImage());
        }
        else
            tileNumber = 0;
        this.xpos = xpos;
        this.ypos = ypos;
        this.imageView = imageView;
    }
    public boolean isAvailable(){
        return available;
    }

    public boolean isSelected() {
        return selected;
    }

    public int getXpos() {
        return xpos;
    }

    public int getYpos() {
        return ypos;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setXpos(int xpos) {
        this.xpos = xpos;
    }

    public void setYpos(int ypos) {
        this.ypos = ypos;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void toggle(){
        this.selected = !this.selected;
    }
}
