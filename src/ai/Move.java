package ai;

import java.awt.Point;

import gameObjects.Board;

public class Move implements Comparable<Move> {
	private final Point piece;
	private final Point move;
	private final int score;
	private final int change;

	public Move(Point piece, Point move, int score, Board b) {
		this.piece = piece;
		this.move = move;
		this.score = score;

		if (piece != null) {
			int old = b.nearestEnemy(piece);
			int afterMove = b.copyMove(piece, move).nearestEnemy(move);
			change = afterMove - old;
		} else
			change = 0;
	}

	/*
	 * Returns the original location of the piece, null if placing
	 */
	public Point getPiece() {
		return piece;
	}

	/*
	 * Returns the new location of the piece
	 */
	public Point getMove() {
		return move;
	}

	public int getScore() {
		return score;
	}

	/*
	 * Returns the change in nearest. Enemy Positive if it moves away from enemies,
	 * negative if it moves towards them
	 * 
	 */
	public int getChange() {
		return change;
	}

	/*
	 * Returns a positive integer if this move results in a board with a higher
	 * score or a negative one if this move results in a board with a lower score.
	 * Change in distance is used to break ties.
	 */
	public int compareTo(Move m) {
		if (score == m.getScore())
			return m.getChange() - change;
		return score - m.getScore();
	}
	
	public void print() {
		System.out.println("There was a move with " + score + " score " + " and " + change + " change");
	}
}
