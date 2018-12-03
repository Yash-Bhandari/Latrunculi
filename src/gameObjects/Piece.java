package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Piece extends GameObject {

	protected Board board;
	protected boolean selected;
	protected Point point;
	protected int team;
	protected ArrayList<Point> moves;
	private Color color;

	public Piece(Board board, int team, Point p) {
		this.board = board;
		point = p;
		this.team = team;
	}
	
	public void render(Graphics g) {
		g.setColor(team == 1 ? Color.red : Color.blue);
		g.fillOval(board.locationOfSquare(point).x, board.locationOfSquare(point).y, board.getSquareSize(),
				board.getSquareSize());
		if (selected)
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

	// moves piece to given point
	public void move(Point p) {
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

	private void remove() {
		inPlay = false;
	}

	public Point getPoint() {
		return point;
	}

	public int getTeam() {
		return team;
	}

}
