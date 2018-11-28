package game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import gameObjects.Board;
import gameObjects.Dux;
import gameObjects.GameObject;
import gameObjects.Piece;

public class Handler {

	private LinkedList<GameObject> objects;
	private Board board;
	private Piece selected;
	private Input input;
	private boolean canDeselect;
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

		// piece placing phase
		if (Level.getPhase() == 0) {
			// adds a piece
			if (addPiece) {
				Piece piece;
				if (Level.isDux())
					piece = new Dux(board, Level.getTurn(), board.squareAtLocation(lastClick.getPoint()));
				else
					piece = new Piece(board, Level.getTurn(), board.squareAtLocation(lastClick.getPoint()));
				add(piece);
				board.addPiece(piece, piece.getPoint());
				Level.added(Level.getTurn());
				addPiece = false;
			}
		}

		// selects a piece
		if (Level.getPhase() == 1 && selectPiece && !Level.moveAgain()) {
			if (selected != null && selected == board.pieceAt(board.squareAtLocation(lastClick.getPoint()))) {
				selected.deselect();
				selected = null;
			} else {
				if (selected != null || selected == board.pieceAt(board.squareAtLocation(lastClick.getPoint()))) {
					selected.deselect();
					selected = null;
				}
				selected = board.pieceAt(board.squareAtLocation(lastClick.getPoint()));
				selected.select();
			}
			selectPiece = false;
		}

		// moves a piece
		if (Level.getPhase() == 1 && movePiece) {
			selected.move(board.squareAtLocation(lastClick.getPoint()));
			if (board.update(board.squareAtLocation(lastClick.getPoint()))) {
				Level.tookPiece();
			} else
				Level.noTake();
			movePiece = false;
			endTurn();
		}
	}

	public void renderObjects(Graphics g) {
		if (Level.getPhase() == 0)
			board.renderPreview(g, input.mouseLocation(), Level.getTurn(), Level.isDux());
		for (GameObject go : objects) {
			go.render(g);
		}
		if (Level.getPhase() == 2) {
			Level.drawWin(g);
		}
	}

	public void endTurn() {
		objects.removeAll(board.toRemove());
		if (!Level.moveAgain()) {
			selected.deselect();
			selected = null;
		}
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
				if (p.equals(clicked))
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
		board = new Board(board.getxDim(), board.getyDim());
		objects.clear();
		add(board);
	}

	public Board getBoard() {
		return board;
	}
}
