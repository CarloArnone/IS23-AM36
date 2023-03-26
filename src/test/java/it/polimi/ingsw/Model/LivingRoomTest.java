/*
package it.polimi.ingsw.Model;

import it.polimi.ingsw.Common.Exceptions.ToManyCardsException;
import it.polimi.ingsw.Server.Model.*;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

class LivingRoomTest extends TestCase {

    static LivingRoom livTest = new LivingRoom("12345");

    @BeforeAll
    static void LivingRoomSetUpTest() {

        //Initializing Fake Board
        Map<BoardPosition, Boolean> testBoard = new HashMap<>();
        BoardPosition p45 = new BoardPosition(4, 5, new ItemCard('W', "4,5 Position"));
        BoardPosition p46 = new BoardPosition(4, 6, new ItemCard('P', "4,6 Position"));
        BoardPosition p47 = new BoardPosition(4, 7, new ItemCard('C', "4,7 Position"));
        BoardPosition p55 = new BoardPosition(5, 5, new ItemCard('C', "5,5 Position"));
        BoardPosition p56 = new BoardPosition(5, 6, new ItemCard('Y', "5,6 Position"));
        BoardPosition p57 = new BoardPosition(5, 7, new ItemCard('Y', "5,7 Position"));
        BoardPosition p65 = new BoardPosition(6, 5, new ItemCard('W', "6,5 Position"));
        BoardPosition p66 = new BoardPosition(6, 6, new ItemCard('W', "6,6 Position"));

        testBoard.put(p45, true);
        testBoard.put(p46, true);
        testBoard.put(p47, true);
        testBoard.put(p55, true);
        testBoard.put(p56, true);
        testBoard.put(p57, true);
        testBoard.put(p65, true);
        testBoard.put(p66, true);

        livTest.setBoard(testBoard);

        //Initializing Common Goal Set
        List<CommonGoalCard> goalCommon = new ArrayList<>();
        List<Integer> commonPoints = new ArrayList<>();
        commonPoints.add(4);
        commonPoints.add(6);
        commonPoints.add(8);
        goalCommon.add(commonGoal1);
        goalCommon.add(commonGoal2);
        livTest.setCommonGoalSet(goalCommon);

        //Inizializing Player List
        Player p1 = new Player("Gianni");
        Player p2 = new Player("Mario");
        Player p3 = new Player("Luca");

        livTest.addPlayer(p1);
        livTest.addPlayer(p2);
        livTest.addPlayer(p3);


        //TODO MODIFY CREATION OF PLAYERS TO ADD SHELF IN COSTRUCTION TO TEST UPDATE GOALS
    }


    @Test
    void givePlayersTheirPick() {

        List<ItemCard> pickTest = new ArrayList<>();
        pickTest.add(new ItemCard('Y', "first_Image"));
        pickTest.add(new ItemCard('B', "second_Image"));
        pickTest.add(new ItemCard('G', "Third_Image"));

        try{
            livTest.givePlayerTheirPick(livTest.getPlayers().get(livTest.getTurn()), pickTest);
        }
        catch(ToManyCardsException e){
            assert false;
        }
        assert livTest.getPlayers().get(livTest.getTurn()).getDrawnCards().equals(pickTest);


        pickTest.add(new ItemCard('W', "Fourth_Image"));
        try{
            livTest.givePlayerTheirPick(livTest.getPlayers().get(livTest.getTurn()), pickTest);
        }
        catch(ToManyCardsException e){
            assert true;
            return;
        }

        assert false;
    }

    @Test
    void checkRearrangeDesk() {

    }

    @Test
    void undoDraft() {

        Player p1 = new Player("Gianni");
        Player p2 = new Player("Mario");
        Player p3 = new Player("Luca");

        livTest.addPlayer(p1);
        livTest.addPlayer(p2);
        livTest.addPlayer(p3);

        List<ItemCard> pickTest = new ArrayList<>();
        pickTest.add(new ItemCard('Y', "first_Image"));
        pickTest.add(new ItemCard('B', "second_Image"));
        pickTest.add(new ItemCard('G', "Third_Image"));

        try{
            livTest.givePlayerTheirPick(livTest.getPlayers().get(livTest.getTurn()), pickTest);
        }
        catch(ToManyCardsException e){
            assert false;
        }

        livTest.undoDraft(livTest.getPlayers().get(livTest.getTurn()));

        assert livTest.getPlayers().get(livTest.getTurn()).getDrawnCards().isEmpty();
    }

    @Test
    void updateGoals() {

        livTest.updateGoals(livTest.getPlayers().get(livTest.getTurn()));

        assert livTest.getPlayers().get(livTest.getTurn()).getAchievedGoals().contains(livTest.getCommonGoalSet().get(0));
        assert livTest.getPlayers().get(livTest.getTurn()).getAchievedGoals().contains(livTest.getCommonGoalSet().get(1));

        assert livTest.getPlayers().get(livTest.getTurn()).getScore() == 8;

        //TO BROAD

    }

    @Test
    void nextTurn() {
        int prevTurn = livTest.getTurn();
        livTest.nextTurn();

        assert livTest.getTurn() == (prevTurn +1)% livTest.getPlayers().size();
    }

    @Test
    void getPlayers() {
        livTest.getPlayers().clear();
        assert livTest.getPlayers().isEmpty();

        Player p1 = new Player("Gianni");
        Player p2 = new Player("Mario");
        Player p3 = new Player("Luca");
        livTest.addPlayer(p1);
        livTest.addPlayer(p2);
        livTest.addPlayer(p3);

        assert livTest.getPlayers().contains(p1);
        assert livTest.getPlayers().contains(p2);
        assert livTest.getPlayers().contains(p3);

    }

    @Test
    void addPlayer() {

        Player p4 = new Player("Carlo");
        assert !livTest.getPlayers().contains(p4);
        livTest.addPlayer(p4);
        assert livTest.getPlayers().contains(p4);
        livTest.addPlayer(p4);
        assert livTest.getPlayers().stream().filter(x -> x.equals(p4)).count() == 1;
    }

    @Test
    void removePlayer() {

        Player p1 = new Player("Gianni");
        Player p2 = new Player("Mario");
        Player p3 = new Player("Luca");

        List<Player> pte = new ArrayList<>();
        pte.add(p1);
        pte.add(p2);
        pte.add(p3);

        for(Player p : pte){
            assert livTest.getPlayers().contains(p);
            livTest.removePlayer(p);
            assert !livTest.getPlayers().contains(p);
        }

        assert livTest.getPlayers().isEmpty();
    }

    @Test
    void removeCard() {

        List<BoardPosition> tilesToEliminate = new ArrayList<>();
        tilesToEliminate.add(new BoardPosition(4, 5));
        tilesToEliminate.add(new BoardPosition(4, 6));
        tilesToEliminate.add(new BoardPosition(4, 7));
        tilesToEliminate.add(new BoardPosition(5, 5));
        tilesToEliminate.add(new BoardPosition(6, 5));




        for (BoardPosition bp : tilesToEliminate){
            livTest.removeCard(bp);
            assert !livTest.getBoard().containsKey(bp);
        }

    }
}
*/
