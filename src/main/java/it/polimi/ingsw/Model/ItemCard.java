package it.polimi.ingsw.Model;

public class ItemCard {

    private Character color;
    private String image; //TODO: work in progress PD.

    public ItemCard(char color, String image) {
        this.color = color;
        this.image = image;
    }
    public ItemCard() {
        this.color = 'Z';
        this.image = "falseMatch";
    }

    public Character getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setColor(Character color) {
        this.color = color;
    }
}
