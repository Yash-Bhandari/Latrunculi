package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gameObjects.Board;

public class Level {

    private static Game game;
    private static Handler handler;
    private static boolean moveAgain = false;
    private static boolean ai = false;
    //set this to 0 for no ai
    private static int aiTeam = 1;
    private static int numPieces;
    private static int numDux = 1;
    private static int phase = -1;
    private static int turn;
    private static int winner;

    public static void startLevel(Game game, int boardX, int boardY, int numPieces) {
        Level.game = game;
        Level.handler = game.handler;
        Level.numPieces = numPieces;
        Board b = new Board(boardX, boardY);
        handler.add(b);
        phase = 0;
        turn = 1;
        winner = 0;
    }

    public static void restart() {
        handler.clear();
        phase = 0;
        turn = 1;
        winner = 0;
    }

    public static void added(int team) {
        nextTurn();
        if (handler.getBoard().numBlue() == numPieces && handler.getBoard().numRed() == numPieces) {
            phase++;
            turn = 1;
        }
        System.out.println("it is turn " + turn);

    }

    public static void nextTurn() {
        if (phase == 1) {
            if (handler.getBoard().numRed() == 1) {
                phase = 2;
                winner = 2;
                handler.deselect();
            } else if (handler.getBoard().numBlue() == 1) {
                phase = 2;
                winner = 1;
                handler.deselect();
            }
        }
        if (!moveAgain()) {
            if (turn == 2)
                turn = 1;
            else
                turn = 2;
        }
    }

    public static void tookPiece() {
        moveAgain = true;
    }

    public static void noTake() {
        moveAgain = false;
    }

    public static void drawWin(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(250, 325, 301, 101);
        // g.fillRect(260, 500, 280, 50);
        String winner = Level.getWinner() == 1 ? "RED" : "BLUE";
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        g.setColor(Color.white);
        g.drawString(winner + " WINS!", 250, 393);
        // g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        // g.drawString("PRESS SPACE TO RESET", 260, 500);
    }

    public static boolean isDux() {
        if (turn == 1)
            if (handler.getBoard().numRed() >= numPieces - numDux && handler.getBoard().numRed() < numPieces)
                return true;
        if (turn == 2)
            if (handler.getBoard().numBlue() >= numPieces - numDux && handler.getBoard().numBlue() < numPieces)
                return true;
        return false;
    }

    public static boolean moveAgain() {
        return moveAgain;
    }

    public static int getPhase() {
        return phase;
    }

    public static int getTurn() {
        return turn;
    }

    public static int getWinner() {
        return winner;
    }
    
    public static int getAITeam() {
        return aiTeam;
    }
    
    public static boolean isAI() {
        return ai;
    }
    
}
