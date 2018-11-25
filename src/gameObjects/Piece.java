package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Piece extends GameObject {

    private Board board;
    private boolean selected;
    private Point point;
    private int team;
    private Point[] moves;

    public Piece(Board board, int team, Point p) {
        this.board = board;
        point = p;
        this.team = team;
    }

    public void render(Graphics g) {
        if (team == 1)
            g.setColor(Color.red);
        if (team == 2)
            g.setColor(Color.blue);
        g.fillOval(board.locationOfSquare(point).x, board.locationOfSquare(point).y, board.getSquareSize(),
                board.getSquareSize());
        if (selected)
            renderMoves(g);
    }

    // draws preview at half size
    private void renderMoves(Graphics g) {
        for (int i = 0; i < moves.length; i++) {
            if (moves[i] != null) {
                g.setColor(team == 1 ? Color.red : Color.blue);
                Point p = board.locationOfSquare(moves[i]);
                p.x += board.getSquareSize() / 4;
                p.y += board.getSquareSize() / 4;
                g.fillOval(p.x, p.y, board.getSquareSize() / 2, board.getSquareSize() / 2);
            }
        }
    }
    
    //moves piece to given point
    public void move(Point p) {
        board.removePiece(point);
        board.addPiece(this, p);
        point = p;
        moves();
    }

    // updates and returns possible moves
    public Point[] moves() {
        moves = new Point[4];
        if (point.x > 0 && board.pieceAt(point.x - 1, point.y) == null)
            moves[0] = new Point(point.x - 1, point.y);
        if (point.x < board.getxDim() - 1 && board.pieceAt(point.x + 1, point.y) == null)
            moves[1] = new Point(point.x + 1, point.y);
        if (point.y > 0 && board.pieceAt(point.x, point.y - 1) == null)
            moves[2] = new Point(point.x, point.y - 1);
        if (point.y < board.getyDim() - 1 && board.pieceAt(point.x, point.y + 1) == null)
            moves[3] = new Point(point.x, point.y + 1);
        return moves.clone();
    }

    public void select() {
        moves();
        selected = true;
    }

    public void deselect() {
        selected = false;
    }
    
    private void remove() {
        inPlay = false;
    }
    
    public Point getPoint() {
        return point;
    }
    
    public int getTeam() {
        return team;
    }
    
}
