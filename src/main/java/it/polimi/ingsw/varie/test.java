package it.polimi.ingsw.varie;

import java.util.Optional;

public class test {

    public static void main(String[] args){

        Optional<Double>[][] x = new Optional[5][6];
        Optional<Double>[][] t;


//        out.println(x[1][1]);
//        x[1][1] = Optional.of(2);
//        out.println(x[1][1].get());

        double n;

        for(int i = 0; i < x.length; i++){
            for(int j = 0; j < x[0].length; j++){
                n = (double)i + (double)j/10;
                x[i][j] = Optional.of(n);
                System.out.println(x[i][j]);
            }
        }

        t = transposeMatrix(x);
        System.out.println("\n\n");


        for(int i = 0; i < x[0].length; i++){
            for(int j = 0; j < x.length; j++){
                System.out.println(t[i][j]);
            }
        }

        System.out.println(t[7][7].isPresent());

    }

    private static Optional<Double>[][] transposeMatrix(Optional<Double>[][] shelfCopy){

        Optional<Double>[][] transposedShelf = new Optional[shelfCopy[0].length][shelfCopy.length];

        for(int i = 0; i < shelfCopy[0].length; i++){
            for(int j = 0; j < shelfCopy.length; j++){
                transposedShelf[i][j] = Optional.of(shelfCopy[j][i].get());
            }
        }
        return transposedShelf;
    }
}
