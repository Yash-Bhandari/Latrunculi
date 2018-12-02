package ai;

import java.awt.Point;
import java.util.ArrayList;

import gameObjects.Board;

public class AI {
    
    public Move bestMove(Board b, int team) {
        Move bestMove;
        int bestScore;
        for (int x = 0; x < b.getxDim(); x++) {
            for (int y =0; y < b.getyDim(); y++) {
                int score = 0;
                Point p = new Point(x, y);
                if (b.pieceAt(p).getTeam() == 0);
            }
        }
        return null;
    }

    private static int score(Board b, int team) {
        int score = 0;
        for (int i = 0; i < b.getxDim(); i++) {
            for (int j = 0; j < b.getyDim(); j++) {
                Point p = new Point(i, j);
                if (b.pieceAt(p) != null && b.pieceAt(p).getTeam() == team) score += simMoves(b, p, team);
            }
        }
        return score;
    }
 
    private static int simMoves(Board b, Point p, int team) {
        ArrayList<Point> moves = b.pieceAt(p).moves();
        //b.move(p, )
        return 1;
    }
}
