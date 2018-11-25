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
    private static int phase = 0;

    public static void startLevel(Game game, int boardX, int BoardY, int numPieces) {
        Level.game = game;
        Level.handler = game.handler;
        Level.numPieces = numPieces;
        Board b = new Board(5, 5);
        handler.add(b);
    }

    public static void added(int team) {
        if (team == 1)
            numR++;
        else
            numB++;
        if (numR == numPieces && numB == numPieces)
            phase++;
    }

    public static int whoPlaces() {
        if (phase == 0) {
            if (numR == numB)
                return 1;
            return 2;
        }
        return 0;
    }

    public static int getPhase() {
        return phase;
    }

}
