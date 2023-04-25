package it.polimi.ingsw.Server.Model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.Common.Utils.JSONInterface;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommonGoalCardTest {

    @Test
    void checkGoalSixCouples() {
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard SixCouple = JSONInterface.getCommonGoalCardFromID("SixCouples", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !SixCouple.checkGoal(voidShelf);

        Player satisfiedGoal = new Player("playertest", jsrdr.getShelf("SixCouples"));
        assert SixCouple.checkGoal(satisfiedGoal);

        Player unsatisfiedGoal = new Player("playertest", jsrdr.getShelf("SixCouplesNotSatisfied"));
        assert !SixCouple.checkGoal(unsatisfiedGoal);


    }

    @Test
    void checkGoalEightSame(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("EightSame", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("EightSameGroup"));
        assert CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("EightSameSparse"));
        assert CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("EightSameFalse"));
        assert ! CommonGoal.checkGoal(test3);

    }

    @Test
    void checkGoalDiagonalsOfFive(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("DiagonalsOfFive", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("DiagonalsOfFive1"));
        assert CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("DiagonalsOfFive-1"));
        assert CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("DiagonalsOfFive2"));
        assert CommonGoal.checkGoal(test3);

        Player test4 = new Player("playertest", jsrdr.getShelf("DiagonalsOfFive-2"));
        assert CommonGoal.checkGoal(test4);

        Player test5 = new Player("playertest", jsrdr.getShelf("DiagonalsOfFiveFalse"));
        assert !CommonGoal.checkGoal(test5);
    }

    @Test
    void checkGoalPyramid(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("Pyramid", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("Pyramid1"));
        assert CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("Pyramid-1"));
        assert CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("PyramidFalseOccupied"));
        assert !CommonGoal.checkGoal(test3);

        Player test4 = new Player("playertest", jsrdr.getShelf("PyramidFalseUnderline"));
        assert !CommonGoal.checkGoal(test4);
    }

    @Test
    void checkGoalThreeColsMaxThreeTypes(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("ThreeColsMaxThreeTypes", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("ThreeColsMaxThreeTypesFalseMissingTile"));
        assert !CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("ThreeColsMaxThreeTypesFalseFourColors"));
        assert !CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("ThreeColsMaxThreeTypes"));
        assert CommonGoal.checkGoal(test3);

    }

    @Test
    void checkGoalFourRowsMaxThreeTypes(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("FourRowsMaxThreeTypes", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("FourRowsMaxThreeTypesFalseMissingTile"));
        assert !CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("FourRowsMaxThreeTypesFalseFourColours"));
        assert !CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("FourRowsMaxThreeTypes"));
        assert CommonGoal.checkGoal(test3);
    }

    @Test
    void checkGoalTwoRowsDiffTypes(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("TwoRowsDiffTypes", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("TwoRowsDiffTypes"));
        assert CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("TwoRowsDiffTypesFalseTwoSameColours"));
        assert !CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("TwoRowsDiffTypesFalseMissingRow"));
        assert !CommonGoal.checkGoal(test3);
    }

    @Test
    void checkGoalTwoColsDiffTypes(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("TwoColsDiffTypes", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("TwoColsDiffTypes"));
        assert CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("TwoColsDiffTypesFalseTwoSameColours"));
        assert !CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("TwoColsDiffTypesFalseMissingCol"));
        assert !CommonGoal.checkGoal(test3);
    }

    @Test
    void checkGoalFiveTilesXShape(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("FiveTilesXShape", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("FiveTilesXShape"));
        assert CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("FiveTilesXShapeFalseDiffColour"));
        assert !CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("FiveTilesXShapeFalseNotComplete"));
        assert !CommonGoal.checkGoal(test3);

        Player test4 = new Player("playertest", jsrdr.getShelf("FiveTilesXShapeFullSquare"));
        assert CommonGoal.checkGoal(test4);

        Player test5 = new Player("playertest", jsrdr.getShelf("FiveTilesXShapeFalseMissingCenter"));
        assert ! CommonGoal.checkGoal(test5);
    }

    @Test
    void checkGoalTwoSquares(){
        JsonReader jsrdr = new JsonReader();
        CommonGoalCard CommonGoal = JSONInterface.getCommonGoalCardFromID("TwoSquares", 3);
        Player voidShelf = new Player("PlayerTest");

        assert !CommonGoal.checkGoal(voidShelf);


        Player test1 = new Player("playertest", jsrdr.getShelf("TwoSquares"));
        assert CommonGoal.checkGoal(test1);

        Player test2 = new Player("playertest", jsrdr.getShelf("TwoSquaresFalseTooNear"));
        assert !CommonGoal.checkGoal(test2);

        Player test3 = new Player("playertest", jsrdr.getShelf("TwoSquaresFalseMissingTileSecondSquare"));
        assert !CommonGoal.checkGoal(test3);

        Player test4 = new Player("playertest", jsrdr.getShelf("TwoSquaresFalseNotSquareGroup"));
        assert !CommonGoal.checkGoal(test4);

        Player test5 = new Player("playertest", jsrdr.getShelf("TwoSquaresWithGroupOfEight"));
        assert CommonGoal.checkGoal(test5);
    }
}

class JsonReader{
    String Path = "src/main/resources/JSONForTesting/CommonGoalsTesting.json";

    public Shelf getShelf(String ShelfID){
        Gson converter = new Gson();
        int Shelf_Height = 6;
        int Shelf_Width = 5;

        JsonObject Obj = converter.fromJson(JSONInterface.getJsonStringFrom(Path), JsonObject.class);
        JsonArray shelfArray = Obj.getAsJsonArray(ShelfID);
        Optional<ItemCard>[][] opt = new Optional[6][5];

        for (JsonElement el : shelfArray) {
            opt[el.getAsJsonObject().get("position").getAsJsonArray().get(0).getAsInt()][el.getAsJsonObject().get("position").getAsJsonArray().get(1).getAsInt()] = Optional.of(new ItemCard(el.getAsJsonObject().get("color").getAsCharacter(), ""));
        }

        for (int r = 0; r < Shelf_Height; r++) {
            for (int c = 0; c < Shelf_Width; c++) {
                if (opt[r][c] == null) {
                    opt[r][c] = Optional.empty();
                }
            }
        }

        return new Shelf(opt);
    }
}