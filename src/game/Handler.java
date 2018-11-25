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
    private Input input;
    private boolean addPiece;
    private boolean selectPiece;
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
        if (Level.getPhase() == 0 && addPiece) {
            Piece piece = new Piece(board, Level.whoPlaces(), board.squareAtLocation(lastClick.getPoint()));
            add(piece);
            board.addPiece(piece, piece.getPoint());
            Level.added(Level.whoPlaces());
            addPiece = false;
        }
        if (Level.getPhase() > 0 && selectPiece) {
            board.pieceAt(board.locationOfSquare(lastClick.getPoint())).select();
            selectPiece = false;
            
        }
    }

    public void renderObjects(Graphics g) {
        for (GameObject go : objects) {
            go.render(g);
        }

    }
    
    public void selectPiece(MouseEvent e) {
        if (board.pieceAt(e.getPoint()) != null) {
            selectPiece = true;
            lastClick = e;
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
