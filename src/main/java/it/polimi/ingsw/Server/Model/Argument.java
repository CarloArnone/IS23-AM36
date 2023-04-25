package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Common.Utils.TestGenerator;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Argument {
    private String type;
    private List<List<String>> arguments;

    public Argument(String type, List<List<String>> arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public int size(){
        return arguments.size();
    }


    public boolean callWithArgumentsOn(Player p) {
        return switch (type) {
            case "m1" -> arguments.stream().allMatch(list -> parseArguments(p, Integer.parseInt(list.get(0)), Integer.parseInt(list.get(1)), Integer.parseInt(list.get(2)), Integer.parseInt(list.get(3)), Integer.parseInt(list.get(4)), Integer.parseInt(list.get(5)),Integer.parseInt(list.get(6)), Integer.parseInt(list.get(7)), Boolean.parseBoolean(String.valueOf(list.get(8))), Boolean.parseBoolean(String.valueOf(list.get(9)))));
            case "m2" -> arguments.stream().allMatch(list -> parseArguments(p, Integer.parseInt(list.get(0)), Integer.parseInt(list.get(1))));
            case "m3" -> arguments.stream().allMatch(list -> parseArguments(p, Boolean.parseBoolean(String.valueOf(list.get(0)))));
            default -> false;
        };
    }


    private boolean parseArguments(Player p, int x, int y, int hInc, int vInc, int lineLength, int tileMatches, boolean full){
        return p.getMyShelf().checkLine(x, y, hInc, vInc, lineLength, tileMatches, full);
    }

    private boolean parseArguments(Player p, int groupSize, int groupsNumber){
        //TODO WRITE A BETTER VERSION
        List<Pair<Integer, Character>> groupList = new ArrayList<>();
        ItemCard falseMatch = new ItemCard('Z', "falseMatch");

        boolean[][] alreadySeen = new boolean[6][5];
        for(int r = 0; r < p.getMyShelf().getShelf().length; r++){
            for(int c = 0; c < p.getMyShelf().getShelf()[0].length; c++){
                //Initialize true because if the cell is empty, because I don't want to check anything on it
                alreadySeen[r][c] = p.getMyShelf().getShelf()[r][c].isEmpty();
            }
        }


        for(int r = 0; r < p.getMyShelf().getShelf().length; r++){
            for(int c = 0; c < p.getMyShelf().getShelf()[0].length; c++){
                if(!alreadySeen[r][c]){
                    groupList.add(new Pair<>(p.getMyShelf().hasAdiacent(r, c, p.getMyShelf().getShelf()[r][c].orElse(falseMatch).getColor(), alreadySeen, falseMatch), p.getMyShelf().getShelf()[r][c].orElse(falseMatch).getColor()));
                }
            }
        }

         return groupList.stream().filter(x -> x.getKey() >= groupSize).count() >= groupsNumber;

    }


    private boolean parseArguments(Player p, boolean isGroupOfEight){

        Optional<ItemCard>[][] shelf = p.getMyShelf().getShelf();
        ItemCard falseMatch = new ItemCard();
        ItemCard falseMatchOther = new ItemCard('Q', "falseMatchOther");

        for(int r = 0; r < shelf.length; r++){
            for(int c = 0; c < shelf[0].length; c++){
                int finalR = r;
                int finalC = c;
                if( Arrays.stream(shelf).map(row -> Arrays.stream(row)
                                            .filter(card -> card.orElse(falseMatchOther).getColor()
                                                                .equals(shelf[finalR][finalC].orElse(falseMatch).getColor()))
                                            .count())
                                        .reduce(0L, Long::sum) >= 8){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean parseArguments(Player p, int x, int y, int hInc, int vInc,int width, int height, int repetitions, int diffTypes, boolean full, boolean exactNum){
        return p.getMyShelf().checkQuadrilateral(x, y, hInc, vInc, width, height, repetitions, diffTypes, full, exactNum);
    }
}
