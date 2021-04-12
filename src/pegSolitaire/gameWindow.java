package pegSolitaire;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import java.awt.*;
import java.awt.event.*;

public class gameWindow extends JFrame {
    private Board board;
    private String boardVersion = "en";

    private static final int WINDOW_INIT_WIDTH = 800;
    private static final int WINDOW_INIT_HEIGHT = 600;
    private static final int WINDOW_MIN_WIDTH = WINDOW_INIT_WIDTH/2;
    private static final int WINDOW_MIN_HEIGHT = WINDOW_INIT_HEIGHT/2;

    /*Menu*/
    private final JMenuBar  menuBar = new JMenuBar();

    private final JMenu menuGame = new JMenu("Game");
    private final JMenu menuMoves = new JMenu("Moves");
    private final JMenu menuSettings = new JMenu("Settings");
    private final JMenu menuHelp = new JMenu("Help");


    private final JMenuItem menuGame1= new JMenuItem("New game");
    private final JMenuItem menuGame2 = new JMenuItem("Quit");

    private final JMenuItem menuMoves1 = new JMenuItem("UP");
    private final JMenuItem menuMoves2 = new JMenuItem("LEFT");
    private final JMenuItem menuMoves3 = new JMenuItem("DOWN");
    private final JMenuItem menuMoves4 = new JMenuItem("RIGHT");
    private final JMenuItem menuMoves5 = new JMenuItem("SELECT");

    private final JMenu menuSettings1 = new JMenu("Board version");
    private final ButtonGroup group = new ButtonGroup();
    private final JRadioButtonMenuItem en = new JRadioButtonMenuItem("en");
    private final JRadioButtonMenuItem eu = new JRadioButtonMenuItem("eu");
    private final JMenu menuSettings2 = new JMenu("Color Settings");
    private final JMenuItem pegColorPicker = new JMenuItem("peg color");
    Color pegColor = Color.RED;
    private final JMenuItem gridColorPicker = new JMenuItem("grid Color");
    Color gridColor = Color.GRAY;
    private final JMenuItem holeColorPicker = new JMenuItem("hole Color");
    Color holeColor = Color.WHITE;
    private final JMenuItem wallColorPicker = new JMenuItem("wall color");
    Color wallColor = Color.BLACK;

    private final JMenuItem menuHelp1= new JMenuItem("Game Rules");
    private final JMenuItem menuHelp2 = new JMenuItem("App info");

    /*Rest*/
    private gamePanel panel;
    private final JLabel infoLabel = new JLabel("Game->New Game");
    static final int LABEL_FONT_SIZE = 24;

    {
//***MENU

//GAME
        menuGame1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board = new Board(boardVersion);
                panel = new gamePanel(board, gameWindow.this);
                enableBoardVersionChange(false);
                panel.setLayout(new BorderLayout());
                setContentPane(panel);
                add(infoLabel, BorderLayout.SOUTH);
                invalidate();
                validate();
            }
        });
        menuGame1.setMnemonic(KeyEvent.VK_N);
        menuGame.add(menuGame1);

        menuGame2.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(gameWindow.this,
                "Do you want to Exit?",
                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        menuGame2.setMnemonic(KeyEvent.VK_Q);
        menuGame.add(menuGame2);

// MOVES
        menuMoves1.addActionListener(e -> panel.up());
        menuMoves1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        menuMoves2.addActionListener(e -> panel.left());
        menuMoves2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0));
        menuMoves3.addActionListener(e -> panel.down());
        menuMoves3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0));
        menuMoves4.addActionListener(e -> panel.right());
        menuMoves4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0));
        menuMoves5.addActionListener(e -> panel.select());
        menuMoves5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0));
        menuMoves.add(menuMoves1);
        menuMoves.add(menuMoves2);
        menuMoves.add(menuMoves3);
        menuMoves.add(menuMoves4);
        menuMoves.add(menuMoves5);

// SETTINGS
        en.addActionListener(e -> boardVersion = "en");
        en.setSelected(true);

        eu.addActionListener(e -> boardVersion = "eu");

        group.add(en);
        group.add(eu);
        menuSettings1.add(eu);
        menuSettings1.add(en);
        menuSettings.add(menuSettings1);

        pegColorPicker.addActionListener(e -> pegColor = colorPicker(pegColor));
        gridColorPicker.addActionListener(e ->
                gridColor = colorPicker(gridColor));
        holeColorPicker.addActionListener(e -> holeColor = colorPicker(holeColor));
        wallColorPicker.addActionListener(e -> wallColor = colorPicker(wallColor));
        menuSettings2.add(pegColorPicker);
        menuSettings2.add(gridColorPicker);
        menuSettings2.add(holeColorPicker);
        menuSettings2.add(wallColorPicker);
        menuSettings.add(menuSettings2);

// HELP
        menuHelp1.addActionListener(e -> JOptionPane.showMessageDialog(
                gameWindow.this,
                "The rules of the game are very simple.\n" +
                        "You make moves by jumping a peg over an " +
                        "adjacent peg, much like in checkers,\n except " +
                        "that the moves must be horizontal or vertical.\n" +
                        "The peg jumped over is removed.\n" +
                        "The aim of the game is to remove all the pegs" +
                        "except the last one."
                ,"Game Rules",
                JOptionPane.INFORMATION_MESSAGE));
        menuHelp.add(menuHelp1);

        menuHelp2.addActionListener(e -> JOptionPane.showMessageDialog(
                gameWindow.this,
                "Author: Daniel Wiczołek\n" +
                        "Version: 1.0\n" +
                        "Released: 01/12/2020\n"
                ,"Credits",
                JOptionPane.INFORMATION_MESSAGE));
        menuHelp.add(menuHelp2);
        menuHelp.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

// MENU BAR
        menuBar.add(menuGame);
        menuBar.add(menuMoves);
        menuBar.add(menuSettings);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(menuHelp);
    }

    public gameWindow() {
        super("Peg Solitaire");
        setSize(WINDOW_INIT_WIDTH, WINDOW_INIT_HEIGHT);
        setMinimumSize(new Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT));
        setLocation(500, 100);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        infoLabel.setFont(new Font("serif", Font.PLAIN, LABEL_FONT_SIZE));
        setJMenuBar(menuBar);

        setVisible(true);
    }

    void enableBoardVersionChange(boolean val) {
        en.setEnabled(val);
        eu.setEnabled(val);
    }

    void updateInfoLabel(String s) {
        infoLabel.setText(s);
    }

    private Color colorPicker(Color defaultColor) {
        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
        for(AbstractColorChooserPanel p : panels) {
            String panelName = p.getDisplayName();
            if (!panelName.equals("RGB")) {
                colorChooser.removeChooserPanel(p);
            }
        }
        colorChooser.setColor(defaultColor);
        JDialog d = colorChooser.createDialog(null, "Choose color",
                true, colorChooser,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        repaint();
                    }
                }, null);

        d.setVisible(true);
        return colorChooser.getColor();
    }

}
