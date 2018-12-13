package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Piece extends GameObject {

	// time it takes for a piece to move in update ticks (60/s)
	private final int MOVETIME = 45;

	protected Board board;
	protected boolean selected;
	protected Point point;
	private Point change;
	private Point screenLocation;
	protected int team;
	private int moveProgress;
	protected ArrayList<Point> moves;
	private Color color;

	public Piece(Board board, int team, Point p) {
		this.board = board;
		point = p;
		this.team = team;
		screenLocation = board.locationOfSquare(point);
		moveProgress = 0;
	}

	public void render(Graphics g) {
		updateLocation();
		g.setColor(team == 1 ? Color.red : Color.blue);
		g.fillOval(screenLocation.x, screenLocation.y, board.getSquareSize(), board.getSquareSize());
		if (selected && !isMoving())
			renderMoves(g);
	}

	// draws preview at half size
	protected void renderMoves(Graphics g) {
		for (Point move : moves) {
			g.setColor(team == 1 ? Color.red : Color.blue);
			Point p = board.locationOfSquare(move);
			p.x += board.getSquareSize() / 4;
			p.y += board.getSquareSize() / 4;
			g.fillOval(p.x, p.y, board.getSquareSize() / 2, board.getSquareSize() / 2);
		}
	}

	private void updateLocation() {
		if (isMoving() && moveProgress <= MOVETIME) {
			screenLocation = board.locationOfSquare(point);
			screenLocation.x += (board.getSquareSize() * moveProgress * change.x) / MOVETIME;
			screenLocation.y += (board.getSquareSize() * moveProgress * change.y) / MOVETIME;
			moveProgress++;
		}
		if (moveProgress > MOVETIME) {
			moveProgress = 0;
			point.x += change.x;
			point.y += change.y;
			moves();
		}

	}

	// starts moving piece to given point, moves() will be called once it completes
	// animation
	public void move(Point p) {
		change = new Point(p.x - point.x, p.y - point.y);
		moveProgress = 1;
		// moves();
	}

	// moves a piece immediately with no animation
	public void moveNoAnimation(Point p) {
		point = p;
		moves();
	}

	// updates and returns possible moves
	public ArrayList<Point> moves() {
		moves = new ArrayList<Point>();
		if (point.x > 0 && board.pieceAt(point.x - 1, point.y) == null)
			moves.add(board.left(point));
		if (point.x < board.getXDim() - 1 && board.pieceAt(point.x + 1, point.y) == null)
			moves.add(board.right(point));
		if (point.y > 0 && board.pieceAt(point.x, point.y - 1) == null)
			moves.add(board.above(point));
		if (point.y < board.getYDim() - 1 && board.pieceAt(point.x, point.y + 1) == null)
			moves.add(board.below(point));
		return (ArrayList<Point>) moves.clone();
	}

	public void select() {
		moves();
		selected = true;
	}

	public void deselect() {
		selected = false;
	}

	/**
	 * Returns true if given piece is an enemy
	 * 
	 * @param piece Piece to be tested
	 * @return true if it's an enemy, false if it's allied or null
	 */
	public boolean isEnemy(Piece piece) {
		return piece == null ? false : piece.team != this.team;
	}

	public Point getPoint() {
		return point;
	}

	public int getTeam() {
		return team;
	}
	
	/*
	 * Returns true if the piece is currently in the process of moving
	 */
	public boolean isMoving() {
		return moveProgress > 0;
	}
	
	protected Point getLocation() {
		return screenLocation;
	}
	
}
