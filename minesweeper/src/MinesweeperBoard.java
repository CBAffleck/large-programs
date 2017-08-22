import com.sun.tools.javac.comp.Flow;

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

    private static int boardSize;

    private int colSize = boardSize;
    private int rowSize = boardSize;
    public static int iconHeight;
    public static int iconWidth;
    private JButton[][] boardTiles = new JButton[colSize][rowSize];
    private JPanel board;
    private int[][] numBoard;
    private int[][] visitedBoard = new int[colSize][rowSize];
    private long start = 0;
    private boolean isGameStarted = false;
    private int numUnderTile;
    private int flagCount = 0;
    private int originalFlagCount;
    private int numUntouched = colSize*rowSize;
    private JLabel numFlags = new JLabel("Flags left: " + flagCount);
    private JFrame winWindow = new JFrame();

    private MinesweeperBoard() {
        startGui();
    }

    private void setSize(final int boardSize) {
        this.boardSize = boardSize;
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        int x;
        int y;
        if (boardSize == 16) { //The numbers that the getX/Y values are being divided by account for the gaps between the JButtons in the grid, so that we can accurately find the x,y coordinates of each tile
            y = ((JButton) e.getSource()).getX()/48; //Gets x index of tile
            x = ((JButton) e.getSource()).getY()/46; //Gets y index of tile
        } else {
            y = ((JButton) e.getSource()).getX()/48; //Gets x index of tile
            x = ((JButton) e.getSource()).getY()/42; //Gets y index of tile
        }
        if (e.getButton() == MouseEvent.BUTTON1) { //Checks if user left-clicks
            System.out.println("(" + x + ", " + y + ")"); //Print out coordinate of clicked tile
            if (!isGameStarted) { //If the user clicks a tile, the game starts
                numUntouched -= 1;
                isGameStarted = true;
                start = System.currentTimeMillis(); //Begins timer
                flagCount = mineGenerator(colSize,rowSize); //Gets random number of mines and sets an equal number of flags
                originalFlagCount = flagCount;
                numFlags.setText("Flags left: " + flagCount);
                numBoard = fillBoard(placeMines(colSize, rowSize, x, y));
                for (int i = 0; i < numBoard.length; i++) {
                    for (int j = 0; j < numBoard[i].length; j++) {
                        System.out.print(numBoard[i][j] + " ");
                    }
                    System.out.println();
                }
            }
            if (numUntouched - originalFlagCount == 0) {
                long totalTime = timePassed();
                winWindow = new JFrame();
                winWindow.setSize(new Dimension(200, 200));
                winWindow.setLocationByPlatform(true);
                winWindow.setLocationRelativeTo(null);
                winWindow.setMinimumSize(winWindow.getSize());
                winWindow.setMaximumSize(winWindow.getSize());
                winWindow.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                JLabel message = new JLabel("You win!");
                winWindow.getContentPane().add(message);
                JLabel stats = new JLabel("Time: " + totalTime + " seconds");
                winWindow.getContentPane().add(stats);
                JButton newGame = new JButton("New Game");
                newGame.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (int i = 0; i < boardTiles.length; i++) {
                            for (int j = 0; j < boardTiles[i].length; j++) {
                                boardTiles[j][i].setIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("untouched_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "background"));
                                boardTiles[j][i].setEnabled(true);
                            }
                        }
                        isGameStarted = false;
                        numUntouched = colSize*rowSize;
                        winWindow.dispose();
                    }
                });
                winWindow.getContentPane().add(newGame);
                JButton quit = new JButton("Quit Game");
                quit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                winWindow.getContentPane().add(quit);
                winWindow.setVisible(true);
            }
            if (!boardTiles[x][y].isEnabled()) { //If a flag is left-clicked, and the tile becomes "touched", the the flag count increases by one.
                flagCount += 1;
                numFlags.setText("Flags left: " + flagCount);
            }
            boardTiles[x][y].setEnabled(true); //If it's a left click, the icon changes to the "touched" icon
            numUntouched -= 1;
            revealNumber(boardTiles, numBoard, visitedBoard, x, y, numUnderTile);
            numUntouched = untouchedTiles(boardTiles);
        } else if (e.getButton() == MouseEvent.BUTTON3) { //If it's a right click, do code for flagging and unflagging tile.
            if (((ImageIcon) boardTiles[x][y].getIcon()).getDescription().equals("touched")) { //Checks if the tile is already touched, so it can't be flagged anymore
                //do nothing
            } else if (boardTiles[x][y].isEnabled() && flagCount != 0) { //If untouched, change icon to flag, letting the user remember the mine location
                numUntouched -= 1;
                boardTiles[x][y].setEnabled(false); //Disabled icon is the flag
                boardTiles[x][y].setDisabledIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("flagged_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "flagged"));
                flagCount -= 1;
                numFlags.setText("Flags left: " + flagCount);
            } else if (boardTiles[x][y].isEnabled()) { //If untouched, and no flags remain, do nothing when right-clicked.
                //do nothing
            } else {
                numUntouched += 1;
                boardTiles[x][y].setEnabled(true); //If the icon is a flag, this unflags the icon.
                boardTiles[x][y].setIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("untouched_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "background"));
                flagCount += 1;
                numFlags.setText("Flags left: " + flagCount);
            }
        }
    }

    public void mousePressed(MouseEvent e) {}

    private int untouchedTiles(JButton [][] board) {
        int numUntouched = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (!((ImageIcon) boardTiles[i][j].getIcon()).getDescription().equals("touched")) {
                    numUntouched += 1;
                }
            }
        }
        return numUntouched;
    }

    private void revealNumber(JButton[][] guiBoard, int[][] numBoard, int[][] visitedBoard, int xPosUserClick, int yPosUserClick, int numUnderTile) {
        numUnderTile = numBoard[xPosUserClick][yPosUserClick];
        if (numBoard[xPosUserClick][yPosUserClick] == 0) {
            revealAroundZero(guiBoard, numBoard, visitedBoard, xPosUserClick, yPosUserClick, numUnderTile, colSize);
        } else if (numBoard[xPosUserClick][yPosUserClick] == 9) {
            guiBoard[xPosUserClick][yPosUserClick].setIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("" + numUnderTile + "_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "touched"));
            long totalTime = timePassed();
            JFrame winWindow = new JFrame();
            winWindow.setSize(new Dimension(200, 200));
            winWindow.setLocationByPlatform(true);
            winWindow.setLocationRelativeTo(null);
            winWindow.setMinimumSize(winWindow.getSize());
            winWindow.setMaximumSize(winWindow.getSize());
            winWindow.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            JLabel message = new JLabel("You hit a mine!");
            winWindow.getContentPane().add(message);
            JLabel stats = new JLabel("Time: " + totalTime + " seconds");
            winWindow.getContentPane().add(stats);
            JButton newGame = new JButton("New Game");
            newGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int i = 0; i < boardTiles.length; i++) {
                        for (int j = 0; j < boardTiles[i].length; j++) {
                            boardTiles[j][i].setIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("untouched_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "background"));
                            boardTiles[j][i].setEnabled(true);
                        }
                    }
                    isGameStarted = false;
                    numUntouched = colSize*rowSize;
                    winWindow.dispose();
                }
            });
            winWindow.getContentPane().add(newGame);
            JButton quit = new JButton("Quit Game");
            quit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            winWindow.getContentPane().add(quit);
            winWindow.setVisible(true);
        } else {
            guiBoard[xPosUserClick][yPosUserClick].setIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("" + numUnderTile + "_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "touched"));
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
        if (boardSize == 16) {
            iconWidth = 45;
            iconHeight = 42;
        } else {
            iconWidth = 44;
            iconHeight = 38;
        }
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar(); //create toolbar to add a new game button above the game grid
        tools.setFloatable(false);
        //Add button to toolbar
        gui.add(tools, BorderLayout.PAGE_START);
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

        board = new JPanel(new GridLayout(0, boardSize));
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
                if (boardSize == 16) {
                    tile.setIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("untouched_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "background"));
                } else {
                    tile.setIcon(new ImageIcon(new ImageIcon(MinesweeperBoard.class.getResource("untouched_icon.png")).getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_SMOOTH), "background"));
                }
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
                JPanel menu = new JPanel();
                Object[] options = {"16x16 Grid", "8x8 Grid"};
                int n = JOptionPane.showOptionDialog(menu, "Choose what size board to play on: ", "Menu", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("9_icon.png"), options, options[1]);
                if (n == JOptionPane.YES_OPTION) {
                    boardSize = 16;
                } else if (n == JOptionPane.NO_OPTION) {
                    boardSize = 8;
                } else {
                    boardSize = 8;
                }

                MinesweeperBoard msb = new MinesweeperBoard();
                msb.setSize(boardSize);

                JFrame frame = new JFrame("Minesweeper");
                frame.add(msb.getGui());
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setLocationByPlatform(true);
                if (boardSize == 8) {
                    frame.setSize(400,400);
                } else {
                    frame.setSize(800,800);
                }
                frame.setMinimumSize(frame.getSize()); //User can't change window size when the min and max are the same
                //frame.setMaximumSize(frame.getSize());
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
