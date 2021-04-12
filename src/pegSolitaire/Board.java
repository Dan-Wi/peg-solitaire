package pegSolitaire;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Board {
    public static final char BOARD_WALL = 'x';
    public static final char BOARD_PEG = '.';
    public static final char BOARD_HOLE = 'o';
    private final StringBuilder[] board;
    private final HashSet<Point> pegs;
    private int boardWidth;
    private int boardHeight;

    /**
    * @param version "en" or "eu"
    * */
    public Board(String version) throws IllegalArgumentException {
        if(version.equals("eu")) {
            board = new StringBuilder[] {
                new StringBuilder("xxxxxxxxxxx"),
                new StringBuilder("xxxxxxxxxxx"),
                new StringBuilder("xxxx...xxxx"),
                new StringBuilder("xxx.....xxx"),
                new StringBuilder("xx.......xx"),
                new StringBuilder("xx...o...xx"),
                new StringBuilder("xx.......xx"),
                new StringBuilder("xxx.....xxx"),
                new StringBuilder("xxxx...xxxx"),
                new StringBuilder("xxxxxxxxxxx"),
                new StringBuilder("xxxxxxxxxxx")
            };
        } else if(version.equals("en")) {
            board = new StringBuilder[] {
                    new StringBuilder("xxxxxxxxxxx"),
                    new StringBuilder("xxxxxxxxxxx"),
                    new StringBuilder("xxxx...xxxx"),
                    new StringBuilder("xxxx...xxxx"),
                    new StringBuilder("xx.......xx"),
                    new StringBuilder("xx...o...xx"),
                    new StringBuilder("xx.......xx"),
                    new StringBuilder("xxxx...xxxx"),
                    new StringBuilder("xxxx...xxxx"),
                    new StringBuilder("xxxxxxxxxxx"),
                    new StringBuilder("xxxxxxxxxxx")
            };
        } else {
            throw new IllegalArgumentException("Unknown board version");
        }

        boardWidth = board[0].length();
        boardHeight = board.length;

        pegs = new HashSet<>();
        for(int i = 0; i < boardHeight; ++i) {
            for(int j = 0; j < boardWidth; ++j) {
                if(board[i].charAt(j) == BOARD_PEG) {
                    pegs.add(new Point(j, i));
                }
            }
        }
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public char at(int y, int x) throws IndexOutOfBoundsException {
        return board[y].charAt(x);
    }

    public boolean isMoveValid(int srcX, int srcY, int destX, int destY) {
        boolean validMove = false;
        try {
            boolean horizontal = (Math.abs(srcX - destX) == 2 && srcY == destY);
            boolean vertical = (Math.abs(srcY - destY) == 2 && srcX == destX);

            if (board[srcY].charAt(srcX) == BOARD_PEG &&
                    board[destY].charAt(destX) == BOARD_HOLE) {

                if (horizontal) {
                    int offset = srcX > destX ? -1 : 1;
                    validMove = board[srcY].charAt(srcX + offset) == BOARD_PEG;
                } else if (vertical) {
                    int offset = srcY > destY ? -1 : 1;
                    validMove = board[srcY + offset].charAt(srcX) == BOARD_PEG;
                }
            }
        } catch (Exception e) { }
        return validMove;
    }

    public boolean makeMove(int srcX, int srcY, int destX, int destY) {
        boolean validMove = false;
        if(validMove = isMoveValid(srcX, srcY, destX, destY)) {
            board[(srcY+destY)/2].setCharAt((srcX+destX)/2, BOARD_HOLE);
            board[srcY].setCharAt(srcX, BOARD_HOLE);
            board[destY].setCharAt(destX, BOARD_PEG);
            pegs.remove(new Point((srcX+destX)/2, (srcY+destY)/2));
            pegs.remove(new Point(srcX, srcY));
            pegs.add(new Point(destX, destY));
        }
        return validMove;
    }

    public int numberOfPegsLeft() {
        return pegs.size();
    }

    public ArrayList<Point> availableMoves(int x, int y) {
        ArrayList<Point> result = new ArrayList<>();
        if(isMoveValid(x, y, x+2, y)) result.add(new Point(x+2, y));
        if(isMoveValid(x, y, x-2, y)) result.add(new Point(x-2, y));
        if(isMoveValid(x, y, x, y+2)) result.add(new Point(x, y+2));
        if(isMoveValid(x, y, x, y-2)) result.add(new Point(x, y-2));
        return result;
    }

    public boolean gameOver() {
        boolean movePossible = false;
        for(Point p : pegs) {
            if(availableMoves(p.x, p.y).size() > 0) {
                movePossible = true;
                break;
            }
        }
        return !movePossible || (numberOfPegsLeft() == 1);
    }

    @Override
    public String toString() {
        String s = new String();
        for(int i = 0; i < board.length; ++i) {
            s += board[i].toString() + '\n';
        }
        return s;
    }
}
