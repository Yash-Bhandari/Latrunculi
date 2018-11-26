package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Board extends GameObject {

    private final int squareSize = 100;
    private final int xOffset = 50;
    private final int yOffset = 25;

    private Piece[][] board;
    private ArrayList<Piece> toRemove;
    private int width;
    private int height;
    private int xDim;
    private int yDim;

    public Board(int xDim, int yDim) {
        board = new Piece[xDim][yDim];
        width = squareSize * xDim;
        height = squareSize * yDim;
        this.xDim = xDim;
        this.yDim = yDim;
        toRemove = new ArrayList<Piece>();
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(50, 25, width, height);
        for (int i = 0; i < yDim; i++)
            g.drawLine(xOffset, yOffset + i * squareSize, xOffset + width, yOffset + i * squareSize);
        for (int i = 0; i < xDim; i++)
            g.drawLine(xOffset + i * squareSize, yOffset, xOffset + i * squareSize, yOffset + height);
    }

    /**
     * Draws a preview of a piece at the given location on the screen
     * @param g Graphics context
     * @param p Coordinates on the screen
     * @param team Team the piece is on
     */
    public void renderPreview(Graphics g, Point p, int team) {
        if (within(p)) {
            g.setColor(team == 1 ? Color.red : Color.blue);
            p = squareAtLocation(p);
            p = locationOfSquare(p);
            p.x += getSquareSize() / 4;
            p.y += getSquareSize() / 4;
            g.fillOval(p.x, p.y, getSquareSize() / 2, getSquareSize() / 2);
        }

    }

    public void update(Point moved) {
        print();
        Point p = moved;
        if (flanked(p.x, p.y)) {
            toRemove.add(pieceAt(p));
            removePiece(p);
        }
        if ((p = left(moved)) != null && flanked(p.x, p.y)) {
            toRemove.add(pieceAt(p));
            removePiece(p);
        }
        if ((p = right(moved)) != null && flanked(p.x, p.y)) {
            toRemove.add(pieceAt(p));
            removePiece(p);
        }
        if ((p = above(moved)) != null && flanked(p.x, p.y)) {
            toRemove.add(pieceAt(p));
            removePiece(p);
        }
        if ((p = below(moved)) != null && flanked(p.x, p.y)) {
            toRemove.add(pieceAt(p));
            removePiece(p);
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                flanked(i, j);
            }
        }
    }

    // returns true if a piece is to be destroyed
    public boolean flanked(int x, int y) {
        Piece centre = board[x][y];
        if (centre != null) {
            Piece left = null, right = null, above = null, below = null;
            if (x > 0)
                left = board[x - 1][y];
            if (x < board.length - 1)
                right = board[x + 1][y];
            if (y > 0)
                above = board[x][y - 1];
            if (y < board[y].length - 1)
                below = board[x][y + 1];

            // enemy left and right
            if (centre.isEnemy(left) && centre.isEnemy(right))
                return true;
            // enemy above and below
            if (centre.isEnemy(above) && centre.isEnemy(below))
                return true;
            // on left edge with two enemies around
            if (x == 0 && centre.isEnemy(right) && (centre.isEnemy(above) || centre.isEnemy(below)))
                return true;
            // on right edge with two enemies around
            if (x == xDim - 1 && centre.isEnemy(left) && (centre.isEnemy(above) || centre.isEnemy(below)))
                return true;
            // on top edge with two enemies around
            if (y == 0 && centre.isEnemy(below) && (centre.isEnemy(left) || centre.isEnemy(right)))
                return true;
            // on bottom edge with two enemies around
            if (x == yDim - 1 && centre.isEnemy(above) && (centre.isEnemy(left) || centre.isEnemy(right)))
                return true;
        }
        return false;
    }

    public void addPiece(Piece piece, Point p) {
        board[p.x][p.y] = piece;
    }

    public void removePiece(Point p) {
        board[p.x][p.y] = null;
    }

    public int squareSize() {
        return squareSize;
    }

    public Point locationOfSquare(Point p) {
        return new Point(xOffset + p.x * squareSize, yOffset + p.y * squareSize);
    }

    /**
     * Returns the index of the square at the given point on the board
     * 
     * @param x
     *            X coordinate
     * @param y
     *            Y coordinate
     * @return index of square as dimension, null if outside board
     */
    public Point squareAtLocation(Point p) {
        if (within(p)) {
            int x = (p.x - xOffset) / squareSize;
            int y = (p.y - yOffset) / squareSize;
            return new Point(x, y);
        }
        return null;
    }

    public boolean within(Point p) {
        return (p.x > xOffset && p.x < width + xOffset && p.y > yOffset && p.y < height + yOffset);
    }

    public ArrayList<Piece> toRemove() {
        ArrayList out = (ArrayList<Piece>) toRemove.clone();
        toRemove.clear();
        return out;

    }

    public Piece pieceAt(int x, int y) {
        return board[x][y];
    }

    public Piece pieceAt(Point p) {
        return board[p.x][p.y];
    }

    public int getxDim() {
        return xDim;
    }

    public int getyDim() {
        return yDim;
    }

    public int getSquareSize() {
        return squareSize;
    }

    private void print() {
        for (int y = 0; y < board[0].length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (board[x][y] == null)
                    System.out.print(" 0 ");
                else if (board[x][y].getTeam() == 1)
                    System.out.print(" 1 ");
                else
                    System.out.print(" 2 ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Returns coordinates of point to the left of p
     * 
     * @param p
     * @return Point to the left, null if on left edge
     */
    private Point left(Point p) {
        if (p.x <= 0)
            return null;
        return new Point(p.x - 1, p.y);
    }

    /**
     * Returns coordinates of point to the right of p
     * 
     * @param p
     * @return Point to the left, null if on right edge
     */
    private Point right(Point p) {
        if (p.x >= xDim - 1)
            return null;
        return new Point(p.x + 1, p.y);
    }

    /**
     * Returns coordinates of point above p
     * 
     * @param p
     * @return Point to the left, null if on top edge
     */
    private Point above(Point p) {
        if (p.y <= 0)
            return null;
        return new Point(p.x, p.y - 1);
    }

    /**
     * Returns coordinates of point below p
     * 
     * @param p
     * @return Point to the left, null if on bottom edge
     */
    private Point below(Point p) {
        if (p.y >= yDim - 1)
            return null;
        return new Point(p.x, p.y + 1);
    }
}
