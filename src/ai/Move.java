package ai;

import java.awt.Point;

public class Move {
    private final Point piece;
    private final Point move;
    
    public Move(Point piece, Point move) {
        this.piece = piece;
        this.move = move;
    }
    
    public Point getPiece() {
        return piece;
    }
    
    public Point getMove() {
        return move;
    }
}
