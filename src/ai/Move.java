package ai;

import java.awt.Point;

import gameObjects.Board;

public class Move implements Comparable<Move> {
	private final Point piece;
	private final Point move;
	private int score;

	public Move(Point piece, Point move, int score, Board b) {
		this.piece = piece;
		this.move = move;
		this.score = score;
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

	public int compareTo(Move m) {
		return score - m.getScore();
	}
}
