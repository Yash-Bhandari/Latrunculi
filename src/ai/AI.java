package ai;

import java.awt.Point;
import java.util.ArrayList;

import gameObjects.Board;

public class AI {

    // private final int samePiece = 2;

    public static Move bestMove(Board b, int team) {
        b.clone().print();
        Move bestMove = null;
        for (int x = 0; x < b.getxDim(); x++) {
            for (int y = 0; y < b.getyDim(); y++) {
                Point p = new Point(x, y);
                if (b.pieceAt(p) != null && b.pieceAt(p).getTeam() == team) {
                    Move temp = bestMove(p, b, team);
                    if (bestMove == null || bestMove.getScore() < temp.getScore())
                        bestMove = temp;
                    else { 
                        //System.out.println("this is ass");
                        //b.copyMove(p, temp.getMove()).print();
                    }
                }
            }
        }
        return bestMove;
    }

    public static Move bestMove(Point piece, Board b, int team) {
        Move bestMove = null;
        for (Point move : b.pieceAt(piece).moves()) {
            Board newBoard = b.copyMove(piece, move);
            int score = score(move, newBoard, team);
            if (bestMove == null || bestMove.getScore() < score)
                bestMove = new Move(piece, move, score);
        }
        return bestMove;
    }

    // Returns the score of a given board for a particular team.
    // The score is a linear combination of the number of different enemy pieces
    // that are within one turn of being taken minus allied pieces that are within
    // one turn of being taken.
    private static int score(Point movedPiece, Board b, int team) {
        int score = b.update(movedPiece, false) * 3;
        boolean capture = score > 0 ? true : false;
        for (int i = 0; i < b.getxDim(); i++) {
            for (int j = 0; j < b.getyDim(); j++) {
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
}
