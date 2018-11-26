package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import gameObjects.Board;

public class Level {

    private static Game game;
    private static Handler handler;
    private boolean setup = true;
    private static int numPieces;
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
    }

    public static void nextTurn() {
        if (phase == 1) {
            if (handler.getBoard().numRed() == 0) {
                phase = 2;
                winner = 2;
            } else if (handler.getBoard().numBlue() == 0) {
                phase = 2;
                winner = 1;
            }
        }
        if (turn == 2)
            turn = 1;
        else
            turn = 2;
    }
    
    public static void drawWin(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(250, 325, 301, 101);
        String winner = Level.getWinner() == 1 ? "RED" : "BLUE"; 
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 50));
        g.setColor(Color.white);
        g.drawString(winner + " WINS!", 250, 393);
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

}
