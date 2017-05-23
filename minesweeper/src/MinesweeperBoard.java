import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

/**
 * Created by CampbellAffleck on 5/17/17.
 */
public class MinesweeperBoard implements MouseListener {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] boardTiles = new JButton[8][8];
    private JPanel board;
    private long start = 0;
    private boolean isGameStarted = false;

    public MinesweeperBoard() {
        startGui();
    }

    public void mouseEntered(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
        return;
    }

    public void mouseExited(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
        return;
    }

    public void mouseClicked(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
        return;
    }

    public void mouseReleased(MouseEvent e) {
        int x = ((JButton) e.getSource()).getX()/48; //Gets x index of tile
        int y = ((JButton) e.getSource()).getY()/42; //Gets y index of tile
        if (e.getButton() == MouseEvent.BUTTON1) { //Checks if user left-clicks
            if (!isGameStarted) { //If the user clicks a tile, the timer begins
                isGameStarted = true;
                start = System.currentTimeMillis();
            }
            boardTiles[x][y].setEnabled(true); //If it's a left click, the icon changes to the "touched" icon
            // TODO: allow icon to change to numbered icon based on number of surrounding bombs, or change to blank icon if no bombs around it
            boardTiles[x][y].setIcon(new ImageIcon(new ImageIcon("Resources/touched_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "touched"));
        } else if (e.getButton() == MouseEvent.BUTTON3) { //If it's a right click, do code for flagging and unflagging tile.
            if (((ImageIcon) boardTiles[x][y].getIcon()).getDescription().equals("touched")) { //Checks if the tile is already touched, so it can't be flagged anymore
                //do nothing
            } else if (boardTiles[x][y].isEnabled()) { //If untouched, change icon to flag, letting the user remember mine location
                boardTiles[x][y].setEnabled(false); //Disabled icon is the flag
                boardTiles[x][y].setDisabledIcon(new ImageIcon(new ImageIcon("Resources/flagged_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "flagged"));
            } else {
                boardTiles[x][y].setEnabled(true); //If the icon is a flag, this unflags the icon.
                boardTiles[x][y].setIcon(new ImageIcon(new ImageIcon("Resources/untouched_icon.png").getImage().getScaledInstance(44,38,Image.SCALE_SMOOTH), "background"));
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        //throw new java.lang.UnsupportedOperationException("Not implemented");
        return;
    }

    public long timePassed() { //Sets timePassed to 0 until the user begins the game
        if (isGameStarted) {
            long passed = (System.currentTimeMillis() - start) / 1000;
            return passed;
        } else {
            return 0;
        }
    }

    public final void startGui() {
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board.add(boardTiles[j][i]);
            }
        }
    }

    public final JComponent getBoard() {
        return board;
    }

    public final JComponent getGui() {
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
