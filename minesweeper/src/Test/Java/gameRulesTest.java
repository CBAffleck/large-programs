import java.util.Arrays;

/**
 * Created by CampbellAffleck on 5/23/17.
 */

/*
    Tests methods in GameRules.java
 */
public class gameRulesTest extends GameRules{

    public static void main(String[] args) {
        checkMineGenerator();
        checkMinePlacement();
        checkFillBoard();
    }

    /**
     *  Checks whether mineGenerator is working and prints the result.
     *
     *  @param  expected    Expected int max
     *  @param  actual      Int received
     *  @param  label       Label for the 'test' case
     */
    private static void checkLessThan(int actual, int expected, String label) {
        if (actual <= expected) {
            System.out.println("PASS: " + label + ": Expected less than " + expected + " and you gave " + actual);
        } else {
            System.out.println("FAIL: " + label + ": Expected less than " + expected + " and you gave " + actual);
        }
    }

    private static void checkMineGenerator() {
        System.out.println("Checking mine generator...");
        int colSize = 8;
        int rowSize = 8;

        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");
        checkLessThan(mineGenerator(colSize, rowSize), 64,"mineGenerator()");

    }

    /**
     *  Checks that the tile the user clicked doesn't contain a mine after the board is filled with mines.
     *
     *  @param  xPosUserClick       X position of user click
     *  @param  yPosUserClick       Y position of user click
     *  @param  label               Label for the 'test' case
     */
    private static int[][] checkPlaceMines(int xPosUserClick, int yPosUserClick, int colSize, int rowSize, String label) {
        mineGenerator(colSize, rowSize);
        int [][] board = placeMines(colSize, rowSize, xPosUserClick, yPosUserClick);
        //*** Print out board to view mine placement ***
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        if (board[xPosUserClick][yPosUserClick] == 0) {
            System.out.println("PASS: " + label + ": 0 found at (" + xPosUserClick + ", " + yPosUserClick + ").");
        } else {
            System.out.println("FAIL: " + label + ": Expected 0 at (" + xPosUserClick + ", " + yPosUserClick + ").");
        }
        return board;
    }

    private static void checkMinePlacement() {
        System.out.println("Checking mine placement...");
        checkPlaceMines(4, 6, 8, 8, "placeMines()");
        checkPlaceMines(6, 2, 8, 8, "placeMines()");
        checkPlaceMines(2, 1, 8, 8, "placeMines()");
        checkPlaceMines(5, 7, 16, 16, "placeMines()");
        checkPlaceMines(1, 3, 16, 16, "placeMines()");
        checkPlaceMines(7, 5, 16, 16, "placeMines()");
    }

    private static void checkPrintBoard(int[][] board, String label) {
//        *** Print out board to view mine placement ***
        System.out.println("Filling " + label + "...");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");
//        if (board[xPosUserClick][yPosUserClick] == 0) {
//            System.out.println("PASS: " + label + ": 0 found at (" + xPosUserClick + ", " + yPosUserClick + ").");
//        } else {
//            System.out.println("FAIL: " + label + ": Expected 0 at (" + xPosUserClick + ", " + yPosUserClick + ").");
//        }
    }

    private static void checkFillBoard() {
        System.out.println("Checking if 8x8 boards are correctly filled...");
        int[][] board = new int[][]{
                {9, 0, 9, 9, 9, 9, 9, 0},
                {0, 9, 9, 9, 0, 9, 0, 0},
                {9, 9, 9, 0, 0, 9, 9, 0},
                {9, 9, 0, 9, 0, 0, 0, 0},
                {9, 0, 0, 9, 9, 0, 0, 0},
                {9, 0, 9, 0, 9, 0, 9, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 9, 9, 9, 0, 0, 0, 0}
        };
        int[][] board1 = new int[][]{
                {9, 0, 9, 9, 9, 9, 9, 0},
                {0, 9, 0, 0, 0, 9, 0, 0},
                {9, 9, 0, 0, 0, 9, 9, 0},
                {9, 9, 0, 0, 0, 0, 0, 0},
                {9, 0, 0, 9, 9, 0, 0, 0},
                {9, 0, 0, 0, 9, 0, 9, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 9, 9, 9, 0, 0, 0, 0}
        };
        int[][] board2 = new int[][]{
                {9, 9, 9, 9, 9, 9, 9, 9},
                {9, 9, 9, 9, 9, 9, 9, 9},
                {9, 9, 0, 0, 0, 0, 9, 9},
                {9, 9, 0, 0, 0, 0, 9, 9},
                {9, 9, 0, 0, 0, 0, 9, 9},
                {9, 9, 0, 0, 0, 0, 9, 9},
                {9, 9, 9, 9, 9, 9, 9, 9},
                {9, 9, 9, 9, 9, 9, 9, 9}
        };

        checkPrintBoard(fillBoard(board), "board");
        checkPrintBoard(fillBoard(board1), "board1");
        checkPrintBoard(fillBoard(board2), "board2");
        checkPrintBoard(fillBoard(checkPlaceMines(5, 7, 16, 16, "placeMines()")), "board3");
    }
}
