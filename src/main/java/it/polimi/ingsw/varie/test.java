package it.polimi.ingsw.varie;

import java.util.Optional;

public class test {

    public static void main(String[] args){

        Optional<Double>[][] x = (Optional<Double>[][]) new Optional<?>[3][3];
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


        for(int i = 0; i < x.length; i++){
            for(int j = 0; j < x[0].length; j++){
                System.out.println(t[i][j]);
            }
        }

    }

    private static Optional<Double>[][] transposeMatrix(Optional<Double>[][] shelfCopy){

        Optional<Double>[][] transposedShelf = (Optional<Double>[][]) new Optional<?>[shelfCopy[0].length][shelfCopy.length];

        for(int i = 0; i < shelfCopy.length; i++){
            for(int j = 0; j < shelfCopy[0].length; j++){
                transposedShelf[j][i] = Optional.of(shelfCopy[i][j].get());
            }
        }
        return transposedShelf;
    }
}
