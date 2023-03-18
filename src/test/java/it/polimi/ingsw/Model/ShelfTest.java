package it.polimi.ingsw.Model;

import Exceptions.NotEnoughSpacesInCol;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ShelfTest {

    static Optional<ItemCard>[][] properShelf;
    static Shelf shelf;
    @BeforeAll
    static void Initialize(){

        properShelf = new Optional[6][5];
        for(int i = 0; i< 6; i++){
            for(int j = 0; j < 5; j++){
                properShelf[i][j] = Optional.empty();
            }
        }

        shelf = new Shelf(properShelf);

    }

    @Test
    void onClickCol() {


        ItemCard card1 = new ItemCard('C', "place");
        ItemCard card2 = new ItemCard('C', "place");
        ItemCard card3 = new ItemCard('Y', "place");
        ItemCard card4 = new ItemCard('B', "place");
        ItemCard card5 = new ItemCard('B', "place");

        ItemCard falseMatch = new ItemCard('Z', "false match");

        List<ItemCard> placeOneTile = new ArrayList<>();
        placeOneTile.add(card1);

        List<ItemCard> placeTwoTiles = new ArrayList<>();
        placeTwoTiles.add(0, card2);
        placeTwoTiles.add(1, card3);

        List<ItemCard> placeThreeTiles = new ArrayList<>();
        placeThreeTiles.add(0, card4);
        placeThreeTiles.add(1, card5);
        placeThreeTiles.add(2, card1);

        //firstPlacement - bottom of the Shelf
        try{
            shelf.onClickCol(placeOneTile, 0);
        }
        catch(NotEnoughSpacesInCol nes){
            assert false;
        }
        finally {
            assert shelf.getShelf()[5][0].orElse(falseMatch).equals(card1);
        }

        //placement of two tiles in a row - check correct order
        try{
            shelf.onClickCol(placeTwoTiles, 0);
        }
        catch(NotEnoughSpacesInCol nes){
            assert false;
        }
        finally {
            assert shelf.getShelf()[4][0].orElse(falseMatch).equals(card2);
            assert shelf.getShelf()[3][0].orElse(falseMatch).equals(card3);
        }

        //already checked this case -- usage only for setting up next case
        try{
            shelf.onClickCol(placeOneTile, 0);
        }
        catch(NotEnoughSpacesInCol nes){
            assert false;
        }

        //placement that throws exceptions three tiles/two spaces
        try{
            shelf.onClickCol(placeThreeTiles, 0);
        }
        catch(NotEnoughSpacesInCol nes){
            System.out.println(nes);
            assert true;
        }


        //placement of last two remaining of a col
        try{
            shelf.onClickCol(placeTwoTiles, 0);
        }
        catch(NotEnoughSpacesInCol nes){
            assert false;
        }
        finally {
            assert shelf.getShelf()[1][0].orElse(falseMatch).equals(card2);
            assert shelf.getShelf()[0][0].orElse(falseMatch).equals(card3);
        }

        //settingUp next case
        try{
            shelf.onClickCol(placeThreeTiles, 1);
            shelf.onClickCol(placeTwoTiles, 1);
        }
        catch(NotEnoughSpacesInCol nes){
            assert false;
        }

        //placement that throws exception two tiles/ one space
        try{
            shelf.onClickCol(placeTwoTiles, 1);
        }
        catch(NotEnoughSpacesInCol nes){
            assert true;
            System.out.println(nes);
        }

        try{
            shelf.onClickCol(placeTwoTiles, 5);
        }
        catch(NotEnoughSpacesInCol nes){
            assert true;
            System.out.println(nes);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    @Test
    void getPointsForAdjacent() {
        //TODO
        assert false;
    }

    @Test
    void getSelectableCols() {

        Optional<ItemCard>[][] properShelf1 = new Optional[6][5];
        for(int i = 0; i< 6; i++){
            for(int j = 0; j < 5; j++){
                properShelf1[i][j] = Optional.empty();
            }
        }
        Shelf shelf1 = new Shelf(properShelf1);

        ItemCard card1 = new ItemCard('C', "place");
        ItemCard card2 = new ItemCard('C', "place");
        ItemCard card3 = new ItemCard('Y', "place");
        ItemCard card4 = new ItemCard('B', "place");
        ItemCard card5 = new ItemCard('B', "place");

        ItemCard falseMatch = new ItemCard('Z', "false match");

        List<ItemCard> placeOneTile = new ArrayList<>();
        placeOneTile.add(card1);

        List<ItemCard> placeTwoTiles = new ArrayList<>();
        placeTwoTiles.add(0, card2);
        placeTwoTiles.add(1, card3);

        List<ItemCard> placeThreeTiles = new ArrayList<>();
        placeThreeTiles.add(0, card4);
        placeThreeTiles.add(1, card5);
        placeThreeTiles.add(2, card1);

        List<Boolean> insert3 = new ArrayList<>();
        insert3.add(false);
        insert3.add(false);
        insert3.add(false);
        insert3.add(false);
        insert3.add(false);

        List<Boolean> insert2 = new ArrayList<>();
        insert2.add(0, false);
        insert2.add(1, false);
        insert2.add(2, true);
        insert2.add(3, false);
        insert2.add(4, false);

        List<Boolean> insert1 = new ArrayList<>();
        insert2.add(0, false);
        insert2.add(1, true);
        insert2.add(2, true);
        insert2.add(3, true);
        insert2.add(4, false);

        try{
            shelf1.onClickCol(placeThreeTiles, 0);
            shelf1.onClickCol(placeThreeTiles, 0);

            shelf1.onClickCol(placeThreeTiles, 1);
            shelf1.onClickCol(placeTwoTiles, 1);

            shelf1.onClickCol(placeThreeTiles, 2);
            shelf1.onClickCol(placeOneTile, 2);

            shelf1.onClickCol(placeThreeTiles, 3);
            shelf1.onClickCol(placeTwoTiles, 3);

            shelf1.onClickCol(placeThreeTiles, 4);
            shelf1.onClickCol(placeThreeTiles, 4);
        }
        catch(NotEnoughSpacesInCol nes){
            assert shelf1.getSelectableCols(1).equals(insert1);
            assert shelf1.getSelectableCols(2).equals(insert2);
            assert shelf1.getSelectableCols(3).equals(insert3);
        }
    }
}