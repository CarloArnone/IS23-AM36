package it.polimi.ingsw.StartUp;

import javafx.scene.image.ImageView;

/**
 *
 */
public class Tile {
    private final int tileNumber;
    private boolean selected = false;
    private int xpos;
    private int ypos;
    ImageView imageView;

    public Tile(int tileNumber, int xpos, int ypos, ImageView imageView) {
        this.tileNumber = tileNumber;
        this.xpos = xpos;
        this.ypos = ypos;
        this.imageView = imageView;
    }

    public int getTileNumber() {
        return tileNumber;
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
