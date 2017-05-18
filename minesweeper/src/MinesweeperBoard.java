import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Created by CampbellAffleck on 5/17/17.
 */
public class MinesweeperBoard {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] boardTiles = new JButton[8][8];
    private JPanel board;
    private JLabel message = new JLabel("The mines are planted!");

    public MinesweeperBoard() {
        startGui();
    }

    public final void startGui() {
        // Sets up gui for the game.
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        tools.add(new JButton("Main Menu"));
        tools.addSeparator();
        tools.add(new JButton("New Game"));
        tools.addSeparator();
        tools.add(new JButton("Reset"));
        tools.addSeparator();
        gui.add(new JLabel("Mines found: 0"));
        tools.add(message);

        board = new JPanel(new GridLayout(0, 8));
        board.setBackground(Color.decode("#fff2d3"));
        board.setBorder(new LineBorder(Color.black));
        gui.add(board);

        //Adds button tiles to the board
        Insets buttonMargin = new Insets(1,1,1,1);
        for (int i = 0; i < boardTiles.length; i++) {
            for (int j = 0; j < boardTiles[i].length; j++) {
                JButton tile = new JButton();
                tile.setMargin(buttonMargin);
                //ImageIcon icon = new ImageIcon(new ImageIcon("Resources/untouched_icon.png").getImage().getScaledInstance(34,34,Image.SCALE_SMOOTH));
                //tile.setIcon(icon);
                //tile.setBackground(Color.decode("#74d1fc"));
                boardTiles[j][i] = tile;
            }
        }

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
                //frame.setBackground(Color.decode("#fff2d3"));
                frame.setMinimumSize(frame.getSize());
                frame.setMaximumSize(frame.getSize());
                frame.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
