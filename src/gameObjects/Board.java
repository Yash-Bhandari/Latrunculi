package gameObjects;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;

import game.Game;

public class Board extends GameObject {

    private final int squareSize = 100;
    private final int xOffset = 50;
    private final int yOffset = 25;

    private Piece[][] board;
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
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(50, 25, width, height);
        for (int i = 0; i < yDim; i++)
            g.drawLine(xOffset, yOffset + i * squareSize, xOffset + width, yOffset + i * squareSize);
        for (int i = 0; i < xDim; i++)
            g.drawLine(xOffset + i * squareSize, yOffset, xOffset + i * squareSize, yOffset + height);
    }

    public void addPiece(Piece piece, Point p) {
        board[p.x][p.y] = piece;
    }

    public int squareSize() {
        return squareSize;
    }

    public Point locationOfSquare(Point p) {
        return new Point(xOffset + p.x * squareSize, yOffset + p.y * squareSize);
    }

    /**
     * Returns the index of the square at the given point
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

}
