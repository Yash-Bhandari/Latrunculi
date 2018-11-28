package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Board extends GameObject {

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
	 * @param g    Graphics context
	 * @param p    Coordinates on the screen
	 * @param team Team the piece is on
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

	public boolean update(Point moved) {
		print();
		Point p = moved;
		boolean taken = false;
		if ((p = left(moved)) != null && flanked(p.x, p.y)) {
			toRemove.add(pieceAt(p));
			removePiece(p);
			taken = true;
		}
		if ((p = right(moved)) != null && flanked(p.x, p.y)) {
			toRemove.add(pieceAt(p));
			removePiece(p);
			taken = true;
		}
		if ((p = above(moved)) != null && flanked(p.x, p.y)) {
			toRemove.add(pieceAt(p));
			removePiece(p);
			taken = true;
		}
		if ((p = below(moved)) != null && flanked(p.x, p.y)) {
			toRemove.add(pieceAt(p));
			removePiece(p);
			taken = true;
		}
		System.out.println(taken);
		return taken;
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
			if (x == XDIM - 1 && centre.isEnemy(left) && (centre.isEnemy(above) || centre.isEnemy(below)))
				return true;
			// on top edge with two enemies around
			if (y == 0 && centre.isEnemy(below) && (centre.isEnemy(left) || centre.isEnemy(right)))
				return true;
			// on bottom edge with two enemies around
			if (x == YDIM - 1 && centre.isEnemy(above) && (centre.isEnemy(left) || centre.isEnemy(right)))
				return true;
		}
		return false;
	}

	public void addPiece(Piece piece, Point p) {
		if (piece.getTeam() == 1)
			numRed++;
		if (piece.getTeam() == 2)
			numBlue++;
		board[p.x][p.y] = piece;
	}

	public void removePiece(Point p) {
		if (board[p.x][p.y].getTeam() == 1)
			numRed--;
		if (board[p.x][p.y].getTeam() == 2)
			numBlue--;
		board[p.x][p.y] = null;
	}

	public Point locationOfSquare(Point p) {
		return new Point(XOFFSET + p.x * SQUARESIZE, YOFFSET + p.y * SQUARESIZE);
	}

	/**
	 * Returns the index of the square at the given point on the board
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
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

	public Piece pieceAt(int x, int y) {
		return board[x][y];
	}

	public Piece pieceAt(Point p) {
		return board[p.x][p.y];
	}

	public int getxDim() {
		return XDIM;
	}

	public int getyDim() {
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
}
