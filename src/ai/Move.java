package ai;

import java.awt.Point;

public class Move implements Comparable<Move>{
    private final Point piece;
    private final Point move;
    private int score;
    
    public Move(Point piece, Point move, int score) {
        this.piece = piece;
        this.move = move;
        this.score = score;
    }
    
    //returns null if placing piece
    public Point getPiece() {
        return piece;
    }
    
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
