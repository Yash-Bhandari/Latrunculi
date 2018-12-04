package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

public class Board extends GameObject implements Cloneable, Iterable<Point> {

    private final int SQUARESIZE = 100;
    private final int XOFFSET = 50;
    private final int YOFFSET = 25;
    private final int WIDTH;
    private final int HEIGHT;
    private final int XDIM;
    private final int YDIM;

    private Piece[][] board;
    private ArrayList<Piece> toRemove;
    private int numRed = 0;
    private int numBlue = 0;

    public Board(int XDIM, int YDIM) {
        board = new Piece[XDIM][YDIM];
        WIDTH = SQUARESIZE * XDIM;
        HEIGHT = SQUARESIZE * YDIM;
        this.XDIM = XDIM;
        this.YDIM = YDIM;
        toRemove = new ArrayList<Piece>();
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(50, 25, WIDTH, HEIGHT);
        for (int i = 0; i < YDIM; i++)
            g.drawLine(XOFFSET, YOFFSET + i * SQUARESIZE, XOFFSET + WIDTH, YOFFSET + i * SQUARESIZE);
        for (int i = 0; i < XDIM; i++)
            g.drawLine(XOFFSET + i * SQUARESIZE, YOFFSET, XOFFSET + i * SQUARESIZE, YOFFSET + HEIGHT);
    }

    /**
     * Draws a preview of a piece at the given location on the screen
     * 
     * @param g
     *            Graphics context
     * @param p
     *            Coordinates on the screen
     * @param team
     *            Team the piece is on
     */
    public void renderPreview(Graphics g, Point p, int team, boolean isDux) {
        if (within(p)) {
            g.setColor(team == 1 ? Color.red : Color.blue);
            p = squareAtLocation(p);
            p = locationOfSquare(p);
            p.x += getSquareSize() / 4;
            p.y += getSquareSize() / 4;
            g.fillOval(p.x, p.y, getSquareSize() / 2, getSquareSize() / 2);
            if (isDux) {
                g.setColor(team == 1 ? new Color(128, 10, 50) : new Color(50, 10, 128));
                p.x += getSquareSize() / 8;
                p.y += getSquareSize() / 8;
                g.fillOval(p.x, p.y, getSquareSize() / 4, getSquareSize() / 4);
            }
        }

    }

    // performs capture logic on pieces immediately adjacent to given point
    // pieces will only actually be removed if remove is set to true
    // returns the number of pieces captured
    public int update(Point moved, boolean remove) {
        Point p = moved;
        int team = pieceAt(p).team;
        int taken = 0;
        if (pieceAt((p = left(moved))) != null && pieceAt(p).getTeam() != team && flanked(p, moved)) {
            if (remove) {
                toRemove.add(pieceAt(p));
                removePiece(p);
            }
            taken++;
        }
        if (pieceAt((p = right(moved))) != null && pieceAt(p).getTeam() != team && flanked(p, moved)) {
            if (remove) {
                toRemove.add(pieceAt(p));
                removePiece(p);
            }
            taken++;
        }
        if (pieceAt((p = above(moved))) != null && pieceAt(p).getTeam() != team && flanked(p, moved)) {
            if (remove) {
                toRemove.add(pieceAt(p));
                removePiece(p);
            }
            taken++;
        }
        if (pieceAt((p = below(moved))) != null && pieceAt(p).getTeam() != team && flanked(p, moved)) {
            if (remove) {
                toRemove.add(pieceAt(p));
                removePiece(p);
            }
            taken++;
        }
        return taken;
    }

    // returns true if attacked piece is to be destroyed
    public boolean flanked(Point attacked, Point attacker) {
        attacker = pieceAt(attacker).getPoint();
        Piece centre = pieceAt(attacked);
        if (centre != null) {

            Piece left, right, above, below;
            left = pieceAt(left(attacked));
            right = pieceAt(right(attacked));
            above = pieceAt(above(attacked));
            below = pieceAt(below(attacked));

            // enemy left and right
            if (pieceLocation(left).equals(attacker) || pieceLocation(right).equals(attacker))
                if (centre.isEnemy(left) && centre.isEnemy(right))
                    return true;
            // enemy above and below
            if (pieceLocation(above).equals(attacker) || pieceLocation(below).equals(attacker))
                if (centre.isEnemy(above) && centre.isEnemy(below))
                    return true;
            // on left edge with two enemies around
            if (pieceLocation(below).equals(attacker) || pieceLocation(left).equals(attacker)
                    || pieceLocation(right).equals(attacker))
                if (attacked.x == 0 && centre.isEnemy(right) && ((centre.isEnemy(above) || centre.isEnemy(below))))
                    return true;
            // on right edge with two enemies around
            if (pieceLocation(below).equals(attacker) || pieceLocation(left).equals(attacker)
                    || pieceLocation(above).equals(attacker))
                if (attacked.x == XDIM - 1 && centre.isEnemy(left)
                        && ((centre.isEnemy(above) || centre.isEnemy(below))))
                    return true;
            // on top edge with two enemies around
            if (pieceLocation(below).equals(attacker) || pieceLocation(left).equals(attacker)
                    || pieceLocation(right).equals(attacker))
                if (attacked.y == 0 && centre.isEnemy(below) && ((centre.isEnemy(left) || centre.isEnemy(right))))
                    return true;
            // on bottom edge with two enemies around
            if (pieceLocation(left).equals(attacker) || pieceLocation(right).equals(attacker)
                    || pieceLocation(above).equals(attacker))
                if (attacked.y == YDIM - 1 && centre.isEnemy(above)
                        && ((centre.isEnemy(left) || centre.isEnemy(right))))
                    return true;
        }
        return false;
    }

