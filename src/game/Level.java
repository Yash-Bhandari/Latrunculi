package game;

import java.awt.Point;

import gameObjects.Board;
import gameObjects.Piece;

public class Level {

    private static Game game;
    private static Handler handler;
    private boolean setup = true;
    private static int numR;
    private static int numB;
    private static int numPieces;
    private static int phase = -1;
    private static int turn;

    public static void startLevel(Game game, int boardX, int boardY, int numPieces) {
        Level.game = game;
        Level.handler = game.handler;
        Level.numPieces = numPieces;
        Board b = new Board(boardX, boardY);
        handler.add(b);
        turn = 1;
        phase = 0;
    }

    public static void added(int team) {
        if (team == 1)
            numR++;
        else
            numB++;
        nextTurn();
        if (numR == numPieces && numB == numPieces) {
            phase++;
            turn = 1;
        }
    }

    public static void nextTurn() {
        if (turn == 2) turn = 1;
        else turn = 2;
    }
    public static int getPhase() {
        return phase;
    }
    
    public static int getTurn() {
        return turn;
    }

}
