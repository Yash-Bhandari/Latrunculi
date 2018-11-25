package game;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import gameObjects.Board;
import gameObjects.GameObject;
import gameObjects.Piece;

public class Handler {

    private LinkedList<GameObject> objects;
    private Board board;
    private Piece selected;
    private Input input;
    private boolean addPiece;
    private boolean selectPiece;
    private boolean movePiece;
    private MouseEvent lastClick;

    public Handler(Input input) {
        objects = new LinkedList<GameObject>();
        this.input = input;
    }

    public void add(GameObject go) {
        if (go instanceof Board)
            board = (Board) go;
        objects.add(go);
    }

    public void updateObjects() {
        //adds a piece
        if (Level.getPhase() == 0 && addPiece) {
            Piece piece = new Piece(board, Level.getTurn(), board.squareAtLocation(lastClick.getPoint()));
            add(piece);
            board.addPiece(piece, piece.getPoint());
            Level.added(Level.getTurn());
            addPiece = false;
        }
        // selects a piece
        if (Level.getPhase() > 0 && selectPiece) {
            if (selected != null)
                selected.deselect();
            selected = board.pieceAt(board.squareAtLocation(lastClick.getPoint()));
            selected.select();
            selectPiece = false;
        }
        // moves a piece
        if (Level.getPhase() > 0 && movePiece) {
            selected.move(board.squareAtLocation(lastClick.getPoint()));
            movePiece = false;
            endTurn();
        }
    }

    public void renderObjects(Graphics g) {
        for (GameObject go : objects) {
            go.render(g);
        }

    }
    
    public void endTurn() {
        board.update();
        objects.removeAll(board.toRemove());
        selected.deselect();
        selected = null;
        Level.nextTurn();
    }

    public void selectPiece(MouseEvent e) {
        Point clicked = board.squareAtLocation(e.getPoint());
        if (board.pieceAt(clicked) != null && board.pieceAt(clicked).getTeam() == Level.getTurn()) {
            selectPiece = true;
            lastClick = e;
        }
        if (selected != null) {
            boolean valid = false;
            for (Point p : selected.moves()) {
                if (p != null && p.equals(clicked))
                    valid = true;
            }
            if (valid) {
                movePiece = true;
                lastClick = e;
            }

        }
    }

    public void addPiece(MouseEvent e) {
        if (board.pieceAt(board.squareAtLocation(e.getPoint())) == null) {
            addPiece = true;
            lastClick = e;
        }
    }

    public void clear() {
        objects.clear();
    }

    public Board getBoard() {
        return board;
    }
}
