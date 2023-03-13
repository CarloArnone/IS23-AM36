package it.polimi.ingsw.varie;

public class ObjectiveChecker {

    private final char[][] mat;
    private final char emptyCell;

    public ObjectiveChecker(char[][] mat){
        this.mat = mat;
        this.emptyCell = 'u';
    }

    /**
     * I check the 4 corners for a match:
     *
     * │ 1 │ - │ - │ - │ 1 │
     * │ - │ - │ - │ - │ - │
     * │ - │ - │ - │ - │ - │
     * │ - │ - │ - │ - │ - │
     * │ - │ - │ - │ - │ - │
     * │ 1 │ - │ - │ - │ 1 │
     *
     */


    /**public boolean fourCorners(){
        if(mat[0][0] == emptyCell) return false;
        if(mat[0][0] != mat[0][mat[0].length - 1]) return false;
        if(mat[0][0] != mat[mat.length - 1][0]) return false;
        if(mat[0][0] != mat[mat.length - 1][mat[0].length - 1]) return false;
        return true;
    }*/

    /**
     * I check the first left to right diagonal:
     *
     * │ 1 │ - │ - │ - │ - │
     * │ - │ 1 │ - │ - │ - │
     * │ - │ - │ 1 │ - │ - │
     * │ - │ - │ - │ 1 │ - │
     * │ - │ - │ - │ - │ 1 │
     * │ - │ - │ - │ - │ - │
     *
     * Then I check the second left to right diagonal:
     *
     * │ - │ - │ - │ - │ - │
     * │ 1 │ - │ - │ - │ - │
     * │ - │ 1 │ - │ - │ - │
     * │ - │ - │ 1 │ - │ - │
     * │ - │ - │ - │ 1 │ - │
     * │ - │ - │ - │ - │ 1 │
     *
     * Then I check the first right to left diagonal:
     *
     * │ - │ - │ - │ - │ 1 │
     * │ - │ - │ - │ 1 │ - │
     * │ - │ - │ 1 │ - │ - │
     * │ - │ 1 │ - │ - │ - │
     * │ 1 │ - │ - │ - │ - │
     * │ - │ - │ - │ - │ - │
     *
     * Finally I check the second right to left diagonal:
     *
     * │ - │ - │ - │ - │ - │
     * │ - │ - │ - │ - │ 1 │
     * │ - │ - │ - │ 1 │ - │
     * │ - │ - │ 1 │ - │ - │
     * │ - │ 1 │ - │ - │ - │
     * │ 1 │ - │ - │ - │ - │
     *




    public boolean diagonals(){

        for(int i = 0; i < mat.length - 2; i++){
            if(mat[i][i] != mat[i+1][i+1]) break;
            if(i == mat.length - 3) return true;
        }

        for(int i = 1; i < mat.length - 1; i++){
            if(mat[i][i] != mat[i+1][i+1]) break;
            if(i == mat.length - 2) return true;
        }

        for(int i = 0; i < mat.length - 2; i++){
            if(mat[mat.length - i - 1][i] != mat[mat.length - i - 2][i+1]) break;
            if(i == mat.length - 3) return true;
        }

        for(int i = 1; i < mat.length - 1; i++){
            if(mat[mat.length - i - 1][i] != mat[mat.length - i - 2][i+1]) break;
            if(i == mat.length - 2) return true;
        }
        return false;
    }

     **/

    /** First I check the columns one by one, up until the second to last cell and until I reach the second to last column.
     * For each cell I check if it's equal with the right one and the bottom one.
     * Then for the last column, I only check the bottom cell, because there is no right one.
     * Finally, I check the last row, confronting each cell only with its right one because there are none below.
     * The last cell, the one on the bottom right, has no cell under or to the right, so it's only confronted by its two adjacent one and doesn't need to check anything itself.
    public boolean adjacent(){
        int ret = 0;

        for(int i = 0; i < mat.length - 1; i++){
            for(int j = 0; j < mat[0].length - 1; j++){
                if(mat[i][j] == mat[i][j + 1]) ret++;
                if(mat[i][j] == mat[i+1][j]) ret++;
            }
            if(mat[i][mat[0].length - 1] == mat[i + 1][mat[0].length - 1]) ret++;
        }

        for(int i = 0; i < mat.length - 1; i++){
            if(mat[mat.length - 1][i] == mat[mat.length - 1][i + 1]) ret++;
        }

        if(ret/6 == 0) return false;
        else if(ret/6 == 1) return true;
        else return false; //consider this exception
    }
     **/

