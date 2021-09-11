package src;
import java.util.Arrays;

// when a cell is added to the bag it must come with col and row names
public class CostList {
    private int[][] list;
    private int[] statCol;
    private String[] statRow;
    private String name;

    public CostList(String name) {
        list = new int[10][4];
        statCol = new int[10];
        for (int i = 0 , j = 2; i < 10; i++ , j = j +2) {
            statCol[i]= j;
        }
        statRow = new String[]{"SPH","2","4","6"};
        this.name = name;
    }
    public void editName(String name) {
        this.name = name;
    }

    public void editCell(int row, int col, int value) {
        list[row][col] = value;
    }
    public void intArrToCostList(int[] inputArr) {
        if (inputArr.length != 40 ){
            System.out.println("incorrect size of arr!!!");
            return;
        }
        for (int row = 0, col = 0, i = 0; row < 10; col++,i++) {
            this.editCell(row, col, inputArr[i]);
            if (col == 3){
                row ++;
                col = -1;
            }
        }
    }
    public int getCell(int row, int col) {
        return list[row][col];
    }

    public int[] getRow(int row) {
        return list[row];
    }
    public String getCellString(int row , int col) {
        return "";
    }
    public String getname() {
        return name;
    }

    public int[] getStatCol() {
        return statCol;
    }
    public String[] getStatRow() {
        return statRow;
    }
    public static String twoLetterExpand(String input) {
        return "";
    }
    @Override
    public String toString() {
        System.out.println("\n" + name);
        System.out.println("\n" + Arrays.toString(statRow));
        for (int i = 0; i < 10; i++) {
            System.out.println(statCol[i]+ " " + Arrays.toString(list[i]));
        }
        return "";
    }

    public static void main(String[] args) {
        CostList test = new CostList("blue cut");
        test.editCell(7, 1,200);
        test.editCell(3, 3,200);
        System.out.println(test);
    }
}
