import sun.jvm.hotspot.utilities.Hashtable;

import javax.swing.*;
import java.util.Random;

/**
 * Created by CampbellAffleck on 5/23/17.
 */
public class GameRules {

    private static int[][] board;
    static Random rand = new Random();
    private static int numMines = 0;

    public static int mineGenerator(int colSize, int rowSize) { //Generates the number of mines to be placed on the board
        int randCol = rand.nextInt(colSize - 1) + 2; //Added 2 to increase the minimum number of mines allowed
        int randRow = rand.nextInt(rowSize - 1) + 2;
        numMines = randCol*randRow;
        return numMines;
    }

    public static int[][] placeMines(int colSize, int rowSize, int xPosUserClick, int yPosUserClick) {
        board = new int[colSize][rowSize];
        while (numMines > 0) {
            int randCol = rand.nextInt(colSize - 1);
            int randRow = rand.nextInt(rowSize -1);
            if (board[randCol][randRow] == 0 && (randCol != xPosUserClick || randRow != yPosUserClick)) {
                board[randCol][randRow] = 9; //This number represents the placement of a mine
                numMines -= 1;
            }
        }
        return board;
    }

    public static void fillBoard() {

    }

    public static void revealNumbers(JButton[][] boardTiles, int XPos, int YPos) {

    }

}

/*
    placeMines: Chooses random number of mines less than or equal to the number of tiles per row/column minus 1. Then randomly places
    them on a 2D int array. This will be an int array representation of the jbutton board.

    Important code to be changed for each numbered tile:
    boardTiles[x][y].setIcon(new ImageIcon(new ImageIcon("Resources/touched_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "touched"));

    fillBoard: After placeMines is called, fillBoard will step through the tiles in the grid and place a number icon at each tile corresponding
    to how many bombs surround that tile. If no bomb is in the surrounding 8 tiles, the tile is blank.

    revealNumbers: If a tile is clicked and it contains a number, only reveal that tile. Otherwise, if a blank tile is clicked, reveal
    the tiles on all sides. If any of those tiles are blank, then each of those tiles reveals all tiles surrounding it, and so on,
    until the point clicked is surrounded by numbered tiles.
 */