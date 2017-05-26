import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by CampbellAffleck on 5/23/17.
 */
public class GameRules {

    private static int[][] board;
    static Random rand = new Random();
    private static int numMines = 0;

    /*
        Generates the number of mines to be placed on the board, returns as int.
     */

    public static int mineGenerator(int colSize, int rowSize) {
        int randCol = rand.nextInt(colSize - 4) + 2;
        int randRow = rand.nextInt(rowSize - 4) + 2;
        numMines = randCol*randRow;
        return numMines;
    }

    /*
        Using the number of mines from the mineGenerator, placeMines places the mines randomly on the board until all of the
        mines have been placed. The bomb is never placed under the user's first click, and the surrounding area is given space.
     */

    public static int[][] placeMines(int colSize, int rowSize, int xPosUserClick, int yPosUserClick) {
        board = new int[colSize][rowSize];
        while (numMines > 0) {
            int randCol = rand.nextInt(colSize - 1);
            int randRow = rand.nextInt(rowSize -1);
            //Places mines randomly, but gives some space around player to start off
            if (board[randCol][randRow] == 0 && (randCol != xPosUserClick || randRow != yPosUserClick)
                                             && (randCol != xPosUserClick || randRow != yPosUserClick + 1)
                                             && (randCol != xPosUserClick || randRow != yPosUserClick - 1)
                                             && (randCol != xPosUserClick + 1 || randRow != yPosUserClick + 1)
                                             && (randCol != xPosUserClick + 1 || randRow != yPosUserClick)
                                             && (randCol != xPosUserClick + 1 || randRow != yPosUserClick - 1)
                                             && (randCol != xPosUserClick - 1 || randRow != yPosUserClick + 1)
                                             && (randCol != xPosUserClick - 1 || randRow != yPosUserClick)
                                             && (randCol != xPosUserClick - 1 || randRow != yPosUserClick - 1)) {
                board[randCol][randRow] = 9; //This number represents the placement of a mine
                numMines -= 1;
            }
        }
        return board;
    }

    /*
        For each tile in the board, checks the tiles around it for a mine, and for each one increments the mineCount. Once all
         tiles are checked, the mineCount is placed in that spot on the board. This is used by the GUI to pick the correct numbered
         icon.
     */

    public static int[][] fillBoard(int[][] board) {
        int rowNum, colNum;
        for (colNum = 0; colNum < board.length; colNum++) {
            for (rowNum = 0; rowNum < board[colNum].length; rowNum++) {
                int mineCount = 0;
                if (board[colNum][rowNum] == 9) {                       //Skip tile if mine is already there
                    //do nothing, go to next rowNum
                } else {
                    if (colNum == 0 && rowNum == 0) {                   //upper left corner
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == 0 && rowNum == board[colNum].length - 1) {            //bottom left corner
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum + 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == board.length - 1 && rowNum == 0) {            //top right corner
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum + 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == board.length - 1 && rowNum == board[colNum].length - 1) {            //bottom right corner
                        if (board[colNum - 1][rowNum] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum - 1][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                        if (board[colNum][rowNum - 1] == 9) {
                            mineCount += 1;
                        }
                    } else if (colNum == 0) {                           //left side
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
                    } else if (colNum == board.length - 1) {            //right side
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
                    } else if (rowNum == 0) {                           //top side
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
                    } else if (rowNum == board[colNum].length - 1) {    //bottom side
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
                    } else {                                            //everywhere else on the board
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

    /*
        checkUnderTile checks what number in the reference board corresponds to the tile clicked in the GUI board. If the number under the tile
        is 0 and the corresponding position in visitedBoard hasn't been visited yet, then reveal the tiles around it. Otherwise, set the GUI tile icon
        to the correct numbered icon.
     */

    private void checkUnderTile(JButton[][] guiBoard, int[][] board, int[][] visitedBoard, int x, int y, int numUnderTile, int colNum) {
        numUnderTile = board[x][y];
        if (numUnderTile == 0 && visitedBoard[x][y] == 0) {
            visitedBoard[x][y] = 1;
            revealAroundZero(guiBoard, board, visitedBoard, x, y, numUnderTile, colNum);
        }
        guiBoard[x][y].setIcon(new ImageIcon(new ImageIcon("Resources/" + numUnderTile + "_icon.png").getImage().getScaledInstance(44,38, Image.SCALE_SMOOTH), "touched"));
    }

    /*
        After the user clicks on a tile that corresponds to a 0 in numBoard, revealAroundZero looks at all of the tiles surrounding the clicked tile,
        and checks each of them using checkUnderTile to see what icons should be placed in the GUI. The if statements take care of different cases
        such as corner tiles and tiles on the edge of the board.
     */

    public void revealAroundZero(JButton[][] guiBoard, int[][] numBoard, int[][] visitedBoard, int xPos, int yPos, int numUnderTile, int colNum) {
        if (xPos == 0 && yPos == 0) {
            for (int xStart = xPos; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else if (xPos == 0 && yPos == numBoard[colNum - 1].length - 1) {
            for (int xStart = xPos; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else if (xPos == numBoard.length - 1 && yPos == 0) {
            for (int xStart = xPos - 1; xStart <= xPos; xStart++) {
                for (int yStart = yPos; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else if (xPos == numBoard.length - 1 && yPos == numBoard[colNum - 1].length - 1) {
            for (int xStart = xPos - 1; xStart <= xPos; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else if (xPos == 0) {
            for (int xStart = xPos; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else if (xPos == numBoard.length - 1) {
            for (int xStart = xPos - 1; xStart <= xPos; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else if (yPos == 0) {
            for (int xStart = xPos - 1; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else if (yPos == numBoard[colNum - 1].length - 1) {
            for (int xStart = xPos - 1; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        } else {
            for (int xStart = xPos - 1; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, visitedBoard, xStart, yStart, numUnderTile, colNum);
                }
            }
        }
    }

}
