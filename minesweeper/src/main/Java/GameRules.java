import java.util.Random;

/**
 * Created by CampbellAffleck on 5/23/17.
 */
public class GameRules {

    private static int[][] board;
    static Random rand = new Random();
    private static int numMines = 0;

    public static int mineGenerator(int colSize, int rowSize) { //Generates the number of mines to be placed on the board
        int randCol = rand.nextInt(colSize - 5) + 2;
        int randRow = rand.nextInt(rowSize - 5) + 2;
        numMines = randCol*randRow;
        return numMines;
    }

    public static int[][] placeMines(int colSize, int rowSize, int xPosUserClick, int yPosUserClick) {
        board = new int[colSize][rowSize];
        while (numMines > 0) {
            int randCol = rand.nextInt(colSize - 1);
            int randRow = rand.nextInt(rowSize -1);
            //Places mines randomly, but gives some space around player to start off
            if (board[randCol][randRow] == 0 && (randCol != xPosUserClick || randRow != yPosUserClick ||
                                                 randCol != xPosUserClick + 1 || randRow != yPosUserClick + 1 ||
                                                 randCol != xPosUserClick - 1 || randRow != yPosUserClick - 1)) {
                board[randCol][randRow] = 9; //This number represents the placement of a mine
                numMines -= 1;
            }
        }
        return board;
    }

    public static int[][] fillBoard(int[][] board) {
        int rowNum, colNum;
        for (colNum = 0; colNum < board.length; colNum++) {
            for (rowNum = 0; rowNum < board[colNum].length; rowNum++) {
                int mineCount = 0;
                if (board[colNum][rowNum] == 9) {
                    //do nothing, go to next rowNum
                } else {
                    if (colNum == 0 && rowNum == 0) {
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == 0 && rowNum == 7) {
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == 7 && rowNum == 0) {
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == 7 && rowNum == 7) {
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == 0) {
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == 7) {
                        if (board[colNum - 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (rowNum == 0) {
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (rowNum == 7) {
                        if (board[colNum - 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                    } else {
                        if (board[colNum - 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    }
                    board[colNum][rowNum] = mineCount;
//                    for (int i = 0; i < board.length; i++) {
//                        for (int j = 0; j < board[i].length; j++) {
//                            System.out.print(board[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println("\n");
                }
            }
        }
        return board;
    }

}

/*
    placeMines: Chooses random number of mines less than or equal to the number of tiles per row/column minus 1. Then randomly places
    them on a 2D int array. This will be an int array representation of the jbutton board.

    fillBoard: After placeMines is called, fillBoard will step through the tiles in the grid and place a number icon at each tile corresponding
    to how many bombs surround that tile. If no bomb is in the surrounding 8 tiles, the tile is blank.

    revealNumbers: If a tile is clicked and it contains a number, only reveal that tile. Otherwise, if a blank tile is clicked, reveal
    the tiles on all sides. If any of those tiles are blank, then each of those tiles reveals all tiles surrounding it, and so on,
    until the point clicked is surrounded by numbered tiles.
 */