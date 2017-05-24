import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;

/**
 * Created by CampbellAffleck on 5/17/17.

    Useful code for printing out numBoard:
        for (int i = 0; i < numBoard.length; i++) {
            for (int j = 0; j < numBoard[i].length; j++) {
                System.out.print(numBoard[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("\n");

 */
public class MinesweeperBoard extends GameRules implements MouseListener {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private int colSize = 8;
    private int rowSize = 8;
    private JButton[][] boardTiles = new JButton[colSize][rowSize];
    private JPanel board;
    private int[][] numBoard;
    private int[][] visitedBoard = new int[colSize][rowSize];
    private long start = 0;
    private boolean isGameStarted = false;
    private int numUnderTile;
    private int flagCount = 0;  //TODO: This is set at 10 for testing, but later will be set to whatever the randomized # of mines is.
    private JLabel numFlags = new JLabel("Flags left: " + flagCount);

    private MinesweeperBoard() {
        startGui();
    }

    public void mouseEntered(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
    }

    public void mouseExited(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
    }

    public void mouseClicked(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
    }

    public void mouseReleased(MouseEvent e) {
        int y = ((JButton) e.getSource()).getX()/48; //Gets x index of tile
        int x = ((JButton) e.getSource()).getY()/42; //Gets y index of tile
        if (e.getButton() == MouseEvent.BUTTON1) { //Checks if user left-clicks
            //System.out.println("(" + x + ", " + y + ")"); //Print out coordinate of clicked tile
            if (!isGameStarted) { //If the user clicks a tile, the game starts
                isGameStarted = true;
                start = System.currentTimeMillis(); //Begins timer
                flagCount = mineGenerator(colSize,rowSize); //Gets random number of mines and sets an equal number of flags
                numFlags.setText("Flags left: " + flagCount);
                numBoard = fillBoard(placeMines(colSize, rowSize, x, y));
                for (int i = 0; i < numBoard.length; i++) {
                    for (int j = 0; j < numBoard[i].length; j++) {
                        System.out.print(numBoard[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
            }
            if (!boardTiles[x][y].isEnabled()) { //If a flag is left-clicked, and the tile becomes "touched", the the flag count increases by one.
                flagCount += 1;
                numFlags.setText("Flags left: " + flagCount);
            }
            boardTiles[x][y].setEnabled(true); //If it's a left click, the icon changes to the "touched" icon
            // TODO: allow icon to change to numbered icon based on number of surrounding bombs, or change to blank icon if no bombs around it
            revealNumber(boardTiles, numBoard, x, y);
        } else if (e.getButton() == MouseEvent.BUTTON3) { //If it's a right click, do code for flagging and unflagging tile.
            if (((ImageIcon) boardTiles[x][y].getIcon()).getDescription().equals("touched")) { //Checks if the tile is already touched, so it can't be flagged anymore
                //do nothing
            } else if (boardTiles[x][y].isEnabled() && flagCount != 0) { //If untouched, change icon to flag, letting the user remember the mine location
                boardTiles[x][y].setEnabled(false); //Disabled icon is the flag
                boardTiles[x][y].setDisabledIcon(new ImageIcon(new ImageIcon("Resources/flagged_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "flagged"));
                flagCount -= 1;
                numFlags.setText("Flags left: " + flagCount);
            } else if (boardTiles[x][y].isEnabled()) { //If untouched, and no flags remain, do nothing when right-clicked.
                //do nothing
            } else {
                boardTiles[x][y].setEnabled(true); //If the icon is a flag, this unflags the icon.
                boardTiles[x][y].setIcon(new ImageIcon(new ImageIcon("Resources/untouched_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "background"));
                flagCount += 1;
                numFlags.setText("Flags left: " + flagCount);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
    }

    private void checkUnderTile(JButton[][] guiBoard, int[][] board, int x, int y) {
        numUnderTile = board[x][y];
        if (numUnderTile == 0 && visitedBoard[x][y] == 0) {
            visitedBoard[x][y] = 1;
            revealAroundZero(guiBoard, board, x, y);
        }
        guiBoard[x][y].setIcon(new ImageIcon(new ImageIcon("Resources/" + numUnderTile + "_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "touched"));
    }

    private void revealAroundZero(JButton[][] guiBoard, int[][] numBoard, int xPos, int yPos) {
        if (xPos == 0 && yPos == 0) {
            for (int xStart = xPos; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else if (xPos == 0 && yPos == 7) {
            for (int xStart = xPos; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else if (xPos == 7 && yPos == 0) {
            for (int xStart = xPos - 1; xStart <= xPos; xStart++) {
                for (int yStart = yPos; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else if (xPos == 7 && yPos == 7) {
            for (int xStart = xPos - 1; xStart <= xPos; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else if (xPos == 0) {
            for (int xStart = xPos; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else if (xPos == 7) {
            for (int xStart = xPos - 1; xStart <= xPos; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else if (yPos == 0) {
            for (int xStart = xPos - 1; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else if (yPos == 7) {
            for (int xStart = xPos - 1; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        } else {
            for (int xStart = xPos - 1; xStart <= xPos + 1; xStart++) {
                for (int yStart = yPos - 1; yStart <= yPos + 1; yStart++) {
                    checkUnderTile(guiBoard, numBoard, xStart, yStart);
                }
            }
        }
    }

    private void revealNumber(JButton[][] guiBoard, int[][] numBoard, int xPosUserClick, int yPosUserClick) {
        int numUnderTile = numBoard[xPosUserClick][yPosUserClick];
        if (numBoard[xPosUserClick][yPosUserClick] == 0) {
            revealAroundZero(guiBoard, numBoard, xPosUserClick, yPosUserClick);
        } else if (numBoard[xPosUserClick][yPosUserClick] == 9) {
            guiBoard[xPosUserClick][yPosUserClick].setIcon(new ImageIcon(new ImageIcon("Resources/" + numUnderTile + "_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "touched"));
        } else {
            guiBoard[xPosUserClick][yPosUserClick].setIcon(new ImageIcon(new ImageIcon("Resources/" + numUnderTile + "_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "touched"));
        }
    }

    private long timePassed() { //Sets timePassed to 0 until the user begins the game
        if (isGameStarted) {
            return (System.currentTimeMillis() - start) / 1000;
        } else {
            return 0;
        }
    }

    private void startGui() {
        // Sets up gui for the game.
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar(); //create toolbar to add a new game button above the game grid
        tools.setFloatable(false);
        //Add button to toolbar
        gui.add(tools, BorderLayout.PAGE_START);
        JButton newGame = new JButton("New Game");
        newGame.addMouseListener(this);
        tools.add(newGame);
        tools.addSeparator();
        //JLabel flagCount = new JLabel("Flags left: " + numFlags); //TODO: Fix flag count not decreasing.
        tools.add(numFlags);
        tools.addSeparator();
        JLabel counter = new JLabel("Time Passed: " + timePassed() + " sec"); //Shows time passed in seconds
        tools.add(counter);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isGameStarted) {
                    counter.setText("Time Passed: " + timePassed() + " sec");
                }
            }
        }; //updates timer
        Timer timer = new Timer(0, actionListener);
        timer.start();

        board = new JPanel(new GridLayout(0, 8));
        board.setBackground(Color.decode("#fff2d3"));
        board.setBorder(new LineBorder(Color.black));
        gui.add(board);

        //Creates JButton tiles for board
        Insets buttonMargin = new Insets(1,1,1,1);
        for (int i = 0; i < boardTiles.length; i++) {
            for (int j = 0; j < boardTiles[i].length; j++) {
                JButton tile = new JButton();
                tile.setMargin(buttonMargin);
                tile.setEnabled(true); //sets default, untouched tile as "enabled"
                tile.setIcon(new ImageIcon(new ImageIcon("Resources/untouched_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "background"));
                tile.setBackground(Color.decode("#74d1fc"));
                tile.addMouseListener(this); //adds mouseListener to the JButton tile
                boardTiles[j][i] = tile;
            }
        }

        //Adds all of the tiles to the board
        for (int i = 0; i < colSize; i++) {
            for (int j = 0; j < rowSize; j++) {
                board.add(boardTiles[i][j]);
            }
        }
    }

    private JComponent getGui() {
        return gui;
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                MinesweeperBoard msb = new MinesweeperBoard();
                JFrame frame = new JFrame("Minesweeper");
                frame.add(msb.getGui());
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setLocationByPlatform(true);
                frame.setSize(400,400);
                frame.setMinimumSize(frame.getSize()); //User can't change window size when the min and max are the same
                frame.setMaximumSize(frame.getSize());
                frame.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