    public void addPiece(Piece piece, Point p) {
        if (piece != null) {
            if (piece.getTeam() == 1)
                numRed++;
            if (piece.getTeam() == 2)
                numBlue++;
        }
        board[p.x][p.y] = piece;
    }

    public void setPiece(Piece piece, Point p) {
        board[p.x][p.y] = piece;
    }

    public void removePiece(Point p) {
        if (pieceAt(p) != null) {
            if (board[p.x][p.y].getTeam() == 1)
                numRed--;
            if (board[p.x][p.y].getTeam() == 2)
                numBlue--;
        }
        board[p.x][p.y] = null;
    }

    // moves the piece at the first point to the second point
    public void move(Point piece, Point toMove) {
        setPiece(pieceAt(piece), toMove);
        setPiece(null, piece);
        pieceAt(toMove).move(toMove);
    }

    // returns a new board after having moved the piece at the original point to a
    // new one.
    // creates a deep clone
    public Board copyMove(Point original, Point toMove) {
        Board b = clone();

        b.addPiece(b.pieceAt(original), toMove);
        b.removePiece(original);
        return b;
    }

    public Point locationOfSquare(Point p) {
        return new Point(XOFFSET + p.x * SQUARESIZE, YOFFSET + p.y * SQUARESIZE);
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
            int x = (p.x - XOFFSET) / SQUARESIZE;
            int y = (p.y - YOFFSET) / SQUARESIZE;
            return new Point(x, y);
        }
        return null;
    }

    // returns the location of the piece or (-1, -1) if the argument is null
    public Point pieceLocation(Piece p) {
        return p != null ? p.getPoint() : new Point(-1, -1);
    }

    public boolean within(Point p) {
        return (p.x > XOFFSET && p.x < WIDTH + XOFFSET && p.y > YOFFSET && p.y < HEIGHT + YOFFSET);
    }

    public ArrayList<Piece> toRemove() {
        ArrayList out = (ArrayList<Piece>) toRemove.clone();
        toRemove.clear();
        return out;

    }

    public void clear() {
        board = new Piece[XDIM][YDIM];
        numRed = 0;
        numBlue = 0;
    }

    // returns a deep clone of the current board
    public Board clone() {
        Board b = new Board(XDIM, YDIM);
        for (int i = 0; i < XDIM; i++) {
            for (int j = 0; j < YDIM; j++) {
                Point p = new Point(i, j);
                if (pieceAt(p) != null) {
                    b.addPiece(new Piece(b, pieceAt(p).getTeam(), p), p);
                }
            }
        }
        return b;
    }

    public Piece pieceAt(int x, int y) {
        return board[x][y];
    }

    public Piece pieceAt(Point p) {
        if (p == null || p.x >= XDIM || p.y >= YDIM || p.x < 0 || p.y < 0)
            return null;
        return board[p.x][p.y];
    }

    public int getXDim() {
        return XDIM;
    }

    public int getYDim() {
        return YDIM;
    }

    public int getSquareSize() {
        return SQUARESIZE;
    }

    public int numRed() {
        return numRed;
    }

    public int numBlue() {
        return numBlue;
    }

    public int SQUARESIZE() {
        return SQUARESIZE;
    }

    public void print() {
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
     * @return Point to the left, negative x if on left edge
     */
    public Point left(Point p) {
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
    public Point right(Point p) {
        if (p.x >= XDIM - 1)
            return null;
        return new Point(p.x + 1, p.y);
    }

    /**
     * Returns coordinates of point above p
     * 
     * @param p
     * @return Point to the left, null if on top edge
     */
    public Point above(Point p) {
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
    public Point below(Point p) {
        if (p.y >= YDIM - 1)
            return null;
        return new Point(p.x, p.y + 1);
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            int x = 0;
            int y = 0;

            @Override
            public boolean hasNext() {
                return y < getYDim();
            }

            @Override
            public Point next() {
                Point next = new Point(x++, y);
                if (x >= getXDim()) {
                    x = 0;
                    y++;
                }
                return next;
            }

        };
    }

}
