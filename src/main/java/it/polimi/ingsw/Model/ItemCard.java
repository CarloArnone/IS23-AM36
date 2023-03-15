package it.polimi.ingsw.Model;

public class ItemCard {

    private char color;
    private String image; //TODO: work in progress PD.

    public ItemCard(char color, String image) {
        this.color = color;
        this.image = image;
    }
    public ItemCard() {
        this.color = 'z';
        this.image = null;
    }

    public char getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setColor(char color) {
        this.color = color;
    }
}
