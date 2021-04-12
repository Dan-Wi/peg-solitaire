package pegSolitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class gamePanel extends JPanel {

    private final Shape[][] grid;
    private Point selected;
    private boolean gameOver = false;

    private int gridCellWidth;
    private int gridPegRadius;
    private int gridPegOffset;
    private int gridCols;
    private int gridRows;
    final Board board;
    final gameWindow window;

    private Point keyboardHighlight = new Point(0,0);

    private void handleSelectEvent(int x, int y) {
        if (!gameOver) {
            char field = '\0';
            try {
                field = board.at(y, x);
            } catch (Exception ex) { }

            if (field == Board.BOARD_PEG) {
                selected = new Point(x, y);
                window.repaint();
            } else if (selected != null) { // make move?
                if (board.makeMove(selected.x, selected.y, x, y)) {
                    if (board.gameOver()) {
                        String s = board.numberOfPegsLeft() == 1 ? "You win! " :
                                "You lose! Number of pegs left: " + board.numberOfPegsLeft();
                        window.updateInfoLabel(s);
                        gameOver = true;
                        window.enableBoardVersionChange(true);
                    } else {
                        window.updateInfoLabel("Pegs left: " + board.numberOfPegsLeft());
                    }
                } else {
                    window.updateInfoLabel("Invalid move");
                }
                selected = null;
                window.repaint();
            }
        }
    }

    void select() {
        handleSelectEvent(keyboardHighlight.x, keyboardHighlight.y);
    }

    void up() {
        if(keyboardHighlight.y > 0) {
            keyboardHighlight.y--;
            repaint();
        }
    }

    void left() {
        if(keyboardHighlight.x > 0) {
            keyboardHighlight.x--;
            repaint();
        }
    }

    void down() {
        if(keyboardHighlight.y < gridRows-1) {
            keyboardHighlight.y++;
            repaint();
        }
    }

    void right() {
        if(keyboardHighlight.x < gridCols-1) {
            keyboardHighlight.x++;
            repaint();
        }
    }

    public gamePanel(Board b, gameWindow w) {
        setFocusable(true);
        board = b;
        window = w;
        gridCols = board.getBoardWidth();
        gridRows = board.getBoardHeight();
        grid = new Shape[gridRows][gridCols];
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX()/gridCellWidth;
                int y = e.getY()/gridCellWidth;
                if(e.getButton() == MouseEvent.BUTTON1) {
                    handleSelectEvent(x, y);
                } else if(e.getButton() == MouseEvent.BUTTON3) {
                    if (!gameOver) {
                        char field = '\0';
                        try {
                            field = board.at(y, x);
                        } catch (Exception ex) {
                        }
                        if(field == Board.BOARD_PEG) {
                            selected = new Point(x, y);
                            repaint();
                            ArrayList<String> directions = new ArrayList<>();
                            directions.add("CLOSE");
                            ArrayList<Point> points = board.availableMoves(x, y);
                            for(Point p : points) {
                                if(p.y > y) directions.add("DOWN");
                                else if(p.x > x) directions.add("RIGHT");
                                else if(p.y < y) directions.add("UP");
                                else if(p.x < x) directions.add("LEFT");
                            }
                            int response = JOptionPane.showOptionDialog(null, "Available moves:",
                                    "Which way?",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                                    null, directions.toArray(), directions.get(0));

                            if(response == JOptionPane.CLOSED_OPTION) response = 0;

                            switch(directions.get(response)) {
                                case "UP": handleSelectEvent(x, y-2); break;
                                case "LEFT": handleSelectEvent(x-2, y); break;
                                case "DOWN": handleSelectEvent(x, y+2); break;
                                case "RIGHT": handleSelectEvent(x+2, y); break;
                            }

                        }
                    }
                }
            }
        });

        updateGrid();
        window.updateInfoLabel("The game has started");
    }

    private void updateGrid() {
        gridCellWidth = Math.min(window.getWidth() - 2 * gameWindow.LABEL_FONT_SIZE,
                window.getHeight() - 2 * gameWindow.LABEL_FONT_SIZE)
                / Math.max(gridCols+2, gridRows+2);
        gridPegRadius = 2 * gridCellWidth / 3;
        gridPegOffset = gridPegRadius - gridCellWidth / 2;
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                grid[row][col] = new Rectangle(
                        gridCellWidth * col,
                        gridCellWidth * row,
                        gridCellWidth, gridCellWidth);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateGrid();
        Graphics2D g2d = (Graphics2D) g;
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                if (board.at(row, col) == Board.BOARD_WALL) {
                    if(row == keyboardHighlight.y && col == keyboardHighlight.x) {
                        g2d.setColor(window.gridColor.darker());
                    } else {
                        g2d.setColor(window.wallColor);
                    }

                    g.fillRect(grid[row][col].getBounds().x,
                            grid[row][col].getBounds().y,
                            grid[row][col].getBounds().width,
                            grid[row][col].getBounds().height
                    );
                } else if (board.at(row, col) == Board.BOARD_HOLE) {
                    if(row == keyboardHighlight.y && col == keyboardHighlight.x) {
                        g2d.setColor(window.gridColor.darker());
                    } else {
                        g2d.setColor(window.holeColor);
                    }

                    g.fillRect(grid[row][col].getBounds().x,
                            grid[row][col].getBounds().y,
                            grid[row][col].getBounds().width,
                            grid[row][col].getBounds().height
                    );
                } else {
                    if(row == keyboardHighlight.y && col == keyboardHighlight.x) {
                        g2d.setColor(window.gridColor.darker());
                    } else {
                        if (selected != null && row == selected.y
                                && col == selected.x) {
                            g2d.setColor(window.pegColor.darker().darker());
                        } else {
                            g2d.setColor(window.pegColor);
                        }
                    }
                    g.fillOval(grid[row][col].getBounds().x + gridPegOffset,
                            grid[row][col].getBounds().y + gridPegOffset,
                            gridPegRadius,
                            gridPegRadius
                    );
                }
                g.setColor(window.gridColor);
                g2d.draw(grid[row][col]);
            }
        }
    }
}