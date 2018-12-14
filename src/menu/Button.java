package menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import com.sun.glass.events.MouseEvent;

public class Button {
	Point location;
	Dimension size;
	String label;
	int fontSize;
	
	
	public static void main(String[] args) {
		Button b = new Button(new Point(10, 10), new Dimension(10, 10), "hello");
		System.out.println(b.isWithin(new Point(19,15)));
	}
	
	public Button(Point location, Dimension size, String label) {
		this.location = location;
		this.size = size;
		this.label = label;
	}
	
	public void render(Graphics g) {
		g.drawRect(location.x, location.y, (int)size.getWidth(), (int)size.getHeight());
		g.drawString(label, location.x, location.y);
	}
	
	
	/**
	 * Returns true if the given point is within the bounds of the button
	 * 
	 * @param cursor The point that will be checked 
	 * @return	True if the given point is within the button
	 */
	public boolean isWithin(Point cursor) {
		if (cursor.x > location.x && cursor.y >location.y && cursor.x < location.x + size.getWidth() && cursor.y < location.y + size.getHeight())
			return true;
		return false;
	}

}
