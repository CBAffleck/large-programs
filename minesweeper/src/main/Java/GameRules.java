import javax.swing.*;
import java.util.Random;

/**
 * Created by CampbellAffleck on 5/23/17.
 */
public class GameRules {

    public static int mineGenerator(int colSize, int rowSize) { //Generates the number of mines to be placed on the board
        Random rand = new Random();
        int randCol = rand.nextInt(colSize - 1) + 2; //Added 2 to increase the minimum number of mines allowed
        int randRow = rand.nextInt(rowSize - 1) + 2;
        return randCol*randRow;
    }

    public static void placeMines(JButton[][] boardTiles, int colSize, int rowSize) {
        int numMines = mineGenerator(colSize, rowSize);
    }

    public static void fillBoard(JButton[][] boardTiles) {

    }

    public static void revealNumbers(JButton[][] boardTiles, int XPos, int YPos) {

    }

}

/*
    placeMines: Chooses random number of mines less than or equal to the number of tiles per row/column minus 1. Then randomly places
    them on the board.

    fillBoard: After placeMines is called, fillBoard will step through the tiles in the grid and place a number icon at each tile corresponding
    to how many bombs surround that tile. If no bomb is in the surrounding 8 tiles, the tile is blank.

    revealNumbers: If a tile is clicked and it contains a number, only reveal that tile. Otherwise, if a blank tile is clicked, reveal
    the tiles on all sides. If any of those tiles are blank, then each of those tiles reveals all tiles surrounding it, and so on,
    until the point clicked is surrounded by numbered tiles.
 */