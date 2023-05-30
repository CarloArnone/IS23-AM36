package it.polimi.ingsw.Client.GUI.Classes;

import it.polimi.ingsw.Common.Utils.JSONInterface;
import it.polimi.ingsw.Server.Model.BoardPosition;
import it.polimi.ingsw.Server.Model.ItemCard;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;

public class Tile extends ItemCard {
    private int posX;
    private int posY;
    private boolean selected;
    private boolean available;
    ImageView imageView;
    private String typeFile;

    public Tile(BoardPosition position, boolean available){
        this.available = available;
        this.selected = false;
        posX = position.getPosX();
        posY = position.getPosY();
        ItemCard card = position.getCard();
        this.setColor(position.getCard().getColor());
        this.setImage(position.getCard().getImage());
        String type = "/17_MyShelfie_BGA/item tiles/";
        switch (card.getColor()) {
            case 'B' -> type += "Cornici1.";
            case 'G' -> type += "Gatti1.";
            case 'Y' -> type += "Giochi1.";
            case 'W' -> type += "Libri1.";
            case 'P' -> type += "Piante1.";
            case 'C' -> type += "Trofei1.";
        }

        type += card.getImage() + ".png";
        this.typeFile = type;
        File fim = new File(JSONInterface.findCorrectPathFromResources(type));
        Image imageI = new Image(fim.toURI().toString());
        this.imageView = new ImageView(imageI);
        this.imageView.setFitHeight(77);
        this.imageView.setFitWidth(77);

    }

    public Tile(ItemCard card, int r, int c, boolean available){
        this.available = available;
        this.selected = false;
        posX = r;
        posY = c;
        this.setColor(card.getColor());
        this.setImage(card.getImage());
        String type = "/17_MyShelfie_BGA/item tiles/";
        switch (card.getColor()) {
            case 'B' -> type += "Cornici1.";
            case 'G' -> type += "Gatti1.";
            case 'Y' -> type += "Giochi1.";
            case 'W' -> type += "Libri1.";
            case 'P' -> type += "Piante1.";
            case 'C' -> type += "Trofei1.";
        }

        type += card.getImage() + ".png";
        this.typeFile = type;

        File fim = new File(JSONInterface.findCorrectPathFromResources(type));
        Image imageI = new Image(fim.toURI().toString());
        this.imageView = new ImageView(imageI);
        this.imageView.setFitHeight(20);
        this.imageView.setFitWidth(20);

    }

    public Tile(ItemCard card, int r, int c, boolean available, int h, int w){
        this.available = available;
        this.selected = false;
        posX = r;
        posY = c;
        this.setColor(card.getColor());
        this.setImage(card.getImage());
        String type = "/17_MyShelfie_BGA/item tiles/";
        switch (card.getColor()) {
            case 'B' -> type += "Cornici1.";
            case 'G' -> type += "Gatti1.";
            case 'Y' -> type += "Giochi1.";
            case 'W' -> type += "Libri1.";
            case 'P' -> type += "Piante1.";
            case 'C' -> type += "Trofei1.";
        }

        type += card.getImage() + ".png";
        this.typeFile = type;

        File fim = new File(JSONInterface.findCorrectPathFromResources(type));
        Image imageI = new Image(fim.toURI().toString());
        this.imageView = new ImageView(imageI);
        this.imageView.setFitHeight(h);
        this.imageView.setFitWidth(w);

    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void trigger(){
        selected = ! selected;
    }

    public ImageView getImageViewCopy() {
        return new ImageView(typeFile);
    }

    public ImageView getImageViewCopy(int h, int w) {
        ImageView im = new ImageView(typeFile);
        im.setFitHeight(h);
        im.setFitWidth(w);
        return im;
    }
}
