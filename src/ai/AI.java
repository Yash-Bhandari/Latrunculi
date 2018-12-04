package ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Random;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import gameObjects.Board;
import gameObjects.Dux;
import gameObjects.Piece;

public class AI {

	// private final int samePiece = 2;
	private static final int captureBonus = 3;

	// finds the best moves when any piece can be moved and randomly returns one
	// from them
	public static Move bestMove(Board b, int team) {
		PriorityQueue<Move> pq = new PriorityQueue<>(5, java.util.Collections.reverseOrder());
		Move bestMove = null;
		for (Point p : b) {
			if (b.pieceAt(p) != null && b.pieceAt(p).getTeam() == team) {
				pq.add(bestMove(p, b, team));
			}
		}
		ArrayList<Move> bestMoves = new ArrayList<>();
		bestMoves.add(pq.remove());
		while (pq.peek() != null && bestMoves.get(0).getScore() == pq.peek().getScore())
			bestMoves.add(pq.remove());
		// Random r = new Random();
		// return bestMoves.get(r.nextInt(bestMoves.size()));
		return lowestDistance(bestMoves, b);
	}

	// finds the best moves when a specific piece needs to be moved and randomly
	// returns one from them
	public static Move bestMove(Point piece, Board b, int team) {
		PriorityQueue<Move> pq = new PriorityQueue<>(5, java.util.Collections.reverseOrder());
		for (Point move : b.pieceAt(piece).moves()) {
			Board newBoard = b.copyMove(piece, move);
			int score = score(move, newBoard, team, false);
			pq.add(new Move(piece, move, score, b));
		}
		ArrayList<Move> bestMoves = new ArrayList<>();
		if (pq.peek() != null)
			bestMoves.add(pq.remove());
		while (pq.peek() != null && bestMoves.get(0).getScore() == pq.peek().getScore())
			bestMoves.add(pq.remove());
		// Random r = new Random();
		// return bestMoves.get(r.nextInt(bestMoves.size()));
		return lowestDistance(bestMoves, b);
	}

	// finds the best spots to place a piece and randomly returns one from them
	public static Point bestPlace(boolean dux, Board b, int team) {
		PriorityQueue<Move> pq = new PriorityQueue<>(5, java.util.Collections.reverseOrder());
		for (Point square : b) {
			if (b.pieceAt(square) == null) {
				Board temp = b.clone();
				if (dux)
					temp.addPiece(new Dux(temp, team, square), square);
				temp.addPiece(new Piece(temp, team, square), square);
				pq.add(new Move(null, square, score(square, temp, team, true), b));
			}
		}
		ArrayList<Move> bestSpots = new ArrayList<>();
		bestSpots.add(pq.remove());
		while (pq.peek() != null && bestSpots.get(0).getScore() == pq.peek().getScore())
			bestSpots.add(pq.remove());
		Random r = new Random();
		return bestSpots.get(r.nextInt(bestSpots.size())).getMove();
	}

	// Returns the score of a given board for a particular team.
	// The score is a linear combination of the number of different enemy pieces
	// that are within one turn of being taken minus allied pieces that are within
	// one turn of being taken.
	private static int score(Point movedPiece, Board b, int team, boolean placing) {
		int score = placing ? 0 : b.update(movedPiece, false) * captureBonus;
		boolean capture = score > 0 ? true : false;
		for (int i = 0; i < b.getXDim(); i++) {
			for (int j = 0; j < b.getYDim(); j++) {
				Point p = new Point(i, j);
				if (b.pieceAt(p) != null) {
					int change = b.pieceAt(p).getTeam() == team ? 1 : -1;
					if (p == movedPiece && capture)
						change = 2;
					for (Point move : b.pieceAt(p).moves())
						score += b.copyMove(p, move).update(move, false) * change;
				}
			}
		}
		return score;
	}

	private static Move lowestDistance(ArrayList<Move> moves, Board b) {
		Move lowest = null;
		int lowestDistanceChange = 10000;
		for (Move move : moves) {
			Board after = b.copyMove(move.getPiece(), move.getMove());
			int distanceChange = after.nearestEnemy(move.getPiece()) - b.nearestEnemy(move.getPiece());
			if (after.nearestEnemy(move.getPiece()) - b.nearestEnemy(move.getPiece()) < lowestDistanceChange) {
				lowest = move;
				lowestDistanceChange = distanceChange;
			}
		}
		return lowest;
	}
	
	public static void main(String[] args) {
		Board b = new Board(3, 3);
		Piece red = new Piece(b, 1, new Point(0, 0));
		Piece blue = new Piece(b, 2, new Point(0, 2));
		b.addPiece(red, new Point(0, 0));
		b.addPiece(blue, new Point(0, 2));
		b.print();
		Move m = new Move(blue.getPoint(), new Point(0, 1), 5, b);
		System.out.println(b.nearestEnemy(blue.getPoint()));
		Board after = b.copyMove(m.getPiece(), m.getMove());
		after.print();
		System.out.println(after.nearestEnemy(m.getMove()));
	}
}
