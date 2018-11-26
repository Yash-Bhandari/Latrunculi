package game;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Input implements KeyListener, MouseListener {

    private Handler handler;
    private Game game;

    public Input(Game game) {
        this.game = game;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        adjust(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        adjust(e, false);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    // sets index in keys[] corresponding to KeyEvent e to boolean a
    private void adjust(KeyEvent e, boolean a) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_SPACE:
            if(Level.getPhase() == 2) Level.restart();
            break;
        }

    }

    public Point mouseLocation() {
        Point a = MouseInfo.getPointerInfo().getLocation();
        Point b = game.getLocationOnScreen();
        return new Point(a.x - b.x, a.y - b.y);
    }

    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (Level.getPhase() == 0) {
            if (handler.getBoard().within(e.getPoint())) {
                handler.addPiece(e);
            }
        }
        if (Level.getPhase() == 1) {
            if (handler.getBoard().within(e.getPoint())) {
                handler.selectPiece(e);
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

}
