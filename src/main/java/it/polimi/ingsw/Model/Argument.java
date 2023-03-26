package it.polimi.ingsw.Model;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Argument {
    private String type;
    private List<List<Object>> arguments;

    public Argument(String type, List<List<Object>> arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    public int size(){
        return arguments.size();
    }


    public boolean callWithArgumentsOn(Player p) {
        return switch (type) {
            case "m1" -> arguments.stream().allMatch(list -> parseArguments(p, (Integer) list.get(0), (Integer) list.get(1), (Integer) list.get(2), (Integer) list.get(3), (Integer) list.get(4), (Integer) list.get(5), (Boolean) list.get(6)));
            case "m2" -> arguments.stream().allMatch(list -> parseArguments(p, (Integer) list.get(0), (Integer) list.get(1)));
            case "m3" -> arguments.stream().allMatch(list -> parseArguments(p, (Boolean) list.get(0)));
            case "m4" -> arguments.stream().allMatch(list -> parseArguments(p, (Integer) list.get(0), (Integer) list.get(1), (Integer) list.get(2), (Integer) list.get(3), (Integer) list.get(4), (Integer) list.get(5),(Integer) list.get(6), (Integer) list.get(7), (Boolean) list.get(8)));
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

        List<Integer> numbersOfGroups = new ArrayList<>();
        numbersOfGroups.add((int) groupList.stream().filter(x -> x.getKey() >= groupSize).filter(x -> x.getValue().equals('W')).count());
        numbersOfGroups.add((int) groupList.stream().filter(x -> x.getKey() >= groupSize).filter(x -> x.getValue().equals('P')).count());
        numbersOfGroups.add((int) groupList.stream().filter(x -> x.getKey() >= groupSize).filter(x -> x.getValue().equals('C')).count());
        numbersOfGroups.add((int) groupList.stream().filter(x -> x.getKey() >= groupSize).filter(x -> x.getValue().equals('Y')).count());
        numbersOfGroups.add((int) groupList.stream().filter(x -> x.getKey() >= groupSize).filter(x -> x.getValue().equals('G')).count());
        numbersOfGroups.add((int) groupList.stream().filter(x -> x.getKey() >= groupSize).filter(x -> x.getValue().equals('B')).count());

        return numbersOfGroups.stream().anyMatch(x -> x >= groupsNumber);
    }

    private boolean parseArguments(Player p, boolean isGroupOfEight){
        //TODO
        return true;
    }

    private boolean parseArguments(Player p, int x, int y, int hInc, int vInc,int width, int height, int repetitions, int diffTypes, boolean full){
        return p.getMyShelf().checkQuadrilateral(x, y, hInc, vInc, width, height, repetitions, diffTypes, full);
    }
}
