package gameObjects;

import java.awt.Graphics;

public abstract class GameObject {
    
    protected boolean inPlay = true;
    
    public abstract void render(Graphics g);
    
    public boolean inPlay() {
        return inPlay;
    }
}
