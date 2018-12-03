package game;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Panel extends Canvas{

   private static final long serialVersionUID = 8524587089709471630L;
    
   public Panel (int width, int height, String title, Game game) {
       Dimension d = new Dimension(width, height);
       JFrame frame = new JFrame();
       frame.setPreferredSize(d);
       frame.setSize(d);
       
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setLocationRelativeTo(null);
       frame.add(game);
       frame.setVisible(true);
      // game.start();
   }
    
}
