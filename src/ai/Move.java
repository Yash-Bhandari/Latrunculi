package ai;

import java.awt.Point;

import gameObjects.Board;

public class Move implements Comparable<Move> {
    private final Point piece;
    private final Point move;
    private int distanceChange;
    private int score;

    public Move(Point piece, Point move, int score, Board b) {
        this.piece = piece;
        this.move = move;
        this.score = score;
        if (piece != null) {
            this.distanceChange = b.nearestEnemy(piece) - b.copyMove(piece, move).nearestEnemy(move);
            System.out.println("before it was " + b.nearestEnemy(piece) + " now it's "
                    + b.copyMove(piece, move).nearestEnemy(move));
        }
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
     * Returns the change in the distance from the nearest enemy Negative means
     * closer, positive means farther
     */
    public int getDistanceChange() {
        return distanceChange;
    }

    public int compareTo(Move m) {
        return score - m.getScore();
    }
}
