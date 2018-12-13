package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Dux extends Piece {
	
	private Color special;
	
	public Dux(Board board, int team, Point p) {
		super(board, team, p);
		special = team == 1 ? new Color(128, 10, 50) : new Color(50, 10, 128);
	}
	
	public void render(Graphics g) {
		super.render(g);
		g.setColor(special);
		int offset = board.getSquareSize() / 4;
		g.fillOval(getLocation().x + offset, getLocation().y + offset, offset * 2, offset * 2);
	}
	
	public ArrayList<Point> moves() {
		moves = super.moves();
		ArrayList<Point> temp = new ArrayList<Point>();
		for (Point m : moves) {
			if (m.equals(board.left(getPoint())) && board.left(m) != null && board.pieceAt(board.left(m)) == null)
				temp.add(board.left(m));
			if (m.equals(board.right(getPoint())) && board.right(m) != null  && board.pieceAt(board.right(m)) == null) 
				temp.add(board.right(m));
			if (m.equals(board.above(getPoint())) && board.above(m) != null && board.pieceAt(board.above(m)) == null) 
				temp.add(board.above(m));
			if (m.equals(board.below(getPoint())) && board.below(m) != null && board.pieceAt(board.below(m)) == null)
				temp.add(board.below(m));
		}
		moves.addAll(temp);
		return (ArrayList<Point>)moves.clone();
	}

}
