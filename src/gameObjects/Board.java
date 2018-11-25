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

    public void update() {
        print();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Piece centre = board[i][j];
                if (centre != null) {
                    Piece left = null, right = null, above = null, below = null;
                    if (i > 0)
                        left = board[i - 1][j];
                    if (i < board.length - 1)
                        right = board[i + 1][j];
                    if (j > 0)
                        above = board[i][j - 1];
                    if (j < board[i].length - 1)
                        below = board[i][j + 1];
                    if ((left != null && right != null && left.getTeam() != centre.getTeam()
                            && right.getTeam() != centre.getTeam())
                            || (above != null && below != null && above.getTeam() != centre.getTeam()
                                    && below.getTeam() != centre.getTeam())) {
                        toRemove.add(centre);
                        removePiece(new Point(i, j));
                    }
                }
            }
        }
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
    }

}