    /** I check each cell one by one, up until the second to last column and row.
     * For each cell I check if it's equal with the right one and the bottom one and the diagonal one.
     * If I find one discrepancy, I discard that set and continue.
     * Once I find a suitable square of cells, I store the value of their color and the column in which i find it + 1.
     *
     * In case there is already a color stored in the temp variable, it means I had already found a square.
     * That means I have to firstly check if the colors match and then if the colum and row that I stored are the same as the ones of the cell I'm checking.
     * If that is the case, I have to ignore this square, because it is formed by two cells that also form the fist square.
     *
     * Example:
     * │ 1 │ 2 │ 1 │ - │ - │
     * │ 1 │ 2 │ 1 │ - │ - │
     * │ - │ - │ - │ 1 │ 1 │
     * │ - │ - │ - │ 2 │ 2 │
     * │ - │ - │ - │ 1 │ 1 │
     * │ - │ - │ - │ - │ - │
     *
     * The cells marked with a '2' would be the ones considered 2 times.
    public boolean square(){
        int ret = 0, control[] = {0, 0};
        char temp = 'z';

        for(int i = 0; i < mat.length - 1; i++){
            for(int j = 0; j < mat[0].length - 1; j++) {
                if (mat[i][j] != mat[i][j + 1]) continue;
                if (mat[i][j] != mat[i + 1][j]) continue;
                if (mat[i][j] != mat[i + 1][j + 1]) continue;

                if(temp == 'z') {
                    temp = mat[i][j];
                    control[0] = i + 1;
                    control[1] = j + 1;
                }  else if(temp == mat[i][j] && (control[0] != i || control[1] != j)) {
                    ret++;
                    temp = 'z';
                }
            }
        }
        if(ret/2 == 0) return false;
        else if(ret/2 == 1) return true;
        else return false; //consider this exception
    }
     **/
    /** Here we simply check each column and search for all different values.
     * The first (i) loop skims column by column.
     * The second (j) loop skims each element of each column.
     * The third (k) loop confronts the current cell with all the cells below it.
     * In this way we check all possibilities in a single column.
     * In case we find two equal cells in a column, we skip that column by updating the j counter and breaking the k loop. **/
    public int diffColumn(){
        int ret = 0;

        for(int i = 0; i <= mat.length - 1; i++) {
            for (int j = 0; j < mat[0].length - 1; j++) {
                for(int k = 0; k < mat[0].length - 1; k++) {
                    if (mat[i][j] == mat[i][k]) {
                        j++;
                        break;
                    }
                    if (k == mat[0].length - 2) ret++;
                }
            }
        }
        return ret/2;
    }

    /** C.
    public int diffRows(){
        int ret = 0;

        for(int i = 0; i < mat[0].length - 1; i++) {
            for (int j = 0; j < mat.length - 1; j++) {
                for(int k = 0; k < mat[0].length - 1; k++) {
                    if (mat[j][i] == mat[i][k]) {
                        i++;
                        break;
                    }
                    if (k == mat.length - 2) ret++;
                }
            }
        }

        return ret/2;
    }
     **/
    public int fourByFour(){
        int ret = 0;

        for(int i = 0; i <= mat.length - 1; i++) {
            for (int j = 0; j < mat[0].length - 1; j++) {
                if (mat[i][j] == mat[i][j+1]) break;
                if(j == mat[0].length - 2) ret++;
            }
        }
        return ret/2;
    }
}
